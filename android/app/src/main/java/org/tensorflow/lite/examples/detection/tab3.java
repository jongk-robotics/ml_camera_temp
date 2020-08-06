package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class tab3 extends AppCompatActivity {

    private ArrayList<String> imageList;
    private ArrayList<String> placeList;

    private ViewPagerAdapter viewPagerAdapter;

    private static final int DP = 24;

    ImageView commuBtn;
    ImageButton scrapBtn;

    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();

    private String TAG = "tab3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab3);

        this.initializeData();



        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        viewPagerAdapter = new ViewPagerAdapter(this, imageList,placeList);
        viewPager.setAdapter(viewPagerAdapter);

        commuBtn=(ImageView)findViewById(R.id.gatherCommunity);
        commuBtn.setColorFilter(Color.parseColor("#FFE91E63"), PorterDuff.Mode.SRC_IN);

        scrapBtn=(ImageButton)findViewById(R.id.scrap);

        downloadData();

        commuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent
                Context context = v.getContext();
                Intent commuIntent = new Intent(context, CommuActivity.class);
                startActivity(commuIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            }

        });

    }

    public void downloadData()
    {
        final CollectionReference imagesRef = mFireStoreRef.collection("Images");

        imageList = new ArrayList<>();
        placeList = new ArrayList<>();

        imagesRef
                .whereEqualTo("isShared", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Photo photo = document.toObject(Photo.class);
                                imageList.add(photo.getUrl().toString());
                                placeList.add(photo.getLocationName().toString());
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewPagerAdapter.setImageList(imageList);
                                    viewPagerAdapter.setPlaceList(placeList);
                                    viewPagerAdapter.notifyDataSetChanged();
                                    Log.d(TAG, "items: " + viewPagerAdapter.getCount());
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void initializeData()
    {



        imageList = new ArrayList();
        placeList = new ArrayList<>();

    }

}
