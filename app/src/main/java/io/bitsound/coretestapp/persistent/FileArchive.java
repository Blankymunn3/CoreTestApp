package io.bitsound.coretestapp.persistent;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by soundlly on 2017. 9. 29..
 */

public class FileArchive implements Archive {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private File archiveFile;
    private String fileName;
    private String dirPath;
    private String filePath;

    public FileArchive(int histogramSize) throws IOException {
        fileName = dateFormat.format(System.currentTimeMillis()) + "_" + Build.MODEL + ".csv";

        dirPath = Environment.getExternalStorageDirectory() + "/corePerfTest";
        File dir = new File(dirPath);

        if (!dir.exists()) {
            dir.mkdir();
        }

        filePath = dirPath + "/" + fileName;

        archiveFile = new File(filePath);
        archiveFile.createNewFile();

        writeHeader(histogramSize);
    }

    public String getArchiveFilePath() {
        return filePath;
    }

    private void writeHeader(int histogramSize) {
        StringBuilder sb = new StringBuilder();
        sb.append("totReceived,preambleCsThres,noSigThres,combiningThres,avgPreCsMar,gamma,");

        for (int i = 0; i < histogramSize; i++) {
            sb.append("symbolDataCsParRatio")
                    .append(i)
                    .append(",");
        }

        for (int i = 0; i < histogramSize; i++) {
            sb.append("symbolDataCsPar")
                    .append(i)
                    .append(",");
        }

        sb.append("totNoEnergy,totFindEnergy,totProcTime,")
                .append("lastProcTime,totNoSig,totFindSig,totGoodSig,totAmbiSig,")
                .append("totCrcErr,totSuccess,avgCurrT,lastCurrT\n");

        byte[] byteArray = sb.toString().getBytes();

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        try {
            writeFile(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCoreStatistics(int totReceived, double preambleCsThres, double noSigThres,
                                   double combiningThres, double avgPreCsMar, double gamma, int[] symbolDataCsParRatioHistogram,
                                   int[] symbolDataCsParHistogram, int totNoEnergy, int totFindEnergy, double totProcTime,
                                   double lastProcTime, int totNoSig, int totFindSig, int totGoodSig, int totAmbiSig,
                                   int totCrcErr, int totSuccess, double avgCurrT, double lastCurrT) throws IOException {

        StringBuilder sb = new StringBuilder();

        sb.append(totReceived)
                .append(",")
                .append(preambleCsThres)
                .append(",")
                .append(noSigThres)
                .append(",")
                .append(combiningThres)
                .append(",")
                .append(avgPreCsMar)
                .append(",")
                .append(gamma)
                .append(",");

        for (int i = 0; i < symbolDataCsParRatioHistogram.length; i++) {
            sb.append(symbolDataCsParRatioHistogram[i])
                    .append(",");
        }

        for (int i = 0; i < symbolDataCsParHistogram.length; i++) {
            sb.append(symbolDataCsParHistogram[i])
                    .append(",");
        }

        sb.append(totNoEnergy)
                .append(",")
                .append(totFindEnergy)
                .append(",")
                .append(totProcTime)
                .append(",")
                .append(lastProcTime)
                .append(",")
                .append(totNoSig)
                .append(",")
                .append(totFindSig)
                .append(",")
                .append(totGoodSig)
                .append(",")
                .append(totAmbiSig)
                .append(",")
                .append(totCrcErr)
                .append(",")
                .append(totSuccess)
                .append(",")
                .append(avgCurrT)
                .append(",")
                .append(lastCurrT)
                .append("\n");

        byte[] byteArray = sb.toString().getBytes();

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        writeFile(byteBuffer);
    }

    private void writeFile(ByteBuffer byteBuffer) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath, true);
        FileChannel channel = fos.getChannel();

        channel.write(byteBuffer);

        if (channel != null) {
            channel.close();
        }

        if (fos != null) {
            fos.close();
        }
    }
}
