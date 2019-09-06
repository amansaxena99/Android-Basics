package com.example.mylocationinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView longitudeTextView, latitudeTextView, altitudeTextView, accuracyTextView, addressEditTextView;

    public void updateLocation(Location location){
        Log.i("Location123:", location.toString());
        latitudeTextView.setText("Latitude : " + location.getLatitude());
        longitudeTextView.setText("Longitude : " + location.getLongitude());
        altitudeTextView.setText("Altitude : " + location.getAltitude());
        accuracyTextView.setText("Accuracy : " + location.getAccuracy());
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAdress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAdress != null && listAdress.size() > 0){
                Log.i("place info", listAdress.get(0).toString());
                String address = "";
                if (listAdress.get(0).getAddressLine(0) != null){
                    address += listAdress.get(0).getAddressLine(0);
                    Log.i("addrss123:", listAdress.get(0).getAddressLine(0));
                    addressEditTextView.setText(address);
                } else {
                    address += "Could not find Addrss :/";
                    addressEditTextView.setText(address);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        addressEditTextView = findViewById(R.id.addressEditTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location:", location.toString());
                updateLocation(location);
                Toast.makeText(MainActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null){
                updateLocation(lastKnownLocation);
            }
        }
    }
}
