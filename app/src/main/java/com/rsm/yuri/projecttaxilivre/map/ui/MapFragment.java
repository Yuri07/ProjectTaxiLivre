package com.rsm.yuri.projecttaxilivre.map.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
//import androidx.core.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.appolica.interactiveinfowindow.InfoWindow;
import com.appolica.interactiveinfowindow.fragment.MapInfoWindowFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Place;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.rsm.yuri.projecttaxilivre.BuildConfig;
import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.main.ui.MainActivity;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.InfoWindowFragment;
import com.rsm.yuri.projecttaxilivre.map.MapPresenter;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.Rating;
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
        , InfoWindowFragment.OnChildFragmentInteractionListener, MainActivity.OnTravelStatusChangedListener {

    @BindView(R.id.container_map)
    FrameLayout container;
    @BindView(R.id.frameLayoutConfirmar)
    FrameLayout frameLayoutConfirmar;
    @BindView(R.id.editTxt_valor_map)
    TextView editTxtValorMap;
    @BindView(R.id.button_confirmar)
    Button buttonConfirmar;
    @BindView(R.id.frameLayoutWaitingForResponse)
    FrameLayout frameLayoutWaitingForResponse;

    @BindView(R.id.frameLayoutBottom)
    FrameLayout frameLayoutBottom;
    @BindView(R.id.map_label_current_street)
    TextView textViewCurrentStreet;
    @BindView(R.id.map_bottom_frame_chat_img_view)
    CircleImageView buttonChat;
    @BindView(R.id.map_bottom_frame_linearlayout_status_travel)
    LinearLayout linearLayoutStatus;
    @BindView(R.id.map_label_status_travel)
    TextView textViewStatusTravel;

    @BindView(R.id.mapFrameLayoutAvaliation)
    FrameLayout frameLayoutAvalition;
    @BindView(R.id.map_avaliation_rating_bar)
    RatingBar ratingTravelBar;
    @BindView(R.id.map_avaliation_edit_text_comment)
    EditText editTextComment;
    @BindView(R.id.map_avaliation_label_ok)
    TextView textViewOk;

    Unbinder unbinder;

    @Inject
    MapPresenter mapPresenter;
    @Inject
    ImageLoader imageLoader;
    @Inject
    SharedPreferences sharedPreferences;

    private MapInfoWindowFragment mapInfoWindowFragment;
    private GoogleMap map;
    private String cidade=null;
    private HashMap<Marker, NearDriver> markers;
    private HashMap<Marker, Driver> myDriverMarkerHasMap;

    private Polyline polyline;

    private Geocoder geocoder;

    private Marker markerClicked;
    private Marker myDriverMarker;
    private InfoWindow.MarkerSpecification markerSpecClicked;
    private InfoWindowFragment infoWindowFragmentClicked;
    private NearDriver requestedDriver;
    private TravelRequest travelRequest;
    private String currentTravelID;

    private PolylineOptions polylineOptions = null;

    private List<NearDriver> nearDriversList;
    private Location lastLocation;
    private Driver myDriver;
    private FusedLocationProviderClient mFusedLocationClient;

    private TaxiLivreApp app;

    private final static int PERMISSIONS_REQUEST_LOCATION = 11;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int MAP_ZOOM_PADDING = 90;
    private static final int MAP_CAMERA_ANIMATION_DURATION_IN_MILLIS = 500;

    private Place destinyPlace;
    private double travelPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
//        inicializaVariavelCidade();

        setupInjection("Fortaleza");

        markers = new HashMap<Marker, NearDriver>();
        nearDriversList = new ArrayList<>();

        myDriverMarkerHasMap = new HashMap<Marker, Driver>();

        mapPresenter.onCreate();
//        mapPresenter.subscribe();
    }

    private String inicializaVariavelCidade() {

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
                                String[] cidadeLine = addressLines[2].split(" - ");
                                cidade = cidadeLine[0];
                                if(cidade.equals("Caucaia")){
                                    cidade="Fortaleza";
                                }
                                Log.d("d", "MapFragment - inicializaVariavelCidade()- cidade: " + cidade);

//                                setupInjection(cidade);
                                mapPresenter.onCreate();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });

        return "";
    }

    private void setupInjection(String cidade) {
        app = (TaxiLivreApp) getActivity().getApplication();
        app.getMapComponent(this, this, cidade).inject(this);
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
                break;
            }
        }
    }

    @Override
    public void onDriverError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }

    public void addMyDriverToMapScreen(LatLng locatinOfMyDriver){
        //this.myDriver = myDriver;

        //LatLng location = new LatLng(locatinOfMyDriver.latitude, locatinOfMyDriver.longitude);
        myDriverMarker = map.addMarker(new MarkerOptions()
                .position(locatinOfMyDriver)
                .title(requestedDriver.getEmail())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp)));

        Log.d("d", "MapFragment - addMyDriverToMapScreen()");

        //myDriverMarkerHasMap.put(marker, myDriver);

    }

    public void removeMyDriverFromMapScreen(){
        if(myDriverMarker!=null)
            myDriverMarker.remove();
        Log.d("d", "MapFragment - removeMyDriverFromMapScreen()");
        //Map.Entry<Marker, Driver> entry = (Map.Entry<Marker, Driver>) myDriverMarkerHasMap.entrySet();
        //myDriverMarkerHasMap.remove(entry.getKey());
        //myDriver = null;
    }

    public void removeNearDriversFromMapScreen(){
        Log.d("d", "MapFragment - removeNearDriversFromMapScreen() ");
        for (Map.Entry<Marker, NearDriver> entry : markers.entrySet()) {
            Log.d("d", "MapFragment - removeNearDriversFromMapScreen() - entrou no for");
            Marker currentMarker = entry.getKey();
            currentMarker.remove();
            //markers.remove(currentMarker);//lanca concurrentmodificaitonexception pq se trata de
            // uma Fail Fast Iterator(https://www.geeksforgeeks.org/fail-fast-fail-safe-iterators-java/)
        }
        markers.clear();
        nearDriversList.clear();
        /*for (NearDriver nearDriverIteractor : nearDriversList) {
            nearDriversList.remove(nearDriverIteractor);
        }*/

    }

    @Override
    public void messageFromWindowInfoToMapFrag(Place destinyPlace) {

        Log.d("d", "MapFragment - Place: " + destinyPlace.getName());

        this.destinyPlace = destinyPlace;

        mapPresenter.retrieveDataUser();

    }

    @Override
    public void onSuccessToGetDataUser(User currentUser) {
        drawRouteTravel(new MapFragmentListenerCallback() {
            @Override
            public void onSuccess() {

                travelRequest = assembleTravelRequest(currentUser);

                frameLayoutConfirmar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(String error) {
                Log.d("e", error);
            }
        });
        //travelRequest = assembleTravelRequest(currentUser);
    }

    @OnClick(R.id.button_confirmar)
    public void onViewClicked() {
        Toast.makeText(getContext(), "Botao confirmar clicado!", Toast.LENGTH_SHORT).show();
        frameLayoutConfirmar.setVisibility(View.GONE);
        //moveCameraToLastKnowLocation();
        mapPresenter.carRequest(requestedDriver, travelRequest);
        mapPresenter.unsubscribe();
        mapPresenter.subscribeForResponseOfDriverRequested();

        frameLayoutWaitingForResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTravelAckReceived(String travelID) {

        Log.d("d", "MapFragment - onTravelAckReceived() - travelID = " + travelID);

        frameLayoutWaitingForResponse.setVisibility(View.GONE);

        currentTravelID = travelID;

        mapPresenter.unsubscribeForResponseOfDriverRequested();
        //mapPresenter.deleteAckTravelRequest();

        removeNearDriversFromMapScreen();
        //Log.d("d", "MapFragment - onTravelAckReceived() - removeu os nearDrivers ");

        if(polyline!=null)
            polyline.remove();

        if(!travelID.equals("travelNotAcceptedAck")){
            setupAckReceivedBottomLayout();
            Log.d("d", "MapFragment - onTravelAckReceived() - travelID nao e igual a travelNotAcceptedAck ");

            if(travelID.equals("rejected")){
                Log.d("d", "MapFragment - onTravelAckReceived() - travelID == rejected: ");
            }else{
                Log.d("d", "MapFragment - onTravelAckReceived() - travelID != rejected: " + travelID);
                mapPresenter.subscribeForMyDriverLocationUpdate(requestedDriver.getEmail(),currentTravelID);
            }

        }else{
            Log.d("d", "MapFragment - onTravelAckReceived() - travelID e igual a travelNotAcceptedAck ");
            Toast.makeText(getContext(), "Motorista não respondeu requisição.",Toast.LENGTH_SHORT).show();

            subscribeForNearDriversUpdate();

        }

    }

    @Override
    public void onTravelInitiate() {
        Log.d("d", "MapFragment - onTravelInitiate");

        setupInitiateTravelBottomLayout();

    }

    @Override
    public void onTravelTerminate() {

        Log.d("d", "MapFragment - onTravelTerminate");

        mapPresenter.unsubscribeForMyDriverLocationUpdate(requestedDriver.getEmail(), currentTravelID);

        setupOnTravelTerminateBottomLayout();

        frameLayoutAvalition.setVisibility(View.VISIBLE);


    }

    @OnClick(R.id.map_avaliation_label_ok)
    public void onOkEvaluationTextViewClicked(){

        Rating rating = setupRatingFromAvaliationFrameLayout();

        mapPresenter.uploadMyRating(requestedDriver.getEmail(), rating);

        frameLayoutAvalition.setVisibility(View.GONE);

    }

    @Override
    public void onMyDriverMoved(LatLng locatinoOfMyDriver) {
        Log.d("d", "MapFragment - onMyDriverMoved() ");
        removeMyDriverFromMapScreen();
        addMyDriverToMapScreen(locatinoOfMyDriver);

        //drawRoute(locatinoOfMyDriver, lastLocation);//maneira correta
        LatLng locationDriverSimulated = new LatLng(-3.741758, -38.607207);//location do assai:


        drawRoute(locationDriverSimulated, lastLocation);//para testar

    }



    private void drawRoute(LatLng myDriverLocation, Location myLastLocation){
        String myApiKey = BuildConfig.GOOGLE_MAPS_API_KEY_GRADLE_PROPERTY;
        GoogleDirection.withServerKey(myApiKey)
                .from(myDriverLocation)
                .to(new LatLng(myLastLocation.getLatitude(), myLastLocation.getLongitude()))
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable Direction direction) {
                        if (direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distanceReadable = distanceInfo.getText();
                            String durationReadable = durationInfo.getText();

                            long distance = distanceInfo.getValue();//Integer.parseInt(distanceInfo.getValue());
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
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

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

                            if(app.getConectivityStatus(getActivity())) {
                                mapPresenter.subscribe(position);
                                mapPresenter.updateMyLocation(position);
                            }else{
                                informUserConnectivityLost();
                            }

                        }
                    }
                });
        return lastLocation;
    }

    public void informUserConnectivityLost(){
        Toast.makeText(getContext(), "Não foi possível se conectar a rede TaxiLivre",Toast.LENGTH_SHORT).show();
        Log.d( "d", "Não foi possível se conectar a rede TaxiLivre" );
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

    private void subscribeForNearDriversUpdate(){
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
        //map.setMyLocationEnabled(true);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {// Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.d("Fragment", "onSuccess");
                            lastLocation = location;
                            Log.d("d", "MapFragment - subscribeForNearDriversUpdate ");

                            LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
                            mapPresenter.subscribe(position);
                            mapPresenter.updateMyLocation(position);
                        }
                    }
                });
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

    private void setupAckReceivedBottomLayout(){
        frameLayoutWaitingForResponse.setVisibility(View.GONE);
        resetBottomLayout();
        frameLayoutBottom.setVisibility(View.VISIBLE);
    }

    private void setupInitiateTravelBottomLayout(){
        buttonChat.setVisibility(View.GONE);
        linearLayoutStatus.setGravity(Gravity.CENTER);
        textViewCurrentStreet.setVisibility(View.VISIBLE);
        textViewStatusTravel.setText("Em viagem");
    }

    private void setupOnTravelTerminateBottomLayout(){
        resetBottomLayout();
        frameLayoutBottom.setVisibility(View.GONE);
    }

    private void resetBottomLayout() {
        linearLayoutStatus.setGravity(Gravity.NO_GRAVITY);
        buttonChat.setVisibility(View.VISIBLE);
        textViewCurrentStreet.setVisibility(View.GONE);
        textViewStatusTravel.setText("Aguardando Motorista");
    }

    private Rating setupRatingFromAvaliationFrameLayout(){
        String comment = String.valueOf(editTextComment.getText());
        double numStars = ratingTravelBar.getNumStars();
        Log.d("d", "MapFragment - onOkEvaluation - numStars: " + numStars);

        Rating rating = new Rating();
        rating.setEmail(sharedPreferences.getString(TaxiLivreApp.EMAIL_KEY, ""));
        rating.setNome(sharedPreferences.getString(TaxiLivreApp.NOME_KEY, ""));
        Date currentTime = Calendar.getInstance().getTime();
        rating.setDate(currentTime.toString());
        rating.setComment(comment);
        long numStarsInt = (long) numStars;
        rating.setVote(numStarsInt);
        rating.setUrlPhotoUser(sharedPreferences.getString(TaxiLivreApp.URL_PHOTO_USER_KEY, ""));

        return rating;
    }

    TravelRequest assembleTravelRequest(User currentUser){
        TravelRequest travelRequest = null;
        if (markerClicked != null && markerSpecClicked != null && infoWindowFragmentClicked != null) {

            final InfoWindow infoWindow = new InfoWindow(markerClicked, markerSpecClicked, infoWindowFragmentClicked);

            markerClicked = null;
            markerSpecClicked = null;
            infoWindowFragmentClicked = null;
            mapInfoWindowFragment.infoWindowManager().toggle(infoWindow, true);

            travelRequest = new TravelRequest();
            travelRequest.setRequesterEmail(currentUser.getEmail());
            travelRequest.setRequesterName(currentUser.getNome());
            travelRequest.setUrlPhotoUser(currentUser.getUrlPhotoUser());
            travelRequest.setAverageRatingsPassenger(currentUser.getAverageRating());
            /*travelRequest.setRequesterEmail(sharedPreferences.getString(TaxiLivreApp.EMAIL_KEY, ""));
            travelRequest.setRequesterName(sharedPreferences.getString(TaxiLivreApp.NOME_KEY, ""));
            travelRequest.setUrlPhotoUser(sharedPreferences.getString(TaxiLivreApp.URL_PHOTO_USER_KEY, ""));
            travelRequest.setAverageRatingsPassenger(sharedPreferences.getFloat(TaxiLivreApp.AVERAGE_RATINGS_PASSENGER_KEY, 1.0f));*/

            List<Address> addresses;
            geocoder = new Geocoder(getContext(), Locale.getDefault());

            travelRequest.setPlaceDestinoAddress(destinyPlace.getAddress());
            travelRequest.setPlaceOriginAddress("default");
            try {
                addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                /*String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL*/

                travelRequest.setPlaceOriginAddress(address);

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

            travelRequest.setTravelPrice(travelPrice);

        }

        return travelRequest;
    }

    void drawRouteTravel(final MapFragmentListenerCallback listenerCallback){
        String myApiKey = BuildConfig.GOOGLE_MAPS_API_KEY_GRADLE_PROPERTY;
        GoogleDirection.withServerKey(myApiKey)
                .from(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                .to(destinyPlace.getLatLng())
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable Direction direction) {
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
                            Log.d("d", "MapFragment - distanceInfo.getValue(): " + distance);
                            travelPrice = (distance*Travel.PRICE_PER_KM)/1000;

                            //travelRequest.setTravelPrice(travelPrice);

                            Log.d("d", "MapFragment - travelPrice: " + travelPrice);

                            polylineOptions = DirectionConverter.createPolyline(
                                    getContext(), directionPositionList, 5, Color.BLUE);

                            polyline = map.addPolyline(polylineOptions);

                            zoomRoute(map, directionPositionList);

                            editTxtValorMap.setText(String.format( "%.2f", travelRequest.getTravelPrice())+"R$");
                            //frameLayoutConfirmar.setVisibility(View.VISIBLE);
                            listenerCallback.onSuccess();


                        } else {
                            listenerCallback.onError("MapFragment - messageFromWindowInfoToMapFrag direction.isOK == false");
                        }
                    }



                    @Override
                    public void onDirectionFailure(Throwable t) {
                    }
                });
    }


}