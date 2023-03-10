package com.example.snake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    // boolean isPermissionGranted;
    MapView mapView;
    GoogleMap mGoogleMap;
    private EditText mapEditText;
    private ImageView mapImageView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        mapView = findViewById(R.id.mapViewid);

        /*if(isPermissionGranted)
        {*/
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        // }

        // mapImageView=findViewById(R.id.mapImageViewId);
        // mapEditText=findViewById(R.id.mapEditTextId);

        searchView = findViewById(R.id.sv_locationId);


//......................for search location...................

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList=null;
                if(location != null || !location.equals("")){
                    Geocoder geocoder= new Geocoder(GoogleMapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        checkPermission();




        //mapImageView.setOnClickListener(this::geoLocate);
    }

    /*private void geoLocate(View view) {
        String userinput = mapEditText.getText().toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address>addressList = geocoder.getFromLocationName(userinput,5);

            if(addressList.size()>0)
            {
                Address address = addressList.get(0);

            }

        }

        catch (IOException e){

        }
    }*/

    private void checkPermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //  isPermissionGranted = true;
                        Toast.makeText(GoogleMapActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

        MarkerOptions markerOpt1 = new MarkerOptions();
        markerOpt1.position(new LatLng(21.9533973,89.1842241));
        //markerOpt1.position(new LatLng(21.8397229,88.8887949));
        //markerOpt1.position(new LatLng(22.2971353,89.5678157));
        markerOpt1.title("Sundarban");
        googleMap.addMarker(markerOpt1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}