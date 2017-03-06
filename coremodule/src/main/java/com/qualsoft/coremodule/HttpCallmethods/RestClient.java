package com.qualsoft.coremodule.HttpCallmethods;

import android.util.Log;

import com.qualsoft.coremodule.CoreUtility.Constants;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suyati on 3/6/17.
 */

public class RestClient {

    static int mConnectionTimeOut = 20000;
    static int mSocketTimeOut = 15000;

    public static String httpGet(String argurl) throws Exception {
        InputStream inputStream = null;
        String response = "";
        HttpURLConnection urlConnection = null;

                /* forming th java.net.URL object */
        URL url = new URL(argurl);

        urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        urlConnection.setConnectTimeout(mConnectionTimeOut);
                /* for Get request */
        urlConnection.setRequestMethod("GET");

        inputStream = new BufferedInputStream(urlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);
        urlConnection.disconnect();

        return response;

    }

    public static String httpPost(String argurl) throws Exception {
        InputStream inputStream = null;
        String response = "";
        HttpURLConnection urlConnection = null;

                /* forming th java.net.URL object */
        URL url = new URL(argurl);

        urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        urlConnection.setConnectTimeout(mConnectionTimeOut);
                /* for Get request */
        urlConnection.setRequestMethod("POST");

        inputStream = new BufferedInputStream(urlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);
        urlConnection.disconnect();

        return response;

    }


   /* public static String httpPostAsMultipart(String url,
                                             File file, String headerKey, String headerValue) throws IOException,
            JSONException {
        HttpParams httpparameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpparameters,
                mConnectionTimeOut);
        HttpConnectionParams.setSoTimeout(httpparameters, mSocketTimeOut);

        HttpClient httpClient = new DefaultHttpClient(httpparameters);
        HttpPost post = new HttpPost(url);
        Log.e("headerKey", "" + headerKey);
        Log.e("headerValue", "" + headerValue);

        post.setHeader(headerKey, "Bearer " + headerValue);

        post.setHeader("Content-Disposition", "multipart/form-data");
        post.setHeader("filename", "image.jpg");
        post.setHeader("Content-Type", "image/jpeg");
        MultipartEntity multipartEntity = new MultipartEntity();

       *//* Iterator<?> keys = jsonObject.keys();
        while (keys.hasNext()) { Content-Disposition: form-data; name="fieldNameHere"; filename="IMG_0756 - Copy.JPG"
Content-Type: image/jpeg
            String key = (String) keys.next();
            multipartEntity.addPart(key, new StringBody(jsonObject.get(key)
                    .toString()));

        }*//*
        if (file == null) {
            Log.e("file", "" + file);
            //multipartEntity.addPart("files",new StringBody(""));
        } else {
            FileBody filebodyImage = new FileBody(file, "image/jpeg");
            Log.e("file lenght", "" + file.length());
            //   FormBodyPart formBodyPart=new FormBodyPart();
            multipartEntity.addPart("file", filebodyImage);
            // multipartEntity.addPart("files", new FileBody(file, "image/jpeg"));
        }
        post.setEntity(multipartEntity);
        HttpResponse response = httpClient.execute(post);
        return EntityUtils.toString(response.getEntity());


    }*/


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    public static String httpGetWithHeader(String argurl, String headerKey,
                                           String headerValue) throws Exception {
        InputStream inputStream = null;
        String response = "";
        HttpURLConnection urlConnection = null;

                /* forming th java.net.URL object */
        URL url = new URL(argurl);

        urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        urlConnection.setRequestProperty(headerKey, "Bearer " + headerValue);
        urlConnection.setConnectTimeout(mConnectionTimeOut);
                /* for Get request */
        urlConnection.setRequestMethod("GET");

        inputStream = new BufferedInputStream(urlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);

        urlConnection.disconnect();

        return response;

    }

    public static String httpPostString(String argurl, String postParams)
            throws Exception {

        String response = null;
        URL url = new URL(argurl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestMethod("POST");

        //httpUrlConnection.setRequestProperty("User-Agent", "GYUserAgentAndroid");
        httpUrlConnection.setRequestProperty("Content-Type", "application/json");

                  /* optional request header */
        httpUrlConnection.setRequestProperty("Accept", "application/json");
        // httpUrlConnection.setConnectTimeout(mConnectionTimeOut);
        httpUrlConnection.setUseCaches(false);

        DataOutputStream outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());

        outputStream.writeBytes(postParams.toString());
        outputStream.flush();
        outputStream.close();
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

            response = convertInputStreamToString(inputStream);
        } catch (IOException e) {
            response = Constants.BAD_REQUEST;
        }
        httpUrlConnection.disconnect();
        Log.e("new HTTP", response);

        return response;
    }

    public static String httpPostJsonObj(String argurl, JSONObject postParams)
            throws Exception {
        String response = null;
        URL url = new URL(argurl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestMethod("POST");

        //httpUrlConnection.setRequestProperty("User-Agent", "GYUserAgentAndroid");
        httpUrlConnection.setRequestProperty("Content-Type", "application/json");

                  /* optional request header */
        httpUrlConnection.setRequestProperty("Accept", "application/json");
        httpUrlConnection.setConnectTimeout(mConnectionTimeOut);
        httpUrlConnection.setUseCaches(false);

        DataOutputStream outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());

        outputStream.writeBytes(postParams.toString());
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);

        httpUrlConnection.disconnect();
        Log.e("new HTTP", response);

        return response;
    }

    public static String httpPostJsonObjWithHeader(String argurl, JSONObject postParams, String headerKey, String headerValue)
            throws Exception {
        String response = null;
        URL url = new URL(argurl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestMethod("POST");

        //httpUrlConnection.setRequestProperty("User-Agent", "GYUserAgentAndroid");
        httpUrlConnection.setRequestProperty("Content-Type", "application/json");

                  /* optional request header */
        httpUrlConnection.setRequestProperty("Accept", "application/json");
        httpUrlConnection.setConnectTimeout(mConnectionTimeOut);

        httpUrlConnection.setRequestProperty(headerKey, "Bearer " + headerValue);
        httpUrlConnection.setUseCaches(false);

        DataOutputStream outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());

        outputStream.writeBytes(postParams.toString());
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);

        httpUrlConnection.disconnect();
        Log.e("new HTTP", response);

        return response;
    }

    public static String httpPostWithHeader(String argurl, String headerKey, String headerValue)
            throws Exception {

        String response = null;
        URL url = new URL(argurl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestMethod("POST");

        //httpUrlConnection.setRequestProperty("User-Agent", "GYUserAgentAndroid");
        httpUrlConnection.setRequestProperty("Content-Type", "application/json");

                  /* optional request header */
        httpUrlConnection.setRequestProperty("Accept", "application/json");
        httpUrlConnection.setConnectTimeout(mConnectionTimeOut);

        httpUrlConnection.setRequestProperty(headerKey, "Bearer " + headerValue);
        httpUrlConnection.setUseCaches(false);


        InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);

        httpUrlConnection.disconnect();
        Log.e("new HTTP", response);

        return response;
    }

    public static String httpPostTextWithHeader(String argurl, String text, String headerKey, String headerValue)
            throws Exception {

        String response = null;
        URL url = new URL(argurl);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestMethod("POST");

        //httpUrlConnection.setRequestProperty("User-Agent", "GYUserAgentAndroid");
        httpUrlConnection.setRequestProperty("Content-Type", "text/html");

                  /* optional request header */
        httpUrlConnection.setRequestProperty("Accept", "application/json");
        httpUrlConnection.setConnectTimeout(mConnectionTimeOut);

        httpUrlConnection.setRequestProperty(headerKey, "Bearer " + headerValue);
        httpUrlConnection.setUseCaches(false);

        DataOutputStream outputStream = new DataOutputStream(httpUrlConnection.getOutputStream());


        outputStream.writeBytes(text);
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = new BufferedInputStream(httpUrlConnection.getInputStream());

        response = convertInputStreamToString(inputStream);
        httpUrlConnection.disconnect();

        Log.e("new HTTP", response);

        return response;
    }


}
