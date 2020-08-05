/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.tensorflow.lite.examples.detection.tflite.SimilarityClassifier;
import org.tensorflow.lite.examples.detection.tflite.TFLiteObjectDetectionAPIModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class MainActivity extends Activity {

    //display size
    int mDpWidth;
    int mDpHeight;


    // MobileFaceNet
    private static final int TF_OD_API_INPUT_SIZE = 112;
    private static final boolean TF_OD_API_IS_QUANTIZED = false;
    private static final String TF_OD_API_MODEL_FILE = "mobile_face_net.tflite";

    private static  boolean checknames = false;

    private static final String TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt";

    private SimilarityClassifier ImageDetector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap1 = null;
    private Bitmap croppedBitmap1 = null;
    private Bitmap rgbFrameBitmap2 = null;
    private Bitmap croppedBitmap2 = null;
    private Bitmap rgbFrameBitmap3 = null;
    private Bitmap croppedBitmap3 = null;
    private Bitmap rgbFrameBitmap4 = null;
    private Bitmap rgbFrameBitmap5 = null;

    private long timestamp = 0;

    // Face detector
    private FaceDetector faceDetector;

    //인식된 사람들의 이름 리스트
    private ArrayList<String> nameList = new ArrayList<>();

    private ImageView bshView;
    private ImageView IUView;
    private ImageView realBshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //여기서 fab add 버튼을 누르면 사람을 태깅할 수 있다
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //핸드폰 화면 크기 구하기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        mDpWidth = size.x;
        mDpHeight = size.y;

        // Real-time contour detection of multiple faces
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                        .build();


        FaceDetector detector = FaceDetection.getClient(options);

        faceDetector = detector;

        try{
            ImageDetector =
                    TFLiteObjectDetectionAPIModel.create(
                            getAssets(),
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_IS_QUANTIZED);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inPreferredConfig = Bitmap.Config.ARGB_8888;

        rgbFrameBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.iu1, ops);
        rgbFrameBitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.iu2, ops);
        rgbFrameBitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.iu3, ops);
        rgbFrameBitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.iu4, ops);
        rgbFrameBitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.iu5, ops);


        bshView.setImageBitmap(rgbFrameBitmap1);
        IUView.setImageBitmap(rgbFrameBitmap2);
        realBshView.setImageBitmap(rgbFrameBitmap3);


        processImage(bshView, rgbFrameBitmap1, true);
        processImage(IUView, rgbFrameBitmap2, true);
        processImage(realBshView, rgbFrameBitmap3, true);
        processImage(realBshView, rgbFrameBitmap4, true);
        processImage(realBshView, rgbFrameBitmap5, false);


    }



    public void processImage(ImageView view, Bitmap bitmap, boolean addPending) {
        ++timestamp;
        final long currTimestamp = timestamp;

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        faceDetector
                .process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        onFacesDetected(view, bitmap, currTimestamp, faces, addPending);
                    }
                });
    }


    //얼굴을 인식해주는 중요한 부분
    private void onFacesDetected(ImageView view, Bitmap bitmap, long currTimestamp, List<Face> faces, boolean add) {

        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        if(!faces.isEmpty())
        {
            Face face = faces.get(0);
            Log.d("MAINACT", face.toString());

            Rect rect = face.getBoundingBox();
            Canvas c = new Canvas(copy);
            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStyle(Style.STROKE);
            p.setStrokeWidth(2.0f);
            c.drawRect(rect, p);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(copy);
                }
            });

            Log.d("MAIN", "copy size: " + copy.getWidth() + ", " + copy.getHeight());
            Log.d("MAIN", "rect size: " + rect.left + ", " + rect.top + ", " + rect.width() + ", " + rect.height());

            Bitmap result;
            Bitmap crop = Bitmap.createBitmap(copy, rect.left, rect.top, rect.width(), rect.height());
            if(crop.getWidth() > TF_OD_API_INPUT_SIZE){
                int width = TF_OD_API_INPUT_SIZE;
                int height = width * crop.getHeight() / crop.getWidth();
                result = Bitmap.createScaledBitmap(crop, width, height, true);
            }
            else{
                result = croppedBitmap1;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(result);
                }
            });


            if(add){
                final List<SimilarityClassifier.Recognition> resultsAux = ImageDetector.recognizeImage(result, true);
                if(!resultsAux.isEmpty()){
                    Log.d("DETECT", "register");
                    ImageDetector.register("iu1", resultsAux.get(0));
                }
            }
            else{
                final List<SimilarityClassifier.Recognition> resultsAux2 = ImageDetector.recognizeImage(result, false);
                if(!resultsAux2.isEmpty()){
                    SimilarityClassifier.Recognition rec =  resultsAux2.get(0);
                    Log.d("DETECT", "rec: " + rec.toString());
                }
            }
        }
    }
}
