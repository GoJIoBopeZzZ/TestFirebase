package com.innopolis.android.testfirebase.models;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlickrFetcher {

    /**
     * Created by _red_ on 06.07.17.
     */

    private static final String TAG = "FlickrFetcher";
    private static final String API_KEY = "18906764b6b90d6701628f0cb67bde4b";

    public String getJSONString(String UrlSpec) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(UrlSpec)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }

    public List<GaleryItem> fetchItems() {
        List<GaleryItem> galeryItems = new ArrayList<>();
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method","flickr.photos.getRecent")
                    .appendQueryParameter("api_key",API_KEY)
                    .appendQueryParameter("format","json")
                    .appendQueryParameter("nojsoncallback","1")
                    .appendQueryParameter("extras","url_s")
                    .build().toString();
            String jsonString = getJSONString(url);
            Log.e(TAG , jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(galeryItems, jsonBody);
        } catch (IOException ex) {
            Log.e(TAG , "Ошибка загрузки данных", ex);
        } catch (JSONException jex) {
            Log.e(TAG , "Ошибка парсинга JSON", jex);
        }

        return galeryItems;
    }

    private void parseItems(List<GaleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photosJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i < photosJsonArray.length(); i++) {
            JSONObject jsonObject = photosJsonArray.getJSONObject(i);
            GaleryItem item = new GaleryItem();
            item.setId(jsonObject.getString("id"));
            item.setCaption(jsonObject.getString("title"));

            if (!jsonObject.has("url_s")) continue;

            item.setUrl(jsonObject.getString("url_s"));
            items.add(item);
        }
    }
}
