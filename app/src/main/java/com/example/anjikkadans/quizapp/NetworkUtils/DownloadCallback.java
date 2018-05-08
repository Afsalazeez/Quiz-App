package com.example.anjikkadans.quizapp.NetworkUtils;

import android.net.NetworkInfo;

/**
 * Created by Anjikkadan's on 4/28/2018.
 */

public interface DownloadCallback<T> {
    interface Progress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int PROCESS_INPUT_STREAM_SUCCESS = 3;
    }
    void updateFromDownload(T result);

    //getting the system's newtork state
    NetworkInfo getActiveNetworkInfo();

    void onProgressUpdate(int progressCode, int percentComplete);

    void finishDownloading();

    void networkFragmentReady();
}
