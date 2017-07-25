package com.innopolis.android.testfirebase.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.innopolis.android.testfirebase.R;
import com.innopolis.android.testfirebase.models.UserNotification;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "LoginActivity";

    private final int REQUEST_CODE = 1;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private Button btnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegistration = (Button) findViewById(R.id.btnRegistration);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.btnRegistration).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        switch (view.getId()) {
            case R.id.btnSignIn:
                if ("".equals(email) || !email.contains("@")) {
                    UserNotification.showMessage(LoginActivity.this, "Wrong Email!!!");
                } else if ("".equals(password) || password.length() < 6) {
                    UserNotification.showMessage(LoginActivity.this,
                            "Password is empty or less than 6!!!");
                }
                else {
                    signin(etEmail.getText().toString(), etPassword.getText().toString());
                }
                break;
            case R.id.btnRegistration:
                intent = new Intent(this, RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Request code - " + requestCode + "result code - " + resultCode);
        if(resultCode == RESULT_OK) {
            etEmail.setText(data.getStringExtra("email"));
            etPassword.setText("");
        }
    }

    public void signin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG, "Соединение прошло успешно!");
                    UserNotification.showMessage(LoginActivity.this, "Авторизация успешна!!!");
                    Intent intent = new Intent(LoginActivity.this, DevicesActivity.class);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Соединение не удалось!");
                    UserNotification.showMessage(LoginActivity.this, "Авторизация провалена!!!");
                }

            }
        });
    }
}
