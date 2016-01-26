package com.kuvi.cuantarazon;

import android.Manifest;
import android.app.ActionBar;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ScrollViewListener {

    private JSONObject jsonObject;
    private int actualID = 0;
    public static boolean loading = true;

    public static int numTokens = 8;

    private SharedPreferences prefs;

    Intent starterIntent;
    private boolean firstInit = true;
    private Long actualTime;
    private Long oldTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = (LayoutInflater) getActionBar()
                .getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.actionbar_custom, null);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView,
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

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

        starterIntent = getIntent();

        new GetTokensTask().execute(new ApiConnector());
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

    private void createTokens(JSONArray jsonArray)
    {

        Token token = new Token(this);

        int lengthArray = jsonArray.length();

        for (int i = 0; i < lengthArray; i++){
            jsonObject = null;

            try {

                jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String url = jsonObject.getString("url");
                int points = jsonObject.getInt("points");

                actualID = id;
                Log.e("Actual ID", String.valueOf(id));
                Log.e("Token", title);

                //new Token(this, id, url, points, title);

                token.createToken(id, url, points, title);


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

    private class GetTokensTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            //Log.e("actualID ", Integer.toString(actualID));
            return params[0].GetTokens(actualID);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray!=null) {
                createTokens(jsonArray);
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

        Log.e("Diferencia de segundos", diferencia.toString());

        if (firstInit) {
            firstInit=false;
        } else if (diferencia>180) {
            Log.e("Resetear activity", "true");
            finish();
            startActivity(starterIntent);
        }

        oldTime = System.currentTimeMillis()/1000;
    }

}

