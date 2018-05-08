package com.example.anjikkadans.quizapp;

import java.util.ArrayList;

/**
 * Created by Anjikkadan's on 5/7/2018.
 */

public class Question {
    private String question;
    private String choices[];
    private String answer;

    public Question(String question, String choices[], String answer) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices;
    }

    public String getAnswer() {
        return answer;
    }
}
