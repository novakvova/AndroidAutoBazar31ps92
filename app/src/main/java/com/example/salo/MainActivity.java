package com.example.salo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        EditText textName = findViewById(R.id.txtName);
        TextView viewTextInfo = findViewById(R.id.viewTextInfo);
        String text = textName.getText().toString();
        viewTextInfo.setText(text);
        Toast.makeText(
                MainActivity.this,
                text, Toast.LENGTH_LONG
        ).show();
    }
}
