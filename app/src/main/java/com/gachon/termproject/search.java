package com.gachon.termproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public search() {


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search.
     */
    // TODO: Rename and change types and number of parameters
    public static search newInstance(String param1, String param2) {
        search fragment = new search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        searchKeyword = (EditText) rootView.findViewById(R.id.searchKeyword);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchImageId = (Button) rootView.findViewById(R.id.searchImageId);
        resImage = (ImageView) rootView.findViewById(R.id.searchResultImage);
        resTitle = (TextView) rootView.findViewById(R.id.searchResultTitle);
        resAuthor = (TextView) rootView.findViewById(R.id.searchResultAuthor);
        resID = (TextView) rootView.findViewById(R.id.searchResultId);

        // 검색 키워드 : searchKeyword, 버튼 누를 시
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    String keyword = searchKeyword.getText().toString();
                    String searchJsonReturn = ManiaDBConnector.getJsonOfSearch(keyword, "album", 2);
                    String[] searchArrayReturn = JsonParserHelper.getSearchResultFromJson(searchJsonReturn);

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(() -> {
                        resTitle.setText(searchArrayReturn[1]);
                        resAuthor.setText(searchArrayReturn[2]);
                        resID.setText(searchArrayReturn[0]);
                    });
                    new DownloadFilesTask().execute(searchArrayReturn[3]);
                }).start();
            }
        });

        // 검색 키워드는 없고, 랜덤으로 사진 한 장 뽑아오는 알고리즘
        searchImageId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    // 이미지 불러오는 함수, 하단 PostExecute 부분의 resImage가 타겟 imageView임.
    private class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try{
                String img_url = strings[0];
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