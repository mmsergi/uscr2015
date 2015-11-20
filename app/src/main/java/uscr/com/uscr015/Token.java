package uscr.com.uscr015;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        Log.e("entra0", "hlalsdmnas");
    }

    public void display_Token(){

        Log.e("entra", "");

        LinearLayout mainLayout = (LinearLayout)this.main_activity.findViewById(R.id.MainLayout);

        View img_olla = new ImageView(this.main_activity.getBaseContext());
        this.img = new ImageView(img_olla.getContext());

        mainLayout.addView(img);

        View footer_olla = new LinearLayout(this.main_activity.getBaseContext());
        this.footer = new LinearLayout(footer_olla.getContext());
        footer.setMinimumHeight(120);

        mainLayout.addView(footer);

    }

}
