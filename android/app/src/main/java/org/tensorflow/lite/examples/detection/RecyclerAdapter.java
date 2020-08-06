package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();
    private Context mContext;
    RecyclerAdapter(Context c){
        mContext = c;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView1;
        private TextView textView2;
        private CircleImageView profile_view;
        private int Image_num;
        private Button button;
        ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Friend_gallery.class);

                    //여기서 어떤 사람을 선택했는지 전달 !


                    mContext.startActivity(intent);
                }
            });

            textView1 = itemView.findViewById(R.id.friend_name);
            textView2 = itemView.findViewById(R.id.timestamp);
            profile_view = itemView.findViewById(R.id.profile_image);
//            button = itemView.findViewById(R.id.PHONEBUTTON);
//            button.setOnClickListener(new Button.OnClickListener() {
//                public void onClick(View v) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        Log.d("RecyclerAdapter","good");
////                        Intent intent =new Intent(mContext,phonepopup.class);
////                        intent.putExtra("name", listData.get(pos).getName());
////                        intent.putExtra("number", listData.get(pos).getNumber());
////
////                        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//                    }
//
//
//                }
//            });
            final Integer[] progr = {0};
            Button button_incr = itemView.findViewById(R.id.button_incr);
            Button button_decr = itemView.findViewById(R.id.button_decr);
//            final ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);



            //여기서 버튼 대신에 이미지 계수 받아서 계산하면 될듯..?
            //Integer Image_num = 디비에서 이미지 개수 가져오기, 여기서 한 번에 가져오는 방법 생각해야 할 듯?  ;
            //progress.setProgress(Image_num);

//            button_incr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (progr[0] <= 90) {
//                        progr[0] += 10;
//                        progressBar.setProgress(progr[0]);
//                    }
//                }
//            });
//            button_decr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (progr[0] >= 10) {
//                        progr[0] -= 10;
//                        progressBar.setProgress(progr[0]);
//                    }
//                }
//            });


        }

        void onBind(Data data) {
            textView1.setText(data.getName());
            textView2.setText(data.getTime());
//            button = itemView.findViewById(R.id.PHONEBUTTON);

            ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
            progressBar.setProgress(data.getCloseCount().intValue());
            //uri-> glide
            Glide.with(mContext).load(data.getProfile()).into(profile_view);
        }

    }
}
