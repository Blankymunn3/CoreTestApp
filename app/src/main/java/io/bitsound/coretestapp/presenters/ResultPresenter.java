package io.bitsound.coretestapp.presenters;

import android.support.annotation.NonNull;

import java.util.List;

import io.bitsound.coretestapp.models.ResultModel;
import io.bitsound.coretestapp.view.ResultView;

/**
 * Created by soundlly on 2017. 9. 28..
 */

public class ResultPresenter implements Presenter {
    private ResultView resultView;
    private List<ResultModel> resultModelList;

    public void setResultView(@NonNull ResultView view) {
        this.resultView = view;
    }

    public void setResultModelList(List<ResultModel> resultModelList) {
        this.resultModelList = resultModelList;
    }

    public void initSoundllySdk() {

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
