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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search extends Fragment {
    private Button change_btn1;
    private Fragment change_fragment;
    private Button save_btn1;
    private Bitmap saveImage;
    private String title;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("Users");
    public search() {


    }

    public static search newInstance() {
        return new search();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // 여기부터 내가 직접 적용한 내용분임. 변수값이랑 각종 함수 복사 붙여넣기 하면 될 것.
    // 레이아웃 변수
    EditText searchKeyword;
    Button searchButton;
    ImageView resImage;
    TextView resTitle, resAuthor, resID;

    // DB에 넣을 수도 있는 값
    String[] mappedAlbumId = new String[2]; // 랜덤 샘플링 직후의 ID와 썸네일 링크 저장

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        save_btn1 =(Button) rootView.findViewById(R.id.saveButton1);
        searchKeyword = (EditText) rootView.findViewById(R.id.searchKeyword);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        resImage = (ImageView) rootView.findViewById(R.id.searchResultImage);
        resTitle = (TextView) rootView.findViewById(R.id.searchResultTitle);
        resAuthor = (TextView) rootView.findViewById(R.id.searchResultAuthor);
        resID = (TextView) rootView.findViewById(R.id.searchResultId);
        change_btn1=(Button) rootView.findViewById(R.id.go_to_random1);
        change_fragment=new search_random();

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
                        title=searchArrayReturn[1];
                        resTitle.setText(searchArrayReturn[1]);
                        resAuthor.setText(searchArrayReturn[2]);
                        resID.setText(searchArrayReturn[0]);
                    });
                    new DownloadFilesTask().execute(searchArrayReturn[3]);
                }).start();
            }
        });

        change_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((FrameActivity)getActivity()).loadFragment(change_fragment);
            }
        });

        save_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ((FrameActivity)getActivity()).valueOfID();
                String temp = BitMapToString.change(saveImage);
                FirebaseArrayUpdater arrayUpdater = new FirebaseArrayUpdater(id);
                arrayUpdater.addValue(temp);

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
                saveImage = bmp;
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
        protected void saveImage(){

        }
    }
}