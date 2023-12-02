package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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
        String username = binding.usernameInput.getText().toString();
        String password = binding.passwordInput.getText().toString();
        if(username.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "⛔ Please enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        db = new MyDatabase(this);
        boolean result = db.checkUsername(username);
        if(result)
        {
            result = db.checkUsernamePassword(username, password);

            if(result)
            {
                Toast.makeText(this, "✅ Login Successful", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "❌ Invalid Password", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "❌ Invalid Username", Toast.LENGTH_SHORT).show();
        }
    }
}