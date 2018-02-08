package com.example.paul.myandroidparctice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private int mCheatCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called");
        setContentView(R.layout.activity_geo_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

                    checkAnswer(true);

            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LayoutInflater inflater = getLayoutInflater();
//                View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_container));
//                TextView text = (TextView) layout.findViewById(R.id.text);
//                text.setText(R.string.incorrect_toast);
//                Toast toast = new Toast(getApplicationContext());
//                toast.setView(layout);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.setDuration(Toast.LENGTH_LONG);
//                toast.show();
                checkAnswer(false);

            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex != 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                } else {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.check_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start cheat Activity
//                Intent intent = new Intent(QuizActivity.this,CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue,mCheatCount);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });


        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mQuestionTextView.setText(mQuestionBank[mCurrentIndex].getTextResId());
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
        }

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putBoolean(KEY_IS_CHEATER,mIsCheater);
    }

    private void updateQuestion() {
        try {
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mIsCheater = mQuestionBank[mCurrentIndex].isCheated();
            mQuestionTextView.setText(question);
            if (mQuestionBank[mCurrentIndex].isAnswered()) {
                //Toast.makeText(QuizActivity.this, "You can just answer this question once!", Toast.LENGTH_SHORT).show();
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            } else {
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "Index was out of bounds.", e);
        }

    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (mQuestionBank[mCurrentIndex].isAnswered()) {
                Toast.makeText(QuizActivity.this, "This question is answered!", Toast.LENGTH_SHORT).show();
            } else {
                if (userPressedTrue == answerIsTrue) {
                    messageResId = R.string.correct_toast;
                } else {
                    messageResId = R.string.incorrect_toast;
                }
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        mQuestionBank[mCurrentIndex].setAnswered(true);
        mQuestionBank[mCurrentIndex].setCheated(mIsCheater);
        updateQuestion();
        checkisAllAnswered();

    }

    /**
     * 每次答完题后检查是否全部答完，如全部答完则给出百分比结果
     */
    private void checkisAllAnswered() {
        boolean isAllAnswered = true;
        int correctCount = 0;
        for (Question q : mQuestionBank) {
            if (!q.isAnswered()) {
                isAllAnswered = false;
            } else {
                if (q.isAnswerTrue()) {
                    correctCount += 1;
                }
            }
        }
        if (isAllAnswered) {
            String msg = "共计" + mQuestionBank.length + "题，答对" + String.valueOf(correctCount) + "题，答题正确率为" + new DecimalFormat("#.00").format((correctCount * 1.00 / mQuestionBank.length) * 100) + "%";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if(mIsCheater == true){
                mCheatCount +=1;
            }
        }
    }

}
