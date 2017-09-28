package com.project.seoulmarket.report.presenter;

import com.project.seoulmarket.report.view.StepThreeView;

/**
 * Created by kh on 2016. 10. 21..
 */
public class FindPresenterImpl implements FindPresenter{

    StepThreeView view;

    public FindPresenterImpl(StepThreeView view) {
        this.view = view;
    }

    @Override
    public void getResultValue() {

        view.changeValue();
    }

    @Override
    public void nullDateValue() {

    }
}
