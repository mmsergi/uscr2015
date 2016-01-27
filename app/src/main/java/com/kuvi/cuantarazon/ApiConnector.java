package com.kuvi.cuantarazon;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiConnector {

    public JSONArray GetTokens(int id, boolean favActivity) {
        String url_select;

        if (favActivity) {
            url_select = "http://mejorandroid.es/uscr/getfav.php";
        } else {
            url_select = "http://mejorandroid.es/uscr/demo.php";
        }

        String number = Integer.toString(id);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", number));

        HttpEntity httpEntity = null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

                //Log.e("Entity Response: ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            } catch (JSONException e){

            }catch(IOException e){

            }

        }
        return jsonArray;
    }


}
