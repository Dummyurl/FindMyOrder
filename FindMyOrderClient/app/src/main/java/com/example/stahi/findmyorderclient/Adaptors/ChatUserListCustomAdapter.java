package com.example.stahi.findmyorderclient.Adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stahi.findmyorderclient.Models.UserRelationship;
import com.example.stahi.findmyorderclient.R;

import java.util.List;

public class ChatUserListCustomAdapter  extends ArrayAdapter<UserRelationship> {

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
            TextView tvUserEmail = v.findViewById(R.id.user_Email);
            TextView tvUserPackagesName = v.findViewById(R.id.user_package_name);

            tvUserId.setText( Long.toString(user.SecondUserId));
            tvUserToken.setText( user.SecondUser.Token);
            tvUserName.setText(user.SecondUser.UserDetails.Name);
            tvUserFirebaseToken.setText(user.SecondUser.FirebaseToken);
            tvIconText.setText(user.SecondUser.UserDetails.Name.substring(0, 1));
            tvUserPhone.setText("Nr Telefon: " + user.SecondUser.UserDetails.PhoneNr);
            tvUserEmail.setText(user.SecondUser.Email);

            String allPackages = "Coletul contine: ";
            assert user.SecondUser.clientPackagesName != null;
            for (int i = 0; i < user.SecondUser.clientPackagesName.size(); i++){
                allPackages = allPackages + user.SecondUser.clientPackagesName.get(i) + "; ";
            }
            tvUserPackagesName.setText(allPackages);
        }

        return v;
    }
}