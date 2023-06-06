package com.gachon.termproject;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;


public class search_random extends Fragment  implements SensorEventListener{
    private Button change_btn2;
    private Fragment change_fragment;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime;
    private Button save_btn2;
    private String saveImage;


    public search_random() {
    }

    public static search_random newInstance() {
        return new search_random();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // 여기부터 내가 직접 적용한 내용분임. 변수값이랑 각종 함수 복사 붙여넣기 하면 될 것.
    // 레이아웃 변수
    EditText searchKeyword;
    Button searchButton, searchImageId;
    ImageView resImage;
    TextView resTitle, resAuthor, resID;

    // DB에 넣을 수도 있는 값
    String[] mappedAlbumId = new String[2]; // 랜덤 샘플링 직후의 ID와 썸네일 링크 저장

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //shake 기능을 위한 선언
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastShakeTime=0;

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_random, container, false);
        save_btn2 =(Button) rootView.findViewById(R.id.saveButton2);
        searchKeyword = (EditText) rootView.findViewById(R.id.searchKeyword);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchImageId = (Button) rootView.findViewById(R.id.saveButton2);
        resImage = (ImageView) rootView.findViewById(R.id.searchResultImage);
        resTitle = (TextView) rootView.findViewById(R.id.searchResultTitle);
        resAuthor = (TextView) rootView.findViewById(R.id.searchResultAuthor);
        resID = (TextView) rootView.findViewById(R.id.searchResultId);
        change_btn2=(Button) rootView.findViewById(R.id.go_to_title2);
        change_fragment=new search();


        change_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((FrameActivity)getActivity()).loadFragment(change_fragment);
            }
        });
        save_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ((FrameActivity)getActivity()).valueOfID();
                FirebaseArrayUpdater arrayUpdater = new FirebaseArrayUpdater(id);
                arrayUpdater.addValue(saveImage);
                Toast.makeText(getContext(), "save successful", Toast.LENGTH_SHORT).show();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener( this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShakeTime >= 300){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = sqrt(abs(x) +abs(y) +abs(z));

            if(acceleration>(5.8)){
                new Thread(() -> {
                    int loopCount = 0;
                    int randId;
                    String searchJsonReturn = null;
                    do {
                        loopCount += 1;
                        randId = (int) Math.round(Math.random() * 600000) + 400000;
                        searchJsonReturn = ManiaDBConnector.getJsonOfAlbumId(Integer.toString(randId));
                    } while (searchJsonReturn == null && loopCount < 5);

                    String thumbnail = JsonParserHelper.getThumbnailFromJson(searchJsonReturn);
                    String finalRandId = Integer.toString(randId);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        resID.setText(finalRandId);
                    });
                    new DownloadFilesTask().execute(thumbnail);
                    mappedAlbumId[0] = finalRandId;
                    mappedAlbumId[1] = thumbnail;
                }).start();
            }
            lastShakeTime = currentTime;

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 이미지 불러오는 함수, 하단 PostExecute 부분의 resImage가 타겟 imageView임.
    private class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try{
                String img_url = strings[0];
                saveImage =img_url;
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch(IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            resImage.setImageBitmap(bitmap);
        }
    }
}