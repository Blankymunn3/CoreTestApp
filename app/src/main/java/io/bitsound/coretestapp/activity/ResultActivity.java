package io.bitsound.coretestapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static final String EXTRA_CORE_VERSION = "extra_core_version";

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
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.info_textview)
    public TextView infoText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
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

        resultModelList.add(new ResultModel(1, 0, 1.1f, 1, 2.2f, 2, -1.0f));
        resultModelList.add(new ResultModel(2, 1, 0.9f, 3, 0.2f, 0, 2.0f));

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
        double unitBufferSize = intent.getDoubleExtra(ResultActivity.EXTRA_UNIT_BUFFER_SIZE,
                resultPresenter.getUnitBufferSize());
        int recCount = intent.getIntExtra(ResultActivity.EXTRA_REC_COUNT,
                resultPresenter.getRecCount());

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
        resultPresenter.setRecCount(recCount);

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
                return true;
            case R.id.menu_stop:
                return true;
            case R.id.menu_reset:
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
        intentFilter.addAction("ACTION_CORE_VERSION");
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
    }

    private BroadcastReceiver soundllySDKBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if ("ACTION_CORE_VERSION".equals(action)) {
                double coreVer = intent.getDoubleExtra(EXTRA_CORE_VERSION, -1.0f);

                String buildDateStr = "None";

                try {
                    ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
                    File zf = new File(ai.sourceDir);
                    long time = zf.lastModified();
                    SimpleDateFormat buildDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    buildDateStr = buildDate.format(new java.util.Date(time));

                } catch(Exception e) {
                    e.printStackTrace();
                }

                infoText.setText("CORE VER : " + coreVer + ", BUILD DATE : " + buildDateStr);
            }
        }
    };
}
