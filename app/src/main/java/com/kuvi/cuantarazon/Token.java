package com.kuvi.cuantarazon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private View bottom_line;
    private int img_pixels_size;
    private int img_pixels_padding;
    private Bitmap resizedbitmap;
    private Bitmap bmp;

    public Activity activity;

    private int counter = 0;
    int pointsSelected;
    private DisplayMetrics metrics;

    public Token(Activity activity) {

        this.activity = activity;

        //return new Token();
    }

    public void createToken(int id_token, String url_token, int points_token, String title_token) {
        this.id = id_token;
        this.url = url_token;
        this.points = points_token;
        this.title = title_token;
        metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        img_pixels_size = metrics.widthPixels/25;
        img_pixels_padding = metrics.widthPixels/25;

        LinearLayout mainLayout = (LinearLayout)this.activity.findViewById(R.id.MainLayout);

        View header_aux = new RelativeLayout(this.activity.getBaseContext());
        header = new LinearLayout(header_aux.getContext());
        //header.setMinimumHeight(120);
        mainLayout.addView(header);

        View title_text_aux = new TextView(this.activity.getBaseContext());
        TextView title_text = new TextView(title_text_aux.getContext());
        title_text.setText(title);
        title_text.setTextSize(20);
        title_text.setTypeface(null, Typeface.BOLD);
        title_text.setTextColor(Color.parseColor("#000000"));
        header.addView(title_text);

        header.setPadding(20, 10, 0, 10); //left, top, right, bottom

        View img_aux = new ImageView(this.activity.getBaseContext());
        img = new ImageView(img_aux.getContext());
        img.setImageResource(R.drawable.download);
        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(this.img);
        downloadTask.execute(this.url);
        mainLayout.addView(this.img);
        final Animation myRotation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.rotator);
        img.startAnimation(myRotation);

        View footer_aux = new RelativeLayout(this.activity.getBaseContext());
        footer = new RelativeLayout(footer_aux.getContext());
        footer.setMinimumHeight(120);
        mainLayout.addView(footer);

        footer.setPadding(20, 0, 20, 0);

        createButtons(footer, title, img, points);

        bottom_line = new View(this.activity);
        bottom_line.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        bottom_line.setBackgroundColor(Color.LTGRAY);
        mainLayout.addView(bottom_line);
    }


    private void createButtons(RelativeLayout footer, final String title_string, final ImageView imgShare , final int points_) {

        pointsSelected = 0;

        View points_text_olla = new TextView(this.activity.getBaseContext());
        final TextView points_text = new TextView(points_text_olla.getContext());
        points_text.setText("Puntos: " + Integer.toString(points_));
        points_text.setTextColor(Color.parseColor("#000000"));
        points_text.setTypeface(null, Typeface.BOLD);
        footer.addView(points_text);

        RelativeLayout.LayoutParams rel_points_text = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        rel_points_text.setMargins(30,10,0,5);

        points_text.setLayoutParams(rel_points_text);

        points_text.setId(points_);

        View btn_UP_aux = new ImageButton(this.activity.getBaseContext());
        final ImageButton btn_UP = new ImageButton(btn_UP_aux.getContext());

        bmp=BitmapFactory.decodeResource(activity.getResources(), R.drawable.vote_up_gray);
        resizedbitmap=Bitmap.createScaledBitmap(bmp, img_pixels_size, img_pixels_size, true);
        btn_UP.setImageBitmap(resizedbitmap);

        //btn_UP.setImageResource(R.drawable.vote_up_gray);
        btn_UP.setBackgroundColor(0x00000000);
        footer.addView(btn_UP);

        RelativeLayout.LayoutParams params_btn_UP = (RelativeLayout.LayoutParams)btn_UP.getLayoutParams();
        //params.addRule(RelativeLayout.BELOW, id);
        params_btn_UP.addRule(RelativeLayout.BELOW, points_);
        params_btn_UP.setMargins(19, 0, 10, 20); //left, top, right, bottom
        btn_UP.setLayoutParams(params_btn_UP);
        btn_UP.setPadding(img_pixels_padding, img_pixels_padding, img_pixels_padding, img_pixels_padding); //left, top, right, bottom


        btn_UP.setId(id);


        //use a GradientDrawable with only one color set, to make it a solid color
        final GradientDrawable border_btm = new GradientDrawable();
        border_btm.setColor(Color.WHITE); //white background
        border_btm.setStroke(2, Color.LTGRAY); //black border with full opacity
        border_btm.setCornerRadius(360);

        //use a GradientDrawable with only one color set, to make it a solid color
        final GradientDrawable border_up = new GradientDrawable();
        border_up.setColor(Color.WHITE); //white background
        border_up.setStroke(2, Color.LTGRAY); //black border with full opacity
        border_up.setCornerRadius(360);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            btn_UP.setBackgroundDrawable(border_up);
        } else {
            btn_UP.setBackground(border_up);
        }

        btn_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                points_text.setText("Puntos: " + Integer.toString(points_ + 1));
                bmp = BitmapFactory.decodeResource(activity.getResources(), R.drawable.vote_up_blue);
                resizedbitmap = Bitmap.createScaledBitmap(bmp, img_pixels_size, img_pixels_size, true);
                btn_UP.setImageBitmap(resizedbitmap);

            }

        });

        btn_UP.setOnTouchListener(new TextView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    border_up.setColor(Color.LTGRAY); //white background
                    border_up.setStroke(2, Color.GRAY); //black border with full opacity
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    border_up.setColor(Color.WHITE); //white background
                    border_up.setStroke(2, Color.LTGRAY); //black border with full opacity
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    border_up.setColor(Color.WHITE); //white background
                    border_up.setStroke(2, Color.LTGRAY); //black border with full opacity
                }

                return false;
            }
        });

        final TextView share_view = new TextView(this.activity);
        share_view.setText("Compartir");
        share_view.setTextColor(Color.WHITE);
        share_view.setTextSize(20);
        share_view.setTypeface(null, Typeface.BOLD);
        share_view.setBackgroundColor(Color.rgb(64, 191, 43));
        share_view.setPadding(30, 10, 30, 10);

        footer.addView(share_view);

        RelativeLayout.LayoutParams params_btn_SHARE = (RelativeLayout.LayoutParams) share_view.getLayoutParams();
        params_btn_SHARE.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params_btn_SHARE.addRule(RelativeLayout.CENTER_VERTICAL);
        params_btn_SHARE.setMargins(0, 0, 30, 0); //left, top, right bottom
        share_view.setLayoutParams(params_btn_SHARE);

        final GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(15);
        shape.setColor(Color.rgb(64, 191, 43));
        share_view.setBackground(shape);

        share_view.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                              SharedPreferences prefs = activity.getSharedPreferences("com.kuvi.cuantarazon", Context.MODE_PRIVATE);

                                              if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                                                  if (prefs.getBoolean("permission", false)) {

                                                      Bitmap bitmapImg = ((BitmapDrawable) imgShare.getDrawable()).getBitmap();

                                                      Intent intent = new Intent(Intent.ACTION_SEND);
                                                      intent.putExtra(Intent.EXTRA_TEXT, "Más imágenes en: https://goo.gl/GPUoTJ");
                                                      String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmapImg, "", null);
                                                      Uri screenshotUri = Uri.parse(path);

                                                      intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                                      intent.setType("image/*");
                                                      activity.startActivity(Intent.createChooser(intent, "Compartir la imagen en:"));

                                                  } else {
                                                      if (!Settings.System.canWrite(activity)) {
                                                          activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                  Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                                                      }
                                                  }
                                              } else {

                                                  Bitmap bitmapImg = ((BitmapDrawable) imgShare.getDrawable()).getBitmap();

                                                  Intent intent = new Intent(Intent.ACTION_SEND);
                                                  intent.putExtra(Intent.EXTRA_TEXT, "Más imágenes en: https://goo.gl/GPUoTJ");
                                                  String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmapImg, "", null);
                                                  Uri screenshotUri = Uri.parse(path);

                                                  intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                                  intent.setType("image/*");
                                                  activity.startActivity(Intent.createChooser(intent, "Compartir la imagen en:"));
                                              }
                                          }
                                      }

        );

            share_view.setOnTouchListener(new TextView.OnTouchListener()

                                          {

                                              @Override
                                              public boolean onTouch(View v, MotionEvent event) {
                                                  // TODO Auto-generated method stub
                                                  if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                      shape.setColor(Color.rgb(34, 139, 34));
                                                  } else if (event.getAction() == MotionEvent.ACTION_UP) {
                                                      shape.setColor(Color.rgb(64, 191, 43));
                                                  } else if (event.getAction() == MotionEvent.ACTION_CANCEL)
                                                      shape.setColor(Color.rgb(64, 191, 43));
                                                  return false;
                                              }
                                          }

            );

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

            bmImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bmImage.setAdjustViewBounds(true);
            counter++;

            if (counter%MainActivity.numTokens==0){
                MainActivity.loading=false;
            }
        }
    }


}
