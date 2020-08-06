package org.tensorflow.lite.examples.detection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Add_profile extends AppCompatActivity {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();      // PLACE reference

    private final FirebaseUser user = fAuth.getCurrentUser();

    private final String TAG = "Add_profile";
    //firestore

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_profile);
        GridView mgridView = (GridView) findViewById(R.id.Add_profile_gridview);
        final Image_Adapter_clickable_add_profile ia = new Image_Adapter_clickable_add_profile(this);
        mgridView.setAdapter(ia);
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){
                ia.callImageViewer(position,0);
                finish();
            }
        });

        EditText editName = findViewById(R.id.Add_profile_edittext1);
        EditText editProfile = findViewById(R.id.Add_profile_edittext2);
        Button btn = findViewById(R.id.btn_profile_change);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inputintent = getIntent();

                String imgPath = inputintent.getExtras().getString("filepath");
                Uri uri= Uri.parse(imgPath);

                String name = editName.getText().toString();
                String message = editProfile.getText().toString();


                //여기서 디비에 전달
                uploadImage(uri, name, message);
//                Intent intent = new Intent(getApplicationContext(),tab1.class);
//
//                startActivity(intent);
                finish();
            }
        });

    }
    void uploadImage(Uri uri, String name, String message)
    {
        //이미지 저장
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String fileName = "image_" + format + ".jpg";

        Log.d(TAG, "uri: " + uri);

        InputStream stream = null;

        try{
            stream = new FileInputStream(uri.toString());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }

        if(stream == null)
        {
            return;
        }



        // get image url
        final StorageReference riversRef = mStorageRef.child(fileName);
        final UploadTask uploadTask = riversRef.putStream(stream);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    //throw.task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String imageUrl = String.valueOf(downloadUri);
                    uploadData(downloadUri, name, message);
                }else{

                }
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


    void uploadData(Uri imgOfUrl, String name, String message)
    {
        String userEmail = user.getEmail();


        HashMap<String, Object> data = new HashMap<>();
        data.put("profileUrl", imgOfUrl.toString());
        data.put("name", name);
        data.put("message", message);

        mFireStoreRef.collection("Users")
                .document(userEmail)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });



    }










}
