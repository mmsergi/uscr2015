package uscr.com.uscr015;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sergi on 19/11/2015.
 */

public class Token {

    private int id;
    private String url;
    private int points;
    private TextView points_text;
    private String title;
    private TextView title_text;

    private LinearLayout header;
    private ImageView img;
    private RelativeLayout footer;
    private ImageButton btn_UP;
    private ImageButton btn_DOWN;
    private ImageButton btn_SHARE;
    private ImageButton btn_FAV;
    private ImageView divider;

    public Activity main_activity;

    private int counter = 0;

    public Token(MainActivity mainActivity) {

        this.id = 0;
        this.url = null;
        this.points = 0;
        this.title = null;
        this.main_activity = mainActivity;

        //return new Token();
    }

    public void data_Token(int id_token, String url_token, int points_token, String title_token) {
        this.id = id_token;
        this.url = url_token;
        this.points = points_token;
        this.title = title_token;
    }

    public void display_Token(){

        LinearLayout mainLayout = (LinearLayout)this.main_activity.findViewById(R.id.MainLayout);

        View header_aux = new RelativeLayout(this.main_activity.getBaseContext());
        header = new LinearLayout(header_aux.getContext());
        //header.setMinimumHeight(120);
        mainLayout.addView(header);

        View title_text_aux = new TextView(this.main_activity.getBaseContext());
        TextView title_text = new TextView(title_text_aux.getContext());
        title_text.setText(title);
        title_text.setTextColor(Color.parseColor("#FFFFFF"));
        header.addView(title_text);

        View img_aux = new ImageView(this.main_activity.getBaseContext());
        img = new ImageView(img_aux.getContext());
        img.setImageResource(R.drawable.download);
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(this.img);
        downloadTask.execute(this.url);
        mainLayout.addView(this.img);
        final Animation myRotation = AnimationUtils.loadAnimation(main_activity.getApplicationContext(), R.anim.rotator);
        img.startAnimation(myRotation);

        View footer_aux = new RelativeLayout(this.main_activity.getBaseContext());
        footer = new RelativeLayout(footer_aux.getContext());
        footer.setMinimumHeight(120);
        mainLayout.addView(footer);

        footer.setPadding(20, 0, 40, 0);

        createButtons(mainLayout, footer, title, img);
/*
        View points_text_olla = new TextView(this.main_activity.getBaseContext());
        TextView points_text = new TextView(points_text_olla.getContext());
        points_text.setText(Integer.toString(points));
        points_text.setTextColor(Color.parseColor("#B40404"));
        footer.addView(points_text);

        View btn_UP_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_UP = new ImageButton(btn_UP_olla.getContext());
        btn_UP.setImageResource(R.drawable.up_vote);
        btn_UP.setBackgroundColor(0x00000000);
        footer.addView(btn_UP);

        btn_UP.setId(id);

        View btn_DOWN_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_DOWN = new ImageButton(btn_DOWN_olla.getContext());
        btn_DOWN.setImageResource(R.drawable.down_vote);
        btn_DOWN.setBackgroundColor(0x00000000);
        footer.addView(btn_DOWN);

        RelativeLayout.LayoutParams params_btn_DOWN = (RelativeLayout.LayoutParams)btn_DOWN.getLayoutParams();
        //params.addRule(RelativeLayout.BELOW, id);
        params_btn_DOWN.addRule(RelativeLayout.RIGHT_OF, id);
        btn_DOWN.setLayoutParams(params_btn_DOWN);

        View btn_FAV_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_FAV = new ImageButton(btn_FAV_olla.getContext());
        btn_FAV.setImageResource(R.drawable.fav);
        btn_FAV.setBackgroundColor(0x00000000);
        footer.addView(btn_FAV);

        RelativeLayout.LayoutParams params_btn_FAV = (RelativeLayout.LayoutParams)btn_FAV.getLayoutParams();
        //params_btn_FAV.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_btn_FAV.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_FAV.setLayoutParams(params_btn_FAV);

        View btn_SHARE_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_SHARE = new ImageButton(btn_SHARE_olla.getContext());
        btn_SHARE.setImageResource(R.drawable.share);
        btn_SHARE.setBackgroundColor(0x00000000);
        footer.addView(btn_SHARE);

        RelativeLayout.LayoutParams params_btn_SHARE = (RelativeLayout.LayoutParams)btn_SHARE.getLayoutParams();
        params_btn_SHARE.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_btn_SHARE.addRule(RelativeLayout.CENTER_VERTICAL);
        btn_SHARE.setLayoutParams(params_btn_SHARE);

        View divider_olla = new ImageView(this.main_activity.getBaseContext());
        ImageView divider = new ImageView(divider_olla.getContext());
        divider.setImageResource(R.drawable.divider);

        mainLayout.addView(divider);

        btn_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_activity, "Btn UP Click - " + title, Toast.LENGTH_SHORT).show();
            }
        });

        btn_DOWN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_activity, "Btn DOWN Click", Toast.LENGTH_SHORT).show();
            }
        });

        btn_FAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_activity, "Btn FAV Click", Toast.LENGTH_SHORT).show();
            }
        });

        btn_SHARE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_activity, "Btn SHARE Click", Toast.LENGTH_SHORT).show();
            }
        });

*/
        /*RelativeLayout.LayoutParams params_DIVIDER = (RelativeLayout.LayoutParams)divider.getLayoutParams();
        params_DIVIDER.addRule(RelativeLayout.BELOW);
        divider.setLayoutParams(params_DIVIDER);*/
    }

    private void createButtons(LinearLayout mainLayout, RelativeLayout footer, final String title_string, final ImageView imgShare ) {

        View points_text_olla = new TextView(this.main_activity.getBaseContext());
        TextView points_text = new TextView(points_text_olla.getContext());
        points_text.setText(Integer.toString(points));
        points_text.setTextColor(Color.parseColor("#FFFFFF"));
        footer.addView(points_text);

        View btn_UP_aux = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_UP = new ImageButton(btn_UP_aux.getContext());
        btn_UP.setImageResource(R.drawable.points_black);
        btn_UP.setBackgroundColor(0x00000000);
        footer.addView(btn_UP);

        btn_UP.setId(id);

        btn_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_activity, "Btn UP Click - " + title_string, Toast.LENGTH_SHORT).show();
            }
        });

        View btn_DOWN_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_DOWN = new ImageButton(btn_DOWN_olla.getContext());
        btn_DOWN.setImageResource(R.drawable.down_vote);
        btn_DOWN.setBackgroundColor(0x00000000);
        footer.addView(btn_DOWN);

        RelativeLayout.LayoutParams params_btn_DOWN = (RelativeLayout.LayoutParams)btn_DOWN.getLayoutParams();
        //params.addRule(RelativeLayout.BELOW, id);
        params_btn_DOWN.addRule(RelativeLayout.RIGHT_OF, id);
        btn_DOWN.setLayoutParams(params_btn_DOWN);

        View btn_FAV_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_FAV = new ImageButton(btn_FAV_olla.getContext());
        btn_FAV.setImageResource(R.drawable.fav);
        btn_FAV.setBackgroundColor(0x00000000);
        footer.addView(btn_FAV);

        RelativeLayout.LayoutParams params_btn_FAV = (RelativeLayout.LayoutParams)btn_FAV.getLayoutParams();
        //params_btn_FAV.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_btn_FAV.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btn_FAV.setLayoutParams(params_btn_FAV);

        View btn_SHARE_olla = new ImageButton(this.main_activity.getBaseContext());
        ImageButton btn_SHARE = new ImageButton(btn_SHARE_olla.getContext());
        btn_SHARE.setImageResource(R.drawable.share);
        btn_SHARE.setBackgroundColor(0x00000000);
        footer.addView(btn_SHARE);

        RelativeLayout.LayoutParams params_btn_SHARE = (RelativeLayout.LayoutParams)btn_SHARE.getLayoutParams();
        params_btn_SHARE.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_btn_SHARE.addRule(RelativeLayout.CENTER_VERTICAL);
        btn_SHARE.setLayoutParams(params_btn_SHARE);

        btn_SHARE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmapImg = ((BitmapDrawable)imgShare.getDrawable()).getBitmap();
                //Bitmap bitmapImg = BitmapFactory.decodeResource(main_activity.getResources(), R.drawable.fav);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, title_string);
                String path = MediaStore.Images.Media.insertImage(main_activity.getContentResolver(), bitmapImg, "", null);
                Uri screenshotUri = Uri.parse(path);

                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                intent.setType("image/*");
                main_activity.startActivity(Intent.createChooser(intent, "Share image via..."));

                //File imageFileToShare = new File(imagePath);
                //Uri pictureUri = Uri.parse("android.resource://uscr.com.uscr015/drawable/fav");
//                Uri pictureUri = Uri.fromFile(new File("/storage/emulated/0/profile.png"));
//                share.putExtra(Intent.EXTRA_STREAM, pictureUri);
//                share.putExtra(Intent.EXTRA_TEXT, title_string);
//                main_activity.startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

    }

    public class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.clearAnimation();
            bmImage.setImageBitmap(result);

            Display display = main_activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // ((display.getWidth()*20)/100)
            int height = result.getHeight();// ((display.getHeight()*30)/100)

            //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            //bmImage.setLayoutParams(parms);
            bmImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bmImage.setAdjustViewBounds(true);
            counter++;
            if (counter%5==0){
                MainActivity.loading=false;
            }
        }
    }


}
