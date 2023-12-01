package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.todoapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(v -> onLoginButtonClicked());

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