package at.htl.easygrow.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import at.htl.easygrow.R;
import at.htl.easygrow.model.Measurement;
import at.htl.easygrow.model.Plant;
import at.htl.easygrow.model.PlantModel;

public class TemperatureFragment extends Fragment implements Observer {
    public TemperatureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PlantModel.getInstance().addObserver(this);
        refresh();
        return inflater.inflate(R.layout.fragment_temperature, container, false);
    }



    @Override
    public void onResume() {
        super.onResume();
        PlantModel.getInstance().addObserver(this);
        refresh();
        System.out.println("resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        PlantModel.getInstance().deleteObserver(this);
        System.out.println("pause");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GraphView graph = (GraphView)getActivity().findViewById(R.id.graph_temperature);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-15);
        graph.getViewport().setMaxY(50);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(120);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time[h]");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Temperature[%]");
    }

    @Override
    public void update(Observable observable, Object o) {
        final FragmentActivity activity = this.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    public void refresh() {
        //don't do anything if Activity is null
        if (getActivity() == null)
            return;

        GraphView graph = (GraphView)getActivity().findViewById(R.id.graph_temperature);

        //don't do anything if graph is null
        if (graph == null)
            return;

        //create new Series and set properties
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        series.setDrawBackground(true);


        Plant plant = PlantModel.getInstance().getPlant();
        //don't do anything if plant is null
        if (plant == null)
            return;
        graph.getSeries().clear();
        List<Measurement> measurements = plant.getMeasurements();

        //don't do anything if list is empty
        if (measurements.size() <= 0)
            return;
        Collections.sort(measurements);
        long minTime = measurements.get(0).getTime();
        for (Measurement m : measurements)
            series.appendData(new DataPoint(
                            Math.abs(m.getTime() - minTime), m.getTemperature()),
                    false, plant.getMeasurements().size(), false);
        graph.addSeries(series);
    }
}
