package io.bitsound.coretestapp.persistent;

/**
 * Created by soundlly on 2017. 9. 29..
 */

public interface Archive {
    void saveCoreStatistics(int totReceived, double preambleCsThres, double noSigThres,
                            double combiningThres, double avgPreCsMar, double gamma, int[] symbolDataCsParRatioHistogram,
                            int[] symbolDataCsParHistogram, int totNoEnergy, int totFindEnergy, double totProcTime,
                            double lastProcTime, int totNoSig, int totFindSig, int totGoodSig, int totAmbiSig,
                            int totCrcErr, int totSuccess, double avgCurrT, double lastCurrT) throws Exception;
}
