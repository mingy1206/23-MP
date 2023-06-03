package com.gachon.termproject;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParserHelper {

    public static String getThumbnailFromJson(String originalJson) {
        try{
            JSONObject jsonObject = new JSONObject(originalJson);
            String jsonChannel = jsonObject.getString("channel");
            JSONObject jsonObject1 = new JSONObject(jsonChannel);
            String jsonItem = jsonObject1.getString("item");
            JSONObject jsonObject2 = new JSONObject(jsonItem);

            return jsonObject2.getString("thumnail");
        } catch (JSONException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public static String[] getSearchResultFromJson(String originalJson) {

        // 리턴 순서는 ID, 이름, 작곡가명, 썸네일 URL 순서로 제공됩니다.
        String[] returnResult = new String[4];

        try{
            JSONObject jsonObject = new JSONObject(originalJson);
            String jsonChannel = jsonObject.getString("channel");
            JSONObject jsonObject2 = new JSONObject(jsonChannel);
            JSONArray jsonArray = jsonObject2.getJSONArray("item");

            JSONObject item = jsonArray.getJSONObject(0);
            returnResult[0] = item.getString("id");
            returnResult[1] = item.getString("title");
            returnResult[2] = item.getJSONObject("artist").getString("name");
            returnResult[3] = item.getString("thumnail");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnResult;
    }
}