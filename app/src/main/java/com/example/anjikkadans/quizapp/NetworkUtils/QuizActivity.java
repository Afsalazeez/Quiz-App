package com.example.anjikkadans.quizapp.NetworkUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anjikkadans.quizapp.R;
import com.example.anjikkadans.quizapp.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class QuizActivity extends FragmentActivity implements DownloadCallback {
    //base url for open trivia quiz api
    private static final String url = "https://opentdb.com/api.php?amount=10&type=multiple";
    //token expires in every 6 hours of inactivity
    private static final String TOKEN_KEY_FOR_URL = "token";
    private static String token = "19805b2c6d72c7521ebe9f636e068bddac9854c3611efa9bc107c702ddeb667c";

    private static final String REQUEST_TOKEN = "https://opentdb.com/api_token.php?command=request";


    private NetworkFragment mNetworkFragment;

    private static final String CATEGORY_KEY_FOR_URL = "category";
    private static int CATEGORY_ID = 0;

    private static final String RESET_TOKEN = "https://opentdb.com/api_token.php?command=reset";


    private static final String DIFFICULTY_KEY_FOR_URL = "difficulty";
    String difficulty;

    private Uri builder;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = sharedPreferences.getString(getString(R.string.pref_difficulty_key),
                getString(R.string.pref_difficulty_medium_value));

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            CATEGORY_ID = intent.getExtras().getInt("CategoryCode");
            if (CATEGORY_ID == 27) {

            }
        }
        builder = Uri.parse(url).buildUpon()
                .appendQueryParameter(CATEGORY_KEY_FOR_URL,String.valueOf(CATEGORY_ID))
                .appendQueryParameter(DIFFICULTY_KEY_FOR_URL,difficulty)
                .appendQueryParameter(TOKEN_KEY_FOR_URL,token)
                .build();

        Log.e("URL",builder.toString());
        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),builder.toString());

    }



    public void StartDownload(View view){
        progressBar.setVisibility(View.VISIBLE);
        Log.v("QuizActivity","StartDownload called");

        mNetworkFragment.startDownload();
    }

    @Override
    public void updateFromDownload(Object result) {
        String resultJson = (String) result;
        textView.setText(resultJson);
        try {
            parseJSON(resultJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        Log.v("QuizActivity","Download finished");
        progressBar.setVisibility(View.INVISIBLE);
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
    public void parseJSON(String jsonString) throws JSONException {
        JSONObject baseJSONObject = new JSONObject(jsonString);
        int responseCode = baseJSONObject.getInt("response_code");
        Log.e("responsecode",String.valueOf(responseCode));
        if (responseCode == 4) {
            Uri buitUri = Uri.parse(RESET_TOKEN);
            buitUri.buildUpon()
                    .appendQueryParameter(TOKEN_KEY_FOR_URL,token)
                    .build();
            Log.e("URL :",buitUri.toString());
            mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),buitUri.toString());
            progressBar.setVisibility(View.VISIBLE);
            mNetworkFragment.startDownload();
        } else if (responseCode == 3) {
            mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),REQUEST_TOKEN);
            progressBar.setVisibility(View.VISIBLE);
            mNetworkFragment.startDownload();
        }

    }


}
