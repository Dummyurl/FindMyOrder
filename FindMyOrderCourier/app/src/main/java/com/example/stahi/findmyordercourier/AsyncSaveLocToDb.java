package com.example.stahi.findmyordercourier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.Models.MyLocation;

import java.time.LocalDateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Date;
public class AsyncSaveLocToDb extends AsyncTask<MyLocation, Void, Void> {



    @Override
    protected Void doInBackground(MyLocation... myLocations) {
        RestService restService = new RestService();

        final MyLocation myLocation = new MyLocation(myLocations[0].Latitude, myLocations[0].Longitude, myLocations[0].CourierId );
        restService.getService().SaveLocationToDatabase(myLocation).enqueue(new Callback<MyLocation>() {
            @Override
            public void onResponse(Call<MyLocation> call, Response<MyLocation> response) {
                if (response.isSuccessful()) {
                    Log.d("LocationDBTest", "Succes upload to DB in background" + myLocation.Latitude + " " + myLocation.Longitude);
                }
            }
            @Override
            public void onFailure(Call<MyLocation> call, Throwable t) {
                Log.d("LocationDBTest", "Fail to upload in DB");

            }
        });


        return null;
    }

}