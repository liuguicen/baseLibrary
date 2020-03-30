package com.lgc.baselibrary.UIWidgets;

public interface ProgressCallback {
    public void progress(float percentage);
    public void msg(String msg);

    void msg(String s, boolean isShort);
}
