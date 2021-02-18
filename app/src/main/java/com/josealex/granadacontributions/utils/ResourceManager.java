package com.josealex.granadacontributions.utils;

public class ResourceManager {

    public static String getString(int stringID) {
        return (GlobalInformation.mainActivity != null) ?
                GlobalInformation.mainActivity.getString(stringID) : "";
    }



}
