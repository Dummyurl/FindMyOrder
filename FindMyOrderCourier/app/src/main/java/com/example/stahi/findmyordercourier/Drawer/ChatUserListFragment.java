package com.example.stahi.findmyordercourier.Drawer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.Adapters.ChatUserListCustomAdapter;
import com.example.stahi.findmyordercourier.Models.GetUserRelationshipResponse;
import com.example.stahi.findmyordercourier.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatUserListFragment extends Fragment {


    RestService restService;
    TextView user_Id;
    TextView user_Token;
    TextView user_Email;
    TextView user_Name;
    TextView user_FirebaseToken;

    public ChatUserListFragment(){

    }

    @Override
    public void onResume() {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(4).setChecked(true);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(4).setChecked(true);
        ((MainActivity) getActivity()).setActionBarTitle("Lista clienti");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_user_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restService = new RestService();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String stringId = sharedPref.getString("id", "0");
        int intId = Integer.parseInt(stringId);

        if(intId != 0){
            restService.getService().GetAllClientsBtCourierId(intId).enqueue(new Callback<GetUserRelationshipResponse>() {
                @Override
                public void onResponse(Call<GetUserRelationshipResponse> call, Response<GetUserRelationshipResponse> response) {

                    if (response.isSuccessful()) {
                        ListView lv = getActivity().findViewById(R.id.chat_user_listView);
                        ChatUserListCustomAdapter customAdapter = new ChatUserListCustomAdapter(getContext(), R.layout.chat_user_list_item, response.body().UserRelationship);

                        lv.setOnItemClickListener((parent, view1, position, id) -> {
                            user_Id = view1.findViewById(R.id.user_Id);
                            user_Token = view1.findViewById(R.id.user_Token);
                            user_FirebaseToken = view1.findViewById(R.id.user_Frb_Token);
                            user_Email = view1.findViewById(R.id.user_Email);
                            user_Name = view1.findViewById(R.id.user_name);

                            String userId = user_Id.getText().toString();
                            String userToken = user_Token.getText().toString();
                            String userFirebaseToken = user_FirebaseToken.getText().toString();
                            String userEmail = user_Email.getText().toString();
                            String userName = user_Name.getText().toString();

                            ChatFragment chatFrag = new ChatFragment();
                            Bundle arguments = new Bundle();
                            arguments.putString("otherUserId", userId);
                            arguments.putString("otherUserToken", userToken);
                            arguments.putString("otherUserEmail", userEmail);
                            arguments.putString("otheruserFirebaseToken", userFirebaseToken);
                            arguments.putString("userName", userName);

                            chatFrag.setArguments(arguments);
                            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.flMain, chatFrag);
                            transaction.addToBackStack(null);
                            transaction.commit();

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

