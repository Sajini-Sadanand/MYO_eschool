package com.qualsoft.coremodule.HttpCallmethods;

import com.qualsoft.coremodule.CoreUtility.Constants;
import com.qualsoft.coremodule.ResponseModel.DashBoardModel;
import com.qualsoft.coremodule.ResponseModel.ModuleModel;
import com.qualsoft.coremodule.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by suyati on 3/6/17.
 */

public class eSchoolParser {

    public static Response<DashBoardModel> parseLogin(
            String serverResponse) throws IOException, JSONException, NullPointerException {
        Response<DashBoardModel> response = new Response<>();
        JSONArray jsonArray = new JSONArray(serverResponse);
        List<ModuleModel> moduleModels = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            ModuleModel moduleModel = new ModuleModel();
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            moduleModel.set$id(jsonObj.getInt("$id"));
            moduleModel.setModule_Id(jsonObj.getInt("Module_Id"));
            moduleModel.setModule_Name(jsonObj.getString("Module_Name"));
            moduleModels.add(moduleModel);
        }
        DashBoardModel dashBoardModel = new DashBoardModel();
        dashBoardModel.setModuleModel(moduleModels);
        response.setResult(dashBoardModel);
        response.setSuccess(true);
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