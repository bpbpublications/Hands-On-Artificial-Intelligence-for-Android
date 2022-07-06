package com.book.example.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        findViewById(R.id.btn_yes).setOnClickListener(this::onYesButtonClicked);
        findViewById(R.id.btn_no).setOnClickListener(this::onNoButtonClicked);
    }

    private void onNoButtonClicked(View view) {
        returnAnswer("no");
    }

    private void onYesButtonClicked(View view) {
        returnAnswer("yes");
    }

    private void returnAnswer(String answer) {
        final Intent result = new Intent();
        result.putExtra("answer", answer);
        setResult(RESULT_OK, result);
        finish();
    }
}