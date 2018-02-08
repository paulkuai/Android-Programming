package com.example.paul.myandroidparctice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.MutableChar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.paul.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.paul.answer_shown";
    private static final String EXTRA_CHEAT_COUNT ="cheat_count";

    private static final String KEY_IS_CHEATER = "is_cheater";

    private int mCheatCount = 0;

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    private TextView mApiLevelTextView;
    private TextView mCheatCountTextView;

    private boolean isCheater = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        mCheatCount = getIntent().getIntExtra(EXTRA_CHEAT_COUNT,0);


        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button)findViewById(R.id.SHOW_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(mAnswerIsTrue){
                        mAnswerTextView.setText(R.string.true_button);
                    }else{
                        mAnswerTextView.setText(R.string.false_button);
                    }
                    setAnswerShowResult(true);
                    isCheater = true;

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                        int cx = mShowAnswerButton.getWidth()/2;
                        int cy = mShowAnswerButton.getHeight()/2;
                        float radius = mShowAnswerButton.getWidth();
                        Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton,cx,cy,radius,0);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mShowAnswerButton.setVisibility(View.INVISIBLE);
                            }
                        });
                        anim.start();
                    }else{
                        mShowAnswerButton.setVisibility(View.INVISIBLE);
                    }

            }
        });

        mApiLevelTextView = (TextView)findViewById(R.id.tv_api_level);
        mApiLevelTextView.setText("API LEVEL "+String.valueOf(Build.VERSION.SDK_INT));

        mCheatCountTextView = (TextView)findViewById(R.id.tv_cheat_count);

        if(savedInstanceState!=null){
            isCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
            setAnswerShowResult(isCheater);
            if(isCheater){
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShowResult(true);
                isCheater = true;
            }
        }

        if(mCheatCount==3){
            mShowAnswerButton.setEnabled(false);
        }else
        {
            mShowAnswerButton.setEnabled(true);
        }

        mCheatCountTextView.setText("剩余次数为"+String.valueOf(3- mCheatCount)+"次");
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue,int cheatCount){
        Intent intent = new Intent(packageContext,CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        intent.putExtra(EXTRA_CHEAT_COUNT,cheatCount);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    private void setAnswerShowResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_CHEATER,isCheater);
    }
}
