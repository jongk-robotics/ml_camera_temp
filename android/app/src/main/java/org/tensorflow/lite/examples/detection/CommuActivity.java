package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class CommuActivity extends AppCompatActivity {

    private GridView mgridView;
    private SharedPreferences sp;
    private Image_Adapter ImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mgridView = (GridView) findViewById(R.id.community_grid_view);
        ImageView profile_image = findViewById(R.id.profile_image);
        ImageAdapter = new Image_Adapter(this);
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
        for( int i=0; i<10; i ++){

            Context context = getApplicationContext();
            Drawable drawable = getResources().getDrawable(R.drawable.cute);

            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            //ImageAdapter.addItem(bitmap);
        }
        ImageAdapter.notifyDataSetChanged();
        mgridView.setAdapter(ImageAdapter);
        Button button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish(){
        super.finish();
//        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_in_right);
    }
}