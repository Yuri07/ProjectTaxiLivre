package com.rsm.yuri.projecttaxilivredriver.home.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
//import android.support.design.widget.Snackbar;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.rsm.yuri.projecttaxilivredriver.BuildConfig;
import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenter;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.animation.Animation.RELATIVE_TO_SELF;


public class HomeFragment extends Fragment implements OnMapReadyCallback,
                                            HomeView, MainActivity.OnSwitchButtonClickedListener {

    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.view_progress_bar)
    ProgressBar viewProgressBar;

    @BindView(R.id.frameLayoutAceitar)
    FrameLayout frameLayoutAceitar;
    @BindView (R.id.editTxt_estimated_time_map)
    TextView editTxtEstimatedTimeMap;

    @BindView(R.id.homeFrameLayoutTop)
    FrameLayout homeFrameLayoutTop;
    @BindView(R.id.home_text_view_next_street)
    TextView textViewNextStreet;
    @BindView(R.id.home_text_view_passenger_address1)
    TextView textViewPassengerAddress1;
    @BindView(R.id.home_text_view_passenger_address2)
    TextView textViewPassengerAddress2;

    @BindView(R.id.homeFrameLayoutBottom)
    FrameLayout homeFrameLayoutBottom;
    @BindView(R.id.home_text_view_label_status_travel)
    TextView textViewStatusTravel;
    @BindView(R.id.home_text_view_label_status_travel2)
    TextView textViewStatusTravel2;
    @BindView(R.id.home_text_view_passenger_name)
    TextView textViewPassengerName;
    @BindView(R.id.home_bottom_frame_chat_img_view)
    CircleImageView chatButton;
    @BindView(R.id.home_bottom_framelayout_chat_img_view)
    FrameLayout frameLayoutChatImgView;
    @BindView(R.id.home_bottom_framelayout_chat_img_view2)
    FrameLayout frameLayoutImgViewPadding;
    @BindView(R.id.home_textview_cron_wait_passenger)
    TextView tv_time;

    //@BindView(R.id.view_progress_bar_waiting_passenger)
    ProgressBar viewProgressBarWaitingPassenger;
    @BindView(R.id.home_frag_button_iniciar_corrida)
    Button initiateTravelButton;

    @BindView(R.id.home_linear_layout_time_distance_tv)
    LinearLayout timeDistanceLinearLayout;
    @BindView(R.id.home_tv_time_to_destiny)
    TextView tvTimeToDestiny;
    @BindView(R.id.home_tv_distance_to_destiny)
    TextView tvDistanceToDestiny;

    @BindView(R.id.home_frag_button_terminar_corrida)
    Button terminateTravelButton;


    @BindView(R.id.homeFrameLayoutAvaliation)
    FrameLayout frameLayoutAvaliation;
    @BindView(R.id.home_avaliation_rating_bar)
    RatingBar avaliationRatingBar;
    @BindView(R.id.home_avaliation_edit_text_comment)
    EditText editTextAvaliationComment;
    @BindView(R.id.home_avaliation_label_ok)
    TextView tvAvaliationOk;


    @BindView(R.id.home_view_padding)
    View homeViewPadding;

    Unbinder unbinder;

    @Inject
    HomePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    //private MainActivity.OnSwitchButtonClickedListener listener;

    private GoogleMap map;
    private Location lastLocation;
    private String cidade = null;
    private Polyline polyline;

    private Geocoder geocoder;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationCallback mLocationForTravelCallback;
    private LocationCallback mLocationInTravelCallback;
    private LocationRequest mLocationRequest;

    private Marker myLocationMarker;

    private String newTravelID = "notInicialized";
    private Travel currentTravel;

    private PolylineOptions polylineOptions = null;

    private final static int PERMISSIONS_REQUEST_LOCATION = 11;

    private static final int MAP_ZOOM_PADDING = 90;
    private static final int MAP_CAMERA_ANIMATION_DURATION_IN_MILLIS = 500;

    int progress;
    CountDownTimer countDownTimer;
    int endTime = 10;
    private Bundle dataTravelRequester;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        inicializaVariavelCidade();

        //setupInjection();
        //presenter.onCreate();
        //presenter.saveCity(cidade);
        //sharedPreferences.edit().putString(TaxiLivreDriverApp.CIDADE_KEY, cidade).apply();

        createLocationRequest();
        createLocationCallback();
        createMyLocationForTravelCallback();
        createMyLocationInTravelCallback();

    }

    private String inicializaVariavelCidade() {

        Log.d("d", "HomeFragment - inicializaVariavelCidade()");

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            }
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);

            }

        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            lastLocation = location;


                            LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                            geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                String address = addresses.get(1).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                                String[] addressLines = address.split(", ");
                                String[] cidadeLine = addressLines[0].split(" - ");
                                cidade = cidadeLine[1];
                                if(cidade.equals(" Caucaia")){
                                    cidade="Fortaleza";
                                }
                                //Log.d("d", "HomeFragment - inicializaVariavelCidade()- address: " + address);
                                //Log.d("d", "HomeFragment - inicializaVariavelCidade()- cidadeLine: " + cidadeLine);
                                Log.d("d", "HomeFragment - inicializaVariavelCidade()- cidade: " + cidade);

//                                for(int i = 0;i<5;i++) {
//                                    Log.d("d", "HomeFragment - inicializaVariavelCidade()- getMaxAddressLine(): " + addresses.get(i).getMaxAddressLineIndex());
//                                    String cityName = addresses.get(i).getAddressLine(0);
//                                    String stateName = addresses.get(i).getAddressLine(1);
//                                    cidade = addresses.get(i).getLocality();
//                                    Log.d("d", "HomeFragment - inicializaVariavelCidade()- getLocality(): " + cidade);
//
//                                    String countryName = addresses.get(i).getAddressLine(2);
//                                    Log.d("d", "HomeFragment - inicializaVariavelCidade()- cityname: " + cityName);
//                                    Log.d("d", "HomeFragment - inicializaVariavelCidade()- staatename: " + stateName);
//                                    Log.d("d", "HomeFragment - inicializaVariavelCidade()- countryname: " + countryName);
//                                }
                                setupInjection(cidade);
                                presenter.onCreate();
                                //presenter.saveCity(cidade);
                                sharedPreferences.edit().putString(TaxiLivreDriverApp.CIDADE_KEY, cidade).apply();

//                                String state = addresses.get(0).getAdminArea();
//                                String country = addresses.get(0).getCountryName();
//                                String postalCode = addresses.get(0).getPostalCode();
//                                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });



        return "";
    }

    private void setupInjection(String cidade) {

//        Log.d("d", "HomeFragment - setupInjection(String cidade)");
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
//            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
//            }
//            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
//
//            }
//
//        }
//
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//
//                            lastLocation = location;
//
//
//                            LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
////                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
//                            geocoder = new Geocoder(getContext(), Locale.getDefault());
//                            List<Address> addresses;
//                            try {
//                                addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 2); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                                String address = addresses.get(1).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//
//                                String[] addressLines = address.split(", ");
//                                String[] cidadeLine = addressLines[2].split(" - ");
//                                cidade = cidadeLine[0];
//                                if(cidade.equals("Caucaia")){
//                                    cidade="Fortaleza";
//                                }
//
//                                Log.d("d", "HomeFragment - inicializaVariavelCidade()- cidade: " + cidade);
//
//                                TaxiLivreDriverApp app = (TaxiLivreDriverApp) getActivity().getApplication();
//                                app.getHomeComponent(getPare(), getParentFragment(), cidade).inject(this);
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }
//                });

        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getActivity().getApplication();
        //app.getHomeComponent(this, this, cidade).inject(this);
        app.getHomeComponent(this, this, "Fortaleza").inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //moveCameraToLastKnowLocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        moveCameraToLastKnowLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (mRequestingLocationUpdates) {
        //startLocationUpdates();
        //}
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    lastLocation = location;
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    presenter.updateLocation(position);
                }
            }
        };
    }

    private void createMyLocationForTravelCallback() {
        mLocationForTravelCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    lastLocation = location;
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    String requestyerEmail = dataTravelRequester.getString("requesterEmail");
                    Log.d("d", "HomeFragment.mLocationForTravelCallback - newTravelID" + newTravelID
                            + " requesterEmail: " + requestyerEmail);

                    presenter.updateLocationForTravel(position, requestyerEmail, newTravelID);

                    /*if(polyline!=null)
                        polyline.remove();*/

                    //drawRoute(lastLocation,new LatLng(dataTravelRequester
                    //.getDouble("latOrigem"),dataTravelRequester.getDouble("longOrigem")));//maneira correta

                    removeMyDriverFromMapScreen();
                    addMyDriverToMapScreen(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                    drawRoute(lastLocation,new LatLng(dataTravelRequester       //para testes
                            .getDouble("latDestino"),dataTravelRequester.getDouble("longDestino")));

                }
            }
        };
    }

    private void createMyLocationInTravelCallback() {
        mLocationInTravelCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    lastLocation = location;
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                    String requestyerEmail = dataTravelRequester.getString("requesterEmail");
                    Log.d("d", "HomeFragment.mLocationForTravelCallback - newTravelID" + newTravelID
                            + " requesterEmail: " + requestyerEmail);

                    presenter.updateLocationForTravel(position, requestyerEmail, newTravelID);

                    /*if(polyline!=null)
                        polyline.remove();*/

                    //drawRoute(lastLocation,new LatLng(currentTravel.getLatDestino(),
                    //                                  currentTravel.getLongDestino()));//maneira correta

                    removeMyDriverFromMapScreen();
                    addMyDriverToMapScreen(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
                    drawRoute(lastLocation,new LatLng(dataTravelRequester       //para testes
                            .getDouble("latDestino"),dataTravelRequester.getDouble("longDestino")));

                }
            }
        };
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void startLocationUpdatesForTravel() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationForTravelCallback, null);
    }

    private void startLocationUpdatesInTravel() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationInTravelCallback, null);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /*public interface OnFrameLayoutAcceptTravelClickedListener {
        public void onFrameLayoutAcceptTravelClicked(boolean switchStatus);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        if(presenter!=null)
            presenter.removeDriverFromArea();
    }

    private  void stopLocationUpdatesForTravel(){
        mFusedLocationClient.removeLocationUpdates(mLocationForTravelCallback);
    }

    private  void stopLocationUpdatesInTravel(){
        mFusedLocationClient.removeLocationUpdates(mLocationInTravelCallback);
    }

    @Override
    public void onDestroyView() {
        if(presenter != null)
            presenter.onDestroy();
        if(unbinder!=null)
            unbinder.unbind();

        super.onDestroyView();

    }

    private Location moveCameraToLastKnowLocation() {
        if (!checkReady()) {
            return null;
        }


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            }
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);

            }
            return null;
        }
        map.setMyLocationEnabled(true);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.d("d", "onSuccessListener");
                            lastLocation = location;
                            Log.d("d", "HomeFragment - movCameratoLastKnowLocation - lastlocation.getLat: " + lastLocation.getLatitude());
                            LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                        }
                    }
                });

        return lastLocation;
    }

    private boolean checkReady() {
        if (map == null) {
            //Log.d("d", "map checado como nulo.");
            return false;
        }
        //Log.d("d", "map checado como diferente de nulo.");
        return true;
    }

    @Override
    public void onLocationReadingError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitchButtonClicked(boolean switchStatus) {
        if (switchStatus) {
            /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                NearDriver nearDriver = getNearDriver(location);

                                presenter.uploadDriverDataToArea(nearDriver);

                                //LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                            }
                        }
                    });*/

            uploadDriverDataToArea();
            startLocationUpdates();

        } else {
            stopLocationUpdates();
        }
    }

    public void addMyDriverToMapScreen(LatLng locatinOfMyDriver){

        myLocationMarker = map.addMarker(new MarkerOptions()
                .position(locatinOfMyDriver)
                .title(sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr"))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp)));

        Log.d("d", "MapFragment - addMyDriverToMapScreen()");

    }

    public void removeMyDriverFromMapScreen(){
        if(myLocationMarker!=null)
            myLocationMarker.remove();
        Log.d("d", "MapFragment - removeMyDriverFromMapScreen()");
    }

    public Location getLastLocation(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lastLocation = location;


                        }
                    }
                });
        return lastLocation;
    }

    @Override
    public void onTravelRequest(Bundle dataTravelRequester) {

        desabilitarDrawerNavigation();

        stopLocationUpdates();

        this.dataTravelRequester = dataTravelRequester;

        double latTravelOrigem = dataTravelRequester.getDouble("latOrigem");
        double longTravelOrigem = dataTravelRequester.getDouble("longOrigem");
        double latTravelDestino = dataTravelRequester.getDouble("latDestino");
        double longTravelDestino = dataTravelRequester.getDouble("longDestino");

        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.d("d", "HomeFragment - mFusedLocationClient.getLastLocation()-location!=null");
                            lastLocation = location;
                            Log.d("d", "HomeFragment - mFusedLocationClient.getLastLocation()" +
                                    " - lastlocation.getLat: " + lastLocation.getLatitude());

                        }else{
                            Log.d("d", "HomeFragment - mFusedLocationClient.getLastLocation() - location==null");
                        }
                    }
                });*/

        /*Log.d("d", "HomeFragment - onTravelRequest- teste: " + teste);
        teste=112;
        Log.d("d", "HomeFragment - onTravelRequest- teste: " + teste);*/
        Log.d("d", "HomeFragment - onTravelRequest- lastlocation.getLat: " + lastLocation.getLatitude());//
        // 1-Maneira certa de fazer comunicação entre service(MyFirebaseMessaginService) e activity(MainActivity) e
        // atraves de LocalBroadCastReceiver.
        //https://stackoverflow.com/questions/14695537/android-update-activity-ui-from-service/14695943#14695943
        //https://stackoverflow.com/questions/48111608/from-service-call-activity-method-if-its-in-the-foreground
        //https://stackoverflow.com/questions/23586031/calling-activity-class-method-from-service-class


        // 2-Tentativa de entender porque starAcitivy(MainActivity.class) no myfirebaseMessagingService chamou o Homefragment com suas
        // variaveis globais nulas sendo que ja tinham sido inicializadas:
        // como onTravelRequest
        // esta sendo ativado diretamente pelo  MyFirebaseMessagingService o estado da MainActiviy não esta sendo salvo, quando
        //onResume() do mainActivity é chamado o estado da activity é restaurado mas como o estadoo método onSaved
        //https://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html

        String myApiKey = BuildConfig.GOOGLE_MAPS_API_KEY_GRADLE_PROPERTY;
        GoogleDirection.withServerKey(myApiKey)
                .from(new LatLng(latTravelDestino, longTravelDestino))
                .to(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()))
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable  Direction direction) {
                        if(direction.isOK()){
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distanceReadable = distanceInfo.getText();
                            String durationReadable = durationInfo.getText();

                            //int distance = Integer.parseInt(distanceInfo.getValue());
                            long distance = distanceInfo.getValue();
                            Log.d("d", "HomeFragment - distanceInfo.getValue(): " + distance);
                            //double travelPrice = (distance*Travel.PRICE_PER_KM)/1000;

                            //Log.d("d", "HomeFragment - travelPrice: " + travelPrice);
                            polylineOptions = DirectionConverter.createPolyline(
                                    getContext(), directionPositionList, 5, Color.BLUE);

                            polyline = map.addPolyline(polylineOptions);

                            //int duration = Integer.parseInt(durationInfo.getValue());
                            long duration = durationInfo.getValue();
                            Log.d("d", "HomeFragment - durationInfo.getValue(): " + duration);
                            double travelDuration = (duration*0.5)/30;
                            Log.d("d", "HomeFragment - travelDuration: " + travelDuration);

                            editTxtEstimatedTimeMap.setText(String.format("%.0f", travelDuration)+"min");

                            zoomRoute(map, directionPositionList);

                            frameLayoutAceitar.setVisibility(View.VISIBLE);

                            fn_countdown();

                        } else {
                            Log.d("d", "HomeFragment - onTravelRequest - direction.isOK == false");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });


    }

    @OnClick(R.id.frameLayoutAceitar)
    public void onViewClicked() {

        Log.d("d", "HomeFragment - onVieClickedt - lastlocation.getLat: " + lastLocation.getLatitude());
        countDownTimer.cancel();
        frameLayoutAceitar.setVisibility(View.GONE);
        Toast.makeText(getContext(),"Corrida aceita!", Toast.LENGTH_LONG).show();

        currentTravel = getTravel(dataTravelRequester, lastLocation);

        informMainActivityTravelAccepted();

        setupScreenOnTravelAccepted();

        //captureScreenMap();

        presenter.acceptTravel(currentTravel);

    }

    private void onFrameLayoutAceitarNotClicked() {
        habilitarDrawerNavigation();
        uploadDriverDataToArea();
        startLocationUpdates();
        presenter.notifyRequesterTravelNotAccepted(dataTravelRequester.getString("requesterEmail"));
        cleanMapRoute();
        frameLayoutAceitar.setVisibility(View.GONE);
    }

    @Override
    public void onTravelCreated(String newTravelID){
        this.newTravelID = newTravelID;
        currentTravel.setTravelId(newTravelID);
        Log.d("d", "HomeFragment.onTravelCreated- newTravelID" + this.newTravelID);
        startLocationUpdatesForTravel();

        fn_countdownSimulateArrival();

        //fn_countdownProgressBarWaitingPassenger();

    }

    private void fn_countdownProgressBarWaitingPassenger() {

        progress = 1;
        endTime = 60;

        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //setProgressBarWaitingPassengerProgress(progress, endTime);
                progress = progress + 1;

                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                //int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                String newtime = minutes + ":" + seconds;

                if (newtime.equals("0:0")) {
                    tv_time.setText("00:00");
                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    tv_time.setText("0" + minutes + ":0" + seconds);
                } else if ((String.valueOf(minutes).length() == 1)) {
                    tv_time.setText("0" + minutes + ":" + seconds);
                } else if ((String.valueOf(seconds).length() == 1)) {
                    tv_time.setText(minutes + ":0" + seconds);
                } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                    tv_time.setText(minutes + ":0" + seconds);
                } else if (String.valueOf(minutes).length() == 1) {
                    tv_time.setText("0" + minutes + ":" + seconds);
                } else if (String.valueOf(seconds).length() == 1) {
                    tv_time.setText(minutes + ":0" + seconds);
                } else {
                    tv_time.setText(minutes + ":" + seconds);
                }

            }

            @Override
            public void onFinish() {
                //setProgressBarWaitingPassengerProgress(progress, endTime);
                onPassengerNotArriverd();

            }
        };
        countDownTimer.start();

    }

    private void fn_countdownSimulateArrival() {

        progress = 1;
        endTime = 20;

        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress = progress + 1;
            }

            @Override
            public void onFinish() {
                onArrivalToGetPassenger();
            }
        };
        countDownTimer.start();

    }

    public void onArrivalToGetPassenger(){

        setupFramesLayoutOnArrivalToGetPassenger();

        fn_countdownProgressBarWaitingPassenger();

    }



    @OnClick(R.id.home_frag_button_iniciar_corrida)
    public void onInitiateTravelButtonClicked(){
        stopLocationUpdatesForTravel();
        startLocationUpdatesInTravel();

        String requesterEmail = dataTravelRequester.getString("requesterEmail");

        presenter.initiateTravel(requesterEmail, newTravelID);

        countDownTimer.cancel();

        setupScreenOnInitiateJourney();

        startLocationUpdatesInTravel();

        fn_countdownSimulateArrivalInDestiny();

    }

    public void onArrivalToLandingPassenger(){
        setupScreenOnArrivalToLandingPassenger();

    }

    @OnClick(R.id.home_frag_button_terminar_corrida)
    public void onTerminateTravelButtonClicked(){
        stopLocationUpdatesInTravel();

        presenter.terminateTravel(currentTravel.getRequesterEmail(), currentTravel.getTravelId());

        setupScreenOnTerminateTravel();

    }

    @OnClick(R.id.home_avaliation_label_ok)
    public void onAvaliationLabelClicked() {
        habilitarDrawerNavigation();

        frameLayoutAvaliation.setVisibility(View.GONE);

        Rating rating = setupRatingFromAvaliationFrameLayout();

        presenter.uploadUserRating(currentTravel.getRequesterEmail(), rating);

        homeViewPadding.setVisibility(View.GONE);

        informMainActivityTravelCompleted();

    }


    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds currentLatLongBounds =
                googleMap.getProjection().getVisibleRegion().latLngBounds;
        boolean updateBounds = false;

        for (LatLng latLng : lstLatLngRoute) {
            if (!currentLatLongBounds.contains(latLng)) {
                updateBounds = true;
            }
        }

        if (updateBounds) {

            CameraUpdate cameraUpdate;

            if (lstLatLngRoute.size() == 1) {

                LatLng latLng = lstLatLngRoute.iterator().next();
                cameraUpdate = CameraUpdateFactory.newLatLng(latLng);

            } else {

                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (LatLng latLng : lstLatLngRoute) {
                    builder.include(latLng);
                }
                LatLngBounds latLongBounds = builder.build();

                cameraUpdate =
                        CameraUpdateFactory.newLatLngBounds(latLongBounds, MAP_ZOOM_PADDING);

            }

            try {
                googleMap.animateCamera(cameraUpdate, MAP_CAMERA_ANIMATION_DURATION_IN_MILLIS,
                        new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
            } catch (IllegalStateException ex) {
                // Ignore it. We're just being a bit lazy, as this exception only happens if
                // we try to animate the camera before the map has a size
            }
        }
    }

    private void drawRoute(Location originLocation, LatLng destinyLocation){
        String myApiKey = BuildConfig.GOOGLE_MAPS_API_KEY_GRADLE_PROPERTY;
        GoogleDirection.withServerKey(myApiKey)
                .from(new LatLng(originLocation.getLatitude(), originLocation.getLongitude()))
                .to(destinyLocation)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable  Direction direction) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distanceReadable = distanceInfo.getText();
                            String durationReadable = durationInfo.getText();

                            //int distance = Integer.parseInt(distanceInfo.getValue());
                            long distance = distanceInfo.getValue();
                            Log.d("d", "MapFragment - drawRoute() - distanceInfo.getValue(): " + distance);
                            //double travelPrice = (distance*Travel.PRICE_PER_KM)/1000;
                            //travelRequest.setTravelPrice(travelPrice);
                            //Log.d("d", "MapFragment - travelPrice: " + travelPrice);

                            polylineOptions = DirectionConverter.createPolyline(
                                    getContext(), directionPositionList, 5, Color.BLUE);

                            if(polyline!=null)
                                polyline.remove();

                            polyline = map.addPolyline(polylineOptions);

                            zoomRoute(map, directionPositionList);


                        } else {
                            Log.d("d", "MapFragment - drawRoute() - direction.isOK == false");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                    }
                });
    }

    public void cleanMapRoute(){
        if(polyline!=null)
            polyline.remove();
    }

    public NearDriver getNearDriver(Location location){
        NearDriver nearDriver = new NearDriver();
        nearDriver.setEmail(sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr"));
        nearDriver.setUrlPhotoDriver(sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "url_sh_pr"));
        nearDriver.setUrlPhotoCar(sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, "url_sh_pr"));
        nearDriver.setNome(sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr"));
        nearDriver.setModelo(sharedPreferences.getString(TaxiLivreDriverApp.MODELO_KEY, "modelo_sh_pr"));
        nearDriver.setPlaca(sharedPreferences.getString(TaxiLivreDriverApp.PLACA_KEY, "placa_sh_pr"));
        nearDriver.setTotalTravels(sharedPreferences.getInt(TaxiLivreDriverApp.TOTAL_RATINGS_KEY, -1));
        nearDriver.setAverageRatings(sharedPreferences.getFloat(TaxiLivreDriverApp.AVERAG_RATING_KEY, 5.5f));
        nearDriver.setLatitude(location.getLatitude());
        nearDriver.setLongitude(location.getLongitude());
        //nearDriver.setCount5Stars();
        return nearDriver;
    }

    public Travel getTravel(Bundle dataRequester, Location location){
        Travel travel = new Travel();
        travel.setDriverEmail(sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr"));
        travel.setUrlPhotoDriver(sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "url_sh_pr"));
        travel.setUrlPhotoCar(sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, "url_sh_pr"));
        travel.setNomeDriver(sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr"));
        travel.setModelo(sharedPreferences.getString(TaxiLivreDriverApp.MODELO_KEY, "modelo_sh_pr"));
        travel.setPlaca(sharedPreferences.getString(TaxiLivreDriverApp.PLACA_KEY, "placa_sh_pr"));
        travel.setTotalTravels(sharedPreferences.getInt(TaxiLivreDriverApp.TOTAL_RATINGS_KEY, -1));
        travel.setAverageRatings(sharedPreferences.getFloat(TaxiLivreDriverApp.AVERAG_RATING_KEY, 5.5f));

        travel.setLatDriver(location.getLatitude());
        travel.setLongDriver(location.getLongitude());

        Log.d("d", "HomeFragment.getTravel() requesterEmail: " +
                            dataRequester.getString("requesterEmail"));
        Log.d("d", "HomeFragment.getTravel() requesterName " +
                            dataRequester.getString("requesterName"));

        Log.d("d", "HomeFragment.getTravel() urlPhotoUser: " +
                dataRequester.getString("urlPhotoUser"));
        Log.d("d", "HomeFragment.getTravel() averageRatingsPassenger " +
                dataRequester.getDouble("averageRatingsPassenger"));

        Log.d("d", "HomeFragment.getTravel() placeOriginAddress: " +
                dataRequester.getString("placeOriginAddress"));
        Log.d("d", "HomeFragment.getTravel() placeDestinoAddress " +
                dataRequester.getString("placeDestinoAddress"));
        Log.d("d", "HomeFragment.getTravel() latOrigem: " +
                            dataRequester.getDouble("latOrigem"));
        Log.d("d", "HomeFragment.getTravel() longOrigem: " +
                            dataRequester.getDouble("longOrigem"));
        Log.d("d", "HomeFragment.getTravel() latDestino: " +
                            dataRequester.getDouble("latDestino"));
        Log.d("d", "HomeFragment.getTravel() longDestino: " +
                            dataRequester.getDouble("longDestino"));
        Log.d("d", "HomeFragment.getTravel() travelDate: " +
                dataRequester.getString("travelDate"));
        Log.d("d", "HomeFragment.getTravel() travelPrice: " +
                dataRequester.getDouble("travelPrice"));
        Log.d("d", "HomeFragment.getTravel() urlPhotoMap: " +
                dataRequester.getString("urlPhotoMap"));

        travel.setRequesterEmail(dataRequester.getString("requesterEmail"));
        travel.setRequesterName(dataRequester.getString("requesterName"));
        travel.setUrlPhotoUser(dataRequester.getString("urlPhotoUser"));
        travel.setAverageRatingsPassenger(dataRequester.getDouble("averageRatingsPassenger"));

        travel.setPlaceOriginAddress(dataRequester.getString("placeOriginAddress"));
        travel.setPlaceDestinoAddress(dataRequester.getString("placeDestinoAddress"));
        travel.setLatOrigem(dataRequester.getDouble("latOrigem"));
        travel.setLongOrigem(dataRequester.getDouble("longOrigem"));
        travel.setLatDestino(dataRequester.getDouble("latDestino"));
        travel.setLongDestino(dataRequester.getDouble("longDestino"));
        travel.setTravelDate(dataRequester.getString("travelDate"));
        travel.setTravelPrice(dataRequester.getDouble("travelPrice"));
        travel.setUrlPhotoMap(dataRequester.getString("urlPhotoMap"));
        return travel;
    }

    public void setupAnimationProgressBarView() {

        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        viewProgressBar.startAnimation(makeVertical);
        viewProgressBar.setSecondaryProgress(endTime);
        viewProgressBar.setProgress(0);
    }

    private void fn_countdown() {

        /*if (et_timer.getText().toString().length()>0) {
            myProgress = 0;

            try {
                countDownTimer.cancel();

            } catch (Exception e) {

            }*/

        //String timeInterval = et_timer.getText().toString();
        progress = 1;
        //endTime = Integer.parseInt(timeInterval); // up to finish time
        endTime = 20;

        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setProgress(progress, endTime);
                progress = progress + 1;
                    /*int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                    String newtime = hours + ":" + minutes + ":" + seconds;

                    if (newtime.equals("0:0:0")) {
                        tv_time.setText("00:00:00");
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + hours + ":0" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(minutes).length() == 1)) {
                        tv_time.setText("0" + hours + ":0" + minutes + ":" + seconds);
                    } else if ((String.valueOf(hours).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText("0" + hours + ":" + minutes + ":0" + seconds);
                    } else if ((String.valueOf(minutes).length() == 1) && (String.valueOf(seconds).length() == 1)) {
                        tv_time.setText(hours + ":0" + minutes + ":0" + seconds);
                    } else if (String.valueOf(hours).length() == 1) {
                        tv_time.setText("0" + hours + ":" + minutes + ":" + seconds);
                    } else if (String.valueOf(minutes).length() == 1) {
                        tv_time.setText(hours + ":0" + minutes + ":" + seconds);
                    } else if (String.valueOf(seconds).length() == 1) {
                        tv_time.setText(hours + ":" + minutes + ":0" + seconds);
                    } else {
                        tv_time.setText(hours + ":" + minutes + ":" + seconds);
                    }*/

            }

            @Override
            public void onFinish() {

                setProgress(progress, endTime);
                onFrameLayoutAceitarNotClicked();

            }
        };

        countDownTimer.start();

    }

    public void setProgress(int startTime, int endTime) {
        viewProgressBar.setMax(endTime);
        viewProgressBar.setSecondaryProgress(endTime);
        viewProgressBar.setProgress(startTime);

    }



    public void setProgressBarWaitingPassengerProgress(int startTime, int endTime) {
        viewProgressBarWaitingPassenger.setMax(endTime);
        viewProgressBarWaitingPassenger.setSecondaryProgress(endTime);
        viewProgressBarWaitingPassenger.setProgress(startTime);
    }



    private void fn_countdownSimulateArrivalInDestiny() {

        progress = 1;
        endTime = 40;

        countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress = progress + 1;
            }

            @Override
            public void onFinish() {
                onArrivalToLandingPassenger();
            }
        };
        countDownTimer.start();

    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }



    private String[] getSplittedAddress(String completeAddress){
        String[] placeOriginAddressSplitted = completeAddress.split(",");
        String address2 = placeOriginAddressSplitted[1] + ", " + placeOriginAddressSplitted[2];
        String[] address = {placeOriginAddressSplitted[0], address2};
        return address;
    }

    public void uploadDriverDataToArea(){
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            NearDriver nearDriver = getNearDriver(location);

                            presenter.uploadDriverDataToArea(nearDriver);

                            //LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                        }
                    }
                });
    }

    private void onPassengerNotArriverd(){
        Log.d("d", "HomeFragment - onPassengerNotArrived");
    }

    private void habilitarDrawerNavigation(){
        if(getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Log.d("d", "HomeFragment - habilitarDrawerNavigation - msgTravelAcceptedFromHomeFragment");
            mainActivity.enableDrawNavigation();
        }
    }

    private void desabilitarDrawerNavigation(){
        if(getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Log.d("d", "HomeFragment - desabilitarDrawerNavigation - msgTravelAcceptedFromHomeFragment");
            mainActivity.disableDrawNavigation();
        }
    }

    private void informMainActivityTravelAccepted(){
        if(getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Log.d("d", "HomeFragment - informMainActivityTravelAccepted - msgTravelAcceptedFromHomeFragment");
            mainActivity.msgTravelAcceptedFromHomeFragment();
        }
    }

    private void informMainActivityTravelCompleted(){
        if(getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Log.d("d", "HomeFragment - informMainActivityTravelCompleted - msgTravelCompletedFromHomeFragment");
            mainActivity.msgTravelCompletedFromHomeFragment();
        }
    }

    private void setupScreenOnTravelAccepted(){
        homeViewPadding.setVisibility(View.GONE);
        homeFrameLayoutTop.setVisibility(View.VISIBLE);
        homeFrameLayoutBottom.setVisibility(View.VISIBLE);
        String[] address = getSplittedAddress(currentTravel.getPlaceOriginAddress());
        textViewPassengerAddress1.setText(address[0]);
        textViewPassengerAddress2.setText(address[1]);
        textViewPassengerName.setText(currentTravel.getRequesterName());
        Log.d("d", "HomeFragment - onViewClicked - antes de presenter.acceptTravel");

        if(polyline!=null)
            polyline.remove();
    }

    private void setupFramesLayoutOnArrivalToGetPassenger(){
        textViewStatusTravel.setVisibility(View.GONE);
        textViewStatusTravel2.setVisibility(View.VISIBLE);
        textViewNextStreet.setVisibility(View.GONE);
        tv_time.setVisibility(View.VISIBLE);
        //viewProgressBarWaitingPassenger.setVisibility(View.VISIBLE);
        initiateTravelButton.setVisibility(View.VISIBLE);
    }

    private void setupScreenOnInitiateJourney(){
        frameLayoutChatImgView.setVisibility(View.GONE);
        frameLayoutImgViewPadding.setVisibility(View.GONE);
        textViewStatusTravel.setVisibility(View.VISIBLE);
        textViewStatusTravel2.setVisibility(View.GONE);
        textViewStatusTravel.setText("Desembarque de "+currentTravel.getRequesterName());
        textViewPassengerName.setVisibility(View.GONE);
        textViewNextStreet.setVisibility(View.VISIBLE);
        initiateTravelButton.setVisibility(View.GONE);
        tv_time.setVisibility(View.GONE);
        //viewProgressBarWaitingPassenger.setVisibility(View.GONE);
        chatButton.setVisibility(View.INVISIBLE);

        String[] address = getSplittedAddress(currentTravel.getPlaceDestinoAddress());
        textViewPassengerAddress1.setText(address[0]);
        textViewPassengerAddress2.setText(address[1]);

        timeDistanceLinearLayout.setVisibility(View.VISIBLE);
        tvTimeToDestiny.setText("0.1 min");
        tvDistanceToDestiny.setText("0.1 km");
    }

    private void setupScreenOnArrivalToLandingPassenger(){
        textViewPassengerAddress2.setVisibility(View.GONE);
        timeDistanceLinearLayout.setVisibility(View.GONE);
        terminateTravelButton.setVisibility(View.VISIBLE);
    }
    private void setupScreenOnTerminateTravel(){

        homeFrameLayoutTop.setVisibility(View.GONE);
        homeFrameLayoutBottom.setVisibility(View.GONE);
        frameLayoutChatImgView.setVisibility(View.VISIBLE);
        frameLayoutImgViewPadding.setVisibility(View.INVISIBLE);
        textViewNextStreet.setVisibility(View.VISIBLE);
        textViewPassengerAddress2.setVisibility(View.VISIBLE);
        textViewStatusTravel.setText("Aguardando Usuário");
        textViewPassengerName.setVisibility(View.VISIBLE);
        frameLayoutAvaliation.setVisibility(View.VISIBLE);
    }

    private Rating setupRatingFromAvaliationFrameLayout(){
        Rating rating = new Rating();
        rating.setTravelId(newTravelID);
        rating.setEmail(sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr"));
        rating.setNome(sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr"));
        rating.setUrlPhotoUser("driverComment");
        Date currentTime = Calendar.getInstance().getTime();
        rating.setDate(currentTime.toString());
        rating.setVote(avaliationRatingBar.getNumStars());
        rating.setComment(String.valueOf(editTextAvaliationComment.getText()));
        return rating;
    }

    public void captureScreenMap(){

        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback()
        {

            @Override
            public void onSnapshotReady(Bitmap snapshot)
            {
                // TODO Auto-generated method stub
                Bitmap bitmap = snapshot;

                OutputStream fout = null;

                String filePath = System.currentTimeMillis() + ".jpeg";

                try
                {
                    //fout = openFileOutput(filePath, MODE_WORLD_READABLE);

                    // Write the string to the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                }
                catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }

                //openShareImageDialog(filePath);
            }
        };

        map.snapshot(callback);
    }

}
