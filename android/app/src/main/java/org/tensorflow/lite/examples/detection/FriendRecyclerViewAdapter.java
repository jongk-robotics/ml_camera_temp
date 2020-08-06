package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRecyclerViewAdapter extends RecyclerView.Adapter<FriendRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    //vars
//    private ArrayList<String> mNames = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<FriendImage> friends = new ArrayList<>();

    private Context mContext;

    public FriendRecyclerViewAdapter(Context context, ArrayList<FriendImage> friends) {
        this.friends = friends;
        mContext = context;
    }

    public FriendRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    public void setFriends(ArrayList<FriendImage> friends) {
        this.friends = friends;
    }

    public void addItem(FriendImage friendImage)
    {
        friends.add(friendImage);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_recommend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(friends.get(position).getUrl())
                .into(holder.image);

        holder.name.setText(friends.get(position).getName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + friends.get(position).getName());
                Toast.makeText(mContext, friends.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.reco_image);
            name = itemView.findViewById(R.id.reco_name);
        }
    }
}
