package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.todoapp.Utils.MyDatabase;
import com.example.todoapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signupLink.setOnClickListener(v -> onSignupLinkClicked());
        binding.loginButton.setOnClickListener(v -> onLoginButtonClicked());

        if (getIntent().getStringExtra("email") != null)
        {
            binding.emailInput.setText(getIntent().getStringExtra("email"));
            binding.passwordInput.setText(getIntent().getStringExtra("password"));
        }
    }

    private void onSignupLinkClicked() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
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
        MyDatabase db = new MyDatabase(this);
        boolean result = db.checkEmailExists(email);
        if(result)
        {
            result = db.checkEmailAndPasswordCorrect(email, password);

            if(result)
            {
                int user_id = db.getUserId(email);
                String toastMessage = String.format("✅ %s", getResources().getString(R.string.login_successful_message));
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);

                SwitchCompat rememberMeSwitch = binding.rememberMeSwitch;

                if (rememberMeSwitch.isChecked())
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", user_id);
                    editor.apply();
                }

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