package com.app.luberack.map;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.luberack.Profile_management.SignUp;
import com.app.luberack.R;
import com.app.luberack.utility.AlertDialogManager;
import com.app.luberack.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

//Taking user's desired location for signup
public class SignupMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Intent intentThatCalled;
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] CAMERA_PERMS = {
            Manifest.permission.CAMERA
    };
    private static final String[] CONTACTS_PERMS = {
            Manifest.permission.READ_CONTACTS
    };
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 1;
    private static final int CONTACTS_REQUEST = INITIAL_REQUEST + 2;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    private AlertDialogManager alert = new AlertDialogManager();
    MapView mMapView;
    TextView done;
    private GoogleMap googleMap;
    Location mLastLocation;
    GoogleApiClient myGoogleApiClient;
    LatLng latLng;
    String userlat, userlng, useraddress;
    List<Address> addresses = null;

    // Session Manager Class
    //private SessionManager session;
    private ProgressDialog sweetProgressDialog;

    Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_signup_map, container,
                false);
        // Session Manager
        // session = new SessionManager(getContext());

        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.retr)));
        sweetProgressDialog.setCancelable(false);

        done = v.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sweetProgressDialog.show();
                if (checkPermission()){

                if (validate()) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Intent intent = new Intent(getActivity(), SignUp.class);
                    intent.putExtra("useraddress", useraddress);
                    intent.putExtra("userlat", userlat);
                    intent.putExtra("userlng", userlng);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    getActivity().getSupportFragmentManager().popBackStack();
                }}
                else
                    askPermission();
            }
        });

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return v;
    }

    private boolean validate() {
        boolean valid = true;
        userlat = String.valueOf(googleMap.getCameraPosition().target.latitude);
        userlng = String.valueOf(googleMap.getCameraPosition().target.longitude);
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(
                    googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            Log.e("Signup Map", " address:" + address);
            String res = address.getFeatureName() + ", " + address.getSubAdminArea() + " , " + address.getAdminArea() + " , " + address.getCountryName();
            useraddress = res;
            marker.setTitle(useraddress);
            //   et_location.setText("" + res);
        }
        Log.e("Signup Map", " address:" + useraddress + "  lat:" + userlat + "  lng:" + userlng);

        if (userlat == null || userlat.equals("")) {
            valid = false;
        } else {
        }

        if (userlng == null || userlng.equals("")) {
            valid = false;
        } else {
        }
        if (useraddress == null || useraddress.equals("")) {
            valid = false;
        } else {
        }
        if (sweetProgressDialog.isShowing()) {
            sweetProgressDialog.dismiss();
        }
        return valid;
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;

        googleMap.setIndoorEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        boolean result = Utility.checkPermission(getContext());
        if (!result) return;
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.setPadding(0, 0, 0, 40);
        buildGoogleApiClient();
        myGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        //on connection it gets the last available location and moves the pointer to that location
        //Checking location permission
        //  boolean result = Utility.checkPermission(getContext());
        //  if (!result) return;
        //create a new LatLng obj to store position
        if (ActivityCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleApiClient);
        if (mLastLocation != null) {
            //create a new LatLng obj to store position
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            userlat = String.valueOf(mLastLocation.getLatitude());
            userlng = String.valueOf(mLastLocation.getLongitude());
            //zoom to current position:
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        //Making marker moveable to select desired location
        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target).title("Selected Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_start)));
                    marker.showInfoWindow();
                } else {
                    marker.setPosition(googleMap.getCameraPosition().target);
                }
            }
        });
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target).title("Selected Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_start)));
                    marker.showInfoWindow();
                    //   googleMap.getCameraPosition().target.latitude;
                } else {
                    marker.setPosition(googleMap.getCameraPosition().target);
                }
            }
        });
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target).title("Selected Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_start)));
                    marker.showInfoWindow();
                    Log.e("Signup Map", " address:" + useraddress + "  lat:" + userlat + "  lng:" + userlng);

                } else {
                    marker.setPosition(googleMap.getCameraPosition().target);
                }
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        myGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnectionSuspended(int i) {
        //    Toast.makeText(getContext(), "onConnectionSuspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //   Toast.makeText(getContext(), "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity().getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    // loc();
                    // GetAddress(lat,lon);

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }



}