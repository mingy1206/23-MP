package com.gachon.termproject;
//갤러리에서 이미지 가져오기
//출처 : https://wikidocs.net/99371
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class archive extends Fragment {
    private int imageViewCount = 0;

    private ImageView picture;

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

        albumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                startActivity(intent);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    picture.setImageURI(uri);
                }
                break;
        }
    }
}

