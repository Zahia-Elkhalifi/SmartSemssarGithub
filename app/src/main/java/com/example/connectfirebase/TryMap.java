package com.example.connectfirebase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TryMap extends AppCompatActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    Boolean isPermissionGranted;
    GoogleMap mGoogleMap;
    FloatingActionButton fab;
    private int GPS_REQUEST_CODE=9001;

    private FusedLocationProviderClient mLocationClient;
    EditText locSearch;
    ImageView searchIcon;
    ImageView exitMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_map);
        fab=findViewById(R.id.fb);
        locSearch=findViewById(R.id.et_search);
        searchIcon=findViewById(R.id.search_iconn);
        exitMap=findViewById(R.id.exit_btn);



        checkPermission();
        initMap();
        mLocationClient=new FusedLocationProviderClient(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrLoc();
            }


        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate(v);
            }
        });
        exitMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TryMap.this,Louer.class);
                startActivity(intent);

            }
        });





    }

    private void geoLocate(View v) {
        String locatinName=locSearch.getText().toString();
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList=geocoder.getFromLocationName(locatinName,3);
            if(addressList.size()>0){


                Address address=addressList.get(0);
                gotoLocation(address.getLatitude(),address.getLongitude());
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng( address.getLatitude(),address.getLongitude())));

                //la fonction qui recupère l'adresse
                Toast.makeText(this,address.getAddressLine(0),Toast.LENGTH_LONG).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrLoc() {
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {

            if (task.isSuccessful()){
                Location location=task.getResult();
                gotoLocation(location.getLatitude(),location.getLongitude());

            }
        });
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng=new LatLng(latitude,longitude);
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng,15);
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    }

    private void initMap() {
        if(isPermissionGranted!=null){
        if (isPermissionGranted) {
            if(isGpsEnabled()){

               SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.asmaefrag);

try{

    supportMapFragment.getMapAsync(this);


}catch(NullPointerException ignored){}

                    supportMapFragment.getMapAsync(this);



            }


        }
    }
    }
   private boolean isGpsEnabled(){
       LocationManager locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
       boolean providerEnable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       if(providerEnable){
           return true;
       }else{
           AlertDialog alertDialog=new AlertDialog.Builder(this).setTitle("GPS permission")
                   .setMessage("GPS est obligatoire ,svp activez votre gps ").setPositiveButton("oui",((dialog, which) ->{
                       Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                       startActivityForResult(intent,GPS_REQUEST_CODE);
                   } )).setCancelable(false).show();
       }

       return false;


    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(TryMap.this, "permission is given ", Toast.LENGTH_LONG).show();
                isPermissionGranted = true;

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

       mGoogleMap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GPS_REQUEST_CODE){

            LocationManager locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
            boolean providerEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(providerEnabled){
                Toast.makeText(this,"gps is enable ",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(this,"gps is not enable ",Toast.LENGTH_LONG).show();
            }
        }
    }
}