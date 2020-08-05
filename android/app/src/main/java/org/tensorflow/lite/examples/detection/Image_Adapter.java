package org.tensorflow.lite.examples.detection;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Image_Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> photos;

    Image_Adapter(Context c){
        mContext = c;
        photos = new ArrayList<Bitmap>();
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
    public void addItem(Bitmap photo) {
        photos.add(photo);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
            imageView = new ImageView(mContext);
        Glide.with(mContext).load(photos.get(position)).into(imageView);

        return imageView;
    }
}
