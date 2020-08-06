package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class checkpermission extends AppCompatActivity {
    private final int PERMISSIONS_ALL = 1000;
    String[] PERMISSIONS = {
//            Manifest.permission.SEND_SMS,
//            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("&&&&","!@#@!#@#####################################");

                    return false;
                }
            }
        }
        Log.d("&&&&","!@#@!#@#####################################");

        return true;
    }
    private void getPermission(){

        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                1000);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= 23) {

            // requestPermission의 배열의 index가 아래 grantResults index와 매칭
            // 퍼미션이 승인되면
            for(int i = 0; i <3 ; i++) {
                if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permission agreed", "Permission: " + permissions[i] + "was " + grantResults[i]);
                    // TODO : 퍼미션이 승인되는 경우에 대한 코드

                }
                // 퍼미션이 승인 거부되면
                else {
                    Log.d("Permission denied", "Permission denied");

                    // TODO : 퍼미션이 거부되는 경우에 대한 코드
                }

            }
            if (!hasPermissions(this, PERMISSIONS)) {
                finish();

            }
            // 권한이 허용되어있다면 다음 화면 진행
            else {
                Intent intent = new Intent(getBaseContext(), DetectorActivity.class);
                startActivity(intent);
                finish();
            }


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //확인요청
        super.onCreate(savedInstanceState);
        //확인받기
        //this
        getPermission();



    }

}
