package com.example.stahi.findmyorderclient.LoginActivities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Models.User;
import com.example.stahi.findmyorderclient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    RestService restService;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        restService = new RestService();
        final EditText etEmail = findViewById(R.id.etEmailforRecoverPass);
        final Button bResetPass = findViewById(R.id.bResetPass);

        bResetPass.setOnClickListener(v -> {

            findViewById(R.id.forgetPasswordId).setAlpha((float)0.5);
            findViewById(R.id.loadingBarForgetPass).setVisibility(View.VISIBLE);
            findViewById(R.id.loadingBarForgetPass).setAlpha((float)1);
            try{
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            catch (Exception e){ }

            final String email = etEmail.getText().toString().trim();

            if(!email.equals("")){
                if (email.matches(emailPattern))
                {
                    //Valid Email
                    User user = new User();
                    user.Email = email;
                    if (findViewById(R.id.bResetPass) == v) {
                        restService.getService().ResetPass(user.Email).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {

                                if (response.isSuccessful()) {
                                    Log.d("TESTPAROLA", "PAS 1");
                                    final User user = response.body();
                                    Log.d("TESTPAROLA", user.OldPassword);

                                    SingInWithFirebase(user);
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(ForgetPasswordActivity.this, "Fail, Nu merge sa trimit mail de pe free hosting azure, merge localhost!", Toast.LENGTH_LONG).show();
                                Log.d("TESTPAROLA", "FAIL");
                                findViewById(R.id.loadingBarForgetPass).setVisibility(View.GONE);
                                findViewById(R.id.forgetPasswordId).setAlpha(1);
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Adresa de mail invalida", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loadingBarForgetPass).setVisibility(View.GONE);
                    findViewById(R.id.forgetPasswordId).setAlpha(1);
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Va rugam introduceti adresa de email", Toast.LENGTH_SHORT).show();
                findViewById(R.id.loadingBarForgetPass).setVisibility(View.GONE);
                findViewById(R.id.forgetPasswordId).setAlpha(1);
            }
        });
    }


    public void SingInWithFirebase (final User user){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(user.Email, user.OldPassword).addOnCompleteListener(ForgetPasswordActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TESTPAROLA", "PAS 2");
                    com.google.firebase.auth.FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                    ResetFirebasePassword (user, userFirebase);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("TESTPAROLA", "PAS 2 - FAIL");
                    findViewById(R.id.loadingBarForgetPass).setVisibility(View.GONE);
                    findViewById(R.id.forgetPasswordId).setAlpha(1);
                }
            }
        });
    }

    public void ResetFirebasePassword(User user, com.google.firebase.auth.FirebaseUser userFirebase){
        userFirebase.updatePassword(user.Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgetPasswordActivity.this, "Un mail cu noua parola a fost trimis", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                ForgetPasswordActivity.this.startActivity(loginIntent);


                findViewById(R.id.loadingBarForgetPass).setVisibility(View.GONE);
                Log.d("TESTPAROLA", "PAS 3");

            } else {
                Log.d("TESTPAROLA", "PAS 3 - FAIL");
                findViewById(R.id.loadingBarForgetPass).setVisibility(View.GONE);
                findViewById(R.id.forgetPasswordId).setAlpha(1);
            }
        });
    }

}
