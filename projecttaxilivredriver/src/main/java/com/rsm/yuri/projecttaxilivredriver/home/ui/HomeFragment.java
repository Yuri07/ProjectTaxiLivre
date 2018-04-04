package com.rsm.yuri.projecttaxilivredriver.home.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenter;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements OnMapReadyCallback, HomeView, MainActivity.OnSwitchButtonClickedListener {

    @BindView(R.id.container)
    FrameLayout container;
    Unbinder unbinder;

    @Inject
    HomePresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;

    //private MainActivity.OnSwitchButtonClickedListener listener;

    private GoogleMap map;
    private Location lastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    private final static int PERMISSIONS_REQUEST_LOCATION = 11;

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
                                NearDriver nearDriver = new NearDriver();
                                nearDriver.setEmail(sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, "email_sh_pr"));
                                nearDriver.setUrlPhotoDriver(sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_DRIVER_KEY, "url_sh_pr"));
                                nearDriver.setUrlPhotoCar(sharedPreferences.getString(TaxiLivreDriverApp.URL_PHOTO_CAR_KEY, "url_sh_pr"));
                                nearDriver.setNome(sharedPreferences.getString(TaxiLivreDriverApp.NOME_KEY, "nome_sh_pr"));
                                nearDriver.setModelo(sharedPreferences.getString(TaxiLivreDriverApp.MODELO_KEY, "modelo_sh_pr"));
                                nearDriver.setTotalTravels(sharedPreferences.getInt(TaxiLivreDriverApp.TOTAL_RATINGS_KEY, -1));
                                nearDriver.setAverageRatings(sharedPreferences.getFloat(TaxiLivreDriverApp.AVERAG_RATING_KEY, 5.5f));
                                lastLocation = location;
                                nearDriver.setLatitude(location.getLatitude());
                                nearDriver.setLongitude(location.getLongitude());
                                presenter.uploadDriverDataToArea(nearDriver);
                                //LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                            }
                        }
                    });

            startLocationUpdates();
        }else {
            stopLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }
}
