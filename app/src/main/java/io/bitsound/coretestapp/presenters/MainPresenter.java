package io.bitsound.coretestapp.presenters;

import android.support.annotation.NonNull;

import io.bitsound.coretestapp.view.MainView;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class MainPresenter implements Presenter {

    private MainView mainView;

    private boolean preambleCsSelected;
    private boolean energyDetectorSelected;
    private boolean qokShapingSelected;
    private boolean localSyncFinderSelected;

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
