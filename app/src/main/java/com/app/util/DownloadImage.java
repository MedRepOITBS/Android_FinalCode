package com.app.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Kishore on 9/27/2015.
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private Activity activity;
    private ProgressDialog progressDialog;
    private String fileName;


    public DownloadImage(Activity activity, ImageView imageView, String fileName) {
        this.activity = activity;
        this.imageView = imageView;
        this.fileName = fileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create a progressdialog
        progressDialog = new ProgressDialog(activity);
        // Set progressdialog title
        progressDialog.setTitle("Download Image Tutorial");
        // Set progressdialog message
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        // Show progressdialog
        progressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... URL) {

        String imageURL = URL[0];

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // Set the bitmap into ImageView
        imageView.setImageBitmap(result);
        // Close progressdialog
        progressDialog.dismiss();
    }

    public void saveFile(Context context, Bitmap bmp, String picName){
        String path = Utils.getDataFolder(activity).getAbsolutePath() + File.separator + fileName + ".jpeg";
        OutputStream fOut = null;
        String imageName = "yourImageName";
        File file = new File(path, imageName);
        try {
            fOut = new FileOutputStream(file);
            if (!bmp.compress(Bitmap.CompressFormat.JPEG, 90, fOut)) {
                Log.e("Log", "error while saving bitmap " + path + imageName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}