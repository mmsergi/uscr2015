package uscr.com.uscr015;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class ApiConnector {

    public JSONArray GetAllTokens() {
        String url_select = "http://mejorandroid.es/uscr/demo.php";

        HttpEntity httpEntity = null;

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

            //Log errors here

        } catch (IOException e) {

            e.printStackTrace();

        }

        //Convert HttpEntity to JSON Array
        JSONArray jsonArray = null;

        if(httpEntity != null) {
            try {
               String entityResponse = EntityUtils.toString(httpEntity);

                Log.e("Entity Response: ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e){

            }catch(IOException e){

            }

        }
        return jsonArray;
    }
}
