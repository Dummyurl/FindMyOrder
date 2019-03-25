package com.example.stahi.findmyorderclient.Adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.stahi.findmyorderclient.Models.UserRelationship;
import com.example.stahi.findmyorderclient.R;

import java.util.List;

public class HomeUserListCustomAdapter  extends ArrayAdapter<UserRelationship> {

    public HomeUserListCustomAdapter(Context context, int resource, List<UserRelationship> user) {
        super(context, resource, user);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.home_user_list_item, parent, false);
        }

        UserRelationship user = getItem(position);

        if (user != null) {

            TextView tvUserId = v.findViewById(R.id.home_user_Id);
            TextView tvUserName = v.findViewById(R.id.home_user_name);
            TextView tvIconText = v.findViewById(R.id.home_iconText);
            TextView tvUserPhone = v.findViewById(R.id.home_user_phone);
            TextView tvUserPackagesName = v.findViewById(R.id.home_user_package_name);
            TextView tvUserLastLatitude = v.findViewById(R.id.home_user_lastLatitude);
            TextView tvUserLastLongitude = v.findViewById(R.id.home_user_lastLongitude);

            tvUserId.setText( Long.toString(user.SecondUserId));
            tvUserName.setText(user.SecondUser.UserDetails.Name);
            tvIconText.setText(user.SecondUser.UserDetails.Name.substring(0, 1));
            tvUserPhone.setText("Nr Telefon: " + user.SecondUser.UserDetails.PhoneNr);
            tvUserLastLatitude.setText(Double.toString(user.SecondUser.LastLatitude));
            tvUserLastLongitude.setText(Double.toString(user.SecondUser.LastLongitude));

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