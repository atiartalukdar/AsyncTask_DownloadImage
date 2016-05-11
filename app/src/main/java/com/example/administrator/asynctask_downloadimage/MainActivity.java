package com.example.administrator.asynctask_downloadimage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    public static final int progress_bar_type = 0;
    private static String image_url = "http://i67.tinypic.com/sqp954.jpg";
    public ImageView myImage;
    Button downloadBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadBtn = (Button) findViewById(R.id.download);
        myImage = (ImageView) findViewById(R.id.imageView);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFile().execute(image_url);
            }
        });

    }

    public void onReset(View view){
        myImage.setImageResource(android.R.color.transparent);
        progressDialog.setProgress(0);
    }
    protected Dialog onCreateDialog(int id){
        switch (id){
            case progress_bar_type:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Downloading file. Please wait....");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgress(0);
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }

    public class DownloadFile extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... params) {
            int count;
            try{
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                int lenfthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),8129);
                OutputStream output = new FileOutputStream("/sdcard/atiar.jpg");
                byte data[] = new byte[1024];
                long total = 0;
                while((count = input.read(data))!=-1){
                    total+=count;
                    publishProgress(""+(int)(total*100)/lenfthOfFile);
                    output.write(data,0,count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... progress){
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String file_url){
            dismissDialog(progress_bar_type);
            String imagePath = Environment.getExternalStorageDirectory().toString()+"/atiar.jpg";
            myImage.setImageDrawable(Drawable.createFromPath(imagePath));
        }


    }
}
