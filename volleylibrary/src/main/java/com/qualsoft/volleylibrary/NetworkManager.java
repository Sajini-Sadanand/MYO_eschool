package com.qualsoft.volleylibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qualsoft.volleylibrary.multipart.MultipartRequest;
import com.qualsoft.volleylibrary.multipart.MultipleMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by suyati on 3/6/17.
 */

public class NetworkManager implements NetworkOptions, Response.ErrorListener { //,Response.Listener<String>

    /**
     * Static declarations
     */
    private static final String RESPONSE_TAG = "RESPONSE";
    private static final String REQUEST_TAG = "REQUEST";
    private static int MY_SOCKET_TIMEOUT_MS = 30000;
    private static NetworkManager mNetworkManager = null;

    private OnNetWorkListener mNetWorkListener = null;
    private boolean mIsProgressEnabled = false;
    private ProgressDialog mDialog = null;
    private JsonObjectRequest mRequest;
    private JsonRequestWithHeaders mCustomRequest;
    private RequestQueue mRequestQueue;

    /**
     * Limiting the constructor for accessing public
     *
     * @param mContext {@link Context}
     */
    private NetworkManager(Context mContext) {
        getRequestQueue(mContext);
    }

    /**
     * Singleton managed class
     *
     * @param context {@link Context}
     * @return It will return the Singleton instance of a {@link NetworkManager}
     */
    public static NetworkManager getInstance(Context context) {
        if (mNetworkManager == null)
            mNetworkManager = new NetworkManager(context);

        return mNetworkManager;
    }

    /**
     * Creating {@link ProgressDialog}
     *
     * @param context            {@link Context} of the {@link android.app.Activity}
     * @param message            {@link ProgressDialog#setMessage(CharSequence)}
     * @param cancelTouchOutSide {@link ProgressDialog#setCanceledOnTouchOutside(boolean)}
     */
    public void setProgressDialog(Context context, String message, boolean cancelTouchOutSide) {
        mDialog = null;
        mDialog = new ProgressDialog(context);
        mDialog.setMessage(message);
        mDialog.setCanceledOnTouchOutside(cancelTouchOutSide);
        mIsProgressEnabled = true;
        mDialog.show();

    }

    /**
     * This method sending {@link JSONObject} to server
     *
     * @param requestType Type of the mRequest <font color=black>eg: {@link NetworkOptions#POST_REQUEST}, {@link NetworkOptions#GET_REQUEST}</font>
     * @param url         Server URL
     * @param jsonObject  Requesting parameter in {@link JSONObject}
     * @param requestId   Request id for identifying the requested method
     */
    public void postJsonRequest(int requestType, String url, final JSONObject jsonObject, final int requestId) {
        Log.d(REQUEST_TAG, url + jsonObject.toString());


        mRequest = new JsonObjectRequest(requestType, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    NetworkManager.this.mNetWorkListener.onResponse(response, JSON_OBJECT_REQUEST, requestId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(RESPONSE_TAG, response.toString());
                if (mIsProgressEnabled) {
                    mDialog.dismiss();
                    mIsProgressEnabled = false;
                }
            }
        }, this);

        addToRequestQueue(mRequest, requestId + "");
    }

    /**
     * This method sending {@link JSONObject} to server
     *
     * @param requestType Type of the mRequest <font color=black>eg: {@link NetworkOptions#POST_REQUEST}, {@link NetworkOptions#GET_REQUEST}</font>
     * @param url         Server URL
     * @param requestId   Request id for identifying the requested method
     */
    public void jsonRequestWithHeader(int requestType, String url, final int requestId, Map<String, String> header) {
        // Log.d(REQUEST_TAG, url + jsonObject.toString());
        mCustomRequest = new JsonRequestWithHeaders(requestType, url, "", header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    NetworkManager.this.mNetWorkListener.onResponse(response, JSON_OBJECT_REQUEST, requestId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(RESPONSE_TAG, response);
                if (mIsProgressEnabled) {
                    mDialog.dismiss();
                    mIsProgressEnabled = false;
                }
            }
        }, this, 25000);

        addToRequestQueue(mCustomRequest, requestId + "");
    }

    /**
     * This method sending {@link JSONObject} to server
     *
     * @param requestType Type of the mRequest <font color=black>eg: {@link NetworkOptions#POST_REQUEST}, {@link NetworkOptions#GET_REQUEST}</font>
     * @param url         Server URL
     * @param requestId   Request id for identifying the requested method
     */
    public void postJsonRequestWithHeader(int requestType, String url, final int requestId, Map<String, String> header, String requestBody) {

        mCustomRequest = new JsonRequestWithHeaders(requestType, url, requestBody, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    NetworkManager.this.mNetWorkListener.onResponse(response, JSON_OBJECT_REQUEST, requestId);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(RESPONSE_TAG, response);
                if (mIsProgressEnabled) {
                    mDialog.dismiss();
                    mIsProgressEnabled = false;
                }
            }
        }, this, 25000);

        addToRequestQueue(mCustomRequest, requestId + "");
    }

    /**
     * This method used send files to server.
     *
     * @param url        URL of the server
     * @param file       file path
     * @param jsonObject server params
     * @param requestId  Request id for identifying the requested method
     */
    public void uploadFile(String url, File file, JSONObject jsonObject, final int requestId, Map<String, String> header) {
        try {
          /*  Map<String, String> header = new HashMap<>();
            //  header.put("Content-Type", "application/json");
            header.put(Constants.API_HEADER_KEY, ApiToken);*/
            MultipartRequest multipartRequest = new MultipartRequest(url, file, jsonObject, header, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        NetworkManager.this.mNetWorkListener.onResponse(response, FILE_UPLOAD, requestId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mIsProgressEnabled && mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            }, this);
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addToRequestQueue(multipartRequest, requestId + "");
            //setMySocketTimeoutMs(MY_SOCKET_TIMEOUT_MS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows to specify the Request time out
     *
     * @param request {@link Request}
     */
    private void setRequestTimeout(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    /**
     * Cancel the The {@link Request}
     */
    public void cancelRequest() {
        if (mRequest != null && !mRequest.isCanceled()) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            mRequest.cancel();
        }
    }
  /*  @Override
    public void onResponse(String response) {
        NetworkManager.this.mNetWorkListener.onResponse(response, JSON_OBJECT_REQUEST, mRequestQueue.getSequenceNumber());
        Log.d(RESPONSE_TAG, response);
        if (mIsProgressEnabled) {
            mDialog.dismiss();
            mIsProgressEnabled = false;
        }
    }*/

    @Override
    public void onErrorResponse(VolleyError error) {
        NetworkManager.this.mNetWorkListener.onErrorResponse(error);
        if (mIsProgressEnabled)
            mDialog.dismiss();
    }

    /**
     * Setup {@link Volley} network callback
     *
     * @param onNetworkListener {@link OnNetWorkListener}
     */
    public void setOnNetworkListener(OnNetWorkListener onNetworkListener) {
        this.mNetWorkListener = onNetworkListener;
    }

    /**
     * Adding {@link Request} to queue
     *
     * @param request add request to Queue.
     * @param <T>     Type of a {@link Request}
     */
    private <T> void addToRequestQueue(Request<T> request, String tag) {
        if (mRequestQueue != null) {
            if (tag.length() > 0)
                request.setTag(tag);
            mRequestQueue.add(request);
        }
    }

    /**
     * Getting {@link RequestQueue}
     *
     * @param context {@link Context }
     */
    private void getRequestQueue(Context context) {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }


    /**
     * This method used send multiple files to server.
     *
     * @param url        URL of the server
     * @param file       file path
     * @param jsonObject server params
     * @param requestId  Request id for identifying the requested method
     */
    public void uploadMultipleFile(String url, List<FileObject> file, JSONObject jsonObject, final int requestId) {
        try {
            MultipleMultipartRequest multipartRequest = new MultipleMultipartRequest(url, file, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("onResponse", "" + response);
                    try {
                        NetworkManager.this.mNetWorkListener.onResponse(response, FILE_UPLOAD, requestId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mIsProgressEnabled && mDialog != null) {
                        mDialog.dismiss();
                    }
                }
            }, this);
            addToRequestQueue(multipartRequest, requestId + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting server time out param in Millie Second default value 500 ms
     *
     * @param mySocketTimeoutMs {@link Integer} in MS
     */
    public void setMySocketTimeoutMs(int mySocketTimeoutMs) {
        MY_SOCKET_TIMEOUT_MS = mySocketTimeoutMs;
        if (mRequest != null)
            setRequestTimeout(mRequest);
    }

    /**
     * This method return cached request {@link Cache}
     * Like below you can check for a cached response of an URL before making a network call.
     *
     * @param url Requested {@link java.net.URL}
     * @return It will return the cached value {@link String}
     * @throws UnsupportedEncodingException
     * @throws NullPointerException
     */
    public String getCachedValue(String url) throws UnsupportedEncodingException, NullPointerException {
        Cache cache = mRequestQueue.getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {

            return new String(entry.data, "UTF-8");
        } else {
            return null;
        }
    }

    /**
     * Invalidate means we are invalidating the cached data instead of deleting it.
     * Volley will still uses the cached object until the new data received from server.
     * Once it receives the response from the server it will override the older cached response.
     *
     * @param url Requested {@link java.net.URL}
     */
    public void invalidateCache(String url) {
        if (mRequestQueue != null)
            mRequestQueue.getCache().invalidate(url, true);
    }

    /**
     * If you want disable the cache for a particular url, you can use method as below.
     * Turn off {@link Cache} functionality
     */
    public void turnOffCache() {
        if (mRequest != null)
            mRequest.setShouldCache(false);
    }

    /**
     * If you want enable the cache for a particular url, you can use method as below.
     * Turn on {@link Cache} functionality
     */
    public void turnOnCache() {
        if (mRequest != null)
            mRequest.setShouldCache(true);
    }

    /**
     * Cancel single request
     * Following will cancel all the request with the tag named.
     *
     * @param requestId Requested TAG ID
     */
    public void cancelAllRequest(int requestId) {
        mRequestQueue.cancelAll(requestId + "");
    }


    /**
     * This callback for {@link Volley} Network management
     */
    public interface OnNetWorkListener {

        /**
         * This method will return the trowed exception details from {@link Volley}
         *
         * @param error returns {@link VolleyError}
         */
        void onErrorResponse(VolleyError error);

        /**
         * This listener for listening {@link Volley} network response
         *
         * @param object    object From server its an instance of {@link JSONObject}
         * @param type      Type of the mRequest <font color=black>eg: {@link NetworkOptions#POST_REQUEST}, {@link NetworkOptions#GET_REQUEST}</font>
         * @param requestId Request id for identifying the requested method
         */
        void onResponse(Object object, int type, int requestId) throws IOException, JSONException;
    }


}
