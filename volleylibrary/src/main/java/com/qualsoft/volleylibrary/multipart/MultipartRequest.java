package com.qualsoft.volleylibrary.multipart;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.qualsoft.volleylibrary.NetworkOptions;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by suyati on 3/6/17.
 */

public class MultipartRequest extends Request<JSONObject> {
    /**
     * Be careful if you want to change this property!!! We figured out on
     * 17.07.2014 that any other format and string can cause web service json
     * trouble in the Apache cfx jar (v. 2.3.2) of WebService layer. The cfx
     * module expects a content-type exactly like "application/json". What can
     * happen if you do not stick to this rule? -> missing json statements in ws
     * logging -> exception in apache cfx in ws code -> for shares with high
     * risk class a hint should be shown to the customer. This hint will not be
     * shown because of earlier exceptions
     */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json", NetworkOptions.PROTOCOL_CHARSET);
    /**
     * Class constants
     */
    public static String KEY_PICTURE = "file";
    private final HttpEntity mHttpEntity;
    private final Response.Listener mListener;
    Map<String, String> header;

    /**
     * This constructor provide send {@link File} using path {@link String}
     *
     * @param url           Server request {@link java.net.URL} in  {@link String}
     * @param filePath      The path of the {@link File} in {@link String}
     * @param jsonObject    Requested parameter in {@link JSONObject} model
     * @param listener      Response listener {@link Response.Listener}
     * @param errorListener Error listener {@link Response.ErrorListener}
     */
    public MultipartRequest(String url, File filePath, JSONObject jsonObject, Map<String, String> headers,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        header = headers;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath, jsonObject);
    }

    /**
     * This constructor provide send {@link File} using path
     *
     * @param url           Server request {@link java.net.URL} in  {@link String}
     * @param file          Provide instance of the {@link File}
     * @param jsonObject    Requested parameter in {@link JSONObject} model
     * @param listener      Response listener {@link Response.Listener}
     * @param errorListener Error listener {@link Response.ErrorListener}
     */
    public MultipartRequest(String url, File file, JSONObject jsonObject,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file, jsonObject);
    }

    /**
     * This method creating {@link File}
     *
     * @param filePath   The path of the {@link File} in {@link String}
     * @param jsonObject Requested parameter in {@link JSONObject} model
     * @return It will return {@link HttpEntity}
     */
    private HttpEntity buildMultipartEntity(String filePath, JSONObject jsonObject) {
        File file = new File(filePath);
        Log.d("file filePath", "" + filePath);
        Log.d("file length", "" + file.length());
        return buildMultipartEntity(file, jsonObject);
    }

    /*@Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }*/

    /**
     * This method creating {@link MultipartEntityBuilder} attaching values and files
     *
     * @param file       Provide instance of the {@link File}
     * @param jsonObject Requested parameter in {@link JSONObject} model
     * @return It will return {@link HttpEntity}
     */
    private HttpEntity buildMultipartEntity(File file, JSONObject jsonObject) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        String fileName = file.getName();
        FileBody fileBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA, fileName);
        builder.addPart(KEY_PICTURE, fileBody);
        if (jsonObject != null) {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    builder.addPart(key, new StringBody(jsonObject.get(key).toString(), ContentType.DEFAULT_TEXT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.build();
    }

    @Override
    public String getBodyContentType() {
//        return PROTOCOL_CONTENT_TYPE;
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(response.data));
        } catch (JSONException e) {
            Response.error(new VolleyError("Response type not a Json"));
            e.printStackTrace();
        }
        return Response.success(jsonObject, getCacheEntry());
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (header == null)
            header = new HashMap<>();
        header.put("Accept", "application/json");
        return header;
    }
}
