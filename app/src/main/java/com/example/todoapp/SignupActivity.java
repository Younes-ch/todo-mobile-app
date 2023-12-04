package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private void onLoginLinkClicked()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void onSignupButtonClicked() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.password1Input.getText().toString().trim();
        String confirmPassword = binding.password2Input.getText().toString().trim();
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
        {
            String toastMessage = "⛔ " + getResources().getString(R.string.empty_fields_message);
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmPassword))
        {
            String toastMessage = "⛔ " + getResources().getString(R.string.passwords_do_not_match_message);
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            return;
        }
        db = new MyDatabase(this);
        boolean result = db.checkEmailExists(email);
        if(result)
        {
            String toastMessage = "⛔ " + getResources().getString(R.string.email_already_exists_message);
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        }
        else
        {
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(!email.matches(emailPattern))
            {
                String toastMessage = "⛔ " + getResources().getString(R.string.invalid_email_message);
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                return;
            }

            result = db.insertUser(email, password);
            if(result)
            {
                String toastMessage = "✅ " + getResources().getString(R.string.signup_successful_message);
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                int userId = db.getUserId(email);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            }
            else
            {
                String toastMessage = "❌ " + getResources().getString(R.string.signup_failed_message);
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }

}