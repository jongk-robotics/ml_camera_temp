package org.tensorflow.lite.examples.detection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.VISIBLE;

public class diary extends AppCompatActivity {
    private LinearLayout bottomSheetLayout;
    private LinearLayout gestureLayout;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;
    private Button editbtn;
    private FloatingActionButton addbtn;
    protected ImageView bottomSheetArrowImageView;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();      // PLACE reference

    private final FirebaseUser user = fAuth.getCurrentUser();


    @SuppressLint("RestrictedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_picture);

        bottomSheetLayout = findViewById(R.id.bottom_diary_sheet);
        gestureLayout = findViewById(R.id.gesture_layout2);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);
        addbtn = findViewById(R.id.Add_community);
        editbtn = findViewById(R.id.diary_edit_btn);
        ImageView imageView = (ImageView) findViewById(R.id.galley_picture);
        TextView Diary = findViewById(R.id.diary);
//        TextView textView = findViewById(R.id.diary_text);
        Photo photo = (Photo)getIntent().getSerializableExtra("photo");
        Glide.with(this).load(photo.getUrl()).into(imageView);
        EditText editText = findViewById(R.id.diary_edit);
//        TextView diaryText=findViewById(R.id.diary_text);
        final boolean[] edit = {false};
        if(!photo.getFriends().isEmpty()) {
            //사람이 여러명일 경우, 자기 자신이 포함된 경우도 생각을 해줘야함!
            String temp = photo.getFriends().toString();
            String friend = temp.substring(1, temp.length() - 1);
            Diary.setText(friend + "와 " + photo.getLocationName() + "에서 함께한 추억");
            editText.setText(photo.getMemo());
        }
        else{
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            String time = timeFormat.format(photo.getTimeStamp().toDate());
            Diary.setText(time+"의 "+photo.getLocationName() + "에서의 추억");
            editText.setText(photo.getMemo());
//            textView.setText(diary);
            addbtn.setVisibility(VISIBLE);
            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //커뮤니티에 업로드
                    uploadData(photo.getUrl(), true);
                    addbtn.setBackgroundColor(Color.GRAY);
                    addbtn.setEnabled(false);
                }
            });

        }
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit[0] =!edit[0];
                if(edit[0]){
//                    String temp = diaryText.getText().toString();
//                    editText.setVisibility(VISIBLE);
                    setUseableEditText(editText, true);
                    Log.d("DIARY", "clik: " + editText.isClickable());
                    Log.d("DIARY", "focu: " + editText.isFocusable());
//                    diaryText.setVisibility(View.INVISIBLE);
//                    editText.setText(temp);
                }
                else{
                    String temp = editText.getText().toString();
                    setUseableEditText(editText, false);
                    Log.d("DIARY", "clik: " + editText.isClickable());
                    Log.d("DIARY", "focu: " + editText.isFocusable());
//                    editText.setVisibility(View.INVISIBLE);
//                    diaryText.setVisibility(VISIBLE);
//                    diaryText.setText(temp);
                    uploadMessage(temp, photo.getUrl());


                }
            }
        });
        ViewTreeObserver vto = gestureLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            gestureLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        //                int width = bottomSheetLayout.getMeasuredWidth();
                        int height = gestureLayout.getMeasuredHeight();

                        sheetBehavior.setPeekHeight(height);
                    }
                });
        sheetBehavior.setHideable(false);

        sheetBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState) {
                            case BottomSheetBehavior.STATE_HIDDEN:
                                break;
                            case BottomSheetBehavior.STATE_EXPANDED: {
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_down);
                            }
                            break;
                            case BottomSheetBehavior.STATE_COLLAPSED: {
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                            }
                            break;
                            case BottomSheetBehavior.STATE_DRAGGING:
                                break;
                            case BottomSheetBehavior.STATE_SETTLING:
                                bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up);
                                break;
                        }
                    }
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {}


                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent  intent = new Intent(this, Tab_Activity.class);
        startActivity(intent);
    }

    private void setUseableEditText(EditText et, boolean useable) {
        et.setClickable(useable);
        et.setEnabled(useable);
        et.setFocusable(useable);
        et.setFocusableInTouchMode(useable);
    }

    void uploadMessage(String message, String url)
    {
        HashMap<String, Object> data = new HashMap<>();
        data.put("memo", message);

        final CollectionReference colRef = mFireStoreRef.collection("Images");

        mFireStoreRef.collection("Images")
                .whereEqualTo("url", url)
                .get()
                .continueWith(new Continuation<QuerySnapshot, Void>() {
                    @Override
                    public Void then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        for (DocumentSnapshot snap : task.getResult()) {
                            snap.getReference()
                                    .update(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Success", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Failure", "Error updating document", e);
                                        }
                                    });

                        }

                        return null;
                    }
                });
    }

    void uploadData(String url, Boolean tf)
    {


        HashMap<String, Object> data = new HashMap<>();
        data.put("isShared", tf);

        final CollectionReference colRef = mFireStoreRef.collection("Images");

        mFireStoreRef.collection("Images")
                .whereEqualTo("url", url)
                .get()
                .continueWith(new Continuation<QuerySnapshot, Void>() {
                    @Override
                    public Void then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        for (DocumentSnapshot snap : task.getResult()) {
                            snap.getReference()
                                    .update(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Success", "DocumentSnapshot successfully updated!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Failure", "Error updating document", e);
                                        }
                                    });

                        }

                        return null;
                    }
                });
    }
}
