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
    private ArrayList<Photo> photos;
    Image_Adapter_clickable(Context c){
        mContext = c;
        photos = new ArrayList<Photo>();

    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public boolean deleteSelected(int sIndex){
        return true;
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return photos.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    public void addItem(Photo photo) {
        photos.add(photo);
    }
    public final void callImageViewer(int selectedIndex){
        Intent i = new Intent(mContext, diary.class);

        i.putExtra("Url", photos.get(selectedIndex).getUrl());
        i.putExtra("Memo", photos.get(selectedIndex).getMemo());
        mContext.startActivity(i);

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView ;
        imageView = new ImageView(mContext);
        Glide.with(mContext).load(photos.get(position).getUrl()).into(imageView);

        return imageView;
    }

}
