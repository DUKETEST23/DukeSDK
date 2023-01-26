package com.dukeai.manageloads.model;

import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SubscriptionPlan {

    MemberStatus memberStatus;
    List<String> free = Arrays.asList("BalanceSheet", "Expenses", "YTD", "PL", "IFTA", "fed_tax", "self_tax");
    HashSet<String> plan;

    public SubscriptionPlan(MemberStatus status) {
        Log.i("memberStatus", status.toString());
        memberStatus = status;
    }

    public static MemberStatus getSubscriptionPlanType(String type) {
        Log.i("plantype", type);

//        if(type.toString()==null||type.toString()==""||type.toString()=="none") {
//
//        }
        if (type.equals("")) {
            type = "none";
        } else if (type.equalsIgnoreCase("none")) {
            type = "none";
        }
        return Enum.valueOf(MemberStatus.class, type.toUpperCase());
    }

    public MemberStatus getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public boolean isFeatureIncluded(String feature) {
        if (plan != null) {
            plan.clear();
        }
        plan = new HashSet<>(free);
        switch (memberStatus) {
            case NONE:
            case FREE:
            case BASIC:
            case PREMIUM:
            case ESSENTIAL:
                break;
            case FREE_POD:
            case BASIC_POD:
            case PREMIUM_POD:
            case ESSENTIAL_POD:
            case MONTHLY_POD:
            case ANNUALLY_POD:
            case POD:
                plan.add("POD");
                break;
            default:
                break;

                //Do nothing
        }
        return plan.contains(feature);
    }

    public enum MemberStatus {
        NONE, POD, FREE, BASIC, PREMIUM, ESSENTIAL, FREE_POD, BASIC_POD, PREMIUM_POD, ESSENTIAL_POD, MONTHLY_POD, ANNUALLY_POD
    }

}
