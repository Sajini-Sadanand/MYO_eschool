package com.qualsoft.coremodule.model;

import android.content.Context;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by suyati on 3/6/17.
 */

public class Response<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1262031876145023450L;
    Context mContext;
    private Throwable throwable;
    private boolean noException = false;
    private String serverMessage;
    private int serverCode;
    private T result;

    public Response() {

    }

    public Response(Throwable throwable) {
        this.throwable = throwable;
        handleException();
    }

    public int getServerCode() {
        return serverCode;
    }

    public void setServerCode(int serverCode) {
        this.serverCode = serverCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
        handleException();

    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return noException;
    }

    public void setSuccess(boolean isSuccess) {
        noException = isSuccess;
    }

    public void handleException() {
        //noException = false;
        if (throwable instanceof UnknownHostException) {
            serverMessage = "Please check your internet connection";
        } else if (throwable instanceof SocketException) {
            serverMessage = "Connection timed out";
        } else if (throwable instanceof IOException) {
            serverMessage = "Oops!! Something went wrong. Please try again after some time.";//"Please check your internet connection";
        } else {
            serverMessage = "Oops!!! Something went wrong. Please try again after some time.";
        }
    }


}

