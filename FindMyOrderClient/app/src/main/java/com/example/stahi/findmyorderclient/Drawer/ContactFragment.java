package com.example.stahi.findmyorderclient.Drawer;

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

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Models.Contact;
import com.example.stahi.findmyorderclient.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactFragment extends Fragment {

    RestService restService;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(1).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Contact");

        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        restService = new RestService();

        final Button bSendContactMessage = getActivity().findViewById(R.id.bSendContactMessage);
        final EditText etContactMessage = getActivity().findViewById(R.id.etContactMessage);
        bSendContactMessage.setOnClickListener(v -> {
            try{
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            catch (Exception e){ }

            final String userMessage = etContactMessage.getText().toString();

            SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
            String userId = sharedPref.getString("id", "0");

            if(!userMessage.equals("") && !userId.equals("0")){

                Contact contact = new Contact();
                contact.Message = userMessage;
                contact.UserId = Integer.parseInt(userId);

                restService.getService().Create(contact).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        //return BadRequest ("message");
                        if (response.code() == 400) {
                            try {
                                Toast.makeText(getActivity(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Mesajul a fost trimis cu succes! Va multumim!", Toast.LENGTH_LONG).show();

                            etContactMessage.setText("");

                            FragmentManager ft = getFragmentManager();
                            FragmentTransaction transaction = ft.beginTransaction();
                            transaction.replace(R.id.flMain, new HomeFragment());
                            transaction.addToBackStack(null);

                            transaction.commit();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}