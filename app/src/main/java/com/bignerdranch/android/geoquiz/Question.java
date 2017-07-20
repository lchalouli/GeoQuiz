package com.bignerdranch.android.geoquiz;

/**
 * Created by lchalouli on 12/07/2017.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private Boolean mCorrectAnswer;

    public Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId;
        this.mAnswerTrue = mAnswerTrue;
    }

    public boolean isAnswered() {
        return mCorrectAnswer != null;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public Boolean getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        mCorrectAnswer = correctAnswer;
    }
}
