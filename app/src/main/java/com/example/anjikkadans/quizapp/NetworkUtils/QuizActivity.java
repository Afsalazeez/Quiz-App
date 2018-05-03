package com.example.anjikkadans.quizapp.NetworkUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anjikkadans.quizapp.R;

public class QuizActivity extends FragmentActivity implements DownloadCallback {

    private static final String url = "https://opentdb.com/api.php?amount=10&type=multiple";

    private NetworkFragment mNetworkFragment;

    private boolean mDownloading = false;

    TextView textView;
    Button button;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textView = (TextView) findViewById(R.id.jsonTextView);
        button = (Button) findViewById(R.id.checkbutton);
        progressBar = (ProgressBar) findViewById(R.id.pb);

        progressBar.setVisibility(View.INVISIBLE);
        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),url);
    }

    public void StartDownload(View view){
        progressBar.setVisibility(View.VISIBLE);
        mNetworkFragment.startDownload();
    }

    @Override
    public void updateFromDownload(Object result) {
        String resultJson = (String) result;
        textView.setText(resultJson);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {
        progressBar.setVisibility(View.INVISIBLE);
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
