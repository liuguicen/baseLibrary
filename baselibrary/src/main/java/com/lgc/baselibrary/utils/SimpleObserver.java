package com.lgc.baselibrary.utils;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *      author : liuguicen
 *      time : 2019/03/19
 *      version : 1.0
 * <pre>
 */
abstract public class SimpleObserver<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        Log.e("SimpleObserver",e.getMessage());
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}
