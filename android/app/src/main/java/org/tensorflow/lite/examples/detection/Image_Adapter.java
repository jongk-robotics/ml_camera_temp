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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Image_Adapter extends BaseAdapter {
    private Context mContext;
    ArrayList<Photo> photos = new ArrayList<Photo>();
    String userEmail;

    Image_Adapter(Context c, String userEmail){
        mContext = c;
        this.userEmail = userEmail;
//        thumbsDataList = new ArrayList<String>();
//        thumbsIDList = new ArrayList<String>();
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

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        imageView = new ImageView(mContext);
//
//        Log.d("adapter", "url: " + photos.get(position).getUrl());
//        Glide.with(mContext).load(photos.get(position).getUrl()).into(imageView);
//
//        return imageView;

        Photo photo = photos.get(position);

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.community_item, parent, false);
        }

        ImageView image = convertView.findViewById(R.id.CommuImageView);
        ImageView like = convertView.findViewById(R.id.CommuLike);

        Glide.with(mContext).load(photo.getUrl()).into(image);
        if(!photo.getLikedPeople().contains(userEmail))
        {
            like.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
