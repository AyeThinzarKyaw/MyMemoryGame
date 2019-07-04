package com.example.mymemorygame;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //protected Bitmap[] imgList20=new Bitmap[20];
    protected String[] urlList20=new String[20];

    Handler handler = new Handler();
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFetch=findViewById(R.id.fetchbtn);
        if (btnFetch!=null)
            btnFetch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v.getId()==R.id.fetchbtn)
        {
            EditText typeUrl=findViewById(R.id.typeurl);
            //String path = getFilesDir() + "/LoadImages/";
            if (typeUrl!=null) {
                for (int i=0;i<20;i++) {
                    ImageView imgBtn = (ImageView) findViewById(getResources().getIdentifier("img" + i, "id", "com.example.mymemorygame"));
                    imgBtn.setImageResource(R.drawable.ic_launcher_background);
                }
                //new ImageLoader(new WeakReference<AppCompatActivity>(this)).execute(typeUrl.getText().toString());
                new ImageLoader(this).execute(typeUrl.getText().toString());
            }
        }

    }


}

