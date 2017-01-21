package at.htl.easygrow.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import at.htl.easygrow.R;
import at.htl.easygrow.model.Plant;
import at.htl.easygrow.model.PlantModel;
import at.htl.easygrow.model.WarningType;

public class OverViewFragment extends Fragment implements Observer {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        PlantModel.getInstance().addObserver(this);
        refresh();

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private void refresh() {
        final FragmentActivity activity = this.getActivity();

        //Get textviews for current values
        TextView tvMoisture = ((TextView)activity.
                findViewById(R.id.content_overview_tv_moisture_value));
        TextView tvHumidity = ((TextView)activity.
                findViewById(R.id.content_overview_tv_humidity_value));
        TextView tvTemperature = ((TextView)activity.
                findViewById(R.id.content_overview_tv_temperature_value));
        LinearLayout laMoisture = (LinearLayout)activity.
                findViewById(R.id.la_moisture);
        LinearLayout laTemperature = (LinearLayout)activity.
                findViewById(R.id.la_temperature);
        LinearLayout laHumidity = (LinearLayout)activity.
                findViewById(R.id.la_humidity);

        Plant plant = PlantModel.getInstance().getPlant();


        if (plant != null) {
            //Set Moisture
            if (tvMoisture != null) {
                if (plant != null && plant.getCurrentMeasurement() != null &&
                        !Float.isNaN(plant.getCurrentMeasurement().getMoisture()))
                    tvMoisture.setText(
                        String.valueOf(plant.getCurrentMeasurement().getMoisture())
                        + "%");
                else
                    tvMoisture.setText("n/a");
            }
            if (laMoisture != null) {

                WarningType warningType = plant.getMoistureWarning();

                if (warningType == WarningType.UNKNOWN)
                    laMoisture.setBackgroundColor(getResources().
                            getColor(R.color.colorUnknown));

                if (warningType == WarningType.OPTIMUM)
                    laMoisture.setBackgroundColor(getResources().
                            getColor(R.color.colorOptimum));

                if (warningType == WarningType.CRITICAL_ABOVE ||
                        warningType == WarningType.CRITICAL_BELOW)
                            laMoisture.setBackgroundColor(getResources().
                                getColor(R.color.colorCritical));

                if (warningType == WarningType.NORMAL)
                    laMoisture.setBackgroundColor(getResources().
                            getColor(R.color.colorNormal));

            }



            //Set Humidity
            if (tvHumidity != null) {
                if (plant != null && plant.getCurrentMeasurement() != null &&
                    !Float.isNaN(plant.getCurrentMeasurement().getHumidity()))
                    tvHumidity.setText(
                            String.valueOf(plant.getCurrentMeasurement().getHumidity())
                            + "%");
                else
                    tvHumidity.setText("n/a");
            }
            if (laHumidity != null) {

                WarningType warningType = plant.getHumidityWarning();

                if (warningType == WarningType.UNKNOWN)
                    laHumidity.setBackgroundColor(getResources().
                            getColor(R.color.colorUnknown));

                if (warningType == WarningType.OPTIMUM)
                    laHumidity.setBackgroundColor(getResources().
                            getColor(R.color.colorOptimum));

                if (warningType == WarningType.CRITICAL_ABOVE ||
                        warningType == WarningType.CRITICAL_BELOW)
                    laHumidity.setBackgroundColor(getResources().
                            getColor(R.color.colorCritical));

                if (warningType == WarningType.NORMAL)
                    laHumidity.setBackgroundColor(getResources().
                            getColor(R.color.colorNormal));
            }



            //Set temperature
            if (tvTemperature != null) {
                if (plant != null && plant.getCurrentMeasurement() != null &&
                        !Float.isNaN(plant.getCurrentMeasurement().getTemperature()))
                    tvTemperature.setText(
                            String.valueOf(plant.getCurrentMeasurement().getTemperature())
                            + " °C");
                else
                    tvTemperature.setText("n/a");
            }
            if (laTemperature != null) {

                WarningType warningType = plant.getTemperatureWarning();

                if (warningType == WarningType.UNKNOWN)
                    laTemperature.setBackgroundColor(getResources().
                            getColor(R.color.colorUnknown));

                if (warningType == WarningType.OPTIMUM)
                    laTemperature.setBackgroundColor(getResources().
                            getColor(R.color.colorOptimum));

                if (warningType == WarningType.CRITICAL_ABOVE ||
                        warningType == WarningType.CRITICAL_BELOW)
                    laTemperature.setBackgroundColor(getResources().
                            getColor(R.color.colorCritical));

                if (warningType == WarningType.NORMAL)
                    laTemperature.setBackgroundColor(getResources().
                            getColor(R.color.colorNormal));
            }
        }
        else {
            if (tvMoisture != null)
                tvMoisture.setText("n/a");
            if (tvHumidity != null)
                tvHumidity.setText("n/a");
            if (tvTemperature != null)
                tvTemperature.setText("n/a");
        }

    }
}
