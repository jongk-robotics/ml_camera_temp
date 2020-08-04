package org.tensorflow.lite.examples.detection;

import android.Manifest;

import android.content.ContentProviderOperation;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
public class tab1 extends Fragment {
    //RECYCLER ADAPTER를 불러옴
    private RecyclerAdapter adapter;
//    public void mOnContactAdd(View v){
//        if(v.getId()!=R.id.btnContactAdd){ return; }
//        adapter.addItem(data);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        return view;
    }
}
