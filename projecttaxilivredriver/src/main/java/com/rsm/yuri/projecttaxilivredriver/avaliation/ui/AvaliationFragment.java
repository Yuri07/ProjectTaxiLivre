package com.rsm.yuri.projecttaxilivredriver.avaliation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.rsm.yuri.projecttaxilivredriver.R;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationPresenter;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.adapters.AvaliationsListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AvaliationFragment extends Fragment implements AvaliationView {

    @BindView(R.id.driver_star1)
    ImageView driverStar1;
    @BindView(R.id.driver_star2)
    ImageView driverStar2;
    @BindView(R.id.driver_star3)
    ImageView driverStar3;
    @BindView(R.id.driver_star4)
    ImageView driverStar4;
    @BindView(R.id.driver_star5)
    ImageView driverStar5;
    @BindView(R.id.container_frag_avaliation)
    LinearLayout container;
    @BindView(R.id.averageTextView)
    TextView averageTextView;
    @BindView(R.id.totalTextView)
    TextView totalTextView;
    @BindView(R.id.barchart)
    HorizontalBarChart barchart;
    @BindView(R.id.recyclerViewContacts)
    RecyclerView recyclerViewContacts;


    Unbinder unbinder;

    @Inject
    AvaliationsListAdapter adapter;
    @Inject
    AvaliationPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;


    public AvaliationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();
        presenter.onCreate();

    }

    private void setupInjection() {
        TaxiLivreDriverApp app = (TaxiLivreDriverApp) getActivity().getApplication();
        app.getAvaliationComponent(this, this).inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avaliation, container, false);
        unbinder = ButterKnife.bind(this, view);

        //setData(5, 1000, );

        setupRecyclerView();
        presenter.getMyRatings(sharedPreferences.getString(TaxiLivreDriverApp.EMAIL_KEY, ""));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        float averagRating = sharedPreferences.getFloat(TaxiLivreDriverApp.AVERAG_RATING_KEY, 0.1f);
        int totalRating = sharedPreferences.getInt(TaxiLivreDriverApp.TOTAL_RATINGS_KEY, -1);
        Vector<Integer> countStars = new Vector<Integer>();
        countStars.add(sharedPreferences.getInt(TaxiLivreDriverApp.COUNT_1_STARS_KEY, 0));
        countStars.add(sharedPreferences.getInt(TaxiLivreDriverApp.COUNT_2_STARS_KEY, 0));
        countStars.add(sharedPreferences.getInt(TaxiLivreDriverApp.COUNT_3_STARS_KEY, 0));
        countStars.add(sharedPreferences.getInt(TaxiLivreDriverApp.COUNT_4_STARS_KEY, 0));
        countStars.add(sharedPreferences.getInt(TaxiLivreDriverApp.COUNT_5_STARS_KEY, 0));

        Log.d("d", "AvaliationFragment.sharedPreferences.getaverageRating(): " + sharedPreferences.getFloat(TaxiLivreDriverApp.AVERAG_RATING_KEY, 0.1f));
        Log.d("d", "AvaliationFragment.sharedPreferences.getTotalRating(): " + sharedPreferences.getInt(TaxiLivreDriverApp.TOTAL_RATINGS_KEY, -1));
        Log.d("d", "AvaliationFragment.sharedPreferences.getCount1Stars(): " + sharedPreferences.getInt(TaxiLivreDriverApp.COUNT_1_STARS_KEY, 0));

        averageTextView.setText(averagRating + "");
        totalTextView.setText(totalRating + "");
        if(averagRating>=0.0f &&averagRating<=0.8f){
            driverStar1.setImageResource(R.drawable.icons8_meia_estrela_preenchida_16);
        }if(averagRating>0.8f &&averagRating<=1.2f){
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
        }if(averagRating>1.2f &&averagRating<=1.8f){
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_meia_estrela_preenchida_16);
        }if(averagRating>1.8f &&averagRating<=2.2f){
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
        }if(averagRating>=2.2f &&averagRating<=2.8f){
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar3.setImageResource(R.drawable.icons8_meia_estrela_preenchida_16);
        }if(averagRating>=2.8f &&averagRating<=3.2f){
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
        }if(averagRating>=3.2f &&averagRating<=3.8f) {
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar4.setImageResource(R.drawable.icons8_meia_estrela_preenchida_16);
        }if(averagRating>=3.8f &&averagRating<=4.2f) {
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar4.setImageResource(R.drawable.icons8_estrela_preenchida_16);
        }if(averagRating>=4.2f &&averagRating<=4.8f) {
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar4.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar5.setImageResource(R.drawable.icons8_meia_estrela_preenchida_16);
        }if(averagRating>=4.8f &&averagRating<=5f) {
            driverStar1.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar2.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar3.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar4.setImageResource(R.drawable.icons8_estrela_preenchida_16);
            driverStar5.setImageResource(R.drawable.icons8_estrela_preenchida_16);
        }

        setData(5, Collections.max(countStars), countStars);

    }

    private void setupRecyclerView() {
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewContacts.setAdapter(adapter);
    }

    private void setData(int count, int range, Vector<Integer> values) {

        float barWidth = 4f;
        float spaceForBar = 5f;

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            yVals.add(new BarEntry(i * spaceForBar, values.get(i)));

            //float val = (float) (Math.random() * range);
            //yVals.add(new BarEntry(i * spaceForBar, val));
            //,getResources().getDrawable(R.drawable.icons8_estrela_preenchida_16)));
        }

        BarDataSet set1 = new BarDataSet(yVals, "Data set1");

        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);
        barchart.getDescription().setEnabled(false);

        barchart.setDrawGridBackground(false);
        barchart.getLegend().setEnabled(false);
        barchart.getAxisLeft().setDrawGridLines(false);
        barchart.getAxisLeft().setDrawLabels(false);
        barchart.getAxisLeft().setDrawAxisLine(false);
        barchart.getAxisRight().setDrawGridLines(false);
        barchart.getAxisRight().setDrawLabels(false);
        barchart.getAxisRight().setDrawAxisLine(false);
        barchart.getXAxis().setDrawGridLines(false);
        barchart.getXAxis().setDrawAxisLine(false);
        barchart.getXAxis().setDrawLabels(false);
        barchart.setDrawValueAboveBar(true);
        /*YAxis left = barchart.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        left.setDrawZeroLine(false); // no draw a zero line
        barchart.getAxisRight().setEnabled(false);*/
        barchart.setData(data);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRatingAdded(Rating rating) {
        Log.d("d", "AvaliationFragment.onRatingAdded: " + rating.getEmail());
        adapter.add(rating);
    }

    @Override
    public void onRatingError(String error) {
        Snackbar.make(container, error, Snackbar.LENGTH_SHORT).show();
    }
}
