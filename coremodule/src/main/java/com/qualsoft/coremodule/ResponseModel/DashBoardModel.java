package com.qualsoft.coremodule.ResponseModel;

import java.util.List;

/**
 * Created by suyati on 3/6/17.
 */

public class DashBoardModel {
    private List<ModuleModel> ModuleModel;

    public List<com.qualsoft.coremodule.ResponseModel.ModuleModel> getModuleModel() {
        return ModuleModel;
    }

    public void setModuleModel(List<com.qualsoft.coremodule.ResponseModel.ModuleModel> moduleModel) {
        ModuleModel = moduleModel;
    }
}
