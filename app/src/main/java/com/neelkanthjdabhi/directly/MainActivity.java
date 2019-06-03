package com.neelkanthjdabhi.directly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    ImageView shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.constraintLayout);

        Snackbar snackbar = Snackbar.make(constraintLayout,"Learn how to use Directly.",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Guide", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/MQSTtCXYOpU")));
            }
        });
        snackbar.show();

        shareButton = findViewById(R.id.sharebutton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Well done! Now do it in another app.", Toast.LENGTH_SHORT).show();
            }
        });





    }
}
