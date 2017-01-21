package tigerspike.andorid.mygallery.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import tigerspike.andorid.mygallery.R;
import tigerspike.andorid.mygallery.adapters.ImageGalleryRecyclerViewAdapter;
import tigerspike.andorid.mygallery.models.FlickerItem;
import tigerspike.andorid.mygallery.mvp.fetcher.ImageGalleryFetcher;
import tigerspike.andorid.mygallery.mvp.presenter.ImageGalleryPresenter;
import tigerspike.andorid.mygallery.mvp.view.ImageGalleryContract;

public class ImagesGalleryActivity extends AppCompatActivity implements ImageGalleryContract.View {

    String images_url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json";

    RecyclerView rvGallery;
    ImageGalleryRecyclerViewAdapter imagesAdapter;
    ImageGalleryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        presenter = new ImageGalleryPresenter(this, ImageGalleryFetcher.newInstance());

        setReferences();

        getGalleryImages();
    }

    private void getGalleryImages() {
        presenter.getGalleryImages(images_url);
    }

    private void setReferences() {
        rvGallery = (RecyclerView) findViewById(R.id.rvGallery);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ImagesGalleryActivity.this, LinearLayoutManager.VERTICAL, false);
        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setHasFixedSize(true);
    }

    @Override
    public void onImagesLoaded(ArrayList<FlickerItem> flickerItems) {
        createAdapter(flickerItems);
    }

    private void createAdapter(ArrayList<FlickerItem> flickerItems) {
        imagesAdapter = new ImageGalleryRecyclerViewAdapter(ImagesGalleryActivity.this, flickerItems);
    }

    @Override
    public void onImagesLoadFailed(String errorMessage) {//showing error messages
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImagesGalleryActivity.this);
        alertDialog.setTitle("Error!");
        alertDialog.setMessage(errorMessage);
        alertDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {//create menu for searchView
        getMenuInflater().inflate( R.menu.home_menu, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {//filtering the images
                    imagesAdapter.filter("");
//                    imagesAdapter.clearFilter();
                } else {
                    imagesAdapter.filter(newText);
                }
                return true;
            }
        });

        return true;
    }
}
