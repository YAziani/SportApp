package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mb7.sportappbp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * activity showing motivation pictures from the database
 */
public class ActivityMotivationMessage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation_message);

        // reference for the firebase storage image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("lift_text.jpg");
        final ImageView imageView0 = (ImageView)findViewById(R.id.imageMotivationMessage0);
        final ImageView imageView1 = (ImageView)findViewById(R.id.imageMotivationMessage1);
        final Drawable drawable = getDrawable(R.drawable.sport_icon);

        imageView0.setImageDrawable(drawable);
        // Load the image using Glide
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e,
                                               StorageReference model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        imageView0.setImageDrawable(drawable);
                        imageView1.setImageDrawable(drawable);
                        imageView1.setAlpha(0.5f);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   StorageReference model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        imageView1.setImageDrawable(resource);
                        imageView1.setAlpha(0.5f);
                        return false;
                    }
                })
                .into(imageView0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // set up save button
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // close activity
        Intent i = new Intent(this,ActivityMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra("startTab",1);
        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onOptionsItemSelected(null);
    }
}
