package io.bitsound.coretestapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        resultPresenter = new ResultPresenter();
        resultPresenter.setResultView(this);

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

        refreshRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_play:
                return true;
            case R.id.menu_pause:
                return true;
            case R.id.menu_reset:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void refreshRecyclerView() {
        resultAdapter.notifyDataSetChanged();
    }

}
