package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.Interface.WebServiceInterface;
import com.example.myapplication.Model.DirectionModel.DirectionLegModel;
import com.example.myapplication.Model.DirectionModel.DirectionResponseModel;
import com.example.myapplication.Model.DirectionModel.DirectionRouteModel;
import com.example.myapplication.Model.DirectionModel.DirectionStepModel;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private  Double endLat, endLng;
    private String placeId;
   private WebServiceInterface webServiceInterface;
    LatLng name;
    LatLng office,dest;

    ArrayList markerpoints = new ArrayList();
    String gmode = "Driving";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        office = new LatLng(12.991136000965103, 80.2430498972985);
        dest = new LatLng(12.989295, 80.251517);



        webServiceInterface = ApiClient.getRetrofit().create(WebServiceInterface.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        if (!Places.isInitialized()) {
            String apiKey = getString(R.string.api_key);
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endLat = place.getLatLng().latitude;
                endLng = place.getLatLng().longitude;
                name = place.getLatLng();

                final MarkerOptions markerOptions = new MarkerOptions();

                Toast.makeText(getApplicationContext(),"tes"+name,Toast.LENGTH_SHORT).show();

                getDirections(gmode);

            }


            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }

        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(office).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(office,16));



    }
    private void getDirections(String mode){
        String url="https://maps.googleapis.com/maps/api/directions/json?" +
                "origin="+office.latitude+","+office.longitude+"&destination="+endLat+","+endLng+
                "&mode="
                +mode+"&key="+"AIzaSyCxjzqu6TtZl--tNK_tK6IhAdrTo2AwYc0";

        webServiceInterface.getDirection(url).enqueue(new Callback<DirectionResponseModel>() {
            @Override
            public void onResponse(Call<DirectionResponseModel> call, Response<DirectionResponseModel> response) {
                Gson gson = new Gson();
                String res = gson.toJson(response.body());
                Log.d("TAG","onResponse"+res);


                            DirectionRouteModel routeModel = response.body().getDirectionRouteModels().get(0);
                            DirectionLegModel legModel = routeModel.getLegs().get(0);

                            mMap.addMarker(new MarkerOptions()
                                    .position(name)
                                    .title("end Location"));


                            mMap.addMarker(new MarkerOptions()
                                    .position(office)
                                    .title("start Location"));

                            List<LatLng> stepList = new ArrayList<>();

                            PolylineOptions options = new PolylineOptions()
                                    .width(10)
                                    .color(Color.BLUE)
                                    .geodesic(true)
                                    .clickable(true)
                                    .visible(true);

                            List<PatternItem> pattern;

                                pattern = Arrays.asList(
                                        new Dash(30));



                            options.pattern(pattern);

                            for (DirectionStepModel stepModel: legModel.getSteps()){
                                List<LatLng>decodedLatLng=decode(stepModel.getPolyline().getPoints());

                                for (LatLng latLng:decodedLatLng){
                                    stepList.add(new LatLng(latLng.latitude,latLng.longitude));
                                }

                            }
                            options.addAll(stepList);
                            Polyline polyline = mMap.addPolyline(options);




                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 14));


            }

            @Override
            public void onFailure(Call<DirectionResponseModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"faillll",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private List<LatLng> decode(String points) {
        int len = points.length();

        final List<LatLng> path = new ArrayList<>(len / 2);
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = points.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }
        return path;


    }
    private void clearUI(){
        mMap.clear();

    }



}



