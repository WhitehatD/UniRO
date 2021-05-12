package com.example.uniro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    University university;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        university = UniversityAdapter.univ;
    }


    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

        map = googleMap;

        LatLng univPlace = new LatLng(UniversityAdapter.univ.getV(),UniversityAdapter.univ.getV1());
        map.addMarker(new MarkerOptions().position(univPlace).title(UniversityAdapter.univ.getName()));
        map.animateCamera(CameraUpdateFactory.newLatLng(univPlace));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(univPlace,10));



    }

}