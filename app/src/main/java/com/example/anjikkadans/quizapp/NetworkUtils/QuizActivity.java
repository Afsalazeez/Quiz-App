package com.example.anjikkadans.quizapp.NetworkUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anjikkadans.quizapp.ChoiceClickCallback;
import com.example.anjikkadans.quizapp.OptionsAdapter;
import com.example.anjikkadans.quizapp.Question;
import com.example.anjikkadans.quizapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends FragmentActivity implements DownloadCallback,ChoiceClickCallback {
    //base url for open trivia quiz api
    private static final String url = "https://opentdb.com/api.php?amount=10&type=multiple";
    //token expires in every 6 hours of inactivity
    private static final String TOKEN_KEY_FOR_URL = "token";
    private static String token = "19805b2c6d72c7521ebe9f636e068bddac9854c3611efa9bc107c702ddeb667c";

    private static final String REQUEST_TOKEN = "https://opentdb.com/api_token.php?command=request";
    private static final String NEW_TOKEN_RESULT_MESSAGE = "Token Generated Succesfully!";

    private NetworkFragment mNetworkFragment;
    private ProgressBar progressBar;

    private static final String CATEGORY_KEY_FOR_URL = "category";
    private static int CATEGORY_ID = 0;

    private static final String RESET_TOKEN = "https://opentdb.com/api_token.php?command=reset";
    private ArrayList<Question> questions;
    Question currentQuestion;
    private String[] currentQuestionChoices;
    private int currentQuestionIndex;
    private int marks;

    private static final String DIFFICULTY_KEY_FOR_URL = "difficulty";
    String difficulty;

    private static Uri builder;

    private boolean mDownloading = false;
    private String outputJSON;


    private OptionsAdapter mOptionsAdapter;
    private ListView optionsListView;

    private TextView questionView;
    private Button passButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionView = (TextView) findViewById(R.id.question_text_view);
        passButton = (Button) findViewById(R.id.pass_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        currentQuestionChoices = new String[4];
        optionsListView = (ListView) findViewById(R.id.options_list_view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        difficulty = sharedPreferences.getString(getString(R.string.pref_difficulty_key),
                getString(R.string.pref_difficulty_medium_value));

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            CATEGORY_ID = intent.getExtras().getInt("CategoryCode");
            if (CATEGORY_ID == 27) {

            }
        }



        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),buildUrlStringforJsonQuestion());
        Log.e("URL",builder.toString());
    }



    public String buildUrlStringforJsonQuestion(){
        builder = Uri.parse(url).buildUpon()
                .appendQueryParameter(CATEGORY_KEY_FOR_URL,String.valueOf(CATEGORY_ID))
                .appendQueryParameter(DIFFICULTY_KEY_FOR_URL,difficulty)
                .appendQueryParameter(TOKEN_KEY_FOR_URL,token)
                .build();
        return builder.toString();
    }





    @Override
    public void updateFromDownload(Object result) {
        Log.v("QuizActivity","updated textView with JSON String");

        String resultJson = (String) result;
        outputJSON = resultJson;
        Log.v("QuizActivity","JSON : "+resultJson);


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

        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();

        }
        parseJSON(outputJSON);
    }

    @Override
    public void networkFragmentReady() {
        Log.e("QuizActivity","NetworkFragment onCreate called and ready for downloading");
        mNetworkFragment.startDownload();
    }

    public void parseJSON(String json){
        if (json == null) {
            Log.e("QuizActivity","json null");
        }else{
            Log.e("QuizActivity","jsonString "+ json);
            try {
                JSONObject baseJsonObject = new JSONObject(json);
                int resultCode = baseJsonObject.getInt("response_code");

                Log.e("QuizActivity","response code"+String.valueOf(resultCode));
                if (resultCode == 0) {

                    if (baseJsonObject.has("token")) {
                        Log.v("QuizActivity","new token received found");
                        token = baseJsonObject.getString("token");
                        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),buildUrlStringforJsonQuestion());
                    }else{
                        getQuestions(json);
                    }
                }
                if (resultCode == 3) {

                    Log.e("QuizActivity","response code was found 3");
                    mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(),REQUEST_TOKEN);

                }
                if (resultCode == 4) {
                    Toast.makeText(getApplicationContext(),"result code : 4",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void getQuestions(String JSON){
        try {
            JSONObject baseJsonObject = new JSONObject(JSON);
            JSONArray results = baseJsonObject.getJSONArray("results");
            questions = new ArrayList<>();
            for (int i=0; i<results.length();i++) {
                JSONObject resultObject = (JSONObject) results.get(i);
                String question = resultObject.getString("question");
                String answer = resultObject.getString("correct_answer");
                JSONArray jsonArray = resultObject.getJSONArray("incorrect_answers");
                String choices[] = new String[4];
                for (int j = 0; j<jsonArray.length();j++) {
                    choices[j]=jsonArray.getString(j);
                }
                choices[3] = answer;
                List<String> choice = Arrays.asList(choices);
                Collections.shuffle(choice);
                choices = choice.toArray(new String[choices.length]);
                Question q = new Question(question,choices,answer);
                questions.add(q);
                Log.v("QuizActivity","New question Added");
            }
            currentQuestionIndex = 0;
            marks =0;
            progressBar.setVisibility(View.INVISIBLE);
            AskNextQuestion(currentQuestionIndex);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean choiceClicked(String choice) {
        currentQuestionIndex +=1;
        questionAsked();
        if (choice.equals(currentQuestion.getAnswer())) {

            return true;
        }
        return false;
    }
    public void AskNextQuestion(int index){
        if (index < 0 || index > 9) {
            Toast.makeText(getApplicationContext(),"Marks : "+String.valueOf(marks),Toast.LENGTH_SHORT).show();
            //finishActivity and print result
            return;
        }

        currentQuestion = questions.get(index);
        questionView.setText(currentQuestion.getQuestion());
        currentQuestionChoices = currentQuestion.getChoices();
        mOptionsAdapter = new OptionsAdapter(this, R.layout.quiz_options_view, currentQuestionChoices);

        optionsListView.setAdapter(mOptionsAdapter);

    }
    public void questionAsked(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AskNextQuestion(currentQuestionIndex);
            }
        },2000);

    }
}
