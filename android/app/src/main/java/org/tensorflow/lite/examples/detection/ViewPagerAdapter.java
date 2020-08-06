package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> imageList;
    private ArrayList<String> placeList;

    public ViewPagerAdapter(Context context, ArrayList<String> imageList,ArrayList<String> placeList)
    {
        this.mContext = context;
        this.imageList = imageList;
        this.placeList = placeList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    public void setPlaceList(ArrayList<String> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpager_activity, null);
        TextView placeName= view.findViewById(R.id.commuPlaceName);
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(mContext).load(imageList.get(position)).into(imageView);
        placeName.setText(placeList.get(position));
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
