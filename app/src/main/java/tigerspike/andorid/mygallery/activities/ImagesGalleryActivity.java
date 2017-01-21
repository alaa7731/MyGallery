package tigerspike.andorid.mygallery.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import tigerspike.andorid.mygallery.R;
import tigerspike.andorid.mygallery.adapters.ImageGalleryRecyclerViewAdapter;
import tigerspike.andorid.mygallery.models.FlickerItem;
import tigerspike.andorid.mygallery.mvp.fetcher.ImageGalleryFetcher;
import tigerspike.andorid.mygallery.mvp.presenter.ImageGalleryPresenter;
import tigerspike.andorid.mygallery.mvp.view.ImageGalleryContract;
import tigerspike.andorid.mygallery.utils.ItemDecoration;

public class ImagesGalleryActivity extends AppCompatActivity implements ImageGalleryContract.View,
        ImageGalleryRecyclerViewAdapter.OnClick {

    String images_url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json";

    RecyclerView rvGallery;
    ImageGalleryRecyclerViewAdapter imagesAdapter;
    ImageGalleryPresenter presenter;
    RadioButton rbDateTaken, rbDatePublished, rbNon;
    RadioGroup radioGroup;
    private int selectedPopsition;

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

        rbDateTaken = (RadioButton) findViewById(R.id.rbDateTaken);
        rbDatePublished = (RadioButton) findViewById(R.id.rbDatePublished);
        rbNon = (RadioButton) findViewById(R.id.rbNon);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ImagesGalleryActivity.this, LinearLayoutManager.VERTICAL, false);
        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setHasFixedSize(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });
    }

    @Override
    public void onImagesLoaded(ArrayList<FlickerItem> flickerItems) {
        createAdapter(flickerItems);
    }

    private void createAdapter(ArrayList<FlickerItem> flickerItems) {
        imagesAdapter = new ImageGalleryRecyclerViewAdapter(ImagesGalleryActivity.this, flickerItems, this);
        rvGallery.setAdapter(imagesAdapter);
        rvGallery.addItemDecoration(new ItemDecoration(convertDpToPixel(10)));
    }

    public int convertDpToPixel(float dp) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
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
    public boolean onCreateOptionsMenu(Menu menu) {//create menu for searchView
        getMenuInflater().inflate(R.menu.home_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
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

    @Override
    public void onDownloadClicked(int position) {
        selectedPopsition = position;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                transferFiles(position);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        } else { //permission is automatically granted on sdk<23 upon installation
            transferFiles(position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            transferFiles(selectedPopsition);
        }
    }

    private void transferFiles(int position) {

        File file = new File(getFilesDir() + "/saved images", imagesAdapter.getItem(position).getDateTaken());

        File fileExternal = new File(Environment.getExternalStorageDirectory().toString() + "/Flicker Images");
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(file).getChannel();
            outChannel = new FileOutputStream(fileExternal).getChannel();

            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onShareEmail(int position) {
        File file = new File(getFilesDir() + "/saved images", imagesAdapter.getItem(position).getDateTaken());
        if (file.exists()) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("application/image");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
    }

    @Override
    public void onOpenInSystem(int position) {

    }
}
