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

public class HomeUserListCustomAdapter extends ArrayAdapter<UserRelationship> {

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
            TextView tvUserAddress = v.findViewById(R.id.home_user_address);
            TextView tvUserPackagesName = v.findViewById(R.id.home_user_package_name);

            tvUserId.setText( Long.toString(user.FirstUserId));
            tvUserName.setText(user.FirstUser.UserDetails.Name);
            tvIconText.setText(user.FirstUser.UserDetails.Name.substring(0, 1));
            tvUserPhone.setText("Nr Telefon: " + user.FirstUser.UserDetails.PhoneNr);
            tvUserAddress.setText("Adresa: " + user.FirstUser.UserDetails.Address);

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