package com.example.stahi.findmyordercourier.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stahi.findmyordercourier.Models.UserRelationship;
import com.example.stahi.findmyordercourier.R;

import java.util.List;

public class ChatUserListCustomAdapter extends ArrayAdapter<UserRelationship> {

    public ChatUserListCustomAdapter(Context context, int resource, List<UserRelationship> user) {
        super(context, resource, user);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat_user_list_item, parent, false);
        }

        UserRelationship user = getItem(position);

        if (user != null) {

            TextView tvUserId = v.findViewById(R.id.user_Id);
            TextView tvUserName = v.findViewById(R.id.user_name);
            TextView tvUserToken = v.findViewById(R.id.user_Token);
            TextView tvUserFirebaseToken = v.findViewById(R.id.user_Frb_Token);
            TextView tvIconText = v.findViewById(R.id.iconText);
            TextView tvUserPhone = v.findViewById(R.id.user_phone);
            TextView tvUserAddress = v.findViewById(R.id.user_address);
            TextView tvUserEmail = v.findViewById(R.id.user_Email);
            TextView tvUserPackagesName = v.findViewById(R.id.user_package_name);

            tvUserId.setText( Long.toString(user.FirstUserId));
            assert user.FirstUser != null;
            tvUserToken.setText( user.FirstUser.Token);
            tvUserName.setText(user.FirstUser.UserDetails.Name);
            tvUserFirebaseToken.setText(user.FirstUser.FirebaseToken);
            tvIconText.setText(user.FirstUser.UserDetails.Name.substring(0, 1));
            tvUserPhone.setText("Nr Telefon: " + user.FirstUser.UserDetails.PhoneNr);
            tvUserAddress.setText("Adresa: " + user.FirstUser.UserDetails.Address);
            tvUserEmail.setText(user.FirstUser.Email);

            String allPackages = "Coletul contine: ";
            assert user.FirstUser.clientPackagesName != null;
            for (int i = 0; i < user.FirstUser.clientPackagesName.size(); i++){
                allPackages = allPackages + user.FirstUser.clientPackagesName.get(i) + "; ";
            }
            tvUserPackagesName.setText(allPackages);
        }

        return v;
    }
}