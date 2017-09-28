package io.bitsound.coretestapp.view;

/**
 * Created by soundlly on 2017. 9. 27..
 */

public interface MainView {

    void startPerformanceRecResultActivity(boolean preambleCsSelected,
                                           boolean energyDetectorSelected, boolean qokShapingSelected,
                                           boolean localSyncFinderSelected, int frameType, int coreType, int noSigThreshold,
                                           int combiningThreshold, double rec, double gamma, double unitBufferSize, int recCount);

}
