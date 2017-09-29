package io.bitsound.coretestapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bitsound.coretestapp.R;
import io.bitsound.coretestapp.adepters.ResultAdapter;
import io.bitsound.coretestapp.models.ResultModel;
import io.bitsound.coretestapp.presenters.ResultPresenter;
import io.bitsound.coretestapp.view.ResultView;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultActivity extends AppCompatActivity implements ResultView {

    public static final String EXTRA_PREAMBLE_CS = "extra_preamble_cs";
    public static final String EXTRA_ENERGY_DETECTOR = "extra_energy_detector";
    public static final String EXTRA_QOK_SHAPING = "extra_qok_shaping";
    public static final String EXTRA_LOCAL_SYNC_FINDER = "extra_local_sync_finder";
    public static final String EXTRA_FRAME_TYPE = "extra_frame_type";
    public static final String EXTRA_CORE_TYPE = "extra_core_type";
    public static final String EXTRA_NO_SIG_THRESHOLD = "extra_no_sig_threshold";
    public static final String EXTRA_COMBINING_THRESHOLD = "extra_combining_threshold";
    public static final String EXTRA_REC = "extra_rec";
    public static final String EXTRA_GAMMA = "extra_gamma";
    public static final String EXTRA_UNIT_BUFFER_SIZE = "extra_unit_buffer_size";
    public static final String EXTRA_REC_COUNT = "extra_rec_count";

    private DecimalFormat dataFormat = new DecimalFormat("###.##");

    private ResultPresenter resultPresenter;
    private ResultAdapter resultAdapter;
    private List<ResultModel> resultModelList;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.received_total_textview)
    public TextView receivedTotalText;
    @BindView(R.id.preamble_cs_thres_textview)
    public TextView preambleCsThresText;
    @BindView(R.id.data_cs_thres_textview)
    public TextView dataCsThresText;
    @BindView(R.id.rec_t_textview)
    public TextView recTText;
    @BindView(R.id.post_rec_dec_t_textview)
    public TextView postRecDecTText;
    @BindView(R.id.pre_cs_mar_textview)
    public TextView preCsMarText;
    @BindView(R.id.data_cs_par_ratio_textview)
    public TextView dataCsParRatioText;
    @BindView(R.id.gamma_textview)
    public TextView gammaText;
    @BindView(R.id.avg_tot_decoding_time_textview)
    public TextView avgTotDecodingTimeText;
    @BindView(R.id.avg_tot_decoding_time_histogram_textview)
    public TextView avgTotDecodingTimeHistogramText;
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.info_textview)
    public TextView infoText;

    @BindView(R.id.no_energy_textview)
    public TextView noEnergyText;
    @BindView(R.id.no_sig_textview)
    public TextView noSignalText;
    @BindView(R.id.find_energy_textview)
    public TextView findEnergyText;
    @BindView(R.id.good_sig_textview)
    public TextView goodSignalText;
    @BindView(R.id.find_sig_textview)
    public TextView findSignalText;
    @BindView(R.id.ambig_sig_textview)
    public TextView ambiSignalText;
    @BindView(R.id.crc_err_textview)
    public TextView crcErrText;
    @BindView(R.id.success_textview)
    public TextView successText;
    @BindView(R.id.curr_part_textview)
    public TextView lastProcTimeText;
    @BindView(R.id.avg_part_textview)
    public TextView avgProcTimeText;
    @BindView(R.id.curr_t_textview)
    public TextView currTText;
    @BindView(R.id.avg_t_textview)
    public TextView avgTText;
    @BindView(R.id.post_cs_per_textview)
    public TextView postCsPerText;
    @BindView(R.id.no_cs_per_textview)
    public TextView noCsPerText;
    @BindView(R.id.spreading_time)
    public TextView spreadingTimeText;
    @BindView(R.id.riciank_factor)
    public TextView ricianKFactorText;
    @BindView(R.id.freqline_slope)
    public TextView freqLineSlopeText;
    @BindView(R.id.freqline_mse_db)
    public TextView freqLineMsedBText;
    @BindView(R.id.ed_snr_db)
    public TextView edSnrdBText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        resultPresenter = new ResultPresenter();
        resultPresenter.setResultView(this);
        resultPresenter.setContext(this);

        toolbar.setTitle(getString(R.string.result_activity_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultModelList = new ArrayList<>();
        resultPresenter.setResultModelList(resultModelList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ResultActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        resultAdapter = new ResultAdapter(ResultActivity.this, resultModelList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(resultAdapter);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();

        boolean preambleCsSelected = intent.getBooleanExtra(ResultActivity.EXTRA_PREAMBLE_CS,
                resultPresenter.isPreambleCsSelected());
        boolean energyDetectorSelected = intent.getBooleanExtra(ResultActivity.EXTRA_ENERGY_DETECTOR,
                resultPresenter.isEnergyDetectorSelected());
        boolean qokShapingSelected = intent.getBooleanExtra(ResultActivity.EXTRA_QOK_SHAPING,
                resultPresenter.isQokShapingSelected());
        boolean localSyncFinderSelected = intent.getBooleanExtra(ResultActivity.EXTRA_LOCAL_SYNC_FINDER,
                resultPresenter.isLocalSyncFinderSelected());
        int frameType = intent.getIntExtra(ResultActivity.EXTRA_FRAME_TYPE,
                resultPresenter.getFrameType());
        int coreType = intent.getIntExtra(ResultActivity.EXTRA_CORE_TYPE,
                resultPresenter.getCoreType());
        int noSigThreshold = intent.getIntExtra(ResultActivity.EXTRA_NO_SIG_THRESHOLD,
                resultPresenter.getNoSigThreshold());
        int combiningThreshold = intent.getIntExtra(ResultActivity.EXTRA_COMBINING_THRESHOLD,
                resultPresenter.getCombiningThreshold());
        double rec = intent.getDoubleExtra(ResultActivity.EXTRA_REC,
                resultPresenter.getRec());
        double gamma = intent.getDoubleExtra(ResultActivity.EXTRA_GAMMA,
                resultPresenter.getGamma());
        int unitBufferSize = intent.getIntExtra(ResultActivity.EXTRA_UNIT_BUFFER_SIZE,
                resultPresenter.getUnitBufferSize());
        int recCount = intent.getIntExtra(ResultActivity.EXTRA_REC_COUNT,
                resultPresenter.getTotRecTryCount());

        resultPresenter.setPreambleCsSelected(preambleCsSelected);
        resultPresenter.setEnergyDetectorSelected(energyDetectorSelected);
        resultPresenter.setQokShapingSelected(qokShapingSelected);
        resultPresenter.setLocalSyncFinderSelected(localSyncFinderSelected);
        resultPresenter.setFrameType(frameType);
        resultPresenter.setCoreType(coreType);
        resultPresenter.setNoSigThreshold(noSigThreshold);
        resultPresenter.setCombiningThreshold(combiningThreshold);
        resultPresenter.setRec(rec);
        resultPresenter.setGamma(gamma);
        resultPresenter.setUnitBufferSize(unitBufferSize);
        resultPresenter.setTotRecTryCount(recCount);

        resultPresenter.initSoundllySdk();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            case R.id.menu_play:
                resultPresenter.startSoundllySDK();
                return true;
            case R.id.menu_stop:
                resultPresenter.stopSoundllySdk();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        this.resultPresenter.resume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("");
        intentFilter.addAction("ACTION_CORE_INIT");
        intentFilter.addAction("ACTION_CORE_PARTIAL_DECODE");
        intentFilter.addAction("ACTION_CORE_DONE_DECODE");
        LocalBroadcastManager.getInstance(this).registerReceiver(soundllySDKBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.resultPresenter.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.resultPresenter.destroy();
    }

    @Override
    public void refreshRecyclerView() {
        resultAdapter.notifyDataSetChanged();
        Log.i("TAG", "refreshRecyclerView()");
    }

    @Override
    public void showMicPermissionError() {
        Toast.makeText(ResultActivity.this, getString(R.string.mic_permission_denied), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshData() {
        // reveived total
        receivedTotalText.setText(String.valueOf(resultPresenter.getTestCount()));
        // preamble cs thres
        preambleCsThresText.setText(String.valueOf(resultPresenter.getPreambleCsThres()));
        // data cs thres
        dataCsThresText.setText(resultPresenter.getNoSigThres() + ", " + resultPresenter.getCombiningThres());
        // recT(avr)
        recTText.setText("0(0.0) ms");
        // post rec decT(avr)
        postRecDecTText.setText("0(0,0) ms");
        // preCS MAR
        preCsMarText.setText(String.format("%.3f", resultPresenter.getAvgPreCsMar()));
        // gamma
        gammaText.setText(String.valueOf(resultPresenter.getGamma()));
        int[] symbolDataCsParRatioHistogram = resultPresenter.getSymbolDataCsParRatioHistogram();

        int[] symbolDataCsParHistogram = resultPresenter.getSymbolDataCsParHistogram();

        int symbolSize = resultPresenter.getSymbolNum() + 1;

        StringBuilder sb = new StringBuilder();

        sb.append("DataCs ParRatio(Par) : ");

        for (int i = 0; i < symbolSize; i++) {
            sb.append("[")
                    .append(i)
                    .append("]: ")
                    .append(symbolDataCsParRatioHistogram[i])
                    .append("(")
                    .append(symbolDataCsParHistogram[i])
                    .append("), ");
        }

        sb.deleteCharAt(sb.length() - 2);

        dataCsParRatioText.setText(sb.toString());

        // no energy
        noEnergyText.setText(String.valueOf(resultPresenter.getTotNoEnergy()));
        // find energy
        findEnergyText.setText(String.valueOf(resultPresenter.getTotFindEnergy()));
        // no signal
        noSignalText.setText(String.valueOf(resultPresenter.getTotNoSig()));
        // good signal
        goodSignalText.setText(String.valueOf(resultPresenter.getTotGoodSig()));
        // find signal
        findSignalText.setText(String.valueOf(resultPresenter.getTotFindSig()));
        // ambiguous signal
        ambiSignalText.setText(String.valueOf(resultPresenter.getTotAmbiSig()));
        // crc error
        crcErrText.setText(String.valueOf(resultPresenter.getTotCrcErr()));
        // success
        successText.setText(String.valueOf(resultPresenter.getTotSuccess()));
        // curr t
        currTText.setText(dataFormat.format(resultPresenter.getLastCurrT()) + " dB");
        // avg t
        avgTText.setText(dataFormat.format(resultPresenter.getAvgCurrT()) + " dB");
        // curr par t
        lastProcTimeText.setText(dataFormat.format(resultPresenter.getLastProcTime()) + " ms");
        // avg par t
        avgProcTimeText.setText(dataFormat.format(resultPresenter.getAvgProcTime()) + " ms");
        // post cs per
        postCsPerText.setText(dataFormat.format(resultPresenter.getPostCsPer()) + " %");
        // no cs per
        noCsPerText.setText(dataFormat.format(resultPresenter.getNoCsPer()) + " %");
        // Spreading Time
        spreadingTimeText.setText(dataFormat.format(resultPresenter.getSpreadingTime()) + " s");
        // Rician K Factor
        ricianKFactorText.setText(dataFormat.format(resultPresenter.getRicianKFactor()) + " dB");
        // Freq Line Slope
        freqLineSlopeText.setText(dataFormat.format(resultPresenter.getFreqLineSlope()));
        // Freq Line MSE
        freqLineMsedBText.setText(dataFormat.format(resultPresenter.getFreqLineMSEdB()) + " dB");
        // ED SNR dB
        edSnrdBText.setText(dataFormat.format(resultPresenter.getEdSNRdB()) + " dB");



        long[] totReceivedTimeHistogram = resultPresenter.getTotReceivedTimeHistogram();
        int[] tryCountHistogram = resultPresenter.getTryCountHistogram();

        sb = new StringBuilder();

        sb.append("avg TotDecodingTimeHistogram : ");

        double totDecodingTime = 0;
        int totTryCnt = 0;

        for (int i = 0; i < totReceivedTimeHistogram.length; i++) {
            if (tryCountHistogram[i] == 0) {
                sb.append("0, ");
            } else {
                double recTime = (double) totReceivedTimeHistogram[i] / (double) tryCountHistogram[i];
                sb.append(dataFormat.format(recTime))
                        .append("(")
                        .append(tryCountHistogram[i])
                        .append(")")
                        .append(", ");

                totTryCnt += tryCountHistogram[i];
                double tmp = recTime * (double) tryCountHistogram[i];

                totDecodingTime += tmp;
            }
        }

        if (totDecodingTime == 0) {
            avgTotDecodingTimeText.setText("0");
        } else {
            double avg = (double) totDecodingTime / (double) totTryCnt;
            avgTotDecodingTimeText.setText(dataFormat.format(avg) + "(" + totTryCnt + ")");
        }

        sb.deleteCharAt(sb.length() - 2);

        avgTotDecodingTimeHistogramText.setText(sb.toString());

    }

    private BroadcastReceiver soundllySDKBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if ("ACTION_CORE_INIT".equals(action)) {
                double coreVer = intent.getDoubleExtra("extra_core_version", -1.0f);
                String buildDateStr = "None";

                try {
                    ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
                    File zf = new File(ai.sourceDir);
                    long time = zf.lastModified();
                    SimpleDateFormat buildDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    buildDateStr = buildDate.format(new java.util.Date(time));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                infoText.setText("CORE VER : " + coreVer + ", MODEL : " + Build.MODEL + ",\n" + "BUILD DATE : " + buildDateStr);

                boolean coreInit = intent.getBooleanExtra("extra_core_init", false);
                double gamma = intent.getDoubleExtra("extra_gamma", 0.0f);
                int symbolNum = intent.getIntExtra("extra_symbol_num", 0);
                double preambleCsThres = intent.getDoubleExtra("extra_preamble_cs_thres", 0.0f);
                double noSigThres = intent.getDoubleExtra("extra_no_sig_thres", 0.0f);
                double combiningThres = intent.getDoubleExtra("extra_combining_thres", 0.0f);

                if (!coreInit) {
                    Toast.makeText(ResultActivity.this, getString(R.string.failed_core_init), Toast.LENGTH_SHORT).show();
                } else {
                    resultPresenter.setSymbolNum(symbolNum);
                    resultPresenter.setGamma(gamma);
                    resultPresenter.setPreambleCsThres(preambleCsThres);
                    resultPresenter.setNoSigThres(noSigThres);
                    resultPresenter.setCombiningThres(combiningThres);

                    refreshData();

                    resultPresenter.startSoundllySDK();

                }
            } else if ("ACTION_CORE_PARTIAL_DECODE".equals(action)) {
                double time = intent.getDoubleExtra("extra_time", 0.0f);
                Log.i("ACTION_PARTIAL_DECODE", String.valueOf(time));
                resultPresenter.addProcTime(time);
            } else if ("ACTION_CORE_DONE_DECODE".equals(action)) {
                // tryCnt 사용 안함. 숫자가 안 맞아서 따로 계산
                int tryCnt = intent.getIntExtra("extra_try_cnt", -1);
                long code = intent.getLongExtra("extra_code", -100);
                double procTime = intent.getDoubleExtra("extra_time", 0.0f);
                boolean isEnergyDetect = intent.getBooleanExtra("extra_is_energy_detect", false);
                long energyDetectTime = intent.getLongExtra("extra_energy_detect_time", 0);
                boolean detection = intent.getBooleanExtra("extra_detection", false);
                boolean decoding = intent.getBooleanExtra("extra_decoding", false);
                double snr = intent.getDoubleExtra("extra_snr", 0.0f);
                double preambleJcsMar = intent.getDoubleExtra("extra_preamble_jcs_mar", 0.0f);
                int dataJcsParRatioGeqCounter = intent.getIntExtra("extra_data_jcs_par_ratio_geq_counter", 0);
                int dataJcsParGeqCounter = intent.getIntExtra("extra_data_jcs_par_geq_counter", 0);
                boolean preambleCsResult = intent.getBooleanExtra("extra_preamble_cs_result", false);
                boolean dataCsResult = intent.getBooleanExtra("extra_data_cs_result", false);
                double currT = intent.getDoubleExtra("extra_curr_t", 0.0f);
                long totReceivedTime = intent.getLongExtra("extra_tot_received_time", -1);
                double spreadingTime = intent.getDoubleExtra("extra_spreading_time", 0.0);
                double ricianKFactor = intent.getDoubleExtra("extra_riciankfactordB", 0.0);
                double freqLineSlope = intent.getDoubleExtra("extra_freqlineslope", 0.0);
                double freqLineMSEdB = intent.getDoubleExtra("extra_freqlinemsedB", 0.0);
                double edSNRdB = intent.getDoubleExtra("extra_edSNRdB", 0.0);

                Log.i("ACTION_DONE_DECODE D", String.valueOf(procTime));
                Log.i("ACTION_DONE_DECODE E", String.valueOf(energyDetectTime));

                resultPresenter.addResultItem(tryCnt, code, procTime,
                        isEnergyDetect, energyDetectTime, detection, decoding, snr, preambleJcsMar,
                        dataJcsParRatioGeqCounter, dataJcsParGeqCounter, preambleCsResult, dataCsResult,
                        currT, totReceivedTime, spreadingTime, ricianKFactor, freqLineSlope, freqLineMSEdB, edSNRdB);
            }
        }
    };
}
