package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
            Toast.makeText(this, "⛔ Please enter all the fields", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "✅ Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(this, "❌ Invalid Password", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "❌ Invalid Email", Toast.LENGTH_SHORT).show();
        }
    }
}