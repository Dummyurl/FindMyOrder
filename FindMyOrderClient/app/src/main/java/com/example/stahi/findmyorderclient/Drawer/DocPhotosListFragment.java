package com.example.stahi.findmyorderclient.Drawer;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Adaptors.DocPhotosAdapter;
import com.example.stahi.findmyorderclient.Models.GetDocumentPhotosRelationshipResponse;
import com.example.stahi.findmyorderclient.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocPhotosListFragment extends Fragment {

    RestService restService;
    ImageView docImage;
    TextView docImageTitle;

    public DocPhotosListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(6).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Istoric imagini documente");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(6).setChecked(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doc_photos_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restService = new RestService();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String stringId = sharedPref.getString("id", "0");
        int intId = Integer.parseInt(stringId);

        restService.getService().GetAllPicturesByClientId(intId).enqueue(new Callback<GetDocumentPhotosRelationshipResponse>() {
            @Override
            public void onResponse(@NonNull Call<GetDocumentPhotosRelationshipResponse> call, Response<GetDocumentPhotosRelationshipResponse> response) {

                if (response.isSuccessful()) {
                    ListView lv = getActivity().findViewById(R.id.doc_picture_listView);
                    DocPhotosAdapter customAdapter = new DocPhotosAdapter(getContext(), R.layout.doc_photo_list_item, response.body().MyDocumentPhotos);

                    lv.setOnItemClickListener((parent, view1, position, id) -> {

                        docImage = view1.findViewById(R.id.doc_image);
                        docImageTitle = view1.findViewById(R.id.doc_image_title);

                        displayPopupImage (((BitmapDrawable)docImage.getDrawable()).getBitmap());

                    });
                    lv.setAdapter(customAdapter);
                }
            }
            @Override
            public void onFailure(Call<GetDocumentPhotosRelationshipResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fail !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayPopupImage(Bitmap img){

        Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.custom_image_popup);
        ImageView imgView = myDialog.findViewById(R.id.PopupImage);
        imgView.setImageBitmap(img);
        myDialog.show();

    }
}
