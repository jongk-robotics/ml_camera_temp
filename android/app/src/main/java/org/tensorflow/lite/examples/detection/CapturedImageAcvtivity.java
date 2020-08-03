package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CapturedImageAcvtivity extends AppCompatActivity {

    private ImageView mCapturedImageView;
    private TextView mCapturedTextView;
    private Button mCaputuredBtn;

    private Activity mActivity;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image_acvtivity);

        mCapturedImageView = findViewById(R.id.CapturedImageView);
        mCapturedTextView = findViewById(R.id.CapturedNames);
        mCaputuredBtn = findViewById(R.id.CaptureSaveBtn);

        mActivity = this;

        Intent intent =  getIntent();
        Uri uri = intent.getParcelableExtra("imageUri");
        ArrayList<String> namelist = (ArrayList<String>) intent.getSerializableExtra("nameList");
        byte[] inputData = new byte[0];
        
        try {
            InputStream iStream =   getContentResolver().openInputStream(uri);
            inputData = getBytes(iStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String fileName = "image_" + format + ".jpg";

        final StorageReference riversRef = mStorageRef.child(fileName);
        UploadTask uploadTask = riversRef.putBytes(inputData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("FIREBASE", "upload failure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                riversRef.getDownloadUrl(); //업로드한 이미지의 url
                Log.d("FIREBASE", "upload success");
            }
        });


        mCapturedTextView.setText(namelist.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(mActivity)
                        .load(uri)
                        .into(mCapturedImageView);
            }
        });
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}