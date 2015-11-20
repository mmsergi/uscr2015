package uscr.com.uscr015;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private ImageView img;
    private LinearLayout footer;
    private ImageButton btn_UP;
    private ImageButton btn_DOWN;
    private ImageButton btn_SHARE;
    private ImageButton btn_FAV;

    public Activity main_activity;

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

        View img_olla = new ImageView(this.main_activity.getBaseContext());
        this.img = new ImageView(img_olla.getContext());
        this.img.setImageResource(R.drawable.up_vote);

        Log.e("urL:", this.url);

        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(this.img);

        downloadTask.execute(this.url);

        mainLayout.addView(this.img);

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
            bmImage.setImageBitmap(result);

            Display display = main_activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // ((display.getWidth()*20)/100)
            int height = result.getHeight();// ((display.getHeight()*30)/100)

            //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            //bmImage.setLayoutParams(parms);
            bmImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bmImage.setAdjustViewBounds(true);
        }
    }

}
