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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FavActivity extends Activity {

    private JSONObject jsonObject;

    private int favID;
    private int lengthArray;
    private int[] arrayIDs = {4, 4, 3, 1, 2, 4};
    private int tokensCreated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        actionBar.setCustomView(R.layout.actionbar_favs);//set the custom view*/

        lengthArray = arrayIDs.length;

        favID = arrayIDs[0];

        ImageButton home_button = (ImageButton) findViewById(R.id.home_button);
        home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavActivity.this,MainActivity.class);
                startActivity(intent);
            }

        });

        new GetTokensTask().execute(new ApiConnector());

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

    private class GetTokensTask extends AsyncTask<ApiConnector, Long, JSONArray> {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            //Log.e("actualID ", Integer.toString(actualID));
            return params[0].GetTokens(favID, true);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if (jsonArray!=null) {
                createTokens(jsonArray);
                tokensCreated++;

                if (tokensCreated<lengthArray) {
                    favID = arrayIDs[tokensCreated];
                    new GetTokensTask().execute(new ApiConnector());
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

}

