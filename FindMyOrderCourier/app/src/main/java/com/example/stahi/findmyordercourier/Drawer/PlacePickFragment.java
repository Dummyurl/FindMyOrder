package com.example.stahi.findmyordercourier.Drawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stahi.findmyordercourier.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;


public class PlacePickFragment extends Fragment {


    public String TAG = "TagNavigatorTest";

    public PlacePickFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(3).setChecked(true);
        ((MainActivity)getActivity()).setActionBarTitle("Navigator");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        NavigationView navigationView = ((MainActivity) getActivity()).navigationView;
        navigationView.getMenu().getItem(3).setChecked(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_pick, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        PlaceAutocompleteFragment mPlaceAutocompleteFragment = (PlaceAutocompleteFragment) fm.findFragmentById(R.id.place_autocomplete_fragment);
        mPlaceAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());

                Location myLocation = ((MainActivity)getActivity()).getMylocation();
                Point origin = Point.fromLngLat(myLocation.getLongitude(), myLocation.getLatitude());
                Point destination = Point.fromLngLat(place.getLatLng().longitude, place.getLatLng().latitude);

                String awsPoolId = "your_cognito_pool_id";

                boolean simulateRoute = true;

                // Create a NavigationLauncherOptions object to package everything together
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .origin(origin)
                        .destination(destination)
                        .awsPoolId(awsPoolId)
                        .shouldSimulateRoute(simulateRoute) //asta simuleaza
                        .build();

                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(getActivity(), options);


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
}
