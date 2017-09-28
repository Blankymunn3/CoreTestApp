package io.bitsound.coretestapp.models;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultModel {
    private int tryCnt;
    private int isEnergyDetect;
    private double currT;
    private int preambleCsResult;
    private double preCsMar;
    private int dataCsResult;
    private double dataCsParRatio;

    public ResultModel(int tryCnt, int isEnergyDetect, double currT, int preambleCsResult,
                       double preCsMar, int dataCsResult, double dataCsParRatio) {
        this.tryCnt = tryCnt;
        this.isEnergyDetect = isEnergyDetect;
        this.currT = currT;
        this.preambleCsResult = preambleCsResult;
        this.preCsMar = preCsMar;
        this.dataCsResult = dataCsResult;
        this.dataCsParRatio = dataCsParRatio;
    }

    public int getTryCnt() {
        return tryCnt;
    }

    public int getIsEnergyDetect() {
        return isEnergyDetect;
    }

    public double getCurrT() {
        return currT;
    }

    public int getPreambleCsResult() {
        return preambleCsResult;
    }

    public double getPreCsMar() {
        return preCsMar;
    }

    public int getDataCsResult() {
        return dataCsResult;
    }

    public double getDataCsParRatio() {
        return dataCsParRatio;
    }
}
