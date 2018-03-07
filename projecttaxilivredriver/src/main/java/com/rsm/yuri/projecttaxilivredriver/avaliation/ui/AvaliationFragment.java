package com.rsm.yuri.projecttaxilivredriver.avaliation.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.rsm.yuri.projecttaxilivredriver.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AvaliationFragment extends Fragment {

    @BindView(R.id.averageTextView)
    TextView averageTextView;
    @BindView(R.id.totalTextView)
    TextView totalTextView;
    @BindView(R.id.barchart)
    HorizontalBarChart barchart;
    @BindView(R.id.recyclerViewContacts)
    RecyclerView recyclerViewContacts;
    Unbinder unbinder;

    public AvaliationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avaliation, container, false);
        unbinder = ButterKnife.bind(this, view);

        barchart.getDescription().setEnabled(false);

        barchart.setDrawGridBackground(false);
        barchart.getLegend().setEnabled(false);
        barchart.getAxisLeft().setDrawGridLines(false);
        //barchart.getAxisLeft().setDrawLabels(false);
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

        setData(5, 1000);

        return view;
    }

    private void setData(int count, int range) {

        float barWidth = 4f;
        float spaceForBar = 5f;

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            yVals.add(new BarEntry(i * spaceForBar, val));
                    //,getResources().getDrawable(R.drawable.icons8_estrela_preenchida_16)));
        }

        BarDataSet set1 = new BarDataSet(yVals, "Data set1");

        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);
        barchart.setData(data);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
