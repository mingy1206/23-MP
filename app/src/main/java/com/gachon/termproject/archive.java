package com.gachon.termproject;
//갤러리에서 이미지 가져오기
//출처 : https://wikidocs.net/99371
//intent 참고
//출처 : https://velog.io/@soyoung-dev/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%84%EB%8B%AC
import static android.app.Activity.RESULT_OK;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

public class archive extends Fragment {
    private int imageViewCount = 0;
    public static String url;
    private ImageView picture;
    private ImageView album;
    private RelativeLayout imageContainer;
    private Button albumBtn, pictureBtn;
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

        albumBtn =(Button)rootView.findViewById(R.id.album_btn);
        picture =(ImageView)rootView.findViewById(R.id.pictucre);
        pictureBtn=(Button) rootView.findViewById(R.id.picture_btn);
        imageContainer = (RelativeLayout) rootView.findViewById(R.id.imageContainer);
        mVibe = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        albumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
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
                (this.imageContainer.getWidth()/4, this.imageContainer.getHeight()/4);
        album.setLayoutParams(layoutParams);
        Glide.with(this.imageContainer).load(u).into(album);
        imageContainer.addView(album);
        album.bringToFront();
        imageViewCount++;
    }


}

