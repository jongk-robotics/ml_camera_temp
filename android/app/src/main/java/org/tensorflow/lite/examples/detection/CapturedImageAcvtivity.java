package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.tensorflow.lite.examples.detection.env.ImageUtils;
import org.tensorflow.lite.examples.detection.tflite.SimilarityClassifier;
import org.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class CapturedImageAcvtivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        PlaceNameRequest.processLocation{

    private Marker currentMarker = null;

    private final CommonConstants CC = new CommonConstants();

    private final String TAG = "CAPTURED";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // MobileFaceNet
    private static final int TF_OD_API_INPUT_SIZE = 112;
    private static final boolean TF_OD_API_IS_QUANTIZED = false;
    private static final String TF_OD_API_MODEL_FILE = "mobile_face_net.tflite";

    private static  boolean checknames = false;

    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt";

    private static final boolean MAINTAIN_ASPECT = false;

    //recogintion array
    RecognitionArray recognitionArray = new RecognitionArray();

    private enum DetectorMode {
        TF_OD_API;
    }

    private String inputName;

    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;

    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    Location mCurrentLocation;
    LatLng currentPosition;

    //place api 관련
    ArrayList<String> mCurrentLocationNames = new ArrayList<>();
    boolean isUpdatingPlaceNameFromFireBase;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;

    //bitmaps
    private Bitmap rgbFrameBitmap = null;
    private Bitmap capturedBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    // here the preview image is drawn in portrait way
    private Bitmap portraitBmp = null;
    // here the face is cropped and drawn
    private Bitmap faceBmp = null;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요합니다.
    // (참고로 Toast에서는 Context가 필요했습니다.)

    List<Marker> previous_marker = null;

    //얼굴을 추출하는 기능
    FaceDetector faceDetector;
    private SimilarityClassifier detector;

    //사진이 찍혔을 때 센서의 기울기
    private Integer sensorOrientation;

    //preview 크기
    int previewWidth;
    int previewHeight;

    //transform
    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    //사진이 셀카인지 후면인지 체크
    private boolean isFacingFront;

    //firebase
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // get current user info
    private final FirebaseUser user = fAuth.getCurrentUser();

    private ImageView mCapturedImageView;
    private TextView mCapturedTextView;
    private ImageButton mCaputuredBtn;
    private ListView mPlaceRecyclerView;
    private RecyclerView mFriendRecyclerView;
    private ImageButton addFriendBtn;

    //recycler view adapter
    private ArrayAdapter<String> mPlaceRecyclerViewAdapter;
    private FriendRecyclerViewAdapter mFriedRecyclerViewAdapter;

    private Activity mActivity;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    private FirebaseFirestore mFireStoreRef = FirebaseFirestore.getInstance();      // PLACE reference
    private FirebaseFirestore mFireStoreRef2 = FirebaseFirestore.getInstance();     // FRIENDS reference
    private FirebaseFirestore mFireStoreRef3 = FirebaseFirestore.getInstance();     // PHOTOS reference

    //장소 이름 가져오는 기능
    private PlaceNameRequest placeNameRequest;


    //이름 리스트
    private ArrayList<String> names = new ArrayList<>();

    @Override
    public void processLocationNames(ArrayList<String> nameList) {
        mCurrentLocationNames = nameList;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updatePlaceRecyclerView(nameList);
            }
        });
    }

//    @Override
//    public void onPlacesFailure(PlacesException e) {
//        isUpdatingFPlaceName = false;
//        e.printStackTrace();
//    }
//
//    @Override
//    public void onPlacesStart() {
//        isUpdatingPlaceName = true;
//    }
//
//    @Override
//    public void onPlacesSuccess(List<Place> places) {
//        mCurrentLocationNames = new ArrayList<>();
//
//        final String point = "point_of_interest";
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for (noman.googleplaces.Place place : places) {
//                    Log.d(TAG, "location: " + place.getLatitude() + ", " + place.getLongitude());
//                    Log.d(TAG, "name: " + place.getName());
//                    for(String type : place.getTypes())
//                    {
//                        String tempType = new String(type);
//                        if(type.equals(point))
//                        {
//                            mCurrentLocationNames.add(place.getName());
//                            break;
//                        }
//
//                    }
//                }
//
//                updatePlaceRecyclerView(mCurrentLocationNames);
//            }
//        });
//    }

//    @Override
//    public void onPlacesFinished() {
//        isUpdatingPlaceName = false;
//    }

//    public void showPlaceInformation(LatLng location)
//    {
//        if (previous_marker != null)
//            previous_marker.clear();//지역정보 마커 클리어
//
//        new NRPlaces.Builder()
//                .listener(CapturedImageAcvtivity.this)
//                .key(getString(R.string.places_api_key))
//                .latlng(location.latitude, location.longitude)//현재 위치
//                .radius(30) //500 미터 내에서 검색
//                .build()
//                .execute();
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image_acvtivity);

        //전체 화면 설정
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mCapturedImageView = findViewById(R.id.CapturedImageView);
        mCapturedTextView = findViewById(R.id.CapturedNames);
        mPlaceRecyclerView = findViewById(R.id.placeRecyclerView);
        mFriendRecyclerView = findViewById(R.id.friendRecyclerView);
        mCaputuredBtn = findViewById(R.id.CaptureSaveBtn);
        addFriendBtn = findViewById(R.id.addFriendBtn);

        mActivity = this;

        Intent intent =  getIntent();
        Uri uri = intent.getParcelableExtra("imageUri");
        previewWidth = intent.getIntExtra("previewWidth", 0);
        previewHeight = intent.getIntExtra("previewHeight", 0);
        sensorOrientation = intent.getIntExtra("sensorOrientation", 0);
        isFacingFront = intent.getBooleanExtra("isFacingFront", false);

        //recyclerViewAdapter
        ArrayList<String> initData = new ArrayList<>();
        initData.add("처리중입니다.");
        mPlaceRecyclerViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, initData);
        mPlaceRecyclerView.setAdapter(mPlaceRecyclerViewAdapter);

        mFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mFriedRecyclerViewAdapter = new FriendRecyclerViewAdapter(this);
        mFriendRecyclerView.setAdapter(mFriedRecyclerViewAdapter);

        //장소 이름 가져오는 기능 초기화
        String key = getString(R.string.places_api_key);
        String userEmail = user.getEmail();

        CollectionReference colRef = mFireStoreRef
                .collection("Users")
                .document(userEmail)
                .collection("Place");

        placeNameRequest = new PlaceNameRequest(key, this, colRef, 30);

        //face detector
        faceDetector = getFaceDetector();

        if(detector == null)
        {
            try{
                detector =
                        TFLiteObjectDetectionAPIModel.create(
                                getAssets(),
                                TF_OD_API_MODEL_FILE,
                                TF_OD_API_LABELS_FILE,
                                TF_OD_API_INPUT_SIZE,
                                TF_OD_API_IS_QUANTIZED);
            } catch (final IOException e) {
                e.printStackTrace();
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }

        Log.d(TAG, "oncreate");


        //비트맵 크기 조절
        int targetW, targetH;

        targetH = previewWidth;
        targetW = previewHeight;

        int cropW = (int) (targetW / 2.0);
        int cropH = (int) (targetH / 2.0);

        croppedBitmap = Bitmap.createBitmap(cropW, cropH, Bitmap.Config.ARGB_8888);
        portraitBmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888);
        faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Bitmap.Config.ARGB_8888);

        //transform 정의
        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropW, cropH,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        //이미지에서 얼굴 인식
        AnalyzeImage(uri);

        //이미지 띄우기
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Glide.with(mActivity)
                        .load(uri)
                        .into(mCapturedImageView);
            }
        });

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        CheckPermissions(); /* TODO splash에 위치 권한요청도 추가하기 */


        mCaputuredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentLocation != null && currentPosition != null)
                {
                    if(mCurrentLocationNames.isEmpty())
                    {
//                        if(!isUpdatingPlaceNameFromFireBase)
//                        {
//                            Log.d(TAG, "update place name");
//                            showPlaceInformation(currentPosition);
//                        }
                        Log.d(TAG, "update place name");
                        placeNameRequest.startGetNames(mCurrentLocation);
                        Toast.makeText(getApplicationContext(), "위치 정보를 받아 오고 있습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        uploadImage(uri);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "위치 정보를 받아 오고 있습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.id.layout_root));

                AlertDialog.Builder aDialog = new AlertDialog.Builder(mContext);//여기서buttontest는 패키지이름
                aDialog.setTitle("Is your friend not in the list?");
                aDialog.setView(layout);

                aDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                aDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog ad = aDialog.create();
                ad.show();
            }
        });
    }

    void updatePlaceRecyclerView(ArrayList<String> placeList)
    {
        mPlaceRecyclerViewAdapter.clear();
        mPlaceRecyclerViewAdapter.addAll(placeList);
        mPlaceRecyclerViewAdapter.notifyDataSetChanged();
    }

    void updateFriendRecyclerView(ArrayList<String> nameList)
    {
        final CommonConstants CC = new CommonConstants();
        final String userEmail = user.getEmail();
        final CollectionReference colRef = mFireStoreRef.collection("Users").document(userEmail).collection("Friend");



        colRef
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<FriendImage> friends = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = (String) document.get("name");
                            String url = (String) document.get("profileUrl");

                            if(nameList.contains(name)){
                                friends.add(new FriendImage(name, url));
                                Log.d(TAG, "name: " + name + "url: " + url);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mFriedRecyclerViewAdapter.setFriends(friends);
                                mFriedRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    void uploadFriends(HashMap<String, Bitmap> friends)
    {
        Log.d(TAG, "map size: " + friends.size());

        for(Map.Entry<String, Bitmap> entry : friends.entrySet())
        {
            uploadFriendProfile(entry.getKey(), entry.getValue());
        }
    }

    void uploadFriendProfile(String name, Bitmap bitmap)
    {
        //이미지 저장
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String fileName = "image_" + format + ".jpg";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] inputData = stream.toByteArray();

        Log.d(TAG, "length: " + inputData.length);

        // get image url
        final StorageReference riversRef = mStorageRef.child(fileName);
        final UploadTask uploadTask=riversRef.putBytes(inputData);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    //throw.task.getException();
                }

                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String imageUrl = String.valueOf(downloadUri);

                    FriendImage friend = new FriendImage(name, imageUrl);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFriedRecyclerViewAdapter.addItem(friend);
                            mFriedRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });

                    registerFriend(name, imageUrl);
                }else{

                }
            }
        });
    }

    void registerFriend(String name, String url)
    {
        String userEmail = user.getEmail();

        HashMap<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("profileUrl", url);
        data.put("count", 0);

        WriteBatch batch = mFireStoreRef.batch();
        mFireStoreRef
                .collection("Users")
                .document(userEmail)
                .collection("Friend")
                .document(name)
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });;

    }

    void uploadImage(Uri uri)
    {
        //이미지 저장
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String fileName = "image_" + format + ".jpg";

        byte[] inputData = new byte[0];

        try {
            InputStream iStream =   getContentResolver().openInputStream(uri);
            inputData = getBytes(iStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        final byte[] inputData2 = inputData;

        // get image url
        final StorageReference riversRef = mStorageRef.child(fileName);
        final UploadTask uploadTask=riversRef.putFile(uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    //throw.task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String imageUrl = String.valueOf(downloadUri);
                    uploadData(downloadUri);
                }else{

                }
            }
        });
    }

    void uploadData(Uri imgOfUrl)
    {
        String userEmail = user.getEmail();

        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
        String fileName = String.valueOf(sdf.format(day));

        //이미지 데이터
        Timestamp timeStamp = new Timestamp(new Date());

        final boolean isLiked = false;

        final GeoPoint location = new GeoPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        final Photo photo = new Photo();
        photo.setUserEmail(userEmail);
        photo.setFriends(names);
        photo.setShared(true);
        photo.setLocation(location);
        photo.setLocationName(mCurrentLocationNames.get(0));
        photo.setTimeStamp(timeStamp);
        photo.setUrl(imgOfUrl.toString());

        final HashMap<String, Object> photoData = photo.toMap();

        Log.d(TAG, "p url: " + imgOfUrl);

        WriteBatch batch = mFireStoreRef.batch();
        DocumentReference ImageRef =  mFireStoreRef
                .collection("Images")
                .document(fileName);

        batch.set(ImageRef, photoData);

        if(!names.isEmpty())
        {
            for(String name : names){
                DocumentReference FriendRef = mFireStoreRef
                        .collection("Users")
                        .document(userEmail)
                        .collection("Friend")
                        .document(name);

                DocumentReference FriendImageRef = FriendRef
                        .collection("Images")
                        .document(fileName);

                batch.set(FriendImageRef, photoData);

                HashMap<String, Object> data = new HashMap<>();
                data.put("count", FieldValue.increment(1));
                data.put("timeStamp", timeStamp);

                batch.update(FriendRef, data);
            }
        }

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
                Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Tab_Activity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });

//        if(names.isEmpty())
//        {
//
//        }
//        else{
//            // FRIENDS DB input
//
//            /*TODO 친구추가 어떻게 ??*/
//            String imgUrlFriends = new String();
//            final Friends friend = new Friends("Sally", photo);
//            final HashMap<String, Object> frienData = friend.toMap();
//
//            mFireStoreRef2
//                    .collection("Users")
//                    .document(userEmail)
//                    .collection("Friends")
//                    .document(friend.getName())
//                    .set(frienData)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(TAG, "DocumentSnapshot successfully updated!");
//
//                            mFireStoreRef2
//                                    .collection("Users")
//                                    .document(userEmail)
//                                    .collection("Friends")
//                                    .document(friend.getName())
//                                    .collection("Photo")
//                                    .document(photo.getUrl())
//                                    .set(photoData)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
//                                            Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(getApplicationContext(), Tab_Activity.class);
//                                            startActivity(intent);
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            Log.w(TAG, "Error updating document", e);
//                                        }
//                                    });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error updating document", e);
//                        }
//                    });
//        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        recognitionArray.saveToShared(getApplicationContext(), CC.RECOG_SHARED);

        Map<String, Object> data = new HashMap<>();
        data.put(CC.RECOG_FIELD, recognitionArray.toString());

        mFireStoreRef
                .collection(CC.USERS)
                .document(user.getEmail())
                .collection(CC.RECOG_COL)
                .document(CC.RECOG_DOC)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");

        if(detector == null)
        {
            try{
                detector =
                        TFLiteObjectDetectionAPIModel.create(
                                getAssets(),
                                TF_OD_API_MODEL_FILE,
                                TF_OD_API_LABELS_FILE,
                                TF_OD_API_INPUT_SIZE,
                                TF_OD_API_IS_QUANTIZED);
            } catch (final IOException e) {
                e.printStackTrace();
                Toast toast =
                        Toast.makeText(
                                getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }
        recognitionArray.loadFromShared(getApplicationContext(), CC.RECOG_SHARED);
        HashMap<String, SimilarityClassifier.Recognition> recognitionHashMap = recognitionArray.getRecognitions();
        Log.d(TAG, "size: " + recognitionHashMap.size());

        detector.setRegsisted(recognitionHashMap);
    }

    /*TODO Think about*/
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private String showAddFaceDialog(SimilarityClassifier.Recognition rec) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_edit_dialog, null);
        ImageView ivFace = dialogLayout.findViewById(R.id.dlg_image);
        TextView tvTitle = dialogLayout.findViewById(R.id.dlg_title);
        EditText etName = dialogLayout.findViewById(R.id.dlg_input);

        tvTitle.setText("누구의 얼굴인가요?");
        ivFace.setImageBitmap(rec.getCrop());
        etName.setHint("이름을 입력해주세요");

        final String[] name = {""};

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int i) {

                name[0] = etName.getText().toString();
                if (name[0].isEmpty()) {
                    return;
                }
                detector.register(name[0], rec);
                recognitionArray.addRecognition(name[0], rec);
                uploadFriendProfile(name[0], rec.getCrop());

//                names.add(name[0]);
                //knownFaces.put(name, rec);
                dlg.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int i) {

                dlg.dismiss();
            }
        });

        builder.setView(dialogLayout);
        builder.show();

        return name[0];
    }

    public void CheckPermissions() {

        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                        ActivityCompat.requestPermissions( CapturedImageAcvtivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
    }

    private FaceDetector getFaceDetector()
    {
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();


        FaceDetector detector = FaceDetection.getClient(options);

        return detector;
    }

    private void AnalyzeImage(Uri uri){

        Bitmap resource = BitmapFactory.decodeFile(uri.getPath());

        Matrix rotateMatrix = new Matrix();
        if(isFacingFront)
        {
            rotateMatrix.postRotate(90); //-360~360
        }
        else{
            rotateMatrix.postRotate(-90); //-360~360
        }


        Bitmap rotated = Bitmap.createBitmap(resource, 0, 0,
                resource.getWidth(), resource.getHeight(), rotateMatrix, true);

        rgbFrameBitmap = rotated.copy(Bitmap.Config.ARGB_8888, true);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        Log.d(TAG, "rgb: " + rgbFrameBitmap.getWidth() + ", " + rgbFrameBitmap.getHeight());
        Log.d(TAG, "sizecrop: " + croppedBitmap.getWidth() + ", " + croppedBitmap.getHeight());
        Log.d(TAG, "sensor: " + sensorOrientation);
        Log.d(TAG, "registed: " + detector.getRegistedNum());

        InputImage image = InputImage.fromBitmap(croppedBitmap, 0);

        Log.d(TAG, "read image");

        faceDetector
                .process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        Log.d(TAG, "process image");
                        if (faces.size() == 0) {
                            updateResults(0, new LinkedList<>());
                            return;
                        }

                        class ProcessImage extends AsyncTask<Void, Void, Void>{
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                onFacesDetected(0, faces, true,false);
                                return null;
                            }
                        }



                        ProcessImage task = new ProcessImage();
                        task.execute();
                    }
                });
    }

    private void updateResults(long currTimestamp, final List<SimilarityClassifier.Recognition> mappedRecognitions) {
        if (mappedRecognitions.size() > 0) {
            Log.d(TAG, "not zero");
            //왜 첫번째 꺼를 할까??????

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> recordedNames = new ArrayList<>();
                    ArrayList<String> newNames = new ArrayList<>();
                    HashMap<String, Bitmap> newFriends = new HashMap<>();

                    for(SimilarityClassifier.Recognition record : mappedRecognitions)
                    {
                        if (record.getExtra() != null) {
                            Log.d(TAG, record.toString());
                            if(record.getDistance() < 1.0f && record.getDistance() > 0.0f){
                                Log.d(TAG, "it is recorded: " + record.getTitle());
                                recordedNames.add(record.getTitle());
                            }
                            else {
                                Log.d(TAG, "it is not recorded");

                                showAddFaceDialog(record);

//                                Log.d(TAG, "name: " + name);
//                                if(!name.isEmpty())
//                                {
//                                    recordedNames.add(name);
//                                    newFriends.put(name, Bitmap.createBitmap(record.getCrop()));
//                                }
                            }
                        }
                    }

                    names = recordedNames;

                    uploadFriends(newFriends);
                    updateFriendRecyclerView(recordedNames);
                }
            });

        }
        else{
            Log.d(TAG, "zero");
        }
    }

    // Face Processing
    private Matrix createTransform(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation) {

        Matrix matrix = new Matrix();
        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
                Log.d(TAG, "Rotation of " + applyRotation + " % 90 != 0");
            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }

        if (applyRotation != 0) {

            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;

    }


    //얼굴을 인식해주는 중요한 부분
    private void onFacesDetected(long currTimestamp, List<Face> faces, boolean add,boolean check) {

        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
        final Canvas canvas = new Canvas(cropCopyBitmap);
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        final List<SimilarityClassifier.Recognition> mappedRecognitions =
                new LinkedList<SimilarityClassifier.Recognition>();

        Log.d(TAG, "faces: " + faces.size());
        //final List<Classifier.Recognition> results = new ArrayList<>();

        // Note this can be done only once
        int sourceW = rgbFrameBitmap.getWidth();
        int sourceH = rgbFrameBitmap.getHeight();
        int targetW = portraitBmp.getWidth();
        int targetH = portraitBmp.getHeight();
        Matrix transform = createTransform(
                sourceW,
                sourceH,
                targetW,
                targetH,
                sensorOrientation);
        final Canvas cv = new Canvas(portraitBmp);

        // draws the original image in portrait mode.
        cv.drawBitmap(rgbFrameBitmap, transform, null);

        final Canvas cvFace = new Canvas(faceBmp);

        boolean saved = false;
        ArrayList<String> labels = new ArrayList<>();
        for (Face face : faces) {
            Log.d("face", face.toString());
            //results = detector.recognizeImage(croppedBitmap);

            final RectF boundingBox = new RectF(face.getBoundingBox());

            //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
            final boolean goodConfidence = true; //face.get;
            if (boundingBox != null && goodConfidence) {

                // maps crop coordinates to original
                cropToFrameTransform.mapRect(boundingBox);

                // maps original coordinates to portrait coordinates
                RectF faceBB = new RectF(boundingBox);
                transform.mapRect(faceBB);

                // translates portrait to origin and scales to fit input inference size
                //cv.drawRect(faceBB, paint);
                float sx = ((float) TF_OD_API_INPUT_SIZE) / faceBB.width();
                float sy = ((float) TF_OD_API_INPUT_SIZE) / faceBB.height();
                Matrix matrix = new Matrix();
                matrix.postTranslate(-faceBB.left, -faceBB.top);
                matrix.postScale(sx, sy);

                cvFace.drawBitmap(portraitBmp, matrix, null);

                //canvas.drawRect(faceBB, paint);

                String label = "";
                float confidence = -1f;
                Integer color = Color.BLUE;
                Object extra = null;
                Bitmap crop = null;

                if (add) {
                    if(portraitBmp.getWidth() < faceBB.left + faceBB.width()
                        || portraitBmp.getHeight() < faceBB.top + faceBB.height())
                    {
                        continue;
                    }

                    crop = Bitmap.createBitmap(portraitBmp,
                            (int) faceBB.left,
                            (int) faceBB.top,
                            (int) faceBB.width(),
                            (int) faceBB.height());
                }





                //여기서 정확도를 측정?해주는 거 같다!
                final List<SimilarityClassifier.Recognition> resultsAux = detector.recognizeImage(faceBmp, add);

                if (resultsAux.size() > 0) {

                    SimilarityClassifier.Recognition result = resultsAux.get(0);

                    extra = result.getExtra();

                    //distance가 뭘 말하는 걸까?? 진짜거리? 아니면 정확도에서 먼 거리???
                    float conf = result.getDistance();
                    if (conf < 1.0f) {

                        confidence = conf;
                        label = result.getTitle();
                        Log.d("#@$@#$@", label);
                        labels.add(label);
                        //tagging을 해둔 이름(사람)에 포함될 때 녹색 박스를 그려주고 아니면 빨간 박스를 그려준다
                        if (result.getId().equals("0")) {
                            color = Color.GREEN;
                        } else {
                            color = Color.RED;
                        }
                    }

                }

                if (isFacingFront) {

                    // camera is frontal so the image is flipped horizontally
                    // flips horizontally
                    Matrix flip = new Matrix();
                    if (sensorOrientation == 90 || sensorOrientation == 270) {
                        flip.postScale(1, -1, previewWidth / 2.0f, previewHeight / 2.0f);
                    } else {
                        flip.postScale(-1, 1, previewWidth / 2.0f, previewHeight / 2.0f);
                    }
                    //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
                    flip.mapRect(boundingBox);

                }
                //여기서는 detector.recognizeImage(faceBmp, add)요기 부터의 코드를 통해 얻어진 값을 넣어주는 거 같다!
                final SimilarityClassifier.Recognition result = new SimilarityClassifier.Recognition(
                        "0", label, confidence, boundingBox);

                result.setColor(color);
                result.setLocation(boundingBox);
                result.setExtra(extra);
                result.setCrop(crop);
                mappedRecognitions.add(result);

            }
        }
        updateResults(currTimestamp, mappedRecognitions);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);



            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());              /*TODO currentPosition.latitude & currentPosition.longitude 나타내주기*/
                mCurrentLocation = location;

                Log.d(TAG, "location updated");
            }
        }
    };

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {
            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            //mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {
            return true;
        }

        return false;

    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                        needRequest = true;

                        return;
                    }
                }

                break;
        }
    }


}