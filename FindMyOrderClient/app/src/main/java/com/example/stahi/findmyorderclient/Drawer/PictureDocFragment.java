package com.example.stahi.findmyorderclient.Drawer;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Models.DocumentPhotos;
import com.example.stahi.findmyorderclient.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PictureDocFragment extends Fragment {

    ImageView imageView;
    EditText tvImageTitle;
    Button btnCamera;
    Button btnSaveToDb;

    String imageString;

    private final static int CAMERA_REQUEST_CODE=10;
    private static final int CAM_REQUEST=1313;

    RestService restService;

    byte[] byteArray;

    public PictureDocFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(5).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Fotografiaza document");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(5).setChecked(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picture_doc, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCamera = getActivity().findViewById(R.id.btnCamera);
        btnSaveToDb = getActivity().findViewById(R.id.btnSaveToDb);
        tvImageTitle = getActivity().findViewById(R.id.tvImageTitle);
        imageView = getActivity().findViewById(R.id.DocImageView);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            btnCamera.setOnClickListener(new btnTakePhotoClicker());
        }else{
            //permission failed. request
            String[] permissionRequest = {Manifest.permission.CAMERA};
            requestPermissions(permissionRequest, CAMERA_REQUEST_CODE);
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("id", "0");

        restService = new RestService();

        btnSaveToDb.setOnClickListener(v -> {
            if(imageView.getDrawable() != null){

                DocumentPhotos docPicture = new DocumentPhotos();
                docPicture.Title = tvImageTitle.getText().toString();
                docPicture.DocStringImage = imageString;
                docPicture.ClientId = Integer.parseInt(userId);

                if(!docPicture.Title.equals("")){
                    restService.getService().Create(docPicture).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.code() == 400) {
                                try {
                                    Toast.makeText(getActivity(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (response.isSuccessful()) {

                                tvImageTitle.setText("");
                                imageView.setImageResource(0);

                                FragmentManager ft = getFragmentManager();
                                FragmentTransaction transaction = ft.beginTransaction();
                                transaction.replace(R.id.flMain, new DocPhotosListFragment());
                                transaction.addToBackStack(null);

                                transaction.commit();

                                Toast.makeText(getActivity(), "Imaginea a fost salvata cu succes!", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else{
                    Toast.makeText(getActivity(),"Va rugam dati un titlu imaginii", Toast.LENGTH_LONG).show();
                }

            }
            else{
                Toast.makeText(getActivity(),"Va rugam faceti o fotografie", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                btnCamera.setOnClickListener(new btnTakePhotoClicker());
            }else{
                Toast.makeText(getActivity(),"Permisiune inexistenta", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST && resultCode == getActivity().RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            assert photo != null;
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();

            imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

            //imageString = byteArray.toString();
            //Log.d("ALSD", "acum");
            //Glide.with(getActivity()).load(Base64.decode(imageString.getBytes())).into(imageView);

        }
    }

    class btnTakePhotoClicker implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAM_REQUEST);
        }
    }
}
