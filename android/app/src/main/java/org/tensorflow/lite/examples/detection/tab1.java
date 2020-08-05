package org.tensorflow.lite.examples.detection;

import android.Manifest;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

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

import java.util.ArrayList;
public class tab1 extends AppCompatActivity {
    GridView gridView;
    EditText editText;
    EditText editText2;
    Button button;
    Image_Adapter ImageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab1);
        gridView = (GridView) findViewById(R.id.grid_view);
        ImageView profile_image = findViewById(R.id.profile_image);
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
}
