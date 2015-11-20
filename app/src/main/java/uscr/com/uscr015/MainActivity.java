package uscr.com.uscr015;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.responseText = (TextView) this.findViewById(R.id.textView);

        //new task().execute();
        new GetAllTokensTask().execute(new ApiConnector());
    }

    public void setText(JSONArray jsonArray)
    {
        String s = "";
        int lengthDB = jsonArray.length();
        for (int i=0; i<lengthDB; i++){
            try {
                JSONObject json = jsonArray.getJSONObject(lengthDB-i);
                s = s + json.getString("id")+"\n";
                s = s + json.getString("url")+"\n";
                s = s + json.getString("points")+"\n";
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        this.responseText.setText(s);
    }

    private void createTokens(JSONArray jsonArray)
    {
        int id = 0;
        String url = null;
        int points = 0;
        Token ficha = new Token(this);
        //int lengthDB = jsonArray.length();
        for (int i=0; i<5; i++){
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                id = json.getInt("id");
                url = json.getString("url");
                points = json.getInt("points");

                ficha.data_Token(id, url, points, "title");

                ficha.display_Token();


                //createTOKEN(id, url, points, "title");

            } catch (JSONException e){
                e.printStackTrace();
            }
        }


    }



    private class GetAllTokensTask extends AsyncTask<ApiConnector, Long, JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params) {
            return params[0].GetAllTokens();

        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            createTokens(jsonArray);
        }
    }








    class task extends AsyncTask<String, String, Void>
    {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        InputStream is = null ;
        String result = "";
        protected void onPreExecute() {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        @Override
        protected Void doInBackground(String... params) {
            String url_select = "http://mejorandroid.es/uscr/demo.php";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is =  httpEntity.getContent();

            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                is.close();
                result=sb.toString();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result "+e.toString());
            }

            return null;

        }
        protected void onPostExecute(Void v) {
            // ambil data dari Json database
            try {
                JSONArray Jarray = new JSONArray(result);

                Log.i("numero imgs: ", Integer.toString(Jarray.length()));
                int nImg = Jarray.length();
                for(int i=0; i<9; i++)
                {
                    JSONObject Jasonobject = null;

                    Jasonobject = Jarray.getJSONObject(nImg-i-1);

                    //get an output on the screen
                    //String id = Jasonobject.getString("id");
                    String name = Jasonobject.getString("id");
                    String db_detail="";

                    db_detail = Jasonobject.getString("url");

                    View img = new ImageView(getBaseContext());
                    ImageView bindImage;
                    bindImage = new ImageView(img.getContext());

                    LinearLayout ll = (LinearLayout)findViewById(R.id.MainLayout);

                    ll.addView(bindImage);

                    View layout = new LinearLayout(getBaseContext());
                    LinearLayout nll;
                    nll = new LinearLayout(layout.getContext());
                    nll.setMinimumHeight(120);

                    ll.addView(nll);

                    View button = new ImageButton(getBaseContext());
                    ImageButton btn = new ImageButton(button.getContext());
                    btn.setImageResource(R.drawable.up_vote);

                    btn.setBackgroundColor(0x00000000);
                    //btn.setScaleX(0.2f);
                    //btn.setScaleY(0.2f);

                    nll.addView(btn);

                    View button2 = new ImageButton(getBaseContext());
                    ImageButton btn2 = new ImageButton(button2.getContext());
                    btn2.setImageResource(R.drawable.down_vote);

                    btn2.setBackgroundColor(0x00000000);


                    nll.addView(btn2);
                    //btn2.setScaleX(0.2f);
                    //btn2.setScaleY(0.2f);
                    //btn2.setLayoutParams(new LinearLayout.LayoutParams(150, 150));

                    String pathToFile = db_detail;
                    DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(bindImage);
                    downloadTask.execute(pathToFile);

                }

                this.progressDialog.dismiss();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data "+e.toString());
            }
        }
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

            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth(); // ((display.getWidth()*20)/100)
            int height = result.getHeight();// ((display.getHeight()*30)/100)

            //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            //bmImage.setLayoutParams(parms);
            bmImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bmImage.setAdjustViewBounds(true);
        }
    }

    private void insertToDatabase(String url){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                //String paramUsername = params[0];
                //String paramAddress = params[1];


                String url = "METIDAAAAA";


                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("url", url));

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
        sendPostReqAsyncTask.execute(url);
    }

}

