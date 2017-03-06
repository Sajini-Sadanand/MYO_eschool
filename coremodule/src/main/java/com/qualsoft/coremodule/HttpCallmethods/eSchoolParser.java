package com.qualsoft.coremodule.HttpCallmethods;

import com.google.gson.Gson;
import com.qualsoft.coremodule.CoreUtility.Constants;
import com.qualsoft.coremodule.ResponseModel.DashBoardModel;
import com.qualsoft.coremodule.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by suyati on 3/6/17.
 */

public class eSchoolParser {

    public static Response<DashBoardModel> parseLogin(
            String serverResponse) throws IOException, JSONException, NullPointerException {
        Response<DashBoardModel> response = new Response<>();
        JSONObject jsonObject = new JSONObject(serverResponse);
        // JSONObject obj = jsonObject.getJSONObject(Constants.API_RESPONSE);


        if (jsonObject.has("error")) {
            response.setSuccess(false);
            if (jsonObject.has("error_description"))
                response.setServerMessage(jsonObject.getString("error_description"));
            else
                response.setServerMessage(jsonObject.getString("error"));

            // response.setServerMessage(obj.getString(Constants.API_MESSAGE));
        } else {
            Gson gson = new Gson();
            DashBoardModel mProfileModel = gson.fromJson(jsonObject.toString(), DashBoardModel.class);
            response.setSuccess(true);
            response.setResult(mProfileModel);
        }
        return response;
    }

    public static Response<HashMap<String, String>> parseSucessMessage(
            String serverResponse) throws IOException, JSONException, NullPointerException {
        Response<HashMap<String, String>> response = new Response<>();
        JSONObject jsonObject = new JSONObject(serverResponse);
        // JSONObject obj = jsonObject.getJSONObject(Constants.API_RESPONSE);
        if (jsonObject.has(Constants.API_STATUS)) {
            response.setServerCode(jsonObject.getInt(Constants.API_STATUS));
            if (jsonObject.getInt(Constants.API_STATUS) == 1000) {

                HashMap<String, String> hashMap = new HashMap<>();
                // hashMap.put("Message",jsonObject.getString("message"));
                response.setServerMessage(jsonObject.getString(Constants.API_MESSAGE));
                response.setResult(hashMap);
                response.setSuccess(true);
                // response.setServerMessage(obj.getString(Constants.API_MESSAGE));
            } else {
                response.setSuccess(false);
                response.setServerMessage(jsonObject.getString(Constants.API_MESSAGE));
            }
        } else {
            response.setSuccess(false);
            response.setServerMessage(jsonObject.getString("message"));
        }
        return response;
    }
}
//{"status":1000,"message":"Success"}