package com.gachon.termproject;
//갤러리에서 이미지 가져오기
//출처 : https://wikidocs.net/99371
//intent 참고
//출처 : https://velog.io/@soyoung-dev/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%84%EB%8B%AC
//permission check 정책변환
//출처 : https://stackoverflow.com/questions/33472723/can-not-find-symbol-manifest-permission-write-external-storage-on-v23
//permission 변경 정책변환 및 api 설정에 따른 변화
//출처 : https://stackoverflow.com/questions/64221188/write-external-storage-when-targeting-android-10
//수동 permission 방법
//출처 : https://changwoos.tistory.com/717
//이미지 업데이트
//출처 : https://hwanschoi.tistory.com/128
import static android.app.Activity.RESULT_OK;


import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class archive extends Fragment {
    private int imageViewCount = 0;
    public static String url;
    private ImageView picture;
    private ImageView album;
    private RelativeLayout imageContainer;
    private Button albumBtn, pictureBtn,capturebtn;
    private int size;
    Vibrator mVibe;

    public archive() {
        // Required empty public constructor
    }

    public static archive newInstance(String param1, String param2) {
        return new archive();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_archive, container, false);
        size =3;
        albumBtn =(Button)rootView.findViewById(R.id.album_btn);
        picture =(ImageView)rootView.findViewById(R.id.pictucre);
        pictureBtn=(Button) rootView.findViewById(R.id.picture_btn);
        imageContainer = (RelativeLayout) rootView.findViewById(R.id.imageContainer);
        mVibe = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        capturebtn=(Button)rootView.findViewById(R.id.capturebtn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(getActivity(), "안드로이드 버전 정책에 의하여 " +
                    "\n설정 -> 어플리케이션에서 사진 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
        }

        capturebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
                // requestStoragePermission();
            }
        });

        albumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                String id = ((FrameActivity)getActivity()).valueOfID();
                intent.putExtra("id", id);
                startActivityForResult(intent, 2);
            }
        });

        pictureBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);            }
        });
        imageContainer.setOnTouchListener(new View.OnTouchListener() {
            float previousX = 0;
            float previousY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (album == null) {
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int touchX = (int) event.getX();
                        int touchY = (int) event.getY();

                        float deltaX = touchX - previousX;
                        float deltaY = touchY - previousY;

                        // Move the album view based on the touch movement
                        album.setTranslationX(album.getTranslationX() + deltaX);
                        album.setTranslationY(album.getTranslationY() + deltaY);

                        previousX = touchX;
                        previousY = touchY;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
        return rootView;

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode==RESULT_OK) {
                    Uri uri = data.getData();
                    picture.setImageURI(uri);
                    break;
                }
            case 2:
                if (resultCode==RESULT_OK) {
                    String url = data.getStringExtra("return");
                    Log.d("getStringExtra", url);
                    createNewImageView(url);
                    break;
                }
                break;
        }
    }
    public void createNewImageView(String u) {
        album = new ImageView(requireContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (this.imageContainer.getWidth()/size, this.imageContainer.getHeight()/size);
        album.setLayoutParams(layoutParams);
        Glide.with(this.imageContainer).load(u).into(album);
        imageContainer.addView(album);
        album.bringToFront();
        imageViewCount++;
    }

    private void saveImage() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/TermProject";
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 파일 이름을 현재 시간으로 생성
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(directory, fileName);

        try {
            Bitmap bitmap = Bitmap.createBitmap(imageContainer.getWidth(), imageContainer.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            imageContainer.draw(canvas);

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            Toast.makeText(getActivity(), "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("file 위치", file.getPath());
            Toast.makeText(getActivity(), "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "이미지 저장 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}

