package com.example.anjikkadans.quizapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

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

    private static final String animal_description ="Play quiz on animals";
    private static final String nature_description = "Check your knowledge about science and nature";
    private static final String sports_description = "Passionate about sports? Here awaits your questions";
    private static final String movie_description = "You a chain movie watcher? Hollywood is here";

    private RecyclerView mListView;

    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mListView = (RecyclerView) findViewById(R.id.options_grid_view);
        //category object creation
        ArrayList<CategoryDet> categoryDets = new ArrayList<>();
        categoryDets.add(new CategoryDet(R.drawable.animal_crop,"Animals"));
        categoryDets.add(new CategoryDet(R.drawable.nature,"Nature"));
        categoryDets.add(new CategoryDet(R.drawable.sports_clipart,"Sports"));
        categoryDets.add(new CategoryDet(R.drawable.movie_clipart,"Movie"));

        mCategoryAdapter = new CategoryAdapter(categoryDets);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(recyclerViewLayoutManager);
        mListView.setAdapter(mCategoryAdapter);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mFAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user signed in
                    Toast.makeText(MainActivity.this,"Welcome to Quizophile ",Toast.LENGTH_SHORT).show();
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
            default:

        }
        return super.onOptionsItemSelected(item);

    }
}
