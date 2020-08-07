package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.tensorflow.lite.examples.detection.tflite.SimilarityClassifier;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommuActivity extends AppCompatActivity {

    private GridView mgridView;
    private SharedPreferences sp;
    private Image_Adapter ImageAdapter;
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();
    private ArrayList<String> imageList;
    private ArrayList<String> placeList;
    private String TAG = "tab3";

    //firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private final FirebaseUser user = fAuth.getCurrentUser();

    private ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mgridView = (GridView) findViewById(R.id.community_grid_view);
        ImageView profile_image = findViewById(R.id.profile_image);
        ImageAdapter = new Image_Adapter(this, user.getEmail());

        Intent intent = getIntent();
        ArrayList<Photo> photoList = (ArrayList<Photo>) intent.getSerializableExtra("photoList");

        ArrayList<Photo> liked = new ArrayList<>();

//        if(!photoList.isEmpty())
//        {
//            for(Photo photo : photoList)
//            {
//                if(photoList.contains(user.getEmail()))
//                {
//                    liked.add(photo);
//                }
//            }
//        }

        ImageAdapter.setPhotos(photoList);
        mgridView.setAdapter(ImageAdapter);

        ImageButton button = findViewById(R.id.back);
//        downloadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        ArrayList<Photo> photoList = (ArrayList<Photo>) intent.getSerializableExtra("photoList");

//        ArrayList<Photo> liked = new ArrayList<>();
//
//        if(!photoList.isEmpty())
//        {
//            for(Photo photo : photoList)
//            {
//                Log.d("PHOHO", "emap: " + photo.getLikedPeople());
//                if(photo.getLikedPeople().contains(user.getEmail()))
//                {
//                    Log.d("PHOHO", "photo: " + photo.getUrl());
//                    liked.add(photo);
//                }
//            }
//        }

        ImageAdapter.setPhotos(photoList);
        mgridView.setAdapter(ImageAdapter);
        ImageAdapter.notifyDataSetChanged();
    }

    public boolean isContained(ArrayList<String> arr, String str)
    {
        if(arr.isEmpty())
        {
            return false;
        }
        else{
            for(String string : arr)
            {
                if(string.compareTo(str) == 0){
                    Log.d("STRING", string + ", " + str);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void finish(){
        super.finish();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
    }
    public void downloadData()
    {
        final CollectionReference imagesRef = mFireStoreRef.collection("Images");

        imageList = new ArrayList<>();
        placeList = new ArrayList<>();
        photos = new ArrayList<>();
        imagesRef
                .whereEqualTo("isShared", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Photo photo = document.toObject(Photo.class);
                                photos.add(photo);
//                                imageList.add(photo.getUrl().toString());
//                                placeList.add(photo.getLocationName().toString());
                        } }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageAdapter.setPhotos(photos);
                                ImageAdapter.notifyDataSetChanged();
                                Log.d(TAG, "items: " + ImageAdapter.getCount());
                            }
                        });
                    }
                });
    }




}