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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class tab3 extends AppCompatActivity {

    private ArrayList<Photo> photoList;
    private ArrayList<Photo> originalPhotoList;
    private ArrayList<DocumentReference> docRefList;

    private ViewPagerAdapter viewPagerAdapter;

    private static final int DP = 24;

    UploadWaiting uploadDialog;

    ImageView commuBtn;

    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();

    private String TAG = "tab3";

    //firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private final FirebaseUser user = fAuth.getCurrentUser();

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

        photoList = new ArrayList<>();
        originalPhotoList = new ArrayList<>();
        docRefList = new ArrayList<>();

        uploadDialog = new UploadWaiting(this);

        viewPagerAdapter = new ViewPagerAdapter(this, user.getEmail());
        viewPager.setAdapter(viewPagerAdapter);

        commuBtn=(ImageView)findViewById(R.id.gatherCommunity);
        commuBtn.setColorFilter(Color.parseColor("#FFE91E63"), PorterDuff.Mode.SRC_IN);

        downloadData();

        commuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent
                Context context = v.getContext();
                Intent commuIntent = new Intent(context, CommuActivity.class);

                commuIntent.putExtra("photoList", photoList);
                startActivity(commuIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            }

        });

        ImageButton button = findViewById(R.id.gotoCamera3);
//        downloadData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetectorActivity.class );
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "pause");

        WriteBatch batch = mFireStoreRef.batch();

        ArrayList<Photo> list = viewPagerAdapter.getPhotoList();

        String userEmail = user.getEmail();

        for(int i = 0; i < list.size(); i++)
        {
            Boolean isOriginalCon = originalPhotoList.get(i).getLikedPeople().contains(userEmail);
            Boolean isCon = list.get(i).getLikedPeople().contains(userEmail);

            if(isOriginalCon && !isCon)
            {
                HashMap<String, Object> data = new HashMap<>();
                data.put("likedPeople", FieldValue.arrayRemove(userEmail));
                batch.update(docRefList.get(i), data);
            }
            else if(!isOriginalCon && isCon)
            {
                HashMap<String, Object> data = new HashMap<>();
                data.put("likedPeople", FieldValue.arrayUnion(userEmail));
                batch.update(docRefList.get(i), data);
            }
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "upload success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    public void downloadData()
    {
        final CollectionReference imagesRef = mFireStoreRef.collection("Images");

        photoList = new ArrayList<>();
        docRefList = new ArrayList<>();

        imagesRef
                .whereEqualTo("isShared", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Photo photo = document.toObject(Photo.class);
                                photoList.add(photo);
                                docRefList.add(document.getReference());
                                originalPhotoList.add(photo.copy());
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewPagerAdapter.setPhotoList(photoList);
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
        photoList = new ArrayList<>();
        originalPhotoList = new ArrayList<>();
    }

}
