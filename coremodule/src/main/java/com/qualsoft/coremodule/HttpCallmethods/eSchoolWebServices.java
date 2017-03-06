package com.qualsoft.coremodule.HttpCallmethods;

import com.qualsoft.coremodule.ResponseModel.DashBoardModel;
import com.qualsoft.coremodule.model.Response;

/**
 * Created by suyati on 3/6/17.
 */

public interface eSchoolWebServices {
    Response<DashBoardModel> login(String UserName, String MobileNumber, String FullName) throws Exception;
}
