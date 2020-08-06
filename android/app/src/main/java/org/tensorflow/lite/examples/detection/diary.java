package org.tensorflow.lite.examples.detection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class diary extends AppCompatActivity {
    private LinearLayout bottomSheetLayout;
    private LinearLayout gestureLayout;
    private BottomSheetBehavior<LinearLayout> sheetBehavior;

    protected ImageView bottomSheetArrowImageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_picture);

        bottomSheetLayout = findViewById(R.id.bottom_diary_sheet);
        gestureLayout = findViewById(R.id.gesture_layout2);
        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetArrowImageView = findViewById(R.id.bottom_sheet_arrow);

        ImageView imageView = (ImageView) findViewById(R.id.galley_picture);
        TextView Diary = findViewById(R.id.diary);
        TextView textView = findViewById(R.id.diary2);
        Bundle extras = getIntent().getExtras();
        String urL = getIntent().getStringExtra("Url");
        String diary = getIntent().getStringExtra("Memo");
        String LocationName = getIntent().getStringExtra("LocationName");
        String Friends = getIntent().getStringExtra("Friends");
        String time = getIntent().getStringExtra("Time");
        Glide.with(this).load(urL).into(imageView);

        if(Friends != null) {
            //사람이 여러명일 경우, 자기 자신이 포함된 경우도 생각을 해줘야함!
            Diary.setText(Friends + "와 " + LocationName + "에서 함께한 추억");
            textView.setText(diary);
        }
        else{
            Diary.setText(time+"의 "+LocationName + "에서의 추억");
            textView.setText(diary);

        }


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

}
