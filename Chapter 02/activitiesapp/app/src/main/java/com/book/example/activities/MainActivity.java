package com.book.example.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.function.BiFunction;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_AN_ANSWER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_about).setOnClickListener(this::onAboutButtonClicked);
        findViewById(R.id.btn_question).setOnClickListener(this::onQuestionButtonClicked);
        findViewById(R.id.btn_web).setOnClickListener(this::onWebButtonClicked);
    }

    private void onWebButtonClicked(View view) {
        final Intent webIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.bpbonline.com"));
        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            Log.d("MainActivity", "No browser is installed.", ex);
        }
    }

    private void onQuestionButtonClicked(View view) {
        final Intent questionIntent = new Intent(
                this,
                QuestionActivity.class);
        startActivityForResult(questionIntent, REQUEST_AN_ANSWER);
    }

    private void onAboutButtonClicked(View view) {
        final Intent aboutIntent = new Intent(
                this,
                AboutActivity.class);
        startActivity(aboutIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_AN_ANSWER) {
            setMessage(
                resultCode == RESULT_OK
                    ? getString(R.string.answer_is, data.getStringExtra("answer"))
                    : getString(R.string.hello_world)
            );
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setMessage(String text) {
        ((TextView) findViewById(R.id.txt_mainMessage))
                .setText(text);
    }
}