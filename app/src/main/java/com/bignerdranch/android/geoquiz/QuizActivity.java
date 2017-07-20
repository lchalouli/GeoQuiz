package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_paris, true),
            new Question(R.string.question_algerie, true),
            new Question(R.string.question_irlande, false)
    };
    private int mCurrentIndex;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);

            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                mIsCheater = false;
                Question question = mQuestionBank[mCurrentIndex];
                startActivityForResult(CheatActivity.newIntent(QuizActivity.this, question.isAnswerTrue()), REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
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
        }
    }

    private void updateQuestion() {
        Question question = mQuestionBank[mCurrentIndex];
        int questionResId = question.getTextResId();
        mQuestionTextView.setText(questionResId);
        if (question.isAnswered()) {
            enableButtons(false);
        } else {
            enableButtons(true);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        Question question = mQuestionBank[mCurrentIndex];
        boolean answerIsTrue = question.isAnswerTrue();
        int messageResId;
        boolean correctAnswer;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
            correctAnswer = false;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                correctAnswer = true;
            } else {
                messageResId = R.string.incorrect_toast;
                correctAnswer = false;
            }
        }

        enableButtons(false);
        question.setCorrectAnswer(correctAnswer);
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.show();
        if (isCompleteQuestions()) {
            String text = getResources().getString(R.string.graded_text, getGoodAnswerPercentage());
            Log.i(TAG, text);
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void enableButtons(boolean enable) {
        if (mTrueButton != null) {
            mTrueButton.setEnabled(enable);
        }
        if (mFalseButton != null) {
            mFalseButton.setEnabled(enable);
        }
    }



    private double getGoodAnswerPercentage() {
        double goodAnswer = 0;
        for (Question question : mQuestionBank) {
            if (question.getCorrectAnswer()) {
                goodAnswer++;
            }
        }
        double result = goodAnswer / mQuestionBank.length;
        Log.i(TAG, "Result : " + result);
        return result;
    }
    private boolean isCompleteQuestions() {
        for (Question question : mQuestionBank) {
            if (!question.isAnswered()) {
                Log.i(TAG, "isCompleteQuestions is false");
                return false;
            }
        }
        Log.i(TAG, "isCompleteQuestions is true");
        return true;
    }
}
