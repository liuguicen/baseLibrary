package com.lgc.baselibrary.baseComponent;

import android.content.Context;
import android.view.View;

/**
 * <pre>
 *      author : liuguicen
 *      time : 2018/12/30
 *      version : 1.0
 * <pre>
 */

public class BaseContract {
    /**
     * Created by LiuGuicen on 2017/1/5 0005.
     */
    public interface BasePresenter {
        void start() throws Exception;
    }

    public interface BaseView extends View.OnClickListener{

    }
}
