package com.example.stahi.findmyordercourier.LoginActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.Drawer.MainActivity;
import com.example.stahi.findmyordercourier.Models.User;
import com.example.stahi.findmyordercourier.R;
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
        final TextView forgetPasswordLink = findViewById(R.id.tvForgetPassword);

        forgetPasswordLink.setOnClickListener(v -> {
            Intent forgetPasswordIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            LoginActivity.this.startActivity(forgetPasswordIntent);
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

                                Call<User> call = restService.getService().LoginCourier(user.Email, user.Password);
                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(response.isSuccessful()){
                                            User user1 = response.body();

                                            Log.d("TESTLOGIN", "PAS 2");


                                            if(user1 == null){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                builder.setMessage("Utilizator inexistent")
                                                        .setNegativeButton("Incearca din nou.", null)
                                                        .create()
                                                        .show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                            else{
                                                SharedPreferences sharePref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharePref.edit();

                                                editor.putString("id", String.valueOf((user1.Id)));
                                                editor.putString("email", user1.Email);
                                                editor.putString("token", user1.Token);
                                                editor.putString("password", user1.Password);

                                                editor.putString("name", user1.UserDetails.Name);
                                                editor.putString("address", user1.UserDetails.Address);
                                                editor.putString("phoneNr", user1.UserDetails.PhoneNr);
                                                editor.apply();

                                                Intent userAreaIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                LoginActivity.this.startActivity(userAreaIntent);
                                                finish();
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
