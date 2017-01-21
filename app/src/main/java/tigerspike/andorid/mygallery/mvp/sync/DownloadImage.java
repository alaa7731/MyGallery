package tigerspike.andorid.mygallery.mvp.sync;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView ivImage;
    Context context;
    String id;

    public DownloadImage(Context context, ImageView ivImage, String id) {
        this.ivImage = ivImage;
        this.context = context;
        this.id = id;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new java.net.URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            ivImage.setImageBitmap(bitmap);

            saveImage(bitmap);
        }
    }

    private void saveImage(Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            File fileDir = new File(context.getFilesDir() + "/saved images");
            fileDir.mkdirs();

            File file = new File(fileDir, id);

            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
