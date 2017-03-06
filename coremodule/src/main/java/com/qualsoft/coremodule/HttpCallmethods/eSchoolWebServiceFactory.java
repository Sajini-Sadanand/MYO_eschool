package com.qualsoft.coremodule.HttpCallmethods;

import android.content.Context;

/**
 * Created by suyati on 3/6/17.
 */

public class eSchoolWebServiceFactory {
    public static final eSchoolWebServices getService() {
        return new eSchoolWebServiceImpl();
    }

    public static final eSchoolWebServices getService(Context context) {
        return new eSchoolWebServiceImpl(context);
    }
}
