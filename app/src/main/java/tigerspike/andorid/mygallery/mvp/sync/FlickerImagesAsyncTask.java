package tigerspike.andorid.mygallery.mvp.sync;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import tigerspike.andorid.mygallery.models.FlickerItem;
import tigerspike.andorid.mygallery.mvp.OnFinishedListener;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class FlickerImagesAsyncTask extends AsyncTask<String, Void, JSONObject> {
    OnFinishedListener<ArrayList<FlickerItem>> listener;

    public FlickerImagesAsyncTask(OnFinishedListener<ArrayList<FlickerItem>> listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setDoOutput(true);

            conn.connect();

            int status = conn.getResponseCode();
            String response = null;
            if (status == 200 || status == 201) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                response = sb.toString();

                Log.e("Response", "===============================================");
                Log.e("Response", response);
                Log.e("Response", "================================================");

            }

            return new JSONObject(getJSONObject(response));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getJSONObject(String response) {
        int index = "jsonFlickrFeed".length() + 1;

        return response.substring(index, response.length() - 1);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        String message = "Sorry, system error\nPlease try again later";
        try {
            if (jsonObject != null) {
                if (jsonObject.has("items")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        listener.onSuccess(createImagesGallery(jsonArray));
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listener.onFailure(message);
    }

    private ArrayList<FlickerItem> createImagesGallery(JSONArray jsonArray) {
        ArrayList<FlickerItem> arrayListImages = new ArrayList<>();
        FlickerItem item;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                item = new FlickerItem();
                item.setTitle(jsonObject.optString("title"));
                item.setAuthor(jsonObject.optString("author"));
                item.setAuthorID(jsonObject.optString("author_id"));
                item.setDateTaken(jsonObject.optString("date_taken"));
                item.setLink(jsonObject.optString("link"));
                item.setDescription(jsonObject.optString("description"));
                item.setMedia(getMedia(jsonObject.getJSONObject("media")));
                item.setPublished(jsonObject.optString("published"));
                item.setTags(jsonObject.optString("tags"));

                arrayListImages.add(item);
            }
            return arrayListImages;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMedia(JSONObject media) {
        return media.optString("m");
    }
}
