package com.webservice;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by vange on 2017/8/31.
 */

public abstract class MyObserve<T> implements Observer<T> {
    Disposable d;
    Object tag;

    public MyObserve(Object tag) {
        this.tag = tag;
    }

    public MyObserve() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (tag != null)
            RxLifeUtils.getInstance().add(tag, d);
        tag = null;
    }

    @Override
    public void onError(Throwable e) {
        Webservice.Log(e.getMessage());
    }

    @Override
    public void onComplete() {
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }
}

