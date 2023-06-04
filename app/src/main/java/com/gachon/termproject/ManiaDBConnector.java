package com.gachon.termproject;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManiaDBConnector {
    private static String API_KEY = "kshswel@naver.com";

    // AlbumID를 넣으면 그에 해당하는 json 파일로 변환하여 return
    public static String getJsonOfAlbumId(String albumID) {
        String requestURI = "http://www.maniadb.com/api/album/" +
                albumID + "/?key=" +
                API_KEY + "&v=0.5";

        String finalResult = null;

        try{
            URL url = new URL(requestURI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // URI에 대한 응답을 받는 부분
            int responseCode = connection.getResponseCode(); // 응답코드
            StringBuilder dbResponse = new StringBuilder(); //

            // 제대로 된 응답이 왔을 경우 (HTTP 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    dbResponse.append(line);
                    Log.i("test1",line);
                }
                reader.close();
            }
            else {
                Log.e("test1","Connection Error : " + responseCode);
                return null;
            }

            // XML 결과값 출력
            Log.i("data","XML Response:");
            Log.i("data", dbResponse.toString());

            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(dbResponse.toString());

            ObjectMapper jsonMapper = new ObjectMapper();
            String jsonResponse = jsonMapper.writeValueAsString(jsonNode);

            Log.i("data","Json Response:");
            Log.i("data", jsonResponse);

            finalResult = jsonResponse;

        } catch (Exception | Error e){
            e.printStackTrace();
        }

        return finalResult;
    }

    public static String getJsonOfSearch(String keyword, String sr, int display) {
        String requestURI = "http://www.maniadb.com/api/search/" +
                keyword + "/?sr=" +
                sr + "&display=" +
                display + "&key=" +
                API_KEY + "&v=0.5";

        String finalResult = null;

        try {
            URL url = new URL(requestURI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // URI에 대한 응답을 받는 부분
            int responseCode = connection.getResponseCode(); // 응답코드
            StringBuilder dbResponse = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    dbResponse.append(line);
                    Log.i("test2",line);
                }
                reader.close();
            }
            else {
                Log.e("test2","Connection Error : " + responseCode);
                return "Connection Error : " + responseCode;
            }

            // XML 결과값 출력
            Log.i("data2","XML Response:");
            Log.i("data2", dbResponse.toString());

            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(dbResponse.toString());

            ObjectMapper jsonMapper = new ObjectMapper();
            String jsonResponse = jsonMapper.writeValueAsString(jsonNode);

            Log.i("data2","Json Response:");
            Log.i("data2", jsonResponse);

            finalResult = jsonResponse;

        }catch (NullPointerException e){
            e.printStackTrace();
        } catch (Exception | Error e){
            e.printStackTrace();
        }

        return finalResult;
    }

}
