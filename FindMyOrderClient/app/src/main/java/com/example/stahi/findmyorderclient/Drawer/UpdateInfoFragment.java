package com.example.stahi.findmyorderclient.Drawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.LoginActivities.LoginActivity;
import com.example.stahi.findmyorderclient.LoginActivities.RegisterActivity;
import com.example.stahi.findmyorderclient.Models.FirebaseUser;
import com.example.stahi.findmyorderclient.Models.User;
import com.example.stahi.findmyorderclient.Models.UserDetails;
import com.example.stahi.findmyorderclient.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateInfoFragment extends Fragment {

    RestService restService;
    User user = new User();

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(2).setChecked(true);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(2).setChecked(true);
        return inflater.inflate(R.layout.fragment_update_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle("Actualizeaza informatiile");

        restService = new RestService();

        final EditText etUpdateName = getActivity().findViewById(R.id.etUpdateName);
        final EditText etUpdateAddress = getActivity().findViewById(R.id.etUpdateAddress);
        final EditText etUpdatePhoneNr = getActivity().findViewById(R.id.etUpdatePhoneNr);
        final Button bUpdateInfo = getActivity().findViewById(R.id.bUpdateInfo);


        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        String userId = sharedPref.getString("id", "");

        String name = sharedPref.getString("name", "");
        String address = sharedPref.getString("address", "");
        String phoneNr = sharedPref.getString("phoneNr", "");

        etUpdateName.setText(name);
        etUpdateAddress.setText(address);
        etUpdatePhoneNr.setText(phoneNr);

        bUpdateInfo.setOnClickListener(v -> {
            try {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
            }

            final EditText etUpdatedName = getActivity().findViewById(R.id.etUpdateName);
            final EditText etUpdatedAddress = getActivity().findViewById(R.id.etUpdateAddress);
            final EditText etUpdatedPhoneNr = getActivity().findViewById(R.id.etUpdatePhoneNr);


            final String updatedName = etUpdatedName.getText().toString();
            final String updatedAddress = etUpdatedAddress.getText().toString();
            final String updatedPhoneNr = etUpdatedPhoneNr.getText().toString();


            if (isValidMobile(phoneNr) == true) {
                if(!updatedName.equals("")) {

                    user.Id = Integer.parseInt(userId);
                    user.UserDetails = new UserDetails();
                    user.UserDetails.Name = updatedName;
                    user.UserDetails.Address = updatedAddress;
                    user.UserDetails.PhoneNr = updatedPhoneNr;

                    restService.getService().Update(Integer.parseInt(userId), user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Informatii actualizate cu succes!", Toast.LENGTH_LONG).show();

                                SharedPreferences sharePref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePref.edit();

                                editor.putString("name", updatedName);
                                editor.putString("address", updatedAddress);
                                editor.putString("phoneNr", updatedPhoneNr);
                                editor.apply();

                                FragmentManager ft = getFragmentManager();
                                FragmentTransaction transaction = ft.beginTransaction();
                                transaction.replace(R.id.flMain, new HomeFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getActivity(), "UpdateInfoFragment" + " " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else{
                    Toast.makeText(getActivity().getApplicationContext(), "Campul nume nu poate fi gol", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Numar de telefon invalid", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;

        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

}
