package org.tensorflow.lite.examples.detection;

import android.Manifest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class tab1 extends AppCompatActivity {
    GridView gridView;
    EditText editText;
    EditText editText2;
    Button button;
    Image_Adapter ImageAdapter;

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
        ImageView profile_image = findViewById(R.id.profile_image);

        //grid view 설정
        ImageAdapter = new Image_Adapter(this);
        ImageAdapter.setPhotos(new ArrayList<>());
        gridView.setAdapter(ImageAdapter);

        Log.d(TAG, "tab1");

        downLoadPhoto();


//        ArrayList<Bitmap> bitmapArrayList = encoding.DecodingImage(email, 0);




        //디비에서 그 계정에 해당하는 이미지 가져오기
        //gridview로 보여줌
        //디비에서 그 계정에 해당하는 이미지 가져오기
        //gridview로 보여줌

//
//        int gallery_length = bitmapArrayList.size(); //갤러리 길이
//        Log.d("bitmap",Integer.toString(gallery_length));
//        for(int i=0; i<gallery_length;i++){
//            //각각의 비트맵
//
//            ImageAdapter.addItem(new SingerItem("dd",bitmapArrayList.get(i)));
//
//        }

//        ImageAdapter.notifyDataSetChanged();
//
//
//        gridView.setAdapter(ImageAdapter);


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
}
