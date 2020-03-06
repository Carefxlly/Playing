package com.example.xrecyclerview;

/**
 * Created by wherevere on 19/5/5.
 */

interface XBaseRefresh {
    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;

    void onMove(float delta);
    void refreshComplate();
    int getVisiableHeight();
    boolean releaseAction();
}
