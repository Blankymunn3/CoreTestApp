package io.bitsound.coretestapp.adepters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.bitsound.coretestapp.R;
import io.bitsound.coretestapp.models.ResultModel;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<ResultModel> resultModelList;
    private Context mContext;

    private DecimalFormat dataFormat = new DecimalFormat("#.00");

    public ResultAdapter(Context context, List<ResultModel> resultModelList) {
        this.resultModelList = resultModelList;
        this.mContext = context;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup viewGroup, int pos) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_result, viewGroup, false);
        ResultViewHolder viewHolder = new ResultViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder resultViewHolder, int pos) {
        ResultModel resultModel = resultModelList.get(pos);

        resultViewHolder.tryCntView.setText(String.valueOf(resultModel.getTryCnt()));
        resultViewHolder.decodingResultView.setText(String.valueOf(resultModel.getCode()));
        resultViewHolder.isEnergyDetectView.setText(String.valueOf(resultModel.isEnergyDetect()));
        resultViewHolder.preCsMarView.setText(dataFormat.format(resultModel.getPreambleJcsMar()));
        resultViewHolder.dataCsResultView.setText(String.valueOf(resultModel.isDataCsResult()));
        resultViewHolder.currTView.setText(dataFormat.format(resultModel.getCurrT()) + " dB");
        resultViewHolder.preCsMarView.setText(dataFormat.format(resultModel.getPreambleJcsMar()));
        resultViewHolder.dataCsParRatioView.setText(dataFormat.format(resultModel.getDataJcsParRatioGeqCounter())
                + "(" + dataFormat.format(resultModel.getDataJcsParGeqCounter()) + ")");
    }

    @Override
    public int getItemCount() {
        return (null != resultModelList ? resultModelList.size() : 0);
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.try_count_textview)
        public TextView tryCntView;
        @BindView(R.id.decoding_result_textview)
        public TextView decodingResultView;
        @BindView(R.id.is_energy_detect_textview)
        public TextView isEnergyDetectView;
        @BindView(R.id.curr_t_textview)
        public TextView currTView;
        @BindView(R.id.preamble_cs_result_textview)
        public TextView preambleCsResultView;
        @BindView(R.id.pre_cs_mar_textview)
        public TextView preCsMarView;
        @BindView(R.id.data_cs_result_textview)
        public TextView dataCsResultView;
        @BindView(R.id.data_cs_par_ratio_textview)
        public TextView dataCsParRatioView;


        public ResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
