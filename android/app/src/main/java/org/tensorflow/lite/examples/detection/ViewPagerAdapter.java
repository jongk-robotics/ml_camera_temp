package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Photo> photoList;
    private ArrayList<Boolean> isScrappedList;

    public ViewPagerAdapter(Context context, ArrayList<Photo> photoList)
    {
        this.mContext = context;
        this.photoList = photoList;
        this.isScrappedList = new ArrayList<>(photoList.size());
        Collections.fill(this.isScrappedList, Boolean.FALSE);
    }

    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photoList = photoList;
        this.isScrappedList = new ArrayList<>(photoList.size());
        Collections.fill(this.isScrappedList, Boolean.FALSE);
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

    public boolean getIsScrapped(int position)
    {
        try{
            return isScrappedList.get(position);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_activity, null);
        TextView placeName= view.findViewById(R.id.commuPlaceName);
        ImageView imageView = view.findViewById(R.id.imageView);
        ImageButton imageButton = view.findViewById(R.id.scrapBtn);
        Glide.with(mContext).load(photoList.get(position).getUrl()).into(imageView);
        placeName.setText(photoList.get(position).getLocationName());
        container.addView(view);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isScrapped = isScrappedList.get(position);
                isScrappedList.set(position, !isScrapped);
                if(isScrapped){
                    imageButton.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                }
                else{
                    imageButton.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        });

        return view;
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
