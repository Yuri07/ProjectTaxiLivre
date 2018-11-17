package com.rsm.yuri.projecttaxilivredriver.home.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rsm.yuri.projecttaxilivredriver.BuildConfig;
import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenter;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;


public class HomeFragment extends Fragment implements OnMapReadyCallback, HomeView, MainActivity.OnSwitchButtonClickedListener {

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
    @BindView(R.id.home_text_view_passenger_address1)
    TextView textViewPassengerAddress1;
    @BindView(R.id.home_text_view_passenger_address2)
    TextView textViewPassengerAddress2;

    @BindView(R.id.homeFrameLayoutBottom)
    FrameLayout homeFrameLayoutBottom;
    @BindView(R.id.home_text_view_passenger_name)
    TextView textViewPassengerName;

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
    private int teste;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

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

        setupInjection();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        createLocationRequest();
        createLocationCallback();

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

    private void setupInjection() {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getActivity().getApplication();
        app.getHomeComponent(this, this).inject(this);
        //Log.d("d", "ProfileFragment.setupInjection:finalizada");
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
        presenter.removeDriverFromArea();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
                            Log.d("Fragment", "onSuccess");
                            lastLocation = location;
                            teste = 111;
                            Log.d("d", "HomeFragment - movCameratoLastKnowLocation - teste: " + teste);
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

            startLocationUpdates();
        } else {
            stopLocationUpdates();
        }
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

        stopLocationUpdates();

        frameLayoutAceitar.setVisibility(View.VISIBLE);
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
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()){
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distanceReadable = distanceInfo.getText();
                            String durationReadable = durationInfo.getText();

                            int distance = Integer.parseInt(distanceInfo.getValue());
                            Log.d("d", "HomeFragment - distanceInfo.getValue(): " + distance);
                            //double travelPrice = (distance*Travel.PRICE_PER_KM)/1000;

                            //Log.d("d", "HomeFragment - travelPrice: " + travelPrice);
                            polylineOptions = DirectionConverter.createPolyline(
                                    getContext(), directionPositionList, 5, Color.BLUE);

                            map.addPolyline(polylineOptions);

                            int duration = Integer.parseInt(durationInfo.getValue());
                            Log.d("d", "HomeFragment - durationInfo.getValue(): " + duration);
                            double travelDuration = (duration*0.5)/30;
                            Log.d("d", "HomeFragment - travelDuration: " + travelDuration);

                            editTxtEstimatedTimeMap.setText(String.format("%.0f", travelDuration)+"min");

                            zoomRoute(map, directionPositionList);

                        } else {
                            Log.d("d", "HomeFragment - onTravelRequest - direction.isOK == false");
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {

                    }
                });

        fn_countdown();
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

        travel.setRequesterEmail(dataRequester.getString("requesterEmail"));
        travel.setRequesterName(dataRequester.getString("requesterName"));
        travel.setPlaceOriginAddress(dataRequester.getString("placeOriginAddress"));
        travel.setPlaceDestinoAddress(dataRequester.getString("placeDestinoAddress"));
        travel.setLatOrigem(dataRequester.getDouble("latOrigem"));
        travel.setLongOrigem(dataRequester.getDouble("longOrigem"));
        travel.setLatDestino(dataRequester.getDouble("latDestino"));
        travel.setLongDestino(dataRequester.getDouble("longDestino"));
        travel.setTravelDate(dataRequester.getString("travelDate"));
        travel.setTravelPrice(dataRequester.getDouble("travelPrice"));

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
        /*}else {
            Toast.makeText(getApplicationContext(),"Please enter the value",Toast.LENGTH_LONG).show();
        }*/

    }

    public void setProgress(int startTime, int endTime) {
        viewProgressBar.setMax(endTime);
        viewProgressBar.setSecondaryProgress(endTime);
        viewProgressBar.setProgress(startTime);

    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }

    @OnClick(R.id.frameLayoutAceitar)
    public void onViewClicked() {
        Log.d("d", "HomeFragment - onVieClickedt- lastlocation.getLat: " + lastLocation.getLatitude());
        frameLayoutAceitar.setVisibility(View.GONE);
        Toast.makeText(getContext(),"Corrida aceita!", Toast.LENGTH_LONG).show();

        Travel travel = getTravel(dataTravelRequester, lastLocation);

        if(getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            Log.d("d", "HomeFragment - onViewClicked - msgTravelAcceptedFromHomeFragment");
            mainActivity.msgTravelAcceptedFromHomeFragment();
        }

        presenter.acceptTravel(travel);

        homeViewPadding.setVisibility(View.GONE);
        homeFrameLayoutTop.setVisibility(View.VISIBLE);
        homeFrameLayoutBottom.setVisibility(View.VISIBLE);
        String placeOriginAddress = travel.getPlaceOriginAddress();
        String[] placeOriginAddressSplitted = placeOriginAddress.split(",");
        String address2 = placeOriginAddressSplitted[1] + ", " + placeOriginAddressSplitted[2];
        textViewPassengerAddress1.setText(placeOriginAddressSplitted[0]);
        textViewPassengerAddress2.setText(address2);
        textViewPassengerName.setText(travel.getRequesterName());

    }

    private void onFrameLayoutAceitarNotClicked() {
        startLocationUpdates();
        frameLayoutAceitar.setVisibility(View.GONE);
    }

}
