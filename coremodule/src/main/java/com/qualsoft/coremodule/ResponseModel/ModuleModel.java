package com.qualsoft.coremodule.ResponseModel;

/**
 * Created by suyati on 3/6/17.
 */

public class ModuleModel {
    private static int $id;
    private static int Module_Id;
    private static String Module_Name;

    public static String getModule_Name() {
        return Module_Name;
    }

    public static void setModule_Name(String module_Name) {
        Module_Name = module_Name;
    }

    public static int get$id() {
        return $id;
    }

    public static void set$id(int $id) {
        ModuleModel.$id = $id;
    }

    public static int getModule_Id() {
        return Module_Id;
    }

    public static void setModule_Id(int module_Id) {
        Module_Id = module_Id;
    }
}
