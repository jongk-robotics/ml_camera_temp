package org.tensorflow.lite.examples.detection;

import android.Manifest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class tab1 extends AppCompatActivity {
    GridView gridView;
    TextView nameView;
    TextView infoView;
    Button button;
    Image_Adapter_clickable ImageAdapter;
    ImageView profile_image;

    private ArrayList<Photo> photos = new ArrayList<>();

    private String TAG = "tab1";

    //firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private final FirebaseUser user = fAuth.getCurrentUser();
    //firestore
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab1);
        gridView = (GridView) findViewById(R.id.grid_view);
        nameView=findViewById(R.id.galley_name);
        infoView=findViewById(R.id.galley_information);

        profile_image = findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new MyListener());
        //grid view 설정
        ImageAdapter = new Image_Adapter_clickable(this);
        ImageAdapter.setPhotos(new ArrayList<>());
        gridView.setAdapter(ImageAdapter);

        // res/drawable 폴더에 있는 이미지로 셋팅하기
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){
                ImageAdapter.callImageViewer(position);
                finish();
            }
        });





        Log.d(TAG, "tab1");

        downLoadPhoto();
        downLoadProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();

        downLoadProfile();
    }

    public void downLoadProfile()
    {
        final DocumentReference docRef = mFireStoreRef.collection("Users").document(user.getEmail());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object url = document.get("profileUrl");
                        Object name = document.get("name");
                        Object message = document.get("message");
                        if(url == null || name == null || message == null)
                        {
                            Log.d(TAG, "No profile");
                        }
                        else{
                            String profileUri = url.toString();
                            String profileName = name.toString();
                            String profileMessage = message.toString();

                            setProfile(profileUri, profileName, profileMessage);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void setProfile(String uri, String name, String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load(uri).into(profile_image);
                nameView.setText(name);
                infoView.setText(message);
            }
        });
    }

    public void downLoadPhoto()
    {
        final CollectionReference imagesRef = mFireStoreRef.collection("Images");

        ArrayList<Photo> photos = new ArrayList<>();

        imagesRef
                .whereEqualTo("userEmail", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Photo photo = document.toObject(Photo.class);
                                photos.add(photo);
                            }


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
    class MyListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext()  , Add_profile.class );
            startActivity(intent);
        } // end onClick


    } // end MyListener()


}
