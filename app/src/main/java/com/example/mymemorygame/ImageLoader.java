package com.example.mymemorygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageLoader extends AsyncTask<String,Integer,Boolean> {

    private WeakReference<MainActivity> parent=null;

    private static final String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
    private static final String imgType = "([^\\s]+(\\.(?i)(jpg|png))$)";

    private Bitmap[] images=new Bitmap[20];

    public ImageLoader(MainActivity parent){
        this.parent=new WeakReference<>(parent);
    }

    @Override
    protected Boolean doInBackground(String... typedUrl){
        try{
            URL url=new URL(typedUrl[0]);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            url.openStream()));

            String inputLine;

            //Bitmap[] imgList=new Bitmap[20];

            int i=0;
            while ((inputLine = in.readLine()) != null && i<20){
               String oneSrcLine=this.extractSource(inputLine);
               if (oneSrcLine != null){
                   URL imgUrl=new URL(oneSrcLine);
                   HttpURLConnection connection =(HttpURLConnection)imgUrl.openConnection();
                   connection.setDoInput(true);
                   connection.addRequestProperty("User-Agent","Mozilla/4.76");
                   connection.connect();
                   InputStream input=connection.getInputStream();
                   Bitmap bmp= BitmapFactory.decodeStream(input);
                   images[i]=bmp;
                   publishProgress(i);
                   i++;

                   //publishProgress((int)(i*5));
               }
            }
            if (i==0){
                return false;
            }
            in.close();
            return true;

        }catch (Exception e)
        {
            return null;
        }


    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (this.parent!=null && values[0]!=null) {
            MainActivity parent = this.parent.get();

            ProgressBar bar = parent.findViewById(R.id.progressBar);

            if (values[0]==0){
                bar.setProgress(0);
                bar.setVisibility(View.VISIBLE);
            }

            bar.setProgress(Math.round((values[0]+1)*5));

            ImageView imgBtn=(ImageView) parent.findViewById(parent.getResources().getIdentifier("img"+values[0],"id","com.example.mymemorygame"));
            imgBtn.setImageBitmap(images[values[0]]);

            TextView downloadedCount=parent.findViewById(R.id.downloadedCount);
            downloadedCount.setText("Downloading "+ (values[0]+1) + " of 20 images...");
        }


    }

    @Override
    protected void onPostExecute(Boolean load){

        if (this.parent!=null){
            MainActivity parent = this.parent.get();
            if (load==true){
                ProgressBar bar = parent.findViewById(R.id.progressBar);
                bar.setProgress(0);
                bar.setVisibility(View.INVISIBLE);

                TextView downloadedCount=parent.findViewById(R.id.downloadedCount);
                downloadedCount.setText("");
            }




        }


    }


    private String extractSource(String htmlLine){

        Pattern p = Pattern.compile(imgRegex);
        if (htmlLine != null){
            Matcher m = p.matcher(htmlLine);
            if (m.find()) {
                String srcResult = m.group(1);
                p=Pattern.compile(imgType);
                m=p.matcher(srcResult);
                if (m.find())
                {
                    srcResult=m.group(1);
                    return srcResult;
                }
            }
        }
        return null;
    }


}
