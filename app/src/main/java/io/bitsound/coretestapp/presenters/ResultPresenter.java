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
    private double unitBufferSize;
    private int recCount;

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

    public double getUnitBufferSize() {
        return unitBufferSize;
    }

    public void setUnitBufferSize(double unitBufferSize) {
        this.unitBufferSize = unitBufferSize;
    }

    public int getRecCount() {
        return recCount;
    }

    public void setRecCount(int recCount) {
        this.recCount = recCount;

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
