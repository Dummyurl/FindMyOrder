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
import com.example.stahi.findmyorderclient.Models.UserDetails;
import com.example.stahi.findmyorderclient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    RestService restService;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        restService = new RestService();
        mAuth = FirebaseAuth.getInstance();

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etAddress = (EditText) findViewById(R.id.etAddress);
        final EditText etPhoneNr = (EditText) findViewById(R.id.etPhoneNr);
        final Button bRegister = (Button) findViewById(R.id.bRegister);



        bRegister.setOnClickListener(v -> {

            findViewById(R.id.registerLayout).setAlpha((float)0.5);
            findViewById(R.id.loadingBar).setVisibility(View.VISIBLE);
            findViewById(R.id.loadingBar).setAlpha((float)1);
            try{
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            catch (Exception e){

            }


            final String name = etName.getText().toString();
            final String email = etEmail.getText().toString().trim();
            final String password = etPassword.getText().toString();
            final String address = etAddress.getText().toString();
            final String phoneNr = etPhoneNr.getText().toString();

            // onClick of button perform this simplest code.
            if (email.matches(emailPattern)) {

                if (isValidMobile(phoneNr) == true) {

                    if (password.toString().length() >= 6) {

                        user.Email = email;
                        user.Password = password;
                        user.UserDetails = new UserDetails();
                        user.UserDetails.Name = name;
                        user.UserDetails.Address = address;
                        user.UserDetails.PhoneNr = phoneNr;


                        if (findViewById(R.id.bRegister) == v) {

                            //FIREBASE
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                SaveUserInSqlServer(task.getResult().getUser().getUid());

                                            } else {
                                                Log.d("RegisterUserToFirebase", "FAIL");
                                                Toast.makeText(RegisterActivity.this, "Acest email corespunde deja unui utilizator", Toast.LENGTH_LONG).show();

                                                findViewById(R.id.loadingBar).setVisibility(View.GONE);
                                                findViewById(R.id.registerLayout).setAlpha(1);
                                            }
                                        }
                                    });
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Parola trebuie sa aiba cel putin 6 caractere", Toast.LENGTH_SHORT).show();
                        findViewById(R.id.loadingBar).setVisibility(View.GONE);
                        findViewById(R.id.registerLayout).setAlpha(1);

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Numar de telefon invalid", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.loadingBar).setVisibility(View.GONE);
                    findViewById(R.id.registerLayout).setAlpha(1);

                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Adresa de email invalida", Toast.LENGTH_SHORT).show();
                findViewById(R.id.loadingBar).setVisibility(View.GONE);
                findViewById(R.id.registerLayout).setAlpha(1);
            }
        });
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;

        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() != 10) {
                check = false;
            }
            else {
                check = true;
            }
        }
        else {
            check=false;
        }
        return check;
    }


    public void SaveUserInSqlServer(String uid){
        user.Token = uid;

        restService.getService().Create(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                //return BadRequest ("message");
                if (response.code() == 400) {
                    findViewById(R.id.loadingBar).setVisibility(View.GONE);
                    findViewById(R.id.registerLayout).setAlpha(1);

                    try {
                        Toast.makeText(RegisterActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User creat cu succes !", Toast.LENGTH_LONG).show();

                    findViewById(R.id.loadingBar).setVisibility(View.GONE);

                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(loginIntent);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                findViewById(R.id.loadingBar).setVisibility(View.GONE);
                findViewById(R.id.registerLayout).setAlpha(1);
                Toast.makeText(RegisterActivity.this, "Fail !", Toast.LENGTH_LONG).show();
            }
        });
    }
}
