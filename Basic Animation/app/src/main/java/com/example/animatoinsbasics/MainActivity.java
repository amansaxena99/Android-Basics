package com.example.animatoinsbasics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
  import android.view.View;
  import android.widget.ImageView;

  import com.example.animatoinsbasics.R;

  public class MainActivity extends AppCompatActivity {

      Boolean i1 = true;

      public void fadeImage(View view){
          ImageView image1 = (ImageView) findViewById(R.id.imageView1);
          ImageView image2 = (ImageView) findViewById(R.id.imageView2);
          if (i1){
              image1.animate().alpha(0).setDuration(2000);
              image2.animate().alpha(1).setDuration(2000);
              i1 = false;
          } else {
              image2.animate().alpha(0).setDuration(2000);
              image1.animate().alpha(1).setDuration(2000);
              i1 = true;
          }

      }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
