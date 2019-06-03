package com.neelkanthjdabhi.directly;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.util.Random;

public class CreateShortcut extends AppCompatActivity implements ColorPickerDialogListener {
    private TextView appNameTV,iconName,colorhexTV;
    private LinearLayout wallpaperView,icon,LLSelectColor;
    EditText iconText,labelText;
    Intent intent;
    String action,type,sharedText;
    Bitmap my_bmp;
    private static final int DIALOG_ID = 0;
    FloatingActionButton createShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shortcut);

        /*
        wallpaperView = findViewById(R.id.wallpaperView);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        wallpaperView.setBackground(wallpaperDrawable);

        */
        icon = findViewById(R.id.icon);
        iconName = findViewById(R.id.iconName);
        iconText = findViewById(R.id.iconText);
        iconText.setText("\uD83E\uDD81");
        labelText=findViewById(R.id.labelText);
        labelText.setText("Directly");
        appNameTV = findViewById(R.id.appName);
        createShortcut = findViewById(R.id.createShortcut);
        LLSelectColor = findViewById(R.id.LLSelectColor);
        colorhexTV = findViewById(R.id.colorhexTV);


        // Get intent, action and MIME type
        intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        final String packageName = this.getCallingPackage();
        PackageManager packageManager= getApplicationContext().getPackageManager();
        try {
            String appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
            appNameTV.setText(appName);
            Toast.makeText(this, "here"+appName, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        LLSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker();
            }
        });




        createShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_bmp =getBitmapFromView(icon);
                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {

                        if(isStoragePermissionGranted())
                        {
                            handleSendText(intent);
                        }else
                        {
                            Toast.makeText(CreateShortcut.this, "Allow Storage permission to, create shortcut.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }
        });




        iconText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    iconName.setText(iconText.getEditableText());
            }
        });

        labelText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    appNameTV.setText(labelText.getEditableText());
            }
        });





    }

    private void colorPicker() {
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowPresets(false)
                .setDialogId(DIALOG_ID)
                .setColor(Color.WHITE)
                .setShowAlphaSlider(false)
                .show(CreateShortcut.this);
    }


    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }



    void handleSendText(Intent intent) {

        String label = labelText.getEditableText().toString();
        String textOnIcon = iconText.getEditableText().toString();
        sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (!label.isEmpty())
        {
            if (!textOnIcon.isEmpty())
            {
                if (sharedText != null) {
                    ShortcutInfoCompat shortcutInfo = new ShortcutInfoCompat.Builder(getApplicationContext(), random())
                            .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedText))) // !!! intent's action must be set on oreo
                            .setShortLabel(label)
                            .setIcon(IconCompat.createWithBitmap(my_bmp))
                            .build();
                    ShortcutManagerCompat.requestPinShortcut(getApplicationContext(), shortcutInfo, null);
                    finish();
                }
            }else
            {
                Toast.makeText(this, "Text on icon can not be empty.", Toast.LENGTH_SHORT).show();
            }

        }else
        {
            Toast.makeText(this, "Label can not be empty.", Toast.LENGTH_SHORT).show();
        }

    }

    public static String random() {
        //Random String Generator, new id each time
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(8);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    @Override public void onColorSelected(int dialogId, int color) {
        switch (dialogId) {
            case DIALOG_ID:
                // We got result from the dialog that is shown when clicking on the icon in the action bar.
                Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle);
                mDrawable.setTint(color);
                icon.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle));
                colorhexTV.setText(String.format("#%06X", (0xFFFFFF & color)));
                break;
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override public void onDialogDismissed(int dialogId) {
    }
}
