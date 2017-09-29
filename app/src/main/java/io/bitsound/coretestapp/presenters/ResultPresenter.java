package io.bitsound.coretestapp.presenters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.soundlly.standalone.sdk.Soundlly;
import com.soundlly.standalone.sdk.SoundllyResult;
import com.soundlly.standalone.sdk.SoundllyResultListener;

import java.util.List;

import io.bitsound.coretestapp.activity.ResultActivity;
import io.bitsound.coretestapp.models.ResultModel;
import io.bitsound.coretestapp.view.ResultView;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultPresenter implements Presenter {
    private static final String TAG = ResultPresenter.class.getSimpleName();
    private static final String SOUNDLLY_APP_KEY = "54bda562-0a9205df-da905901-YDAJ1861";

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

    private volatile boolean isRunning;


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
                    if (totRecTryCount <= 0 || testCount < totRecTryCount) {
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
        return this.totCurrT / (double) totRecTryCount;
    }

    public double getAvgProcTime() {
        return this.totProcTime / (double) totRecTryCount;
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

    public void startSoundllySDK() {
        this.isRunning = true;
        Soundlly.startDetect(false);
    }

    public void addProcTime(double procTime) {
        totProcTime += procTime;
        lastProcTime += procTime;
    }

    public void addResultItem(int tryCnt, long code, double procTime, boolean isEnergyDetect, long energyDetectTime,
                              boolean detection, boolean decoding, double snr, double preambleJcsMar,
                              int dataJcsParRatioGeqCounter, int dataJcsParGeqCounter, boolean preambleCsResult,
                              boolean dataCsResult, double currT) {
        if (totRecTryCount <= 0 || testCount < totRecTryCount) {
            testCount++;

            ResultModel model = new ResultModel(testCount, code, procTime, isEnergyDetect, energyDetectTime,
                    detection, decoding, snr, preambleJcsMar, dataJcsParRatioGeqCounter,
                    dataJcsParGeqCounter, preambleCsResult, dataCsResult, currT);
            resultModelList.add(model);

            // data cs par ratio
            symbolDataCsParRatioHistogram[dataJcsParRatioGeqCounter] += 1;
            // data cs par
            symbolDataCsParHistogram[dataJcsParGeqCounter] += 1;

            lastCurrT = currT;
            totCurrT += currT;
            totPreambleJcsMar += preambleJcsMar;
            // 총 디코딩 시간
            totProcTime += procTime;
            // 마지막 디코딩 시간
            lastProcTime += procTime;

            // 에너지 유무
            if (isEnergyDetect) {
                totFindEnergy++;

                // 신호 유무
                if (preambleCsResult) {
                    totFindSig++;
                } else {
                    totNoSig++;
                }

                // good sig
                if (dataCsResult) {
                    totGoodSig++;
                } else {
                    totAmbiSig++;
                }
            } else {
                totNoEnergy++;
            }

            // crc error -> code == -3
            if (code == -3) {
                totCrcErr++;
            }

            // success -> code >= 0
            if (code >= 0) {
                totSuccess++;
            }

            resultView.refreshRecyclerView();
            resultView.refreshData();

            lastProcTime = 0;
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
}
