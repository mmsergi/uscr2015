package com.kuvi.cuantarazon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity implements ScrollViewListener {

    private JSONObject jsonObject;
    private int actualID = 0;
    public static boolean loading = true;

    public static int numTokens = 8;

    private SharedPreferences prefs;

    Intent starterIntent;
    private boolean firstInit = true;
    private Long actualTime;
    private Long oldTime;

    public static Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        mTracker.setScreenName("MainActiviy");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        oldTime = System.currentTimeMillis()/1000;

        prefs = this.getSharedPreferences("com.kuvi.cuantarazon", Context.MODE_PRIVATE);

        if (!prefs.getBoolean("permission", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                }
            }
        }

        ScrollViewExt mainScrollView = (ScrollViewExt) findViewById(R.id.scrollView);
        mainScrollView.setScrollViewListener(this);
        mainScrollView.setBackgroundColor(Color.WHITE);

        starterIntent = getIntent();

        //insertToDatabase(58, 5);
        new GetTokensTask().execute(new ApiConnector());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");

                    prefs.edit().putBoolean("permission", true).commit();
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    private void createTokens(JSONArray jsonArray) {
        int id = 0;
        String title;
        String url = null;
        int points = 0;
        Token ficha = new Token(this);
        int lengthArray = jsonArray.length();
        for (int i=0; i<lengthArray; i++){
            jsonObject = null;

            try {
                jsonObject = jsonArray.getJSONObject(i);
                id = jsonObject.getInt("id");
                actualID = id;
                title = jsonObject.getString("title");
                Log.e("",String.valueOf(id)+" - "+title);
                url = jsonObject.getString("url");
                points = jsonObject.getInt("points");

                ficha.data_Token(id, url, points, title);

                ficha.display_Token();

                //createTOKEN(id, url, points, "title");

            } catch (JSONException e){
                e.printStackTrace();
                Log.e("ERROR","no se encuentran más tokens en el json array, la i = "+String.valueOf(i));
            }
        }
    }

    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        // We take the last son in the scrollview
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
        // if diff is zero, then the bottom has been reached

        if (diff == 0) {
            // do stuff
            if (!loading) {
                MainActivity.loading = true;
                Toast.makeText(this, "Cargando más...", Toast.LENGTH_SHORT).show();
                new GetTokensTask().execute(new ApiConnector());
            }
        }

    }

    private class GetTokensTask extends AsyncTask<ApiConnector, Long, JSONArray> {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            //Log.e("actualID ", Integer.toString(actualID));
            return params[0].GetTokens(actualID);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray!=null) {
                if (jsonArray.isNull(0)) {
                    Log.e("ERROR", "JSON ARRAY no tiene contenido.");
                } else {
                    createTokens(jsonArray);
                }
            } else {
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.MainLayout);

                View img_aux = new ImageView(getBaseContext());
                ImageView img = new ImageView(img_aux.getContext());
                img.setImageResource(R.drawable.noconnection);
                mainLayout.addView(img);
                img.setPadding(0, 400, 0, 0);

                View message_text_olla = new TextView(getBaseContext());
                TextView message_text = new TextView(message_text_olla.getContext());
                message_text.setText("Comprueba tu conexión y reinicia la aplicación");
                message_text.setTextColor(Color.parseColor("#000000"));
                message_text.setTypeface(null, Typeface.BOLD);
                //message_text.setGravity(Gravity.CENTER);
                message_text.setPadding(100,100,0,0);
                mainLayout.addView(message_text);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        actualTime = System.currentTimeMillis()/1000;

        Long diferencia = (actualTime-oldTime);

        Log.e("DIFERENCIA", diferencia.toString());

        if (firstInit) {
            firstInit=false;
        } else if (diferencia>180) {
            Log.e("RESET", "true");
            finish();
            startActivity(starterIntent);
        }

        oldTime = System.currentTimeMillis()/1000;
    }

}

