package com.qualsoft.volleylibrary.multipart;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.qualsoft.volleylibrary.FileObject;

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
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by suyati on 3/6/17.
 */

public class MultipleMultipartRequest extends Request<JSONObject> {

    /**
     * Class constants
     */
    public static String KEY_PICTURE = "image";
    public static String KEY_VIDEO = "video";
    private final HttpEntity mHttpEntity;

    private final Response.Listener mListener;

    /**
     * This constructor provide send {@link File} using path {@link String}
     *
     * @param url           Server request {@link java.net.URL} in  {@link String}
     * @param filePaths     The path of the {@link File} in {@link String}
     * @param jsonObject    Requested parameter in {@link JSONObject} model
     * @param listener      Response listener {@link Response.Listener}
     * @param errorListener Error listener {@link Response.ErrorListener}
     */
    public MultipleMultipartRequest(String url, String filePaths, JSONObject jsonObject,
                                    Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);

        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePaths, jsonObject);
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
    public MultipleMultipartRequest(String url, List<FileObject> file, JSONObject jsonObject,
                                    Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        super(Request.Method.POST, url, errorListener);
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

        return buildMultipartEntity(file, jsonObject);
    }

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

        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                builder.addPart(key, new StringBody(jsonObject.get(key).toString(), ContentType.DEFAULT_TEXT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    /**
     * This method creating {@link MultipartEntityBuilder} attaching values and files
     *
     * @param files      Provide instance of the {@link File}
     * @param jsonObject Requested parameter in {@link JSONObject} model
     * @return It will return {@link HttpEntity}
     */
    private HttpEntity buildMultipartEntity(List<FileObject> files, JSONObject jsonObject) throws UnsupportedEncodingException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (FileObject file : files) {

            String fileName = file.getFile().getName();
            FileBody fileBody = new FileBody(file.getFile(), ContentType.MULTIPART_FORM_DATA, fileName);

            if (file.getType().equalsIgnoreCase(FileObject.TYPE_IMAGE))
                builder.addPart(KEY_PICTURE, fileBody);
            else if (file.getType().equalsIgnoreCase(FileObject.TYPE_VIDEO))
                builder.addPart(KEY_VIDEO, fileBody);

            if (files.size() == 1 && file.getType().equalsIgnoreCase(FileObject.TYPE_IMAGE))
                builder.addPart(KEY_VIDEO, new StringBody("", ContentType.DEFAULT_TEXT));
            if (files.size() == 1 && file.getType().equalsIgnoreCase(FileObject.TYPE_VIDEO))
                builder.addPart(KEY_PICTURE, new StringBody("", ContentType.DEFAULT_TEXT));
        }
        if (files == null || files.size() == 0) {
            builder.addPart(KEY_PICTURE, new StringBody("", ContentType.DEFAULT_TEXT));
            builder.addPart(KEY_VIDEO, new StringBody("", ContentType.DEFAULT_TEXT));
        }
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                builder.addPart(key, new StringBody(jsonObject.get(key).toString(), ContentType.DEFAULT_TEXT));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    @Override
    public String getBodyContentType() {
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

}


