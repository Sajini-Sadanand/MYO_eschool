package com.qualsoft.volleylibrary;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suyati on 3/6/17.
 */

public class JsonRequestWithHeaders extends JsonRequest<String> {
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
    Map<String, String> headers;
    private int mCacheExpireTime = NetworkOptions.CACHE_EXPIRE_TIME;
    /**
     * Constructor method
     *
     * @param method
     * - indicate GET/POST
     * @param url
     * - web service url
     * @param requestBody
     * - json request string
     * @param listener
     * - response listener
     * @param errorListener
     * - error listener
     */
    private String requestBody;

    public JsonRequestWithHeaders(int method, String url, String requestBody, Response.Listener<String> listener, Response.ErrorListener errorListener, int cacheExpireTime) {
        super(method, url, requestBody, listener, errorListener);
        this.requestBody = requestBody;
        this.mCacheExpireTime = cacheExpireTime;
        setRetryPolicy(new DefaultRetryPolicy(NetworkOptions.CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * For including custom headers
     *
     * @param method
     * @param url
     * @param requestBody
     * @param listener
     * @param errorListener
     */
    public JsonRequestWithHeaders(int method, String url, String requestBody, Map<String, String> headers, Response.Listener<String> listener, Response.ErrorListener errorListener, int cacheExpireTime) {
        super(method, url, requestBody, listener, errorListener);
        this.requestBody = requestBody;
        this.headers = headers;
        this.mCacheExpireTime = cacheExpireTime;
        setRetryPolicy(new DefaultRetryPolicy(NetworkOptions.CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public JsonRequestWithHeaders(int method, String url, String requestBody, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.requestBody = requestBody;
        setRetryPolicy(new DefaultRetryPolicy(NetworkOptions.CONNECTION_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * used to perform delivery of the parsed response to their listeners.
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            // default character encoding is ISO-8859-1 so we have to set the
            // UTF_8
            final String json = new String(response.data, "UTF-8");
            final Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
            if (this.mCacheExpireTime == NetworkOptions.CACHE_EXPIRE_TIME) {
                final long now = System.currentTimeMillis();
                entry.ttl = NetworkOptions.CACHE_EXPIRE_TIME + now; // 1 MINUTE
                entry.softTtl = entry.ttl;
            }

            return Response.success(json, entry);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public String getCacheKey() {
        return requestBody;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public Map<String, String> getHeaders() {
        if (headers == null)
            headers = new HashMap<>();
        // headers.put("Accept", "application/json");
        return headers;
    }
}