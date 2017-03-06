package com.qualsoft.volleylibrary;

import com.android.volley.Request;

/**
 * Created by suyati on 3/6/17.
 */

public interface NetworkOptions {
    /**
     * Request type
     */
    int POST_REQUEST = Request.Method.POST;
    int GET_REQUEST = Request.Method.GET;
    int PUT_REQUEST = Request.Method.PUT;
    int DELETE_REQUEST = Request.Method.DELETE;
    int HEAD_REQUEST = Request.Method.HEAD;
    // volley cache expiring time
    int CACHE_EXPIRE_TIME = 5 * 60 * 1000;
    // timeout for webservice connections
    int CONNECTION_TIMEOUT = 30 * 1000;
    String PROTOCOL_CHARSET = "utf-8";
    /**
     * Request methods
     */
    int JSON_OBJECT_REQUEST = 0x3;
    int STRING_REQUEST = 0x4;
    int FILE_UPLOAD = 0x5;
}
