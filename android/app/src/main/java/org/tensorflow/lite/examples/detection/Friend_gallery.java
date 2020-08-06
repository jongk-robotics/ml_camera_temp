package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Friend_gallery extends AppCompatActivity {
    GridView gridView;
    EditText editText;
    EditText editText2;
    Button button;
    Image_Adapter ImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_gallery);
        gridView = (GridView) findViewById(R.id.friend_gallery_grid_view);
        ImageView profile_image = findViewById(R.id.profile_image);
        ImageAdapter = new Image_Adapter(this);
        gridView.setAdapter(ImageAdapter);

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
//        for( int i=0; i<10; i ++){
//
//            Context context = getApplicationContext();
//            Drawable drawable = getResources().getDrawable(R.drawable.cute);
//
//            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
//            ImageAdapter.addItem(bitmap);
//        }
//        ImageAdapter.notifyDataSetChanged();
//        gridView.setAdapter(ImageAdapter);


    }
    public void downLoadPhoto()
    {
//        final CollectionReference imagesRef = mFireStoreRef.collection("Images");
//
//        ArrayList<Photo> photos = new ArrayList<>();
//
//        imagesRef
//                .whereEqualTo("userEmail", user.getEmail())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Photo photo = document.toObject(Photo.class);
//                                photos.add(photo);
//                            }
//
//
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ImageAdapter.setPhotos(photos);
//                                ImageAdapter.notifyDataSetChanged();
//                                Log.d(TAG, "items: " + ImageAdapter.getCount());
//                            }
//                        });
//                    }
//                });


    }


}
