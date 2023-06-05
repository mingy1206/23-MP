package com.gachon.termproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link archive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class archive extends Fragment {

    private ImageView picture;
    private Button albumBtn;
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


        albumBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}