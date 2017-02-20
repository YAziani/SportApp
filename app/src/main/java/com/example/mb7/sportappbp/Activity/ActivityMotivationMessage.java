package com.example.mb7.sportappbp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mb7.sportappbp.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ActivityMotivationMessage extends AppCompatActivity {

    TextView t;
    String s = "";
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivation_message);


        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("lift.jpg");
        System.out.println(storageRef + " **************************************************************************************");

        ImageView imageView = (ImageView)findViewById(R.id.imageView3);



        // Load the image using Glide
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(imageView);


        /*
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    System.out.println(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    exception.printStackTrace();
                }
            });
        */

        /*
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = mStorageRef.child("lift.jpg");
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        }catch(Exception e) {
            e.printStackTrace();
        }
        ref.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("download success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("download failure");
            }
        });
        */

        /*
        t = (TextView)findViewById(R.id.textViewMotivationMessage);
        t.setTextSize(20);
        try {
            Firebase root = new Firebase("https://sportapp-cbd6b.firebaseio.com/players/");
            root.child("Amanda").child("age").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        s = dataSnapshot.getValue().toString();
                        t.setText(s);
                        System.out.println("DATABASE READ OPERATION COMPLETE");
                    }else {
                        s = "";
                        t.setText(s);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }catch(Exception e) {
            e.printStackTrace();
        }
        */

    }
}
