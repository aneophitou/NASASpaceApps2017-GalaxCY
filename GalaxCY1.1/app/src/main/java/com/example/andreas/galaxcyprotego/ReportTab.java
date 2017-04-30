package com.example.andreas.galaxcyprotego;
/**
 * Created by Andreas on 29/04/2017.
 */


import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

public class ReportTab extends Fragment implements SensorEventListener{
    float azimuth;
    private SensorManager sensorMgr;
    private Sensor sensor;

    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View reportView = inflater.inflate(R.layout.tab_report, container, false);
        tv = (TextView) reportView.findViewById(R.id.textView2);




                Button uploadImg=(Button) reportView.findViewById(R.id.btnUploadImage);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 123);
            }
        });

        /**DropDownSpinner for distance**/
        Spinner spinner = (Spinner) reportView.findViewById(R.id.spinner);
        List<String> dropDownItems = new ArrayList<>();
        dropDownItems.add("<20m");
        dropDownItems.add("<50m");
        dropDownItems.add("<100m");
        dropDownItems.add("<300m");
        dropDownItems.add("<500m");
        dropDownItems.add("<1km");
        dropDownItems.add(">1km");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropDownItems);
        spinner.setAdapter(dataAdapter);
        /**End of spinner**/

        sensorMgr = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return reportView;
    }
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Toast.makeText(getActivity(), "Image taken!", Toast.LENGTH_SHORT ).show();
    }
    /**
     COMPASS SENSOR
     **/
    float[] orientation = new float[3];
    float[] r = new float[9];

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorManager.getRotationMatrixFromVector(r, sensorEvent.values);
        /**NEEDS A DELAY BECAUSE IT COUNTS UP/DOWN FOR SOME REASON**/
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                azimuth = (float) (Math.toDegrees(sensorMgr.getOrientation(r, orientation)[0]) + 360) % 360;
            }
        }, 2000);
        final Handler textHandler = new Handler();
        textHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if ((azimuth >= 0 && azimuth <= 22.25) || azimuth >= 337.25 && azimuth <= 360) {
                    tv.setText("North");
                } else if (azimuth > 22.25 && azimuth <= 67.25) {
                    tv.setText("North East");

                } else if (azimuth > 67.25 && azimuth <= 102.25) {
                    tv.setText("East");

                } else if (azimuth > 102.25 && azimuth <= 157.25) {
                    tv.setText("South East");

                } else if (azimuth > 157.25 && azimuth <= 202.25) {
                    tv.setText("South");

                } else if (azimuth > 202.25 && azimuth <= 247.25) {
                    tv.setText("South West");

                } else if (azimuth > 247.25 && azimuth <= 292.25) {
                    tv.setText("West");

                } else {
                    tv.setText("North West");
                }
            }
        }, 2000);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}