package io.bitsound.coretestapp.presenters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.soundlly.standalone.sdk.Soundlly;
import com.soundlly.standalone.sdk.SoundllyResult;
import com.soundlly.standalone.sdk.SoundllyResultListener;

import java.io.IOException;
import java.util.List;

import io.bitsound.coretestapp.activity.ResultActivity;
import io.bitsound.coretestapp.models.ResultModel;
import io.bitsound.coretestapp.persistent.Archive;
import io.bitsound.coretestapp.persistent.FileArchive;
import io.bitsound.coretestapp.view.ResultView;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultPresenter implements Presenter {
    private static final String TAG = ResultPresenter.class.getSimpleName();
    private static final String SOUNDLLY_APP_KEY = "54bda562-0a9205df-da905901-YDAJ1861";
    private static final int ARCHIVE_COUNT = 5;
    private static final int SDK_RETRY_COUNT = 4;

    private Archive corePerfTestArchive;

    private Context context;
    private ResultView resultView;
    private List<ResultModel> resultModelList;

    private boolean preambleCsSelected = true;
    private boolean energyDetectorSelected = true;
    private boolean qokShapingSelected = true;
    private boolean localSyncFinderSelected = true;

    private int frameType = 1;

    private int coreType = 1;
    private int noSigThreshold;
    private int combiningThreshold;
    private double rec;
    private double gamma;
    private int unitBufferSize;
    /** 총 테스트 진행할 횟수, 0이면 중지할때까지 **/
    private int totRecTryCount;
    private int symbolNum;
    private int[] symbolDataCsParRatioHistogram;
    private int[] symbolDataCsParHistogram;
    private double preambleCsThres;
    private double noSigThres;
    private double combiningThres;

    // statistics
    /** 현재 테스트 진행 횟수 **/
    private int testCount = 0;
    private double totPreambleJcsMar;
    private int totNoEnergy;
    private int totFindEnergy;
    private double totProcTime;
    private double lastProcTime;
    private int totNoSig;
    private int totFindSig;
    private int totGoodSig;
    private int totAmbiSig;
    private int totCrcErr;
    private int totSuccess;
    private double totCurrT;
    private double lastCurrT;
    private double mSpreadingTime;
    private double mRicianKFactor;
    private double mFreqLineSlope;
    private double mFreqLineMSEdB;

    // 응답시간 통계
    private long[] totReceivedTimeHistogram = new long[SDK_RETRY_COUNT];
    private int[] tryCountHistogram = new int[SDK_RETRY_COUNT];

    private volatile boolean isRunning;

    public ResultPresenter() {
    }

    public void setContext(@NonNull Context context) {
        this.context = context;
    }

    public void setResultView(@NonNull ResultView view) {
        this.resultView = view;
    }

    public void setResultModelList(List<ResultModel> resultModelList) {
        this.resultModelList = resultModelList;
    }

    public void initSoundllySdk() {
        int ret = Soundlly.init(context, SOUNDLLY_APP_KEY, new SoundllyResultListener() {

            @Override
            public void onInitialized() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onStateChanged(int i) {
                if (i == SoundllyResultListener.STOPPED) {
                    if (isRunning && (totRecTryCount <= 0 || testCount < totRecTryCount)) {
                        Soundlly.startDetect(false);
                    }
                }
            }

            @Override
            public void onResult(int i, SoundllyResult soundllyResult) {

            }
        });

        if (ret != Soundlly.SUCCESS) {
            if (ret == Soundlly.INVALID_ARGUMENTS) {
                Log.e(TAG, "Soundlly init error : appkey is null");
            } else if (ret == Soundlly.MIC_PERMISSION_DENIED) {
                Log.e(TAG, "Soundlly init error : mic permission denied");
                resultView.showMicPermissionError();
            }
        } else {
            Intent intent = new Intent();
            intent.setAction("ACTION_CORE_PERF_TEST");
            intent.putExtra(ResultActivity.EXTRA_PREAMBLE_CS, preambleCsSelected);
            intent.putExtra(ResultActivity.EXTRA_ENERGY_DETECTOR, energyDetectorSelected);
            intent.putExtra(ResultActivity.EXTRA_QOK_SHAPING, qokShapingSelected);
            intent.putExtra(ResultActivity.EXTRA_LOCAL_SYNC_FINDER, localSyncFinderSelected);
            intent.putExtra(ResultActivity.EXTRA_FRAME_TYPE, frameType);
            intent.putExtra(ResultActivity.EXTRA_CORE_TYPE, coreType);
            intent.putExtra(ResultActivity.EXTRA_NO_SIG_THRESHOLD, noSigThreshold);
            intent.putExtra(ResultActivity.EXTRA_COMBINING_THRESHOLD, combiningThreshold);
            intent.putExtra(ResultActivity.EXTRA_REC, rec);
            intent.putExtra(ResultActivity.EXTRA_GAMMA, gamma);
            intent.putExtra(ResultActivity.EXTRA_UNIT_BUFFER_SIZE, unitBufferSize);

            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    public boolean isPreambleCsSelected() {
        return preambleCsSelected;
    }

    public void setPreambleCsSelected(boolean preambleCsSelected) {
        this.preambleCsSelected = preambleCsSelected;
    }

    public boolean isEnergyDetectorSelected() {
        return energyDetectorSelected;
    }

    public void setEnergyDetectorSelected(boolean energyDetectorSelected) {
        this.energyDetectorSelected = energyDetectorSelected;
    }

    public boolean isQokShapingSelected() {
        return qokShapingSelected;
    }

    public void setQokShapingSelected(boolean qokShapingSelected) {
        this.qokShapingSelected = qokShapingSelected;
    }

    public boolean isLocalSyncFinderSelected() {
        return localSyncFinderSelected;
    }

    public void setLocalSyncFinderSelected(boolean localSyncFinderSelected) {
        this.localSyncFinderSelected = localSyncFinderSelected;
    }

    public int getFrameType() {
        return frameType;
    }

    public void setFrameType(int frameType) {
        this.frameType = frameType;
    }

    public int getCoreType() {
        return coreType;
    }

    public void setCoreType(int coreType) {
        this.coreType = coreType;
    }

    public int getNoSigThreshold() {
        return noSigThreshold;
    }

    public void setNoSigThreshold(int noSigThreshold) {
        this.noSigThreshold = noSigThreshold;
    }

    public int getCombiningThreshold() {
        return combiningThreshold;
    }

    public void setCombiningThreshold(int combiningThreshold) {
        this.combiningThreshold = combiningThreshold;
    }

    public double getRec() {
        return rec;
    }

    public void setRec(double rec) {
        this.rec = rec;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getTotNoEnergy() {
        return totNoEnergy;
    }

    public int getTotFindEnergy() {
        return totFindEnergy;
    }

    public double getLastProcTime() {
        return lastProcTime;
    }

    public int getTotNoSig() {
        return totNoSig;
    }

    public int getTotFindSig() {
        return totFindSig;
    }

    public int getTotGoodSig() {
        return totGoodSig;
    }

    public int getTotAmbiSig() {
        return totAmbiSig;
    }

    public int getTotCrcErr() {
        return totCrcErr;
    }

    public int getTotSuccess() {
        return totSuccess;
    }

    public double getLastCurrT() {
        return lastCurrT;
    }

    public double getAvgCurrT() {
        return this.totCurrT / (double) testCount;
    }

    public double getAvgProcTime() {
        return this.totProcTime / (double) (testCount - totNoEnergy);
    }

    public int getUnitBufferSize() {
        return unitBufferSize;
    }

    public void setUnitBufferSize(int unitBufferSize) {
        this.unitBufferSize = unitBufferSize;
    }

    public int getTotRecTryCount() {
        return totRecTryCount;
    }

    public void setTotRecTryCount(int totRecTryCount) {
        this.totRecTryCount = totRecTryCount;

    }
    public int getTestCount() {
        return testCount;
    }

    public void setSymbolNum(int symbolNum) {
        this.symbolNum = symbolNum;
        this.symbolDataCsParRatioHistogram = new int[symbolNum + 1];
        this.symbolDataCsParHistogram = new int[symbolNum + 1];

        try {
            corePerfTestArchive = new FileArchive(symbolNum + 1);
        } catch (IOException e) {
            e.printStackTrace();
            corePerfTestArchive = null;
        }
    }

    public int getSymbolNum() {
        return symbolNum;
    }

    public int[] getSymbolDataCsParRatioHistogram() {
        return symbolDataCsParRatioHistogram;
    }

    public void setSymbolDataCsParRatioHistogram(int[] symbolDataCsParRatioHistogram) {
        this.symbolDataCsParRatioHistogram = symbolDataCsParRatioHistogram;
    }

    public int[] getSymbolDataCsParHistogram() {
        return symbolDataCsParHistogram;
    }

    public void setSymbolDataCsParHistogram(int[] symbolDataCsParHistogram) {
        this.symbolDataCsParHistogram = symbolDataCsParHistogram;
    }

    public void setPreambleCsThres(double preambleCsThres) {
        this.preambleCsThres = preambleCsThres;
    }

    public double getPreambleCsThres() {
        return preambleCsThres;
    }

    public double getCombiningThres() {
        return combiningThres;
    }

    public void setCombiningThres(double combiningThres) {
        this.combiningThres = combiningThres;
    }

    public double getNoSigThres() {
        return noSigThres;
    }

    public void setNoSigThres(double noSigThres) {
        this.noSigThres = noSigThres;
    }

    public double getAvgPreCsMar() {
        return totPreambleJcsMar / (double) testCount;
    }

    public double getPostCsPer() {
        double temp = ((double) totGoodSig - (double) totSuccess) / (double) totGoodSig * 100.0f;

        if (Double.isNaN(temp)) {
            return 0.0f;
        } else {
            return temp;
        }
    }

    public double getNoCsPer() {
        double temp = ((double) totGoodSig - (double) totSuccess) / (double) totFindEnergy * 100.0f;

        if (Double.isNaN(temp)) {
            return 0.0f;
        } else {
            return temp;
        }
    }

    public long[] getTotReceivedTimeHistogram() {
        return totReceivedTimeHistogram;
    }

    public int[] getTryCountHistogram() {
        return tryCountHistogram;
    }

    public double getSpreadingTime() {
        return mSpreadingTime;
    }

    public double getRicianKFactor() {
        return mRicianKFactor;
    }

    public double getFreqLineSlope() {
        return mFreqLineSlope;
    }

    public double getFreqLineMSEdB() {
        return mFreqLineMSEdB;
    }

    public void startSoundllySDK() {
        this.isRunning = true;
        Soundlly.startDetect(false);
    }

    public void stopSoundllySdk() {
        this.isRunning = false;
    }

    public void addProcTime(double procTime) {
        totProcTime += procTime;
        lastProcTime += procTime;
    }

    public void addResultItem(int tryCnt, long code, double procTime, boolean isEnergyDetect, long energyDetectTime,
                              boolean detection, boolean decoding, double snr, double preambleJcsMar,
                              int dataJcsParRatioGeqCounter, int dataJcsParGeqCounter, boolean preambleCsResult,
                              boolean dataCsResult, double currT, long totRecTime, double spreadingTime, double ricianKFactor, double freqLineSlope, double freqLineMSEdB) {
        if (totRecTryCount <= 0 || testCount < totRecTryCount) {
            testCount++;

            ResultModel model = new ResultModel(testCount, code, procTime, isEnergyDetect, energyDetectTime,
                    detection, decoding, snr, preambleJcsMar, dataJcsParRatioGeqCounter,
                    dataJcsParGeqCounter, preambleCsResult, dataCsResult, currT);
            resultModelList.add(model);


            lastCurrT = currT;
            totCurrT += currT;
            totPreambleJcsMar += preambleJcsMar;
            // 총 디코딩 시간
            totProcTime += procTime;
            // 마지막 디코딩 시간
            lastProcTime += procTime;

            if (code >= 0 && totRecTime > 0) {
                // tryCnt는 0,1,2,3 이렇게 옴

                // 해당 재시작 횟수에 디코딩 시간 누적
                totReceivedTimeHistogram[tryCnt] += totRecTime;
                // 해당 재시작 횟수 누적
                tryCountHistogram[tryCnt]++;
            }

            // 에너지 유무
            if (isEnergyDetect) {
                totFindEnergy++;

                // 신호 유무
                if (preambleCsResult) {
                    totFindSig++;
                    // data cs par ratio
                    symbolDataCsParRatioHistogram[dataJcsParRatioGeqCounter] += 1;
                    // data cs par
                    symbolDataCsParHistogram[dataJcsParGeqCounter] += 1;

                    // good sig
                    if (dataCsResult) {
                        totGoodSig++;

                        // crc error -> code == -3
                        if (code == -3) {
                            totCrcErr++;
                        }

                        // success -> code >= 0
                        if (code >= 0) {
                            totSuccess++;
                        }
                    } else {
                        totAmbiSig++;
                    }
                }
                else {
                    totNoSig++;
                }

            } else {
                totNoEnergy++;
            }

            resultView.refreshRecyclerView();
            resultView.refreshData();

            if (testCount % ARCHIVE_COUNT == 0) {
                FileArchiveTask task = new FileArchiveTask(corePerfTestArchive, testCount,
                        preambleCsThres, noSigThres, combiningThres, getAvgPreCsMar(), gamma,
                        symbolDataCsParRatioHistogram, symbolDataCsParHistogram, totNoEnergy,
                        totFindEnergy, totProcTime, lastProcTime, totNoSig, totFindSig, totGoodSig,
                        totAmbiSig, totCrcErr, totSuccess, getAvgCurrT(), lastCurrT);
                task.execute();
            }
            lastProcTime = 0;


            mSpreadingTime = spreadingTime;
            mRicianKFactor = ricianKFactor;
            mFreqLineSlope = freqLineSlope;
            mFreqLineMSEdB = freqLineMSEdB;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        Soundlly.stopDetect();
    }

    @Override
    public void destroy() {
        Soundlly.release();
    }

    public String getArchiveFilePath() {
        return ((FileArchive)corePerfTestArchive).getArchiveFilePath();
    }

    private static class FileArchiveTask extends AsyncTask<Void, Void, Void> {

        private Archive archive;
        private final int totReceived;
        private final double preambleCsThres;
        private final double noSigThres;
        private final double combiningThres;
        private final double avgPreCsMar;
        private final double gamma;
        private final int[] symbolDataCsParRatioHistogram;
        private final int[] symbolDataCsParHistogram;
        private final int totNoEnergy;
        private final int totFindEnergy;
        private final double totProcTime;
        private final double lastProcTime;
        private final int totNoSig;
        private final int totFindSig;
        private final int totGoodSig;
        private final int totAmbiSig;
        private final int totCrcErr;
        private final int totSuccess;
        private final double avgCurrT;
        private final double lastCurrT;

        public FileArchiveTask(Archive archive, int totReceived, double preambleCsThres, double noSigThres,
                               double combiningThres, double avgPreCsMar, double gamma, int[] symbolDataCsParRatioHistogram,
                               int[] symbolDataCsParHistogram, int totNoEnergy, int totFindEnergy, double totProcTime,
                               double lastProcTime, int totNoSig, int totFindSig, int totGoodSig, int totAmbiSig,
                               int totCrcErr, int totSuccess, double avgCurrT, double lastCurrT) {
            this.archive = archive;
            this.totReceived = totReceived;
            this.preambleCsThres = preambleCsThres;
            this.noSigThres = noSigThres;
            this.combiningThres = combiningThres;
            this.avgPreCsMar = avgPreCsMar;
            this.gamma = gamma;
            this.symbolDataCsParRatioHistogram = symbolDataCsParRatioHistogram;
            this.symbolDataCsParHistogram = symbolDataCsParHistogram;
            this.totNoEnergy = totNoEnergy;
            this.totFindEnergy = totFindEnergy;
            this.totProcTime = totProcTime;
            this.lastProcTime = lastProcTime;
            this.totNoSig = totNoSig;
            this.totFindSig = totFindSig;
            this.totGoodSig = totGoodSig;
            this.totAmbiSig = totAmbiSig;
            this.totCrcErr = totCrcErr;
            this.totSuccess = totSuccess;
            this.avgCurrT = avgCurrT;
            this.lastCurrT = lastCurrT;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            if (archive != null) {
                try {
                    archive.saveCoreStatistics(totReceived, preambleCsThres, noSigThres,
                            combiningThres, avgPreCsMar, gamma, symbolDataCsParRatioHistogram,
                            symbolDataCsParHistogram, totNoEnergy, totFindEnergy, totProcTime,
                            lastProcTime, totNoSig, totFindSig, totGoodSig, totAmbiSig,
                            totCrcErr, totSuccess, avgCurrT, lastCurrT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
