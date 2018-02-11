package com.example.paul.myandroidparctice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
//        if(fragment==null){
//            fragment = new CrimeFragment();
//            fm.beginTransaction()
//                    .add(R.id.fragment_container,fragment)
//                    .commit();
//        }
//    }
    private static final String EXTRA_CRIME_ID = "crime_id";
    private static final String EXTRA_POSITION = "position";

    public static Intent newIntent(Context packageContext,UUID crimeId,int position){
        Intent intent = new Intent(packageContext,CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        intent.putExtra(EXTRA_POSITION,position);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        //return new CrimeFragment();
        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        int position = (int)getIntent().getSerializableExtra(EXTRA_POSITION);
        return CrimeFragment.newInstance(crimeId,position);
    }
}
