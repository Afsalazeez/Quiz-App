package com.example.anjikkadans.quizapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.anjikkadans.quizapp.NetworkUtils.QuizActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mFAuthStateListener;
    private static int RC_SIGN_IN =123;

    private GridView mGridView;

    private static final int animalCode = 27;

    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mGridView = (GridView) findViewById(R.id.options_grid_view);
        //category object creation
        List<CategoryDet> categoryDets = new ArrayList<>();

        mCategoryAdapter = new CategoryAdapter(this,R.layout.button_layout,categoryDets);
        mGridView.setAdapter(mCategoryAdapter);

        mCategoryAdapter.add(new CategoryDet(R.drawable.animal_crop,"Animals"));
        mCategoryAdapter.add(new CategoryDet(R.drawable.nature,"Nature"));
        mCategoryAdapter.add(new CategoryDet(R.drawable.sports_clipart,"Sports"));
        mCategoryAdapter.add(new CategoryDet(R.drawable.movie_clipart,"Movie"));



        mFirebaseAuth = FirebaseAuth.getInstance();

        mFAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed in

                }else{
                    //user signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setLogo(R.drawable.quizophile_logo_transparent)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))

                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {

                switch (pos){
                    case 0:
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        if (preferences.getString(getString(R.string.pref_difficulty_key),
                                getString(R.string.pref_difficulty_medium_value))
                                .equals(getString(R.string.pref_difficulty_easy_value))){
                            Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(getString(R.string.conditional_message))
                                    .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }else{
                            Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                            intent.putExtra("CategoryCode",27);
                            startActivity(intent);
                        }
                        break;

                    case 1:
                        Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                        intent.putExtra("CategoryCode",17);
                        startActivity(intent);
                    case 2:
                    case 3:
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFAuthStateListener != null) {
            mFirebaseAuth.addAuthStateListener(mFAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mFAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout_menu :
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
            default:

        }
        return super.onOptionsItemSelected(item);

    }
}
