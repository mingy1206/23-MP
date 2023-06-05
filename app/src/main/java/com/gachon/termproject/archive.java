package com.gachon.termproject;
//갤러리에서 이미지 가져오기
//출처 : https://wikidocs.net/99371
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
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
    private RelativeLayout imageContainer;

    private Button albumBtn, pictureBtn;
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
        ImageView imageView = new ImageView(requireContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (this.imageContainer.getWidth()/3, this.imageContainer.getHeight()/3);
        imageView.setLayoutParams(layoutParams);
        Glide.with(this.imageContainer).load(u).into(imageView);
        imageContainer.addView(imageView);
        imageView.bringToFront();
        imageViewCount++;
    }

}

