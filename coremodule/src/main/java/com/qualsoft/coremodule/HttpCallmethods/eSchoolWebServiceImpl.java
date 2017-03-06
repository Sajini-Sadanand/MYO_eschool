package com.qualsoft.coremodule.HttpCallmethods;

import android.content.Context;
import android.util.Log;

import com.qualsoft.coremodule.CoreUtility.Constants;
import com.qualsoft.coremodule.ResponseModel.DashBoardModel;
import com.qualsoft.coremodule.model.Response;

import org.json.JSONObject;

/**
 * Created by suyati on 3/6/17.
 */

public class eSchoolWebServiceImpl implements eSchoolWebServices {

    private Context context;


    public eSchoolWebServiceImpl() {

    }


    public eSchoolWebServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public Response<DashBoardModel> login(String UserName, String MobileNumber, String FullName) throws Exception {
        JSONObject object = new JSONObject();
        object.put("User_Name", UserName);
        object.put("Name", FullName);
        object.put("Mobile_Number", MobileNumber);
        String serverResponse = RestClient.httpPostJsonObj(Constants.BASE_URL + Constants.VERIFICATION_API, object);
        Log.e("serverResponse ", "" + serverResponse);
        return eSchoolParser.parseLogin(serverResponse);
    }
}

