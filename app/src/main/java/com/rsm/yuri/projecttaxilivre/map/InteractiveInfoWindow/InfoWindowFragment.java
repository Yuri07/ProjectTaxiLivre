package com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.location.places.
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.chat.ui.ChatActivity;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.main.ui.MainActivity;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;

import java.text.DecimalFormat;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by yuri_ on 27/03/2018.
 */

public class InfoWindowFragment extends Fragment {

    @Inject
    ImageLoader imageLoader;

    private View view;

    private NearDriver nearDriver;

    //protected GeoDataClient mGeoDataClient;

    private OnChildFragmentInteractionListener mParentListener;

    public final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 81;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();

        /*mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();*/

    }

    private void setupInjection() {
        TaxiLivreApp app = (TaxiLivreApp) getActivity().getApplication();
        app.getInfoWindowComponent(this).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.info_window, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.info_window_button_solicitar:
                        //Toast.makeText(getContext(), "Solicitar clicado!", Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                            .build(getActivity());
                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            // TODO: Solucionar o erro.
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // TODO: Solucionar o erro.
                        }
                    break;
                    case R.id.info_window_chat:
                        //Toast.makeText(getContext(), "Chat clicado!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ChatActivity.class);
                        i.putExtra(ChatActivity.EMAIL_KEY, nearDriver.getEmail());
                        i.putExtra(ChatActivity.STATUS_KEY, Driver.WAITING_TRAVEL);
                        i.putExtra(ChatActivity.URL_KEY, nearDriver.getUrlPhotoDriver());
                        startActivity(i);
                    break;
                    case R.id.info_window_info:
                        Toast.makeText(getContext(), "Info clicado!", Toast.LENGTH_SHORT).show();
                    break;
                }

            }
        };

        view.findViewById(R.id.info_window_button_solicitar).setOnClickListener(onClickListener);
        view.findViewById(R.id.info_window_chat).setOnClickListener(onClickListener);
        view.findViewById(R.id.info_window_info).setOnClickListener(onClickListener);

    }

    public void render(final NearDriver nearDriver) {

        /*CircleImageView imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
        TextView txtUser = (TextView) view.findViewById(R.id.txtUser);
        final ImageView imgMain = (ImageView) view.findViewById(R.id.imgMain);
        String userEmail = !TextUtils.isEmpty(nearDriver.getEmail()) ? nearDriver.getEmail() : "";

        imageLoader.load(imgMain, nearDriver.getUrlPhotoCar());
        imageLoader.load(imgAvatar, nearDriver.getUrlPhotoDriver());
        txtUser.setText(nearDriver.getEmail());*/

        this.nearDriver = nearDriver;
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

    public interface OnChildFragmentInteractionListener {
        void messageFromWindowInfoToMapFrag(Place place);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            MapFragment parentFragment = mainActivity.getMapFragment();
            if (parentFragment != null) {
                mParentListener = (OnChildFragmentInteractionListener) parentFragment;
            } else {
                throw new RuntimeException("The parent fragment must implement OnChildFragmentInteractionListener");
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.d("d", "InfoWindowFragment - Place: " + place.getName());
                mParentListener.messageFromWindowInfoToMapFrag(place);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.d("d", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }

}
