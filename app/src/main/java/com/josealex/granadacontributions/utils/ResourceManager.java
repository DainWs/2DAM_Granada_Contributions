package com.josealex.granadacontributions.utils;

import com.josealex.granadacontributions.R;

public class ResourceManager {

    public static String getString(int stringID) {
        return (GlobalInformation.mainActivity != null) ?
                GlobalInformation.mainActivity.getString(stringID) : "";
    }

    public static String[] getArray(int arrayId) {
        return (GlobalInformation.mainActivity != null) ?
                GlobalInformation.mainActivity.getResources().getStringArray(arrayId)
                : new String[0];
    }

}
