package com.example.pixelcanvas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;

public class FullimageActivity extends AppCompatActivity {

    private ImageView fullimage;
    private Button apply;

    private AdView adview;
    private final String TAG = "GGLADS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adview = findViewById(R.id.adView);

        bannerAds();

        fullimage = findViewById(R.id.fullImage);
        apply = findViewById(R.id.apply);

        Glide.with(this).load(getIntent().getStringExtra("image")).into(fullimage);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackground();
            }
        });
    }

    private void bannerAds() {

        adview.loadAd(new AdRequest.Builder().build());
        adview.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                Log.d(TAG,"onAdClicked: ");
            }

            @Override
            public void onAdClosed() {

                Log.d(TAG,"onAdClosed: ");
            }

            @Override
            public void onAdImpression() {
                Log.d(TAG,"onAdImpression: ");
            }

            @Override
            public void onAdLoaded() {
                Log.d(TAG,"onAdLoaded: ");
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG,"onAdOpened: ");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(TAG, "onAdFailedToLoad: "+loadAdError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.share:
                BitmapDrawable drawable = (BitmapDrawable)fullimage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"title",null);

                Uri uri = Uri.parse(bitmapPath);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM,uri);
                intent.putExtra(Intent.EXTRA_TEXT,"Playstore Link : https://play.google.com/store/apps/details?id="+getPackageName());

                startActivity(Intent.createChooser(intent,"share"));

            break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setBackground() {

        Bitmap bitmap =  ((BitmapDrawable)fullimage.getDrawable()).getBitmap();

        WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

        try {
            manager.setBitmap(bitmap);
        } catch (IOException e) {
            Toast.makeText(this, "Error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}