package com.rsm.yuri.projecttaxilivre.map.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.InfoWindowFragment;
import com.rsm.yuri.projecttaxilivre.map.MapPresenter;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yuri_ on 07/01/2018.
 */

public class MapFragment extends Fragment implements MapView, OnMapReadyCallback, //GoogleMap.InfoWindowAdapter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.container)
    FrameLayout container;
    //@BindView(R.id.map_fragment_click_frame)
    //FrameLayout clickFrameLayout;
    Unbinder unbinder;

    @Inject
    MapPresenter mapPresenter;
    @Inject
    ImageLoader imageLoader;

    private MapInfoWindowFragment mapInfoWindowFragment;
    private GoogleMap map;
    private HashMap<Marker, NearDriver> markers;
    private List<NearDriver> nearDriversList;
    private Location lastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private TaxiLivreApp app;

    private final static int PERMISSIONS_REQUEST_LOCATION = 11;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (TaxiLivreApp) getActivity().getApplication();
        setupInjection();

        markers = new HashMap<Marker, NearDriver>();
        nearDriversList = new ArrayList<>();

        mapPresenter.onCreate();
        //mapPresenter.subscribe();
    }

    private void setupInjection() {
        app.getMapComponent(this, this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_main, container, false);
        //Toast.makeText(getContext(), lastLocation.toString(),Toast.LENGTH_SHORT).show();

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        //moveCameraToLastKnowLocation();

        mapInfoWindowFragment =
                (MapInfoWindowFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapInfoWindowFragment.getMapAsync(this);

        //final InfoWindow infoWindow = new InfoWindow(marker, markerSpec, fragment);
        // Shows the InfoWindow or hides it if it is already opened.
        //mapInfoWindowFragment.infoWindowManager().toggle(infoWindow, true);

    }

    @Override
    public void onDriverMoved(NearDriver nearDriver) {
        removeDriverFromList(nearDriver);
        addDriverToList(nearDriver);
    }

    @Override
    public void onDriverAdded(NearDriver nearDriver) {
        addDriverToList(nearDriver);
    }

    public void addDriverToList(NearDriver nearDriver){
        nearDriversList.add(nearDriver);
        LatLng location = new LatLng(nearDriver.getLatitude(), nearDriver.getLongitude());
        Marker marker = map.addMarker(new MarkerOptions()
                .position(location)
                .title(nearDriver.getEmail())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp)));
        markers.put(marker, nearDriver);

        /*for (NearDriver nearDrivert : nearDriversList) {
            Log.d("d", "NearDriverList(i).getemail(): " + nearDrivert.getEmail());
        }*/
    }

    @Override
    public void onDriverRemoved(NearDriver nearDriver) {
        removeDriverFromList(nearDriver);
    }

    public void removeDriverFromList(NearDriver nearDriver){
        for (Map.Entry<Marker, NearDriver> entry : markers.entrySet()) {
            NearDriver currentNearDriver = entry.getValue();
            Marker currentMarker = entry.getKey();
            if (currentNearDriver.getEmail().equals(nearDriver.getEmail())) {
                currentMarker.remove();
                markers.remove(currentMarker);
                break;
            }
        }

        for (NearDriver nearDriverIteractor : nearDriversList) {
            if(nearDriverIteractor.getEmail().equals(nearDriver.getEmail())){
                nearDriversList.remove(nearDriverIteractor);
            }
        }
    }

    @Override
    public void onDriverError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        //mapPresenter.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mapPresenter.onResume();
    }

    @Override
    public void onDestroy() {
        mapPresenter.unsubscribe();
        mapPresenter.onDestroy();
        super.onDestroy();
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
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        //map.setInfoWindowAdapter(this);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                NearDriver nearDriver = markers.get(marker);
                LatLng position = new LatLng(nearDriver.getLatitude()+0.007, nearDriver.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

                //marker.showInfoWindow();

                final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
                final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

                final InfoWindow.MarkerSpecification markerSpec =
                        new InfoWindow.MarkerSpecification(offsetX, offsetY);

                InfoWindowFragment infoWindowFragment = new InfoWindowFragment();

                final InfoWindow infoWindow = new InfoWindow(marker, markerSpec, infoWindowFragment);
                mapInfoWindowFragment.infoWindowManager().toggle(infoWindow, true);
                infoWindowFragment.render(nearDriver);
                return true;
            }
        });
        //Log.d("d", "Entrou no onMapReady");
        moveCameraToLastKnowLocation();

        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            Log.d("d", "Nao tinha as permissoes.");
        } else {
            Log.d("d", "Tem as permissoes");
            map.setMyLocationEnabled(true);
            LatLng location = null;
            for (NearDriver nearDriver : nearDriversList) {
                Log.d("d", "NearDriverList(i).getemail(): " + nearDriver.getEmail());
                location = new LatLng(nearDriver.getLatitude(), nearDriver.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(location)
                        .title(nearDriver.getEmail())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp)));
                markers.put(marker, nearDriver);
            }
            //if (location != null) map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }*/

    }

    private Location moveCameraToLastKnowLocation() {
        if (!checkReady()) {
            return null;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
                            mapPresenter.subscribe(position);
                            mapPresenter.updateMyLocation(position);
                        }
                    }
                });
        return lastLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    moveCameraToLastKnowLocation();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;

        }
    }

    private void checkLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionLocation = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);

        }

    }

    @Override
    public void onStart() {
        //googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        //googleApiClient.disconnect();
        super.onStop();
    }

    /*@Override
    public View getInfoWindow(Marker marker) {
        return null;
    }*/

    /*@Override
    public View getInfoContents(Marker marker) {*/

        /*FrameLayout clickFrameLayout = (FrameLayout) getActivity().findViewById(R.id.map_fragment_click_frame);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        clickFrameLayout.setTranslationY(metrics.heightPixels);*/

        /*try {
            Interpolator interpolator = new DecelerateInterpolator();
            clickFrameLayout.animate().setInterpolator(interpolator)
                    .setDuration(300)
                    .setStartDelay(500)
                    .translationYBy(-metrics.heightPixels)
                    .start();
        }catch (Exception e){
            e.printStackTrace();
        }*/
        /*ViewGroup root = (ViewGroup) getActivity().findViewById(R.id.map_fragment_content_frame);
        FrameLayout clickFrameLayout = (FrameLayout) getActivity().findViewById(R.id.map_fragment_click_frame);

        TransitionSet set = new TransitionSet()
                //.addTransition(new Scale(0.7f))
                .addTransition(new Fade())
                .setInterpolator(clickFrameLayout.getVisibility()==View.VISIBLE ? new LinearOutSlowInInterpolator() :
                        new FastOutLinearInInterpolator());

        TransitionManager.beginDelayedTransition(root, set);
        clickFrameLayout.setVisibility(clickFrameLayout.getVisibility()==View.VISIBLE ? View.VISIBLE : View.INVISIBLE);*/
        //text2.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);*/

        /*TransitionManager.beginDelayedTransition(root, new android.support.transition.Fade());
        FrameLayout clickFrameLayout = (FrameLayout) getActivity().findViewById(R.id.map_fragment_click_frame);
        clickFrameLayout.setVisibility(View.VISIBLE);*/

        /*View window = ((LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window, null);
        NearDriver nearDriver = markers.get(marker);
        render(window, nearDriver);
        return window;
    }*/

    private void render(View view, final NearDriver nearDriver) {

        /*CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
        TextView txtUser = (TextView) view.findViewById(R.id.txtUser);
        final ImageView imgMain = (ImageView) view.findViewById(R.id.imgMain);
        String userEmail = !TextUtils.isEmpty(nearDriver.getEmail()) ? nearDriver.getEmail() : "";

        imageLoader.load(imgMain, nearDriver.getUrlPhotoCar());
        imageLoader.load(imgAvatar, nearDriver.getUrlPhotoDriver());
        txtUser.setText(nearDriver.getEmail());*/

        CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.info_window_imgDriverAvatar);
        CircleImageView imgCar = (CircleImageView) view.findViewById(R.id.info_window_imgCar);
        TextView txtNome = (TextView) view.findViewById(R.id.info_window_NomeTextView);
        TextView txtModelo = (TextView) view.findViewById(R.id.info_window_ModeloCarTextView);
        TextView txtTotalTravels = (TextView) view.findViewById(R.id.info_window_TotalTravelsTextView);
        TextView txtAverage = (TextView) view.findViewById(R.id.info_window_averageTextView);

        imageLoader.load(imgAvatar, nearDriver.getUrlPhotoDriver());
        imageLoader.load(imgCar, nearDriver.getUrlPhotoCar());
        txtNome.setText(nearDriver.getNome());
        txtModelo.setText(nearDriver.getModelo());
        txtTotalTravels.setText(nearDriver.getTotalTravels()+"");
        DecimalFormat df = new DecimalFormat("#.0");
        txtAverage.setText(df.format(nearDriver.getAverageRatings())+"");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationPermission();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(getView(), connectionResult.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}