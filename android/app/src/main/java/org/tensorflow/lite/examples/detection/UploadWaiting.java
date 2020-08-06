package org.tensorflow.lite.examples.detection;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class UploadWaiting extends Dialog {

    private Context context;
    private ImageView imageView;

    public UploadWaiting(Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        setContentView(R.layout.upload_waiting);

        setCanceledOnTouchOutside(false);

        imageView = findViewById(R.id.logoView);
    }

    public void showDialog()
    {
        Glide.with(context).load(R.drawable.eye).into(imageView);
        show();
    }

    @Override
    public void onBackPressed() {

    }
}