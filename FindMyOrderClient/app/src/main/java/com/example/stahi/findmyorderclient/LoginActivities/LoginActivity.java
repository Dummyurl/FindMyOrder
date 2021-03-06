package com.example.stahi.findmyorderclient.LoginActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Drawer.MainActivity;
import com.example.stahi.findmyorderclient.Models.User;
import com.example.stahi.findmyorderclient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    RestService restService;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        restService = new RestService();

        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button bLogin = findViewById(R.id.bLogin);
        final TextView registerLink = findViewById(R.id.tvRegisterHere);
        final TextView forgetPasswordLink = findViewById(R.id.tvForgetPassword);

        forgetPasswordLink.setOnClickListener(v -> {
            Intent forgetPasswordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            LoginActivity.this.startActivity(forgetPasswordIntent);
        });

        registerLink.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(registerIntent);
        });

        bLogin.setOnClickListener(v -> {

            try{
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            catch (Exception e){ }


            final String email = etEmail.getText().toString();
            final String password = etPassword.getText().toString();

            if(!email.equals("") && !password.equals("")){
                final User user = new User();
                user.Email = email;
                user.Password = password;


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.d("TESTLOGIN", "PAS 1");

                                Call<User> call = restService.getService().LoginClient(user.Email, user.Password);
                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                        if(response.code() == 400){
                                            Log.d("TESTLOGIN", "PAS 2 - fail, nu a ajuns la API, code 400 BadRequest");
                                        }

                                        if(response.isSuccessful()){
                                            User user1 = response.body();

                                            Log.d("TESTLOGIN", "PAS 2");

                                            if(user1 == null){
                                                FirebaseAuth.getInstance().signOut();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                builder.setMessage("Utilizator inexistent")
                                                        .setNegativeButton("Incearca din nou.", null)
                                                        .create()
                                                        .show();
                                            }
                                            else{


                                                SharedPreferences sharePref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharePref.edit();

                                                editor.putString("name", user1.UserDetails.Name);
                                                editor.putString("email", user1.Email);
                                                editor.putString("password", user1.Password);
                                                editor.putString("id", String.valueOf((user1.Id)));
                                                editor.putString("token", user1.Token);

                                                Intent userAreaIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                LoginActivity.this.startActivity(userAreaIntent);
                                                finish();






                                                editor.putString("address", user1.UserDetails.Address);
                                                editor.putString("phoneNr", user1.UserDetails.PhoneNr);
                                                editor.apply();


                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.d("TESTLOGIN", "PAS 2 - FAIL");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("Autentificarea nu a reusit!")
                                                .setNegativeButton("Incearca din nou.", null)
                                                .create()
                                                .show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                });

                            } else {
                                Log.d("TESTLOGIN", "PAS 1 - FAIL");
                                Toast.makeText(LoginActivity.this, "Autentificarea nu a reusit!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
