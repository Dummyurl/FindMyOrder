package com.example.stahi.findmyordercourier.Drawer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.Models.FinalizeCommand;
import com.example.stahi.findmyordercourier.Models.UserRelationship;
import com.example.stahi.findmyordercourier.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalizesDeliveryFragment extends Fragment {

    RestService restService;

    public FinalizesDeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(0).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Acasa");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(0).setChecked(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finalizes_delivery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restService = new RestService();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String stringCourierId = sharedPref.getString("id", "0");

        Bundle arguments = getArguments();
        String stringclientUserId = arguments.getString("clientUserId");
        String clientName = arguments.getString("clientName");

        String message = "Prin apasarea butonului FINALIZARE, veti confirma predarea coletului/coletelor catre clientul: " + clientName +"!";
        TextView tvFinalizesDelivery = getActivity().findViewById(R.id.tvFinalizesDelivery);
        tvFinalizesDelivery.setText(message);

        final Button bFinalizesDelivery = getActivity().findViewById(R.id.bFinalizesDelivery);

        bFinalizesDelivery.setOnClickListener(v -> {
            try{
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            catch (Exception e){ }


            if(!stringCourierId.equals("") && !stringclientUserId.equals("0")){
                final EditText etFinalizesDeliveryObs = getActivity().findViewById(R.id.etFinalizesDeliveryObs);
                String Observations = etFinalizesDeliveryObs.getText().toString();


                DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String date = df2.format(new Date()).toString();

                int courierId = Integer.parseInt(stringCourierId);
                int clientId = Integer.parseInt(stringclientUserId);

                FinalizeCommand myObj = new FinalizeCommand(courierId, clientId, Observations);

                restService.getService().FinalizeDelivery(myObj).enqueue(new Callback<UserRelationship>() {
                    @Override
                    public void onResponse(Call<UserRelationship> call, Response<UserRelationship> response) {

                        //return BadRequest ("message");
                        if (response.code() == 500) {
                            Log.d("finalizeDelivery", "PAS 1 - cod 500");
                        }

                        if (response.code() == 400) {
                            Log.d("finalizeDelivery", "PAS 1 - cod 400");
                        }
                        if (response.isSuccessful()) {

                            Log.d("finalizeDelivery", "PAS 1 - succes");
                            Toast.makeText(getActivity(), "Pachet livrat cu succes", Toast.LENGTH_LONG).show();

                            FragmentManager ft = getFragmentManager();
                            FragmentTransaction transaction = ft.beginTransaction();
                            transaction.replace(R.id.flMain, new HomeFragment());
                            transaction.addToBackStack(null);

                            transaction.commit();
                        }
                    }
                    @Override
                    public void onFailure(Call<UserRelationship> call, Throwable t) {
                        Log.d("finalizeDelivery", "PAS 1 - fail");

                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}
