package com.example.stahi.findmyordercourier.Drawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.stahi.findmyordercourier.Models.ReportProblemForm;
import com.example.stahi.findmyordercourier.R;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportProblemFragment extends Fragment {

    RestService restService;

    public ReportProblemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(1).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Raporteaza o problema");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(1).setChecked(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_problem, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restService = new RestService();

        final Button bSendReportProblemMessage = getActivity().findViewById(R.id.bSendReportProblemMessage);
        final EditText etReportProblemMessage = getActivity().findViewById(R.id.etReportProblemMessage);
        bSendReportProblemMessage.setOnClickListener(v -> {
            try{
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            catch (Exception e){ }

            final String userMessage = etReportProblemMessage.getText().toString();

            SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
            String userId = sharedPref.getString("id", "0");

            if(!userMessage.equals("") && !userId.equals("0")){

                ReportProblemForm reportProblemForm = new ReportProblemForm();
                reportProblemForm.Message = userMessage;
                reportProblemForm.UserId = Integer.parseInt(userId);

                restService.getService().Create(reportProblemForm).enqueue(new Callback<String>() {
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
                            Toast.makeText(getActivity(), "Va multumim pentru raportarea problemei, vom incerca sa gasim o solutie in cel mai scurt timp posibil.", Toast.LENGTH_LONG).show();

                            etReportProblemMessage.setText("");

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