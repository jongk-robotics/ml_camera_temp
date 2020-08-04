package org.tensorflow.lite.examples.detection;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class Tab_Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1,tab2,tab3;
    public PageAdapter pageradapter;

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    { //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity","권한 허용 : " + permissions[i]);
                }
            }
        }
    }
    public void checkSelfPermission() {
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        { temp += Manifest.permission.READ_EXTERNAL_STORAGE + " "; }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else { // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show(); } }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        checkSelfPermission();
        setContentView(R.layout.activity_main);

        tabLayout =(TabLayout) findViewById(R.id.tablayout);
        tab1=(TabItem) findViewById(R.id.Tab1);
        tab2=(TabItem) findViewById(R.id.Tab2);
        tab3=(TabItem) findViewById(R.id.Tab3);
        viewPager=findViewById(R.id.viewpager);
        pageradapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageradapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
