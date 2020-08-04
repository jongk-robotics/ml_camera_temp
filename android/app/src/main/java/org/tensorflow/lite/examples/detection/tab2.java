package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class tab2 extends Fragment {

    private GridView mgridView;
    private SharedPreferences sp;
//권한에 대한 응답이 있을때 작동하는 함수

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

//        mgridView = (GridView) view.findViewById(R.id.grid_view);
//        final Image_Adapter ia = new Image_Adapter(getActivity());
//        mgridView.setAdapter(ia);
//        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            public void onItemClick(AdapterView parent, View v, int position, long id){
//                ia.callImageViewer(position);
//            }
//        });
        return view;
    }
}
