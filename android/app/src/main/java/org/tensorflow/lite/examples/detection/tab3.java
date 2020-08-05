package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class tab3 extends AppCompatActivity {

    private ArrayList<Integer> imageList;
    private ArrayList<String> placeList;

    private static final int DP = 24;

    ImageButton commuBtn;
    ImageButton scrapBtn;


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

        viewPager.setAdapter(new ViewPagerAdapter(this, imageList,placeList));

        commuBtn=(ImageButton)findViewById(R.id.gatherCommunity);
        scrapBtn=(ImageButton)findViewById(R.id.scrap);

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

    public void initializeData()
    {
        imageList = new ArrayList();
        placeList = new ArrayList<>();
        imageList.add(R.drawable.iu2);
        imageList.add(R.drawable.iu4);
        imageList.add(R.drawable.iu5);
        imageList.add(R.drawable.iufullscreen);
        imageList.add(R.drawable.iutwo);
        placeList.add("경주");
        placeList.add("서울");
        placeList.add("대전");
        placeList.add("남양주");
        placeList.add("텍사스");

    }

}
