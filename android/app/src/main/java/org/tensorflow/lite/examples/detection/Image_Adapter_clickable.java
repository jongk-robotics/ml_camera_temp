package org.tensorflow.lite.examples.detection;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class Image_Adapter_clickable extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> photos;
    private ArrayList<String> diary;
    Image_Adapter_clickable(Context c){
        mContext = c;
        photos = new ArrayList<Bitmap>();
        diary = new ArrayList<String>();
    }


    public boolean deleteSelected(int sIndex){
        return true;
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    public void addItem(Bitmap photo,String text) {
        photos.add(photo);
        diary.add(text);
    }
    public final void callImageViewer(int selectedIndex){
        Intent i = new Intent(mContext, diary.class);
        String text= diary.get(selectedIndex);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = photos.get(selectedIndex);
//        float scale = (float) (1024/(float)bitmap.getWidth());
//        int image_w = (int) (bitmap.getWidth() * scale);
//        int image_h = (int) (bitmap.getHeight() * scale);
//        Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        i.putExtra("image", byteArray);
        i.putExtra("diary", text);
        mContext.startActivity(i);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView ;
        imageView = new ImageView(mContext);
        Glide.with(mContext).load(photos.get(position)).into(imageView);

        return imageView;
    }

}
