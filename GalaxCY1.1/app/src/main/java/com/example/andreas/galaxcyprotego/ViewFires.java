package com.example.andreas.galaxcyprotego;

/**
 * Created by Andreas on 29/04/2017.
 */

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ViewFires extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager locManager;
    GoogleMapOptions options = new GoogleMapOptions();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_fires, container, false);

        Button btnFindPath = (Button) rootView.findViewById(R.id.btnFindPath);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }


        });
        Button btnLegend = (Button) rootView.findViewById(R.id.btnLegend);
        btnLegend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
         new AlertDialog.Builder(getContext())
                       .setTitle("Map Legend: ")
                .setMessage("GREEN- ALL OK \n YELLOW-PROTEGO ON STANDBY \nGREY- SMOKE\n RED- HIGH TEMPERATURE/FIRE \nDARK RED- KAMIKAZE\n PURPLE- MODIS DATA \n BLUE-USER REPORTS")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){

                            }
                        }).show();
            }


        });

        Button btnSOS = (Button) rootView.findViewById(R.id.btnSOS);
        btnSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Your SOS report has been received.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Would you like to call a medic?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();
                            }
                        })
                        .show();
            }
        });

        Button btnChangeView = (Button) rootView.findViewById(R.id.btnChangeView);
        btnChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        Spinner spin = (Spinner) rootView.findViewById(R.id.spinnerSelectOverlay);
        List<String> dropdown = new ArrayList<String>();

        dropdown.add("View fires from satellite");
        dropdown.add("View fires from Protego");
        dropdown.add("View reported fires");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dropdown);
        spin.setAdapter(dataAdapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", String.valueOf(position));
                switch (position) {
                    case 0:
                        mMap.clear();
                        List<Double> latList = new ArrayList<java.lang.Double>();
                        java.lang.Double[] latArray = scanLatLong(latList, getResources().openRawResource(R.raw.firestop));
                        List<Double> longList = new ArrayList<java.lang.Double>();
                        java.lang.Double[] longArray = scanLatLong(longList, getResources().openRawResource(R.raw.firestopy));
                        for (int i=0; i<latArray.length;i++){
                            mMap.addMarker(new MarkerOptions() .position(createLocation(latArray[i],longArray[i] ))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.watchtower)));
                        }

                        latLngToMap(getResources().openRawResource(R.raw.latitude_vertic), getResources().openRawResource(R.raw.longitude_horiz), "#66800080", "0.1", true);

                        break;
                    case 1:

                        mMap.clear();
                        List<Double> latList2 = new ArrayList<java.lang.Double>();
                        java.lang.Double[] latArray2 = scanLatLong(latList2, getResources().openRawResource(R.raw.firestop));
                        List<Double> longList2 = new ArrayList<java.lang.Double>();
                        java.lang.Double[] longArray2 = scanLatLong(longList2, getResources().openRawResource(R.raw.firestopy));
                        for (int i=0; i<latArray2.length;i++){
                            mMap.addMarker(new MarkerOptions() .position(createLocation(latArray2[i],longArray2[i] ))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.watchtower)));
                        }
                        latLngToMap(getResources().openRawResource(R.raw.x_0), getResources().openRawResource(R.raw.y_0), "#666DC066", "0.1", true);
                        latLngToMap(getResources().openRawResource(R.raw.x_1), getResources().openRawResource(R.raw.y_1), "#66ffff7f", "0.2", true);
                        latLngToMap(getResources().openRawResource(R.raw.x_2), getResources().openRawResource(R.raw.y_2), "#667a8b7f", "0.3", true);
                        latLngToMap(getResources().openRawResource(R.raw.x_3), getResources().openRawResource(R.raw.y_3), "#66e90510", "0.4", true);
                        latLngToMap(getResources().openRawResource(R.raw.x_4), getResources().openRawResource(R.raw.y_4), "#66831225", "0.5", true);

                        break;
                    case 2:
                        mMap.clear();
                        List<Double> latList3 = new ArrayList<java.lang.Double>();
                        java.lang.Double[] latArray3 = scanLatLong(latList3, getResources().openRawResource(R.raw.firestop));
                        List<Double> longList3 = new ArrayList<java.lang.Double>();
                        java.lang.Double[] longArray3 = scanLatLong(longList3, getResources().openRawResource(R.raw.firestopy));
                        for (int i=0; i<latArray3.length;i++){
                            mMap.addMarker(new MarkerOptions() .position(createLocation(latArray3[i],longArray3[i] ))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.watchtower)));
                        }
                        latLngToMap(getResources().openRawResource(R.raw.fory), getResources().openRawResource(R.raw.forx), "#660000FF", "0.1", true);

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //PERMISSIONS CHECK
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 99);

        } else {

        }
        mMap.setMyLocationEnabled(true);

           /*METHOD FOR EXTERNAL STORAGE OF CSV*/
        //Scanner scanLat, scanLong;
        //String latFile="latitude_vertic.csv";
        //File longFile = new File("/storage/emulated/0/longitude_horiz.csv");
        //List<java.lang.Double> longList = new ArrayList<java.lang.Double>();
        //java.lang.Double[] longArray = scanLatLong((longList),longFile);
        //String longFile="/res/longitude_horiz.csv";

        /**METHOD FOR INTERNAL CSV STORAGE**/
    }

    /*Clean up by destroying the map fragment...Crashes app if left in memory*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            getChildFragmentManager().beginTransaction().remove(mapFragment).commitAllowingStateLoss();
    }


    /**
     * Function takes two double values and creates a LatLng object with them
     **/
    private LatLng createLocation(double lat, double lon) {
        LatLng latlng = new LatLng(lat, lon);
        return latlng;
    }

    /***
     * Function reads the latitude and longitude from the files, and the array it should be placed in, and returns the populated array
     ***/
    private java.lang.Double[] scanLatLong(List<java.lang.Double> latLongArray, InputStream file) {
      /*METHOD1 USING EXTERNAL STORAGE*/
        //  try {
        //Scanner scanLatLong = new Scanner(file);
        //scanLatLong.useDelimiter(",");

//            while(scanLatLong.hasNextDouble()){
        //              latLongArray.add( Double.parseDouble(scanLatLong.next()) );
        //        }
        //      scanLatLong.close();

        //}catch(FileNotFoundException e){Log.d("failure", "Failed "+file);}
        //return latLongArray.toArray(new java.lang.Double[latLongArray.size()]);

            /*METHOD 2 USING INTERNAL STORAGE*/
        InputStreamReader isr = new InputStreamReader(file);
        BufferedReader buffer = new BufferedReader(isr);
        try {
            String read = "";

            while ((read = buffer.readLine()) != null) {

                for (int j = 0; j < read.split(",").length; j++) {
                    latLongArray.add(Double.parseDouble(read.split(",")[j]));

                }
            }
            buffer.close();

        } catch (IOException ioe) {
            Log.d("failure", "failed IO " + file);
        }
        return latLongArray.toArray(new java.lang.Double[latLongArray.size()]);
    }

    /***
     * Function takes file input streams of X & Y value files, and the color needed, and draws it onto the map
     ***/
    private void latLngToMap(InputStream iStreamLat, InputStream iStreamLong, String color, String zInd, boolean vis) {
        List<Double> latList = new ArrayList<java.lang.Double>();
        java.lang.Double[] latArray = scanLatLong(latList, iStreamLat);
        List<Double> longList = new ArrayList<java.lang.Double>();
        java.lang.Double[] longArray = scanLatLong(longList, iStreamLong);

        /**Create circle**/
        for (int i = 0; i < latArray.length; i++) {
            mMap.addCircle(new CircleOptions().center(createLocation(latArray[i],
                    longArray[i])).radius(100).strokeColor(Color.parseColor(color)).fillColor(Color.parseColor(color)).zIndex(Float.parseFloat(zInd)).visible(vis));
        }
    }
}
