package com.example.mb7.sportappbp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mb7.sportappbp.DataAccessLayer.DAL_Utilities;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.Iterator;
import java.util.Random;

/**
 * activity showing motivation pictures from the database
 */
public class ActivityMotivationMessage extends AppCompatActivity {

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation_message);

        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wird_geladen));
        pd.show();

        getImgName();

    }

    /**
     * behavior if database is not available
     */
    public void cancel() {
        pd.dismiss();
        Toast.makeText(
                ActivityMotivationMessage.this,
                "Datenbankzugriff fehlgeschlagen",
                Toast.LENGTH_SHORT

        ).show();
        Intent i = new Intent(ActivityMotivationMessage.this, ActivityMain.class);
        i.putExtra("startTab", 1);
        startActivity(i);
    }

    /**
     * get the names of all images in the database and choose a random one to show
     */
    public void getImgName() {
        try {
            URL url = new URL(DAL_Utilities.DatabaseURL + "Administration/motivationImages");
            Firebase root = new Firebase(url.toString());
            root.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // choose random index to show
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    int index = (int) dataSnapshot.getChildrenCount();
                    index = new Random().nextInt(index);
                    // iterate through the names
                    for (int i = 0; i < index; i++) {
                        if (iterator.hasNext()) {
                            iterator.next();
                        } else {
                            ActivityMotivationMessage.this.cancel();
                            return;
                        }
                    }
                    showImage((String) iterator.next().child("imagename").getValue());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    ActivityMotivationMessage.this.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * show the image with this name into the activity
     *
     * @param imgName name of image
     */
    public void showImage(String imgName) {
        // reference for the firebase storage image
        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference().child("motivationImage/" + imgName);

        final ImageView imageView0 = (ImageView) findViewById(R.id.imageMotivationMessage0);
        final ImageView imageView1 = (ImageView) findViewById(R.id.imageMotivationMessage1);
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
                        ActivityMotivationMessage.this.cancel();
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   StorageReference model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        pd.dismiss();
                        imageView0.setImageDrawable(resource);
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
        Intent i = new Intent(this, ActivityMain.class);
        i.putExtra("startTab", 1);
        startActivity(i);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onOptionsItemSelected(null);
    }
}
