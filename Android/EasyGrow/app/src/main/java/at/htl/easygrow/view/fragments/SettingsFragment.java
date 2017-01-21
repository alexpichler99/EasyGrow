package at.htl.easygrow.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import at.htl.easygrow.R;
import at.htl.easygrow.model.Plant;
import at.htl.easygrow.model.PlantModel;

public class SettingsFragment extends Fragment {

    private SeekBar sbMoistureOptimum;
    private TextView tvMoistureOptimum;
    private EditText etIp;

    private SeekBar sbTemperatureOptimum;
    private TextView tvTemperatureOptimum;

    private SeekBar sbHumidityOptimum;
    private TextView tvHumidityOptimum;

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    //Moisture
                    if (seekBar == sbMoistureOptimum) {
                        PlantModel.getInstance().getPlant().setMoistureOptimum(i);

                        if (tvMoistureOptimum != null)
                            tvMoistureOptimum.setText(i + "%");
                    }
                    //Humidity
                    else if (seekBar == sbHumidityOptimum) {
                        PlantModel.getInstance().getPlant().setHumidityOptimum(i);

                        if (tvHumidityOptimum != null)
                            tvHumidityOptimum.setText(PlantModel.getInstance().getPlant()
                                    .getHumidityOptimum() + "%");
                    }
                    //Temperature
                    else if (seekBar == sbTemperatureOptimum) {
                        PlantModel.getInstance().getPlant().setTemperatureOptimumPerCent(i);

                        if (tvTemperatureOptimum != null)
                            tvTemperatureOptimum.setText(PlantModel.getInstance().getPlant()
                                    .getTemperatureOptimum() + "°C");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etIp = (EditText)getActivity().findViewById(R.id.et_arduino_ip);
        if (etIp != null) {
            etIp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Plant plant = PlantModel.getInstance().getPlant();
                    if (plant != null) {
                        plant.setIp(charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etIp.setText(PlantModel.getInstance().getPlant().getIp());
        }
        sbMoistureOptimum = (SeekBar)getActivity().findViewById(R.id.sb_moisture_optimum);
        tvMoistureOptimum = (TextView)getActivity().findViewById(R.id.tv_moisture_optimum);
        sbHumidityOptimum = (SeekBar)getActivity().findViewById(R.id.sb_humidity_optimum);
        tvHumidityOptimum = (TextView)getActivity().findViewById(R.id.tv_humidity_optimum);
        sbTemperatureOptimum = (SeekBar)getActivity().findViewById(R.id.sb_temperature_optimum);
        tvTemperatureOptimum = (TextView)getActivity().findViewById(R.id.tv_temperature_optimum);


        if (sbMoistureOptimum != null) {
            sbMoistureOptimum.setProgress(PlantModel.getInstance().
                    getPlant().getMoistureOptimum());
            sbMoistureOptimum.setOnSeekBarChangeListener(seekBarChangeListener);
        }
        if (tvMoistureOptimum != null)
            tvMoistureOptimum.setText(PlantModel.getInstance().getPlant().getMoistureOptimum()
                + "%");

        if (sbHumidityOptimum != null) {
            sbHumidityOptimum.setProgress(PlantModel.getInstance().
                    getPlant().getHumidityOptimum());
            sbHumidityOptimum.setOnSeekBarChangeListener(seekBarChangeListener);
        }
        if (tvHumidityOptimum != null)
            tvHumidityOptimum.setText(PlantModel.getInstance().getPlant().getHumidityOptimum()
                    + "%");

        if (sbTemperatureOptimum != null) {
            sbTemperatureOptimum.setProgress(PlantModel.getInstance().
                    getPlant().getTemperatureOptimumPerCent());
            sbTemperatureOptimum.setOnSeekBarChangeListener(seekBarChangeListener);
        }
        if (tvTemperatureOptimum != null)
            tvTemperatureOptimum.setText(PlantModel.getInstance().getPlant().getTemperatureOptimum()
                    + "°C");


    }
}
