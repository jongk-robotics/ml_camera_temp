package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.Reference;
import java.sql.Ref;
import java.util.ArrayList;

public class tab2 extends AppCompatActivity {

    private String TAG = "tab1";


    FirebaseFirestore db=FirebaseFirestore.getInstance();
    //firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private final FirebaseUser user = fAuth.getCurrentUser();
    //firestore
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();

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
        CollectionReference collRef = db.collection("Users").document(user.getEmail())
                .collection("Friend");

        ArrayList<Data> dataList = new ArrayList<>();

        collRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Data data = new Data();
                                data.setProfile(document.get("profileUrl").toString());
                                data.setName(document.get("name").toString());
                                data.setCloseCount((Long) document.get("count")*10);
                                Log.d(TAG, "items: " + document.get("profileUrl"));

                                adapter.addItem(data);
                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ImageAdapter.setPhotos(photos);
                                adapter.notifyDataSetChanged();
                                //Log.d(TAG, "items: " + adapter.getCount());
                            }
                        });

                    }
                });

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        //adapter.notifyDataSetChanged();
    }
}
