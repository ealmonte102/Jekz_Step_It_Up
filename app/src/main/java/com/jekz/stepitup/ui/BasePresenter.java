package com.jekz.stepitup.ui;


/**
 * Created by evanalmonte on 12/10/17.
 */

public interface BasePresenter<T> {
    void onViewAttached(T view);

    void onViewDetached();
}
