package com.example.stahi.findmyorderclient.Drawer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.Adaptors.HomeUserListCustomAdapter;
import com.example.stahi.findmyorderclient.Models.GetUserRelationshipResponse;
import com.example.stahi.findmyorderclient.Models.MyLocation;
import com.example.stahi.findmyorderclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CourierMapLocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mMapView;
    View mView;
    RestService restService;

    String courierName;

    public CourierMapLocationFragment (){}

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(0).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Locatia curierului");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(0).setChecked(true);
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_courier_map_location, container, false);
        return mView;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Button bRefreshCurierLocation = mView.findViewById(R.id.bRefreshCurierLocation);

        mMapView =  mView.findViewById(R.id.fragmentMap);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        Bundle arguments = getArguments();
        String curierUserId = arguments.getString("courierUserId");

        restService = new RestService();


        bRefreshCurierLocation.setOnClickListener(v -> {

            restService.getService().GetCourierLocationById(Integer.parseInt(curierUserId)).enqueue(new Callback<MyLocation>() {
                @Override
                public void onResponse(Call<MyLocation> call, Response<MyLocation> response) {
                    if (response.isSuccessful()) {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(response.body().Latitude, response.body().Longitude)).title(courierName));

                        CameraPosition CurierPosition = CameraPosition.builder().target(new LatLng(response.body().Latitude, response.body().Longitude)).zoom(12).bearing(0).tilt(45).build();

                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CurierPosition));
                    }
                }
                @Override
                public void onFailure(Call<MyLocation> call, Throwable t) {
                    Toast.makeText(getContext(), "Fail !", Toast.LENGTH_LONG).show();
                }
            });
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap = googleMap;

        Bundle arguments = getArguments();
        courierName = arguments.getString("courierName");
        String curierUserId = arguments.getString("courierUserId");
        String lastLongitude = arguments.getString("courierLastLongitude");
        String lastLatitude = arguments.getString("courierLastLatitude");

        double lastDoubleLong = Double.parseDouble(lastLongitude);
        double lastDoubleLat = Double.parseDouble(lastLatitude);

        MapsInitializer.initialize(getContext());
        mMap.addMarker(new MarkerOptions().position(new LatLng(lastDoubleLat, lastDoubleLong)).title(courierName));

        CameraPosition CurierPosition = CameraPosition.builder().target(new LatLng(lastDoubleLat, lastDoubleLong)).zoom(12).bearing(0).tilt(45).build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CurierPosition));

    }

}
