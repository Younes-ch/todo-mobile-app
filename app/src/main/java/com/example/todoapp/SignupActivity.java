package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.todoapp.Utils.MyDatabase;
import com.example.todoapp.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginLink.setOnClickListener(v -> onLoginLinkClicked());
        binding.signupButton.setOnClickListener(v -> onSignupButtonClicked());

    }

    private void onLoginLinkClicked() {
        finish();
    }

    private void onSignupButtonClicked() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.password1Input.getText().toString().trim();
        String confirmPassword = binding.password2Input.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
        {
            Toast.makeText(this, "⛔ Please enter all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, "⛔ Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        db = new MyDatabase(this);
        boolean result = db.checkEmailExists(email);
        if(result)
        {
            Toast.makeText(this, "❌ Email already exists", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(!email.matches(emailPattern))
            {
                Toast.makeText(this, "⛔ Invalid Email", Toast.LENGTH_SHORT).show();
                return;
            }

            result = db.insertUser(email, password);
            if(result)
            {
                Toast.makeText(this, "✅ Signup Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(this, "❌ Signup Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}