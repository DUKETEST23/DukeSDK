package com.dukeai.manageloads.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.apiUtils.ApiConstants;

public class NavigationFlowManager {

    public static void openFragments(Fragment fragment, Bundle bundle, FragmentActivity fragmentActivity, int id) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static void openFragments(Fragment fragment, Bundle bundle, FragmentActivity fragmentActivity, int id, String tag) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(id, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
//        fragmentManager.executePendingTransactions();
    }

    public static void openFragmentsWithOutBackStack(Fragment fragment, Bundle bundle, FragmentActivity fragmentActivity, int id, String tag) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static void replaceFragment(Fragment fragment, Bundle bundle, FragmentActivity fragmentActivity, int id){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        ft.replace(id, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void addNewFragment(Fragment fragment,Bundle bundle,FragmentActivity fragmentActivity,int id){
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }        ft.replace(id, fragment);
        ft.commit();
    }

    public static void removeFragment(FragmentActivity activity, Fragment fragment){
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }

}
