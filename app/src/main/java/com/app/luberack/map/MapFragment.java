package com.app.giftfcard.map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.giftfcard.Profile_management.SessionManager;
import com.app.giftfcard.R;
import com.app.giftfcard.utility.AlertDialogManager;
import com.app.giftfcard.utility.Config;
import com.app.giftfcard.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Main screen to show other users on map
public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SwipeRefreshLayout.OnRefreshListener {
    ImageView imageView;

    String id, name, image, review, address, lat, lng, type;
    TextView reviews, shopName, location;
    Button view_shop;
    //TextView rating;
    private AlertDialogManager alert = new AlertDialogManager();
    MapView mMapView;
    Context context;
    private GoogleMap googleMap;
    Location mLastLocation;
    GoogleApiClient myGoogleApiClient;
    LatLng latLng;
    String userlat, userlng, userName, userAddress, userId;
    List<Marker> properties_markers = new ArrayList<>();
    JSONArray jsonArr = null;
    JSONArray searchResultsJsonArray = new JSONArray();
    // Session Manager Class
    private SessionManager session;
    private ProgressDialog sweetProgressDialog;
    AlertDialog alertDialog;
    Fragment fragment = null;
    JSONArray dataArr = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        View v = inflater.inflate(R.layout.activity_curr_loc, container,
                false);
        // Session Manager
        session = new SessionManager(getContext());
        imageView = v.findViewById(R.id.iv_oil_shop);
        location = v.findViewById(R.id.tv_shop_address);
        reviews = v.findViewById(R.id.tv_review);
        shopName = v.findViewById(R.id.tv_shop_name);
        view_shop = v.findViewById(R.id.btn_view_shop);

        sweetProgressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.retr)));
        sweetProgressDialog.setCancelable(false);

        Bundle b = getArguments();
        id = b.getString("id");
        name = b.getString("name");
        image = b.getString("img_url");
        review = b.getString("reviews");
        address = b.getString("address");
        lat = b.getString("lat");
        lng = b.getString("address");
        type = b.getString("type");
        Log.e("TYPE :", "" + type+id+address);
        location.setText(address);
        shopName.setText(name);
        reviews.setText(review);
        Picasso.with(context).load(image).into(imageView);

        jsonArr = new JSONArray();

        // Configure the swipe refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorAccent,
                R.color.colorPrimaryDark, R.color.colorAccent);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Utility.isNetworkAvailable(getActivity())) {
            if (Utility.isLocationEnabled(getActivity())) {
                mMapView.getMapAsync(this);
            } else {
                alert.showLocationAlertDialog(getContext());
            }
        } else {
            alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
        }
        //    mMapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onRefresh() {
        // Start showing the refresh animation
        mSwipeRefreshLayout.setRefreshing(true);

        // Simulate a long running activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utility.isNetworkAvailable(getActivity())) {
                    if (Utility.isLocationEnabled(getActivity())) {
                        mMapView.getMapAsync(MapFragment.this);
                    } else {
                        alert.showLocationAlertDialog(getContext());
                    }
                } else {
                    alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                }
            }
        }, 5000);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.setIndoorEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//Checking location permission for android version > 5
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
        googleMap.setPadding(0, 0, 0, 160);
        if (mMapView != null &&
                mMapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 20, 400);

        }
        buildGoogleApiClient();
        myGoogleApiClient.connect();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                if (arg0.getTitle().equals("Your current Location")) // if marker source is clicked
                {
                    Toast.makeText(getActivity(), arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                } else {
                    if (dataArr != null) {
                        if (!dataArr.equals("")) {
                            JSONObject resultsObj = null;
                            try {
                                resultsObj = new JSONObject(arg0.getTitle());
                                userName = resultsObj.getString("name");
                                userAddress = resultsObj.getString("address");
                                userId = resultsObj.getString("user_id");

                                //onFetchSuccess();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            retrieveServicesAgain();
                        }
                    } else {
                        retrieveServicesAgain();
                    }

                }
                return true;
            }

        });

        //Driving directions between two points
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                // Setting Dialog Title
                alertDialog.setTitle("Directions");
                // Setting Dialog Message
                alertDialog.setMessage("Are You sure You Want to get directions from this point?");
                if ((Boolean) false != null)
                    // Setting OK Button
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                            "http://maps.google.com/maps?saddr=" + mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude() + "&daddr=" + latLng.latitude + " , " + latLng.longitude));
                                    startActivity(intent);
                                }
                            });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                        "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                            }
                        });
                // Showing Alert Message
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        //on connection it gets the last available location and moves the pointer to that location
        boolean result = Utility.checkPermission(getContext());
        if (!result) return;
        //create a new LatLng obj to store position
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(myGoogleApiClient);
        if (mLastLocation != null) {
            //create a new LatLng obj to store position
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            userlat = String.valueOf(mLastLocation.getLatitude());
            userlng = String.valueOf(mLastLocation.getLongitude());

            if (Utility.isNetworkAvailable(getActivity())) {
                //      engine();
            } else {
                alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
            }
            //zoom to current position:
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } else if (session.getLat() != null && session.getLng() != null) {
            userlat = session.getLat();
            userlng = session.getLng();
            if (!userlat.equals("") && !userlng.equals("")) {
                latLng = new LatLng(Double.parseDouble(userlat), Double.parseDouble(userlng));

                getShops();

                if (Utility.isNetworkAvailable(getActivity())) {
                    //engine();
                    getShops();
                } else {
                    alert.showAlertDialog(getContext(), "No Network Available", "Please turn on Internet Connectivity", false);
                }
                //zoom to current position:
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        myGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void getShops() {
        sweetProgressDialog = ProgressDialog.show(getActivity(), "Loading nearby shops", "Please Wait...", false, false);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty() || response == null || response.equals("0")) {
                    sweetProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Alert: Email/Mobile No OR Password Incorrect.", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    sweetProgressDialog.dismiss();
                    Log.e("tag", "response " + response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    // boolean error = jObj.getBoolean("error");
                    if (success == -2) {
                        sweetProgressDialog.dismiss();
                        Toast.makeText(getContext(), "No Shops near you!", Toast.LENGTH_LONG).show();
                    } else if (success == 1) {
                        sweetProgressDialog.dismiss();
                        Toast.makeText(getContext(), "Shops near you.", Toast.LENGTH_LONG).show();
                        JSONObject temp = jObj.getJSONObject("shops");
                        JSONArray jsonObject = temp.getJSONArray("results");
                        // Toast.makeText(getContext(), "LNG"+session.getLongitude(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(), "LAT"+session.getLatitude(), Toast.LENGTH_SHORT).show();

                        Log.d("Response", response);
                        SetProperties(jsonObject);
                    }
                } catch (Exception e) {
                    //   if (sweetAlertDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                    //  }
                    Log.i("myTag", e.toString());
                    Toast.makeText(getActivity(), "Alert: Connection Error", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();

                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", "Connection Error!");
                        //  if (sweetAlertDialog.isShowing()) {
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        //   }
                        Toast.makeText(getActivity(), "" + "Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected java.util.Map<String, String> getParams() {
                // Posting params to register url
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "showNearShops");
                params.put("id", id);
                params.put("type",type);

                //  params.put("RegId",regId);
                return params;
            }
        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        //  sweetAlertDialog.show();
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

    //Showing all user's markers on map
    public void SetProperties(JSONArray jsonArray) {
        JSONObject resultsObj = null;
        String lat = null;
        String lng = null;
        try {
            // before setting new data, first remove the old markers
            for (int i = 0; i < properties_markers.size(); i++) {
                properties_markers.get(i).remove();
            }
            // also clear the holding list
            properties_markers.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                resultsObj = jsonArray.getJSONObject(i);
                Log.e("resultObj is: ", " " + resultsObj);
                lat = resultsObj.getString("lat");
                lng = resultsObj.getString("lng");

                if (lat != null && !lat.equals("")) {
                    if (((lat != null) || !(lat.equals("")) || (lat.equalsIgnoreCase("null"))) && ((lng != null) || !(lng.equals("")) || (lng.equalsIgnoreCase("null")))) {
                        //create a new LatLng obj to store position
                        LatLng temp = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        MarkerOptions markerOptions = new MarkerOptions();
                        //set the position of marker to new location
                        markerOptions.position(temp);
                        //set name in title
                        markerOptions.title("" + resultsObj.toString());
                        // Changing marker icon
                        Drawable layer1 = null;
                        layer1 = ContextCompat.getDrawable(getActivity(), R.drawable.profile_logo);

                        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(layer1);
                        markerOptions.icon(markerIcon);
                        Marker marker = googleMap.addMarker(markerOptions);
                        properties_markers.add(marker);
                    }
                }
// Signify that we are done refreshing
                mSwipeRefreshLayout.setRefreshing(false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSwipeRefreshLayout.setEnabled(false);

    }

    //Retrieving all users from server
    private void engine() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                try {
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    if (success == 1) {

                        //else showing data on map
                        JSONObject temp = jObj.getJSONObject("users");
                        jsonArr = temp.getJSONArray("results");
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        retrieveServices();
                        SetProperties(jsonArr);


                    } //if success
                    else if (success == -3) {
                        String errorMsg = jObj.getString("error_msg");
                        alert.showAccountDeletedAlertDialog(getContext(), "Failed!", "" + errorMsg, false);
                    }
//                    else if (success == -4) {
//                        String balance= jObj.getString("balance");
//                        if(balance!=null && !balance.isEmpty() && Integer.parseInt(balance)<0){
//                            //Payment remaining
//                            int balanceInt=Integer.parseInt(balance)*(-1);
//                            session.saveAmount(String.valueOf(balanceInt));
//                            session.savePaid("0");
//                            Intent intent = new Intent(getActivity(), PaymentMainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//                    }
                    else {
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        // Error occurred. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(), "" + errorMsg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                        Toast.makeText(getContext(), "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "showNearShops");
                params.put("id", id);
                params.put("type", type);
//                params.put("lat", userlat);
//                params.put("lng", userlng);
                return params;
            }
        };
        myReq.setRetryPolicy(new DefaultRetryPolicy(
                25000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        myReq.setShouldCache(false);
        queue.add(myReq);
        sweetProgressDialog.show();
    }

    //  Making custom marker icon
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        alertDialog.dismiss();
        mMapView.getMapAsync(MapFragment.this);
        Toast.makeText(getActivity(), "Swipe down to refresh", Toast.LENGTH_LONG).show();

    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search Requests");

        // Here we can implement the funcionality we want when the Search Query is made.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  Toast.makeText(getActivity(),query, Toast.LENGTH_SHORT).show();
                searchView.clearFocus();
                searchAndDisplayResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Here we can clear the search results when the cross button is pressed.
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                // The logic for clearing the search results goes here. //
                //   Toast.makeText(getActivity(),"Results Cleared", Toast.LENGTH_SHORT).show();
                clearSearchResults();
            }
        });
    }*/
/*
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            session.logoutUser();
            return true;
        } else if (id == R.id.menuitem_invite_friends) {
            //  Toast.makeText(getActivity(), "NO Fragment Found", Toast.LENGTH_SHORT).show();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getActivity().getResources().getString(R.string.app_link));
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    // Member function searches the adapter data and shows the results in the list view.
    public void searchAndDisplayResults(String searchQuery) {
        sweetProgressDialog = new ProgressDialog(getActivity());
        sweetProgressDialog.setMessage(String.format(getResources().getString(R.string.searching_data)));
        sweetProgressDialog.setCancelable(false);

        sweetProgressDialog.show();
        searchResultsJsonArray = null;
        searchResultsJsonArray = new JSONArray();
        JSONObject resultsObj = null;
        String name = "";
        String address = "";

        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                resultsObj = jsonArr.getJSONObject(i);
                name = resultsObj.getString("name");
                address = resultsObj.getString("address");

                //   name = resultsObj.getString("name");
                //  address = resultsObj.getString("address");
                if (compareStringsSimilarity(searchQuery, name)
                        || compareStringsSimilarity(searchQuery, address)
                        ) {
                    searchResultsJsonArray.put(resultsObj);
                }
            }
        } catch (JSONException e) {
            Log.e("JSONException", " " + e);
        }

        SetProperties(searchResultsJsonArray);
        if (sweetProgressDialog.isShowing()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    sweetProgressDialog.dismiss();
                }
            });
        }
    }

    public void clearSearchResults() {
        SetProperties(jsonArr);
    }

    public boolean compareStringsSimilarity(String s1, String s2) {
        //s1->Search Query / Primary String
        //s2->News attribute with which we are comparing the search query.
        // Returns true if the strings are similar to a particular degree/threshold.
        final double THRESHOLD = 0.98; // Value between 0 and 1
        /*
        Increasing the threshold value strickens the comparison. Decreasing it relaxes the comparison.
        If you desire that the comparison be extremely accurate increase the value to near 1.0;
        1.000 is the similarity between "" and ""
        0.100 is the similarity between "1234567890" and "1"
        0.300 is the similarity between "1234567890" and "123"
        0.700 is the similarity between "1234567890" and "1234567"
        1.000 is the similarity between "1234567890" and "1234567890"
        0.800 is the similarity between "1234567890" and "1234567980"
        0.857 is the similarity between "47/2010" and "472010"
        0.714 is the similarity between "47/2010" and "472011"
        0.000 is the similarity between "47/2010" and "AB.CDEF"
        0.125 is the similarity between "47/2010" and "4B.CDEFG"
        0.000 is the similarity between "47/2010" and "AB.CDEFG"
        0.700 is the similarity between "The quick fox jumped" and "The fox jumped"
        0.350 is the similarity between "The quick fox jumped" and "The fox"
        0.571 is the similarity between "kitten" and "sitting"
         */
        double similarityIndex;

        String[] s1Array = s1.split(" "); // Splits the search query into words for better recognition.
        String[] s2Array = s2.split(" "); // Splits the internship attribute string into seperate word for better recognition.

        for (String s1word : s1Array) {
            for (String s2word : s2Array) {
                similarityIndex = Utility.similarity(s1word, s2word);
                //Log.d("Similarity Index",Double.toString(similarityIndex));
                if (similarityIndex > THRESHOLD) {
                    return true;
                }
            }
        }
        return false;
    }

    private void retrieveServices() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                Log.e("myTag:", "" + response);
                try {
                    Log.e("myRES", response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    // boolean error = jObj.getBoolean("error");
                    if (success == 1) {
                        JSONObject temp = jObj.getJSONObject("services");
                        dataArr = temp.getJSONArray("results");
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.e("myTag", e.toString());
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("myTag", "error response" + error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getservices");
                params.put("user_id", "1");
                return params;
            }

        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        myReq.setShouldCache(false);
        queue.add(myReq);
        if (!sweetProgressDialog.isShowing()) {
            sweetProgressDialog.show();
        }
    }

    private void retrieveServicesAgain() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest myReq = new StringRequest(Request.Method.POST,
                Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (sweetProgressDialog.isShowing()) {
                    sweetProgressDialog.dismiss();
                }
                Log.e("myTag:", "" + response);
                try {
                    Log.e("myRES", response);
                    JSONObject jObj = new JSONObject(response);
                    int success = jObj.getInt("success");
                    // boolean error = jObj.getBoolean("error");
                    if (success == 1) {
                        JSONObject temp = jObj.getJSONObject("services");
                        dataArr = temp.getJSONArray("results");

                        //onFetchSuccess();
                    }

                } catch (JSONException e) {
                    if (sweetProgressDialog.isShowing()) {
                        sweetProgressDialog.dismiss();
                    }
                    Log.e("myTag", e.toString());
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("myTag", "error response" + error.toString());
                        if (sweetProgressDialog.isShowing()) {
                            sweetProgressDialog.dismiss();
                        }
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getservices");
                params.put("user_id", "1");
                return params;
            }

        };


        myReq.setRetryPolicy(new DefaultRetryPolicy(
                20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        myReq.setShouldCache(false);
        queue.add(myReq);
        if (!sweetProgressDialog.isShowing()) {
            sweetProgressDialog.show();
        }
    }


    public void showLocationAlertDialog(final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.gps_network_not_enabled));

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                //get gps
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }
/*
    public void onFetchSuccess(){
        Log.e("myTag", "onFetch success");

        fragment =new TabbedHomeServicesFragment();
        Bundle args = new Bundle();
        args.putString("dataArr", dataArr.toString());
        args.putString("userName", userName);
        args.putString("userAddress", userAddress);
        args.putString("userId", userId);
        Log.e("MapFragment", "user Address: "+userAddress);
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.user_frame,fragment)
                .addToBackStack("customtag")
                .commit();
    }*/
}