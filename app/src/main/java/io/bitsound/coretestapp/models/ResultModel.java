package io.bitsound.coretestapp.models;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultModel {
    private int tryCnt;
    private long code;
    private double procTime;
    private boolean isEnergyDetect;
    private long energyDetectTime;
    private boolean detection;
    private boolean decoding;
    private double currT;
    private boolean preambleCsResult;
    private double preambleJcsMar;
    private boolean dataCsResult;
    private int dataJcsParRatioGeqCounter;
    private int dataJcsParGeqCounter;
    private double snr;

    public ResultModel(int tryCnt, long code, double procTime, boolean isEnergyDetect, long energyDetectTime,
                       boolean detection, boolean decoding, double snr, double preambleJcsMar, int
                               dataJcsParRatioGeqCounter, int dataJcsParGeqCounter, boolean preambleCsResult,
                       boolean dataCsResult, double currT) {
        this.tryCnt = tryCnt;
        this.code = code;
        this.procTime = procTime;
        this.isEnergyDetect = isEnergyDetect;
        this.energyDetectTime = energyDetectTime;
        this.detection = detection;
        this.decoding = decoding;
        this.currT = currT;
        this.preambleCsResult = preambleCsResult;
        this.preambleJcsMar = preambleJcsMar;
        this.dataCsResult = dataCsResult;
        this.dataJcsParRatioGeqCounter = dataJcsParRatioGeqCounter;
        this.dataJcsParGeqCounter = dataJcsParGeqCounter;
        this.snr = snr;
    }

    public int getTryCnt() {
        return tryCnt;
    }

    public void setTryCnt(int tryCnt) {
        this.tryCnt = tryCnt;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public double getProcTime() {
        return procTime;
    }

    public void setProcTime(double procTime) {
        this.procTime = procTime;
    }

    public boolean isEnergyDetect() {
        return isEnergyDetect;
    }

    public void setEnergyDetect(boolean energyDetect) {
        isEnergyDetect = energyDetect;
    }

    public long getEnergyDetectTime() {
        return energyDetectTime;
    }

    public void setEnergyDetectTime(long energyDetectTime) {
        this.energyDetectTime = energyDetectTime;
    }

    public boolean isDetection() {
        return detection;
    }

    public void setDetection(boolean detection) {
        this.detection = detection;
    }

    public boolean isDecoding() {
        return decoding;
    }

    public void setDecoding(boolean decoding) {
        this.decoding = decoding;
    }

    public double getCurrT() {
        return currT;
    }

    public void setCurrT(double currT) {
        this.currT = currT;
    }

    public boolean isPreambleCsResult() {
        return preambleCsResult;
    }

    public void setPreambleCsResult(boolean preambleCsResult) {
        this.preambleCsResult = preambleCsResult;
    }

    public double getPreambleJcsMar() {
        return preambleJcsMar;
    }

    public void setPreambleJcsMar(double preambleJcsMar) {
        this.preambleJcsMar = preambleJcsMar;
    }

    public boolean isDataCsResult() { return dataCsResult;
    }

    public void setDataCsResult(boolean dataCsResult) {
        this.dataCsResult = dataCsResult;
    }

    public int getDataJcsParRatioGeqCounter() {
        return dataJcsParRatioGeqCounter;
    }

    public void setDataJcsParRatioGeqCounter(int dataJcsParRatioGeqCounter) {
        this.dataJcsParRatioGeqCounter = dataJcsParRatioGeqCounter;
    }

    public int getDataJcsParGeqCounter() {
        return dataJcsParGeqCounter;
    }

    public void setDataJcsParGeqCounter(int dataJcsParGeqCounter) {
        this.dataJcsParGeqCounter = dataJcsParGeqCounter;
    }

    public double getSnr() {
        return snr;
    }

    public void setSnr(double snr) {
        this.snr = snr;
    }
}
