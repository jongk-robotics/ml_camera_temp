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
import com.google.firebase.firestore.FirebaseFirestore;
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

    private final String TAG = "CAPTURED IMAGE";

    private ImageView mCapturedImageView;
    private TextView mCapturedTextView;
    private Button mCaputuredBtn;

    private Activity mActivity;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();      // PLACE reference
    private FirebaseFirestore mFireStoreRef2 = FirebaseFirestore.getInstance();     // FRIENDS reference
    private FirebaseFirestore mFireStoreRef3 = FirebaseFirestore.getInstance();     // PHOTOS reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image_acvtivity);

        mCapturedImageView = findViewById(R.id.CapturedImageView);
        mCapturedTextView = findViewById(R.id.CapturedNames);
        mCaputuredBtn = findViewById(R.id.CaptureSaveBtn);

        mActivity = this;

        // get current user info
        final FirebaseUser user = fAuth.getCurrentUser();

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

        final byte[] inputData2 = inputData;

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String fileName = "image_" + format + ".jpg";

//        final StorageReference riversRef = mStorageRef.child(fileName);
//        UploadTask uploadTask = riversRef.putBytes(inputData);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Log.d("FIREBASE", "upload failure");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//                riversRef.getDownloadUrl(); //업로드한 이미지의 url
//                Log.d("FIREBASE", "upload success");
//            }
//        });


        mCapturedTextView.setText(namelist.toString());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(mActivity)
                        .load(uri)
                        .into(mCapturedImageView);
            }
        });

        mCaputuredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StorageReference riversRef = mStorageRef.child(fileName);
                UploadTask uploadTask = riversRef.putBytes(inputData2);
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

                String userEmail = user.getEmail();
                mFireStoreRef
                        .collection("Users")
                        .document(userEmail)
                        .update("Place", data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

                mFireStoreRef2
                        .collection("Users")
                        .document(userEmail)
                        .update("Friends", data2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });

                mFireStoreRef3
                        .collection("Users")
                        .document(userEmail)
                        .update("Photos", data3)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
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