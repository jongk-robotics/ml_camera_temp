package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Collections;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Photo> photoList;
    private String userEmail;

    public ViewPagerAdapter(Context context, String userEmail)
    {
        this.mContext = context;
        this.photoList = new ArrayList<>();
        this.userEmail = userEmail;
    }



    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photoList = photoList;
    }

    public String getImageUrl(int position)
    {
        try{
            return photoList.get(position).getUrl();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getLocationName(int position)
    {
        try{
            return photoList.get(position).getLocationName();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        Log.d("KAKAKA", "idi: " + position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_activity, null);
        TextView placeName= view.findViewById(R.id.commuPlaceName);
        TextView scrapCount = view.findViewById(R.id.scrapsCount);
        ImageView imageView = view.findViewById(R.id.imageView);
        ImageButton imageButton = view.findViewById(R.id.scrapBtn);
        Glide.with(mContext).load(photoList.get(position).getUrl()).into(imageView);
        placeName.setText(photoList.get(position).getLocationName());
        scrapCount.setText(String.valueOf(photoList.get(position).getLikedPeople().size()));
        container.addView(view);

        Photo photo = photoList.get(position);
        Boolean isScrapped = photo.getLikedPeople().contains(userEmail);
        if(isScrapped){
            Log.d("SCRAP: ", "1" + ", " + position);
            imageButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
        }
        else{
            Log.d("SCRAP: ", "2" + ", " + position);
            imageButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Photo photo = photoList.get(position);
                Boolean isScrapped = photo.getLikedPeople().contains(userEmail);
                if(isScrapped){
                    Log.d("SCRAP: ", "3" + ", " + position);
                    imageButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    photo.removeLikedPeople(userEmail);
                    scrapCount.setText(String.valueOf(photo.getLikedPeople().size()));
                }
                else{
                    Log.d("SCRAP: ", "4" + ", " + position);
                    imageButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    photo.addLikedPeople(userEmail);
                    scrapCount.setText(String.valueOf(photo.getLikedPeople().size()));

                }
            }
        });

        return view;
    }

    public ArrayList<Photo> getPhotoList() {

        return photoList;
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
