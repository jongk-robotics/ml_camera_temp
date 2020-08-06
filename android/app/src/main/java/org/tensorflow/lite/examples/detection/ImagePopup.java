package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImagePopup extends Activity implements View.OnClickListener {
    private Context mContext = null;
    private final int imgWidth = 300;
    private final int imgHeight = 372;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_image_popup);
        mContext = this;

        /** 전송메시지 */
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String imgPath = extras.getString("filename");

        /** 완성된 이미지 보여주기  */
//        BitmapFactory.Options bfo = new BitmapFactory.Options();
        ImageView iv = (ImageView) findViewById(R.id.imageView);
//        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);
//        Bitmap resized = Bitmap.createScaledBitmap(bm, imgWidth, imgHeight, true);
//        iv.setScaleType(ImageView.ScaleType.CENTER);
//        iv.setImageBitmap(bm); //bm -> resized\
        Glide.with(mContext).load(imgPath).into(iv);


        /** 리스트로 가기 버튼*/
        Button btn = (Button) findViewById(R.id.btn_back);
        btn.setOnClickListener(this);
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:

                //여기서 디비의 개인 갤러리에 사진을 추가함
                Intent i = getIntent();
                Bundle extras = i.getExtras();
                String imgPath = extras.getString("filename");
                Log.d("filename",imgPath);
                String what = extras.getString("go");
                if (what.equals("friend")){
                    Intent intent = new Intent(ImagePopup.this, Add_friend.class);
                    intent.putExtra("filepath",imgPath);

                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(ImagePopup.this, Add_profile.class);
                    intent.putExtra("filepath",imgPath);

                    startActivity(intent);

                }
                this.finish();
                break;
        }
    }


}
