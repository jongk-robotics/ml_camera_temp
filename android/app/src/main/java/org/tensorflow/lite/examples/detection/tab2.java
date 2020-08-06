package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class tab2 extends AppCompatActivity {

    //RECYCLER ADAPTER를 불러옴
    private RecyclerAdapter adapter;
//    public void mOnContactAdd(View v){
//        if(v.getId()!=R.id.btnContactAdd){ return; }
//        adapter.addItem(data);
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab2);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        ArrayList name =new ArrayList();
        ArrayList<String> profile  = new ArrayList<>();

//여기서 firebase에서 데이터 가져와서 넣음

        for (int i = 0; i < name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setName((String) name.get(i));
            data.setProfile((String) profile.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
//        Bitmap bm =BitmapFactory.decodeResource(getResources(), R.drawable.profile);

//        Data data = new Data();
//        data.setName("종서");
//        data.setTime("4일전");
//        data.setProfile(bm);
//        Data data2 = new Data();
//        data2.setName("승현");
//        data2.setTime("3시간전");
//
//        data2.setProfile(bm);
//
//        // 각 값이 들어간 data를 adapter에 추가합니다.
//        adapter.addItem(data);
//        adapter.addItem(data2);
        // adapter의 값이 변경되었다는 것을 알려줍니다.
//        adapter.notifyDataSetChanged();
    }
}
