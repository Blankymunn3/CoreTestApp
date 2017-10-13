package io.bitsound.coretestapp.presenters;

import android.support.annotation.NonNull;

import io.bitsound.coretestapp.view.MainView;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class MainPresenter implements Presenter {

    private MainView mainView;

    private boolean preambleCsSelected = true;
    private boolean energyDetectorSelected = true;
    private boolean qokShapingSelected = true;
    private boolean localSyncFinderSelected = true;

    /**
     * 프레임 타입.
     * 0 : QOK22 FrameTypeOne
     * 1 : QOK32(TV) FrameTypeTwo
     * 2 : QOK32(Cinema) FrameTypeThee
     * 3 : QOK32(D2D) FrameTypeFour
     */

    private int frameType = 1;

    private int coreType = 1;
    private int noSigThreshold;
    private int combiningThreshold;
    private double rec;
    private double gamma;
    private int unitBufferSize;
    private int recCount;

    public void setMainView(@NonNull MainView view) {
        this.mainView = view;
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

    public void setCsParam(int noSigThreshold, int combiningThreshold) {
        this.noSigThreshold = noSigThreshold;
        this.combiningThreshold = combiningThreshold;
    }

    public void setDetectParam(double rec, double gamma) {
        this.rec = rec;
        this.gamma = gamma;
    }


    public void setUnitBufferSize(int unitBufferSize) {
        this.unitBufferSize = unitBufferSize;
    }

    public void setRecCount(int recCount) {
        this.recCount = recCount;
    }


    public void startPerformanceRecord() {
        mainView.startPerformanceRecResultActivity(preambleCsSelected, energyDetectorSelected,
                qokShapingSelected, localSyncFinderSelected, frameType, coreType,
                noSigThreshold, combiningThreshold, rec, gamma, unitBufferSize, recCount);
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
