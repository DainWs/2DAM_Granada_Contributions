package com.josealex.granadacontributions.utils;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.josealex.granadacontributions.R;

public class NavigationManager {

    public static void navigateTo(int to) {
        if(GlobalInformation.mainActivity != null) {
            Navigation.findNavController(
                    GlobalInformation.mainActivity,
                    R.id.nav_host_fragment
            ).navigate(to);
        }
    }

    public static void navigateTo(int to, Bundle bundle) {
        if(GlobalInformation.mainActivity != null) {
            Navigation.findNavController(
                    GlobalInformation.mainActivity,
                    R.id.nav_host_fragment
            ).navigate(to, bundle);
        }
    }
}
