package com.rsm.yuri.projecttaxilivre.map.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rsm.yuri.projecttaxilivre.BuildConfig;
import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.InfoWindowFragment;
import com.rsm.yuri.projecttaxilivre.map.MapPresenter;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;
import com.rsm.yuri.projecttaxilivre.travelslist.entities.Travel;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yuri_ on 07/01/2018.
 */

public class MapFragment extends Fragment implements MapView, OnMapReadyCallback, //GoogleMap.InfoWindowAdapter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , InfoWindowFragment.OnChildFragmentInteractionListener {

    @BindView(R.id.container_map)
    FrameLayout container;
    @BindView(R.id.frameLayoutConfirmar)
    FrameLayout frameLayoutConfirmar;
    @BindView(R.id.editTxt_valor_map)
    TextView editTxtValorMap;
    @BindView(R.id.button_confirmar)
    Button buttonConfirmar;

    Unbinder unbinder;

    @Inject
    MapPresenter mapPresenter;
    @Inject
    ImageLoader imageLoader;
    @Inject
    SharedPreferences sharedPreferences;


    private MapInfoWindowFragment mapInfoWindowFragment;
    private GoogleMap map;
    private HashMap<Marker, NearDriver> markers;

    private Geocoder geocoder;

    private Marker markerClicked;
    private InfoWindow.MarkerSpecification markerSpecClicked;
    private InfoWindowFragment infoWindowFragmentClicked;
    private NearDriver requestedDriver;
    private TravelRequest travelRequest;

    private PolylineOptions polylineOptions = null;

    private List<NearDriver> nearDriversList;
    private Location lastLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private TaxiLivreApp app;

    private final static int PERMISSIONS_REQUEST_LOCATION = 11;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int MAP_ZOOM_PADDING = 90;
    private static final int MAP_CAMERA_ANIMATION_DURATION_IN_MILLIS = 500;

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

        mapInfoWindowFragment =
                (MapInfoWindowFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapInfoWindowFragment.getMapAsync(this);

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

    public void addDriverToList(NearDriver nearDriver) {
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

    public void removeDriverFromList(NearDriver nearDriver) {
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
            if (nearDriverIteractor.getEmail().equals(nearDriver.getEmail())) {
                nearDriversList.remove(nearDriverIteractor);
            }
        }
    }

    @Override
    public void onDriverError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTravelAckReceived(String travelAck) {
        mapPresenter.unsubscribeForResponseOfDriverRequested();
        if(travelAck.equals("rejected")){

        }else{
            //Travel travel = mapPresenter.getTravel(requestedDriver.getEmail(), travelAck);
            //mapPresenter.confirmTravel(travel);
        }
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
        setOnMakerClick(map);
        moveCameraToLastKnowLocation();
    }

    public void setOnMakerClick(final GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                requestedDriver = markers.get(marker);
                markerClicked = marker;
                LatLng position = new LatLng(requestedDriver.getLatitude() + 0.007, requestedDriver.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

                //marker.showInfoWindow();

                final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
                final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

                final InfoWindow.MarkerSpecification markerSpec =
                        new InfoWindow.MarkerSpecification(offsetX, offsetY);
                markerSpecClicked = markerSpec;

                InfoWindowFragment infoWindowFragment = new InfoWindowFragment();
                infoWindowFragmentClicked = infoWindowFragment;

                final InfoWindow infoWindow = new InfoWindow(marker, markerSpec, infoWindowFragment);

                if(polylineOptions!=null){
                    map.clear();
                    polylineOptions=null;
                }

                mapInfoWindowFragment.infoWindowManager().toggle(infoWindow, true);
                infoWindowFragment.render(requestedDriver);
                return true;
            }
        });
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
    public void messageFromWindowInfoToMapFrag(Place destinyPlace) {

        Log.d("d", "MapFragment - Place: " + destinyPlace.getName());

        if (markerClicked != null && markerSpecClicked != null && infoWindowFragmentClicked != null) {

            final InfoWindow infoWindow = new InfoWindow(markerClicked, markerSpecClicked, infoWindowFragmentClicked);
            markerClicked = null;
            markerSpecClicked = null;
            infoWindowFragmentClicked = null;
            mapInfoWindowFragment.infoWindowManager().toggle(infoWindow, true);

            travelRequest = new TravelRequest();
            travelRequest.setRequesterEmail(sharedPreferences.getString(TaxiLivreApp.EMAIL_KEY, ""));
            travelRequest.setRequesterName(sharedPreferences.getString(TaxiLivreApp.NOME_KEY, ""));



            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            travelRequest.setPlaceOriginAddress(destinyPlace.getAddress().toString());
            travelRequest.setPlaceDestinoAddress("default");
            try {
                addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                /*String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL*/

                travelRequest.setPlaceDestinoAddress(address);

            } catch (IOException e) {
                e.printStackTrace();
            }

            travelRequest.setLatOrigem(lastLocation.getLatitude());
            travelRequest.setLongOrigem(lastLocation.getLongitude());
            travelRequest.setLatDestino(destinyPlace.getLatLng().latitude);
            travelRequest.setLongDestino(destinyPlace.getLatLng().longitude);

            Date currentTime = Calendar.getInstance().getTime();
            Log.d("d", "MapFragment - travelDate: " + currentTime.toString());
            travelRequest.setTravelDate(currentTime.toString());

            String myApiKey = BuildConfig.GOOGLE_MAPS_API_KEY_GRADLE_PROPERTY;
            GoogleDirection.withServerKey(myApiKey)
                    .from(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                    .to(destinyPlace.getLatLng())
                    .transportMode(TransportMode.DRIVING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            if (direction.isOK()) {
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                                Info distanceInfo = leg.getDistance();
                                Info durationInfo = leg.getDuration();
                                String distanceReadable = distanceInfo.getText();
                                String durationReadable = durationInfo.getText();

                                int distance = Integer.parseInt(distanceInfo.getValue());
                                Log.d("d", "MapFragment - distanceInfo.getValue(): " + distance);
                                double travelPrice = (distance*Travel.PRICE_PER_KM)/1000;
                                travelRequest.setTravelPrice(travelPrice);
                                Log.d("d", "MapFragment - travelPrice: " + travelPrice);

                                polylineOptions = DirectionConverter.createPolyline(
                                        getContext(), directionPositionList, 5, Color.BLUE);

                                map.addPolyline(polylineOptions);

                                zoomRoute(map, directionPositionList);

                                editTxtValorMap.setText(String.format( "%.2f", travelRequest.getTravelPrice())+"R$");
                                frameLayoutConfirmar.setVisibility(View.VISIBLE);

                            } else {
                                Log.d("d", "MapFragment - messageFromWindowInfoToMapFrag direction.isOK == false");
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                        }
                    });
        }
    }

    @OnClick(R.id.button_confirmar)
    public void onViewClicked() {
        Toast.makeText(getContext(), "Botao confirmar clicado!", Toast.LENGTH_SHORT).show();
        frameLayoutConfirmar.setVisibility(View.GONE);
        //moveCameraToLastKnowLocation();
        mapPresenter.carRequest(requestedDriver, travelRequest);
        mapPresenter.subscribeForResponseOfDriverRequested();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        txtTotalTravels.setText(nearDriver.getTotalTravels() + "");
        DecimalFormat df = new DecimalFormat("#.0");
        txtAverage.setText(df.format(nearDriver.getAverageRatings()) + "");
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