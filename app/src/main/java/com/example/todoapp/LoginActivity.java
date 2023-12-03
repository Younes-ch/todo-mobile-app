package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.todoapp.Utils.MyDatabase;
import com.example.todoapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signupLink.setOnClickListener(v -> onSignupLinkClicked());
        binding.loginButton.setOnClickListener(v -> onLoginButtonClicked());

    }

    private void onSignupLinkClicked() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void onLoginButtonClicked() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty())
        {
            String toastMessage = "⛔ " + getResources().getString(R.string.empty_fields_message);
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        db = new MyDatabase(this);
        boolean result = db.checkEmailExists(email);
        if(result)
        {
            result = db.checkEmailAndPasswordCorrect(email, password);

            if(result)
            {
                int user_id = db.getUserId(email);
                String toastMessage = String.format("✅ %s", getResources().getString(R.string.login_successful_message), email);
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
            else
            {
                String toastMessage = String.format("❌ %s", getResources().getString(R.string.invalid_credential_password_message));
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            String toastMessage = String.format("❌ %s", getResources().getString(R.string.invalid_credential_email_message));
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        }
    }
}