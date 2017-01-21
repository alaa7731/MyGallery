package tigerspike.andorid.mygallery.mvp.fetcher;

import android.os.Bundle;

import java.util.ArrayList;

import tigerspike.andorid.mygallery.models.FlickerItem;
import tigerspike.andorid.mygallery.mvp.OnFinishedListener;
import tigerspike.andorid.mygallery.mvp.sync.FlickerImagesAsyncTask;

/**
 * Created by alaa.salah on 1/21/2017.
 */

public class ImageGalleryFetcher {

    public static ImageGalleryFetcher newInstance() {
        return new ImageGalleryFetcher();
    }

    public void getGalleryImages(String url, OnFinishedListener<ArrayList<FlickerItem>> listener) {
        FlickerImagesAsyncTask flickerImagesAsyncTask = new FlickerImagesAsyncTask(listener);
        flickerImagesAsyncTask.execute(url);
    }
}
