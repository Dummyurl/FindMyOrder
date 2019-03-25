package com.example.stahi.findmyordercourier.Drawer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.Models.User;
import com.example.stahi.findmyordercourier.R;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    RestService restService;
    private FirebaseAuth mAuth;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(2).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Reseteaza parola");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(2).setChecked(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText etOldPassword = getActivity().findViewById(R.id.etOldPassword);
        final EditText etNewPassword = getActivity().findViewById(R.id.etNewPassword);
        final EditText etNewPasswordRe = getActivity().findViewById(R.id.etNewPasswordRe);
        final Button bResetPassword = getActivity().findViewById(R.id.bResetPassword);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        String userId = sharedPref.getString("id", "");
        String password = sharedPref.getString("password", "");

        bResetPassword.setOnClickListener(v -> {
            try {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) { }

            final String oldPass = etOldPassword.getText().toString();
            final String newPass = etNewPassword.getText().toString();
            final String newPassRe = etNewPasswordRe.getText().toString();

            if(oldPass.equals(password)){
                if(newPass.equals(newPassRe)){
                    if (newPass.toString().length() >= 6) {

                        com.google.firebase.auth.FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                        userFirebase.updatePassword(newPass).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                resetUserPasswordInDb(userId, newPass);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(), "Eroare Firebase Reset Password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else{
                        Toast.makeText(getActivity().getApplicationContext(), "Noua parola trebuie sa contina minim 6 caractere", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Parolele noi introduse nu corespund.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Vechea parola nu este corecta.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void resetUserPasswordInDb(String id, String newPass) {

        restService = new RestService();
        restService.getService().ChangePassword(Integer.parseInt(id), newPass).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Parola schimbata cu succes!", Toast.LENGTH_LONG).show();

                    SharedPreferences sharePref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharePref.edit();
                    editor.putString("password", newPass);
                    editor.apply();

                    final EditText etOldPassword = getActivity().findViewById(R.id.etOldPassword);
                    final EditText etNewPassword = getActivity().findViewById(R.id.etNewPassword);
                    final EditText etNewPasswordRe = getActivity().findViewById(R.id.etNewPasswordRe);
                    etOldPassword.setText("");
                    etNewPassword.setText("");
                    etNewPasswordRe.setText("");

                    FragmentManager ft = getFragmentManager();
                    FragmentTransaction transaction = ft.beginTransaction();
                    transaction.replace(R.id.flMain, new HomeFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "resetUserPasswordInDb - Eroare SQL DB Reset Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
