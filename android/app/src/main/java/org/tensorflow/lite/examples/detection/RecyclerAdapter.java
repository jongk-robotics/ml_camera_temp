package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        private Button button;
        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            button = itemView.findViewById(R.id.PHONEBUTTON);
            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        Log.d("RecyclerAdapter","good");
//                        Intent intent =new Intent(mContext,phonepopup.class);
//                        intent.putExtra("name", listData.get(pos).getName());
//                        intent.putExtra("number", listData.get(pos).getNumber());
//
//                        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                    }


                }
            });

        }

        void onBind(Data data) {
            textView1.setText(data.getName());
            textView2.setText(data.getNumber());
            button = itemView.findViewById(R.id.PHONEBUTTON);

        }

    }
}
