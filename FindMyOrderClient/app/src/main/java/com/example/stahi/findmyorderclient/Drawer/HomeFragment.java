package com.example.stahi.findmyorderclient.Drawer;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Adaptors.ChatUserListCustomAdapter;
import com.example.stahi.findmyorderclient.Adaptors.HomeUserListCustomAdapter;
import com.example.stahi.findmyorderclient.Models.GetUserRelationshipResponse;
import com.example.stahi.findmyorderclient.Models.User;
import com.example.stahi.findmyorderclient.R;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    RestService restService;

    TextView home_user_Id;
    TextView home_user_Name;
    TextView home_user_lastLongitude;
    TextView home_user_lastLatitude;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restService = new RestService();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String name = sharedPref.getString("name", "name");
        String uid = sharedPref.getString("token", "0");
        String stringId = sharedPref.getString("id", "0");
        String message = "Lista curierilor dumneavoastra, " + name +"!";

        TextView tvCourierHomeFragmentList = getActivity().findViewById(R.id.tvCourierHomeFragmentList);
        tvCourierHomeFragmentList.setText(message);

        String firebaseToken = FirebaseInstanceId.getInstance().getToken();

        restService.getService().UpdateFirebaseToken(uid, firebaseToken, "testString").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(getContext(), "FirebaseToken refreshed !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //Toast.makeText(getContext(), "Fail to refresh FirebaseToken !", Toast.LENGTH_LONG).show();
            }
        });

        int intId = Integer.parseInt(stringId);
        if(intId != 0){
            restService.getService().GetAllCouriersByClientId(intId).enqueue(new Callback<GetUserRelationshipResponse>() {
                @Override
                public void onResponse(Call<GetUserRelationshipResponse> call, Response<GetUserRelationshipResponse> response) {

                    if (response.isSuccessful()) {
                        ListView lv = getActivity().findViewById(R.id.home_chat_user_listView);
                        HomeUserListCustomAdapter customAdapter = new HomeUserListCustomAdapter(getContext(), R.layout.home_user_list_item, response.body().UserRelationship);

                        lv.setOnItemClickListener((parent, view1, position, id) -> {
                            home_user_Id = view1.findViewById(R.id.home_user_Id);
                            home_user_Name = view1.findViewById(R.id.home_user_name);
                            home_user_lastLongitude = view1.findViewById(R.id.home_user_lastLongitude);
                            home_user_lastLatitude  = view1.findViewById(R.id.home_user_lastLatitude);

                            String courierUserId = home_user_Id.getText().toString();
                            String courierName = home_user_Name.getText().toString();
                            String courierLastLatitude = home_user_lastLatitude.getText().toString();
                            String courierLastLongitude = home_user_lastLongitude.getText().toString();


                            if(courierLastLongitude.equals("0.0") && courierLastLatitude.equals("0.0")){
                                Toast.makeText(getContext(), "Curierul nu a inceput cursa inca", Toast.LENGTH_LONG).show();
                            } else{

                                CourierMapLocationFragment courierMapLocationFragment = new CourierMapLocationFragment();
                                Bundle arguments = new Bundle();
                                arguments.putString("courierUserId", courierUserId);
                                arguments.putString("courierName", courierName);
                                arguments.putString("courierLastLongitude", courierLastLongitude);
                                arguments.putString("courierLastLatitude", courierLastLatitude);

                                courierMapLocationFragment.setArguments(arguments);
                                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.flMain, courierMapLocationFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        lv.setAdapter(customAdapter);
                    }
                }

                @Override
                public void onFailure(Call<GetUserRelationshipResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Fail !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
