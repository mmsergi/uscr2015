package uscr.com.uscr015;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements ScrollViewListener {

    private JSONObject jsonObject;
    private int actualID = 0;
    public static boolean loading = false;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("uscr.com.uscr015", Context.MODE_PRIVATE);

        if (!prefs.getBoolean("permission", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                } else {
                    // continue with your code
                }
            } else {
                // continue with your code
            }
        }

        ScrollViewExt mainScrollView = (ScrollViewExt) findViewById(R.id.scrollView);
        mainScrollView.setScrollViewListener(this);

        //insertToDatabase(58, 5);
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
        int id = 0;
        String title;
        String url = null;
        int points = 0;
        Token ficha = new Token(this);
        int lengthDB = jsonArray.length();
        for (int i=0; i<10; i++){
            jsonObject = null;

            try {
                jsonObject = jsonArray.getJSONObject(lengthDB-i);
                id = jsonObject.getInt("id");
                title = jsonObject.getString("title");
                Log.e("ERROR",jsonObject.getString("title"));
                url = jsonObject.getString("url");
                points = jsonObject.getInt("points");

                ficha.data_Token(id, url, points, title);

                ficha.display_Token();

                //createTOKEN(id, url, points, "title");

            } catch (JSONException e){
                e.printStackTrace();
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
                this.actualID += 5;
                new GetTokensTask().execute(new ApiConnector());
            }
        }
    }

    private class GetTokensTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            Log.e("actualID ", Integer.toString(actualID));
            return params[0].GetTokens(actualID);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray.isNull(0)){
                Log.e("ERROR","JSON ARRAY no tiene contenido.");
            } else {
                createTokens(jsonArray);

            }
        }
    }

    private void insertToDatabase(final int id, final int value){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                //String paramUsername = params[0];
                //String paramAddress = params[1];

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(id)));
                nameValuePairs.add(new BasicNameValuePair("value", Integer.toString(value)));

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://mejorandroid.es/uscr/insert-db.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "success";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                //TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Inserted");
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(Integer.toString(value));
    }
}

