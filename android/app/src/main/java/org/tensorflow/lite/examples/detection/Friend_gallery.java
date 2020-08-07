package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Ref;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_gallery extends AppCompatActivity {

    private String TAG = "FRIEND GALLERY";

    GridView gridView;
    TextView nameView;
    CircleImageView profile_view;
    String friendName = "";

    Image_Adapter_clickable ImageAdapter;
    private ArrayList<Photo> photos = new ArrayList<>();

    //firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private final FirebaseUser user = fAuth.getCurrentUser();
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_gallery);
        gridView = (GridView) findViewById(R.id.friend_gallery_grid_view);
        ImageView profile_image = findViewById(R.id.profile_image);
        nameView= (TextView)findViewById(R.id.friend_galley_name);
        profile_view = findViewById(R.id.friend_profile_image);

//        ArrayList<Bitmap> bitmapArrayList = encoding.DecodingImage(email, 0);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        friendName = name;

        int count = Integer.parseInt(intent.getExtras().getString("count"));
        String profile = intent.getStringExtra("url");

        Log.d("NAME","name: " + name);
        Log.d("NAME","count: " + count);
        Log.d("NAME","profile: " + profile);

        nameView.setText(name);

        Glide.with(getApplicationContext()).load(profile).into(profile_view);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar2);
        progressBar.setProgress(count);
        ImageAdapter = new Image_Adapter_clickable(this);
        gridView.setAdapter(ImageAdapter);
        downLoadPhoto();
    }

    public void downLoadPhoto()
    {
        final CollectionReference collRef = db.collection("Users").document(user.getEmail())
                .collection("Friend").document(friendName).collection("Image");

        ArrayList<Photo> photos = new ArrayList<>();

        collRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Photo photo = document.toObject(Photo.class);
                                photos.add(photo);
                                Log.d(TAG, "url: " + photo.getUrl());
                            }

                            Log.d(TAG, "end: " + photos.size());


                        } else {
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
