package com.example.salo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.salo.dto.RegisterDTO;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        TextView login = (TextView)findViewById(R.id.lnkLogin);
        login.setMovementMethod(LinkMovementMethod.getInstance());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SendRegistrationData(View view){
        RegisterDTO data = new RegisterDTO();

        EditText Login = (EditText) findViewById(R.id.txtLogin);
        data.Login = Login.getText().toString().trim();

        EditText Pass = (EditText) findViewById(R.id.txtPassword);
        data.Password = Pass.getText().toString();

        EditText Name = (EditText) findViewById(R.id.txtName);
        data.Name = Name.getText().toString();

        EditText Phone = (EditText) findViewById(R.id.txtPhone);
        data.Phone = Phone.getText().toString();

        if(data.Login.length() == 0 || data.Password.length() == 0)
        {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            String msg = "";
            if(data.Login.length() == 0)
                msg += "Login field is required!\n";
            if(data.Password.length() == 0)
                msg += "Password field is required!\n";
            dlgAlert.setMessage(msg);
            dlgAlert.setTitle("Registration error");
            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) { }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.show();
        }



    }
}
