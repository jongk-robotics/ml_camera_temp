package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.tensorflow.lite.examples.detection.tflite.SimilarityClassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecognitionArray {
    private HashMap<String, SimilarityClassifier.Recognition> recognitions = new HashMap<>();

    public void addRecognition(String name, SimilarityClassifier.Recognition rec)
    {
        recognitions.put(name, rec);
    }

    public void setRecognitions(HashMap<String, SimilarityClassifier.Recognition> recs)
    {
        recognitions = new HashMap<>(recs);
    }

    public void saveToShared(Context context, String key)
    {
        Gson gson = new Gson();
        String json = gson.toJson(recognitions);

        Log.d("SAVESAVE", "savejson: " + json);

        PreferenceManager.setString(context, key, json);
    }

    public void loadFromShared(Context context, String key)
    {
        String json = PreferenceManager.getString(context, key);
        Gson gson = new Gson();
        Log.d("SAVESAVE", "loadjson: " + json);
        recognitions = gson.fromJson( json , new TypeToken<HashMap<String, SimilarityClassifier.Recognition>>(){}.getType() );
        if(recognitions == null)
        {
            recognitions = new HashMap<>();
            return;
        }

        for(Map.Entry<String, SimilarityClassifier.Recognition> entry : recognitions.entrySet()){
            ArrayList<ArrayList<Double>> array= (ArrayList<ArrayList<Double>>) entry.getValue().getExtra();
            int length = array.get(0).size();
            float[][] temp = new float[1][length];
            for(int i = 0; i < length; i++){
                temp[0][i] = array.get(0).get(i).floatValue();
            }

            entry.getValue().setExtra(temp);
        }
    }

    public HashMap<String, SimilarityClassifier.Recognition> getRecognitions() {
        if(recognitions == null)
        {
            recognitions =  new HashMap<String, SimilarityClassifier.Recognition>();
        }

        return recognitions;
    }

    public int getCount()
    {
        return recognitions.size();
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(recognitions);
    }
}
