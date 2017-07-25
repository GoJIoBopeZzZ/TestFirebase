package com.innopolis.android.testfirebase.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.innopolis.android.testfirebase.R;
import com.innopolis.android.testfirebase.models.UserNotification;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "RegistrationActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private CheckBox cbContract;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

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

        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etEmail);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        cbContract = (CheckBox) findViewById(R.id.cbContract);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ("".equals(etName.getText().toString())) {
            Log.e(TAG, "Пустое имя пользователя");
            UserNotification.showMessage(RegistrationActivity.this,
                    "Незаполненное имя!!!");
            etName.requestFocus();
        }
        else if ("".equals(etLastName.getText().toString( ))) {
            Log.e(TAG, "Пустое фамилия пользователя");
            UserNotification.showMessage(RegistrationActivity.this,
                    "Незаполненная фамилия!!!");
            etLastName.requestFocus();
        }
        else if ("".equals(etEmail.getText().toString())) {
            Log.e(TAG, "Пустое email");
            UserNotification.showMessage(RegistrationActivity.this,
                    "Незаполненная почта!!!");
            etEmail.requestFocus();
        }
        else if ("".equals(etPassword.getText().toString())) {
            Log.e(TAG, "Пустое пароль");
            UserNotification.showMessage(RegistrationActivity.this,
                    "Незаполненный пароль!!!");
            etPassword.requestFocus();
        }
        else if ("".equals(etConfirmPassword.getText().toString())) {
            Log.e(TAG, "Пустое пароль");
            UserNotification.showMessage(RegistrationActivity.this,
                    "Незаполненный пароль!!!");
            etConfirmPassword.requestFocus();
        } else if (!cbContract.isChecked()) {
            Log.e(TAG, "Не приняты условия соглашения");
            UserNotification.showMessage(RegistrationActivity.this,
                    "Примите условия соглашения!!!");
            cbContract.requestFocus();
        } else {
            switch (view.getId()) {
                case R.id.btnRegister:
                    registration(etEmail.getText().toString(), etPassword.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("email", etEmail.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
            }
        }
    }

    public void registration(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this,
                                    "Регистрация успешна!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistrationActivity.this,
                                    "Регистрация провалена!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
