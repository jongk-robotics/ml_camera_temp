package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class PlaceNameRequest implements PlacesListener {
    private static String TAG = "PlaceNameRequest";
    private CollectionReference colRef;
    private processLocation process;

    private boolean isUpdatingPlaceNameFromAPI;
    private boolean isUpdatingPlaceNameFromDB;

    private String API_KEY;
    private Location mLocation;

    private ArrayList<String> locationFromAPI;
    private ArrayList<String> locationFromDB;

    private int searchRadius;

    //gps
    private GpsTracker gpsTracker;

    PlaceNameRequest(String key, processLocation process, CollectionReference colRef, int searchRadius)
    {
        API_KEY = key;
        this.process = process;
        this.colRef = colRef;
        this.searchRadius = searchRadius;
    }

    public interface processLocation{
        public void processLocationNames(ArrayList<String> nameList);
    }

    private void getNamesFromAPI()
    {
        new NRPlaces.Builder()
                .listener(this)
                .key(API_KEY)
                .latlng(mLocation.getLatitude(), mLocation.getLongitude())//현재 위치
                .radius(searchRadius) //500 미터 내에서 검색
                .build()
                .execute();
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {
        isUpdatingPlaceNameFromAPI = true;
    }

    @Override
    public void onPlacesSuccess(List<Place> places) {
        locationFromAPI = new ArrayList<>();

        final String point = "point_of_interest";

        for (noman.googleplaces.Place place : places) {
            Log.d(TAG, "location: " + place.getLatitude() + ", " + place.getLongitude());
            Log.d(TAG, "name: " + place.getName());
            for(String type : place.getTypes())
            {
                String tempType = new String(type);
                if(type.equals(point))
                {
                    locationFromAPI.add(place.getName());
                    break;
                }

            }
        }

        isUpdatingPlaceNameFromAPI = false;

        if(!isUpdatingPlaceNameFromDB)
        {
            process.processLocationNames(addNames());
        }
    }

    @Override
    public void onPlacesFinished() {
        isUpdatingPlaceNameFromAPI = false;
    }

    private void getNamesFromDB()
    {
        locationFromDB = new ArrayList<>();
        isUpdatingPlaceNameFromDB = true;

        colRef.get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            GeoPoint tempLocation = (GeoPoint) document.get("location");
                            String placeName = (String) document.get("placeName");

                            Location location = new Location(placeName);
                            location.setLatitude(tempLocation.getLatitude());
                            location.setLongitude(tempLocation.getLongitude());

                            float distance = location.distanceTo(mLocation);

                            if(distance < searchRadius)
                            {
                                locationFromDB.add(placeName);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    isUpdatingPlaceNameFromDB = false;

                    if(!isUpdatingPlaceNameFromAPI)
                    {
                        process.processLocationNames(addNames());
                    }
                }
            });
    }

    public void startGetNames(Location location)
    {
        this.mLocation = location;

        getNamesFromDB();
        getNamesFromAPI();
    }

    public ArrayList<String> addNames()
    {
        TreeSet<String> nameList = new TreeSet<>(locationFromAPI);
        nameList.addAll(locationFromDB);

        ArrayList<String> result = new ArrayList<>(nameList);

        return result;
    }
}
