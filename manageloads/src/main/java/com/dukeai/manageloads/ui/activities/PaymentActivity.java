package com.dukeai.manageloads.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
//import com.dukeai.android.BuildConfig;
//import com.dukeai.android.Duke;
//import com.dukeai.android.R;
//import com.dukeai.android.interfaces.MemberStatusUpdateObserver;
//import com.dukeai.android.models.SubscriptionPlan;
//import com.dukeai.android.models.UpdatePaymentModel;
//import com.dukeai.android.utils.CustomProgressLoader;
//import com.dukeai.android.viewModel.FileStatusViewModel;
import com.dukeai.manageloads.BuildConfig;
import com.dukeai.manageloads.Duke;
import com.dukeai.manageloads.R;
import com.dukeai.manageloads.model.SubscriptionPlan;
import com.dukeai.manageloads.utils.CustomProgressLoader;
import com.dukeai.manageloads.viewmodel.FileStatusViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.JsonObject;
import com.revenuecat.purchases.Offering;
import com.revenuecat.purchases.Offerings;
import com.revenuecat.purchases.Package;
import com.revenuecat.purchases.PurchaserInfo;
import com.revenuecat.purchases.Purchases;
import com.revenuecat.purchases.PurchasesError;
import com.revenuecat.purchases.interfaces.MakePurchaseListener;
import com.revenuecat.purchases.interfaces.ReceiveOfferingsListener;
import com.revenuecat.purchases.interfaces.ReceivePurchaserInfoListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PaymentActivity extends BaseActivity implements View.OnClickListener {

    MaterialCardView subscriptionOption1;
    MaterialCardView subscriptionOption2;
    MaterialCardView subscriptionOption3;
    ImageButton closeBtn;
    ImageView applogo;
    TextView subscriptionPeriod1;
    TextView subscriptionPeriod2;
    TextView subscriptionAmount1;
    TextView subscriptionAmount2;
    TextView subscriptionPeriod3;
    TextView subscriptionAmount3;
    TextView subscriptionDescription;
    TextView restorePurchase;
    View separator1;
    View separator2;
    View separator3;
    Button continueBtn;
    Package selectedPackage;
    String selectedPlan = "monthly";
    List<Package> availablePackages;
    FileStatusViewModel fileStatusViewModel;
    JsonObject plan;
    Offering currentOffering;
    CustomProgressLoader customProgressLoader;
    int selectedColor;
    int defaultColor;
    TextView packageDetail;
    TextView privacyDescription;
    boolean isPaywallOnetime = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        customProgressLoader = new CustomProgressLoader(this);
        View view = this.getWindow().getDecorView();
        window.setStatusBarColor(getResources().getColor(R.color.colorBlack, null));
        view.setBackgroundColor(getResources().getColor(R.color.colorBlack, null));
        setContentView(R.layout.activity_payment);
        setInitials();
    }

    private void setInitials() {
        isPaywallOnetime = true;
        applogo = findViewById(R.id.duke_logo);
        subscriptionOption1 = findViewById(R.id.subscription_option1);
        subscriptionOption2 = findViewById(R.id.subscription_option2);
        subscriptionOption3 = findViewById(R.id.subscription_option3);
        closeBtn = findViewById(R.id.close_btn);
        subscriptionPeriod1 = findViewById(R.id.subscription_period1);
        subscriptionPeriod2 = findViewById(R.id.subscription_period2);
        subscriptionAmount1 = findViewById(R.id.subscription_amount1);
        subscriptionAmount2 = findViewById(R.id.subscription_amount2);
        subscriptionPeriod3 = findViewById(R.id.subscription_period3);
        subscriptionAmount3 = findViewById(R.id.subscription_amount3);
        subscriptionDescription = findViewById(R.id.subscription_description);
        separator1 = findViewById(R.id.separator1);
        separator2 = findViewById(R.id.separator2);
        separator3 = findViewById(R.id.separator3);
        continueBtn = findViewById(R.id.continue_btn);
        restorePurchase = findViewById(R.id.restore_purchase);
        packageDetail = findViewById(R.id.subscription_details);
        privacyDescription = findViewById(R.id.privacy_desc);

        subscriptionOption1.setOnClickListener(this);
        subscriptionOption2.setOnClickListener(this);
        subscriptionOption3.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        restorePurchase.setOnClickListener(this);
        fileStatusViewModel = ViewModelProviders.of(this).get(FileStatusViewModel.class);
        showHideOptions();
        identifyUser();
        setUIInitials();
        getOfferings();
    }

    private void showHideOptions() {
        if(isPaywallOnetime) {
            subscriptionOption1.setVisibility(View.GONE);
            subscriptionOption2.setVisibility(View.GONE);
            subscriptionDescription.setVisibility(View.GONE);
            restorePurchase.setVisibility(View.GONE);
            packageDetail.setText("Convenience fee for automated invoicing and payment tracking");
            selectedPlan = "invoice_processing_fee";
            // Create LayoutParams to set the margins
            ViewGroup.MarginLayoutParams layoutParamsAppLogo = (ViewGroup.MarginLayoutParams) applogo.getLayoutParams();
            ViewGroup.MarginLayoutParams layoutParamsPrivacyDesc = (ViewGroup.MarginLayoutParams) privacyDescription.getLayoutParams();
            layoutParamsAppLogo.setMargins(0, 200, 0, 0);
            layoutParamsPrivacyDesc.setMargins(0, 0, 0, 60);
        } else {
            subscriptionOption3.setVisibility(View.GONE);
            packageDetail.setText("Get unlimited access. Unlimited document upload.");
        }
    }

    private void setUIInitials() {
//        if(BuildConfig.theme.equals("truck")) {
//            applogo.setImageResource(R.drawable.truck);
////            applogo.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT-100;
////            applogo.getLayoutParams().height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
//            selectedColor = getResources().getColor(R.color.colorTTT2, null);
//            defaultColor = getResources().getColor(R.color.colorGray, null);
//            setSubscriptionOptionColorScheme(selectedColor, defaultColor);
//            applogo.requestLayout();
//        } else {
//            selectedColor = getResources().getColor(R.color.yellow_BBAE27, null);
//            defaultColor = getResources().getColor(R.color.colorGray, null);
//        }

        selectedColor = getResources().getColor(R.color.yellow_BBAE27, null);
        defaultColor = getResources().getColor(R.color.colorGray, null);

        if(isPaywallOnetime) {
            setSubscriptionOptionColorScheme(selectedColor, defaultColor);
        }
    }

    private void identifyUser() {
        if(Duke.uniqueUserId.length()>0) {
            String appUserId = Duke.uniqueUserId + "#" + Duke.fileStatusModel.getRequest().getCustomerId();
            Purchases.getSharedInstance().identify(appUserId, new ReceivePurchaserInfoListener() {
                @Override
                public void onReceived(@NonNull PurchaserInfo purchaserInfo) {
                    Log.d("User uniquely identify", purchaserInfo.toString());
                }

                @Override
                public void onError(@NonNull PurchasesError error) {
                    Log.d("User could not be found", error.toString());
                }
            });
        }
    }

    private void getOfferings() {
        Purchases.getSharedInstance().getOfferings(new ReceiveOfferingsListener() {
            @Override
            public void onReceived(@NonNull Offerings offerings) {
                if (offerings.getCurrent() != null) {
                    availablePackages = offerings.getCurrent().getAvailablePackages();
                    currentOffering = offerings.getCurrent();
                    System.out.println("#Offering:" + currentOffering.toString());
                    Log.d("#Offering:" , currentOffering.toString());
                    // Display packages for sale
                    if (currentOffering.getMonthly() != null) {
                        SkuDetails monthlyProduct = currentOffering.getMonthly().getProduct();
                        subscriptionAmount1.setText(monthlyProduct.getPrice());
                        String freeTrialEndDate = getFreeTrialEndDate(monthlyProduct.getFreeTrialPeriod());
                        subscriptionDescription.setText("Include 14-day free trial. Cancel before " + freeTrialEndDate + " and nothing will be billed.");
                        Log.d("product 1", monthlyProduct.toString());
                        // Get the price and introductory period from the SkuDetails
                    }

                    if (currentOffering.getAnnual() != null) {
                        SkuDetails annualProduct = currentOffering.getAnnual().getProduct();
                        subscriptionAmount2.setText(annualProduct.getPrice());
                        Log.d("product 2", annualProduct.toString());
                        // Get the price and introductory period from the SkuDetails
                    }


//                    currentOffering.getPackage("One-time") != null

                    if(currentOffering.getPackage("One-time") != null) {
                        SkuDetails customProduct = currentOffering.getPackage("One-time").getProduct();
                        subscriptionAmount3.setText(customProduct.getPrice());
                        Log.d("product 3", customProduct.toString());
                    }
                }
            }

            private String getFreeTrialEndDate(String freeTrialPeriod) {
                int noOfWeek = Integer.parseInt(String.valueOf(freeTrialPeriod.charAt(1)));
                Date date = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.WEEK_OF_MONTH, noOfWeek);
                date = c.getTime();
                SimpleDateFormat DateFor = new SimpleDateFormat("MMMM d");
                return DateFor.format(date);
            }

            @Override
            public void onError(@NonNull PurchasesError error) {
                // An error occurred
            }
        });
    }

    @Override
    public void onClick(View v) {
//        int selectedColor = getResources().getColor(R.color.yellow_BBAE27, null);
//        int defaultColor = getResources().getColor(R.color.colorGray, null);
//        int temp = selectedColor;
//        selectedColor = defaultColor;
//        defaultColor = temp;
//        String theme = BuildConfig.theme;

//        v.setBackgroundColor(getResources().getColor(R.color.colorBlack, null));

        int id = v.getId();
        if (id == R.id.subscription_option1) {
            subscriptionOption1.setStrokeColor(selectedColor);
            subscriptionOption2.setStrokeColor(defaultColor);
            subscriptionPeriod1.setTextColor(selectedColor);
            subscriptionPeriod2.setTextColor(defaultColor);
            subscriptionAmount1.setTextColor(selectedColor);
            subscriptionAmount2.setTextColor(defaultColor);
            separator1.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            separator2.setBackgroundTintList(ColorStateList.valueOf(defaultColor));

            subscriptionOption3.setStrokeColor(defaultColor);
            subscriptionPeriod3.setTextColor(defaultColor);
            subscriptionAmount3.setTextColor(defaultColor);
            separator3.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
            selectedPlan = "monthly";
        } else if (id == R.id.subscription_option2) {
            subscriptionOption2.setStrokeColor(selectedColor);
            subscriptionOption1.setStrokeColor(defaultColor);
            subscriptionPeriod1.setTextColor(defaultColor);
            subscriptionPeriod2.setTextColor(selectedColor);
            subscriptionAmount1.setTextColor(defaultColor);
            subscriptionAmount2.setTextColor(selectedColor);
            separator2.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            separator1.setBackgroundTintList(ColorStateList.valueOf(defaultColor));

            subscriptionOption3.setStrokeColor(defaultColor);
            subscriptionPeriod3.setTextColor(defaultColor);
            subscriptionAmount3.setTextColor(defaultColor);
            separator3.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
            selectedPlan = "annual";
        } else if (id == R.id.subscription_option3) {
            subscriptionOption2.setStrokeColor(defaultColor);
            subscriptionOption1.setStrokeColor(defaultColor);
            subscriptionPeriod1.setTextColor(defaultColor);
            subscriptionPeriod2.setTextColor(defaultColor);
            subscriptionAmount1.setTextColor(defaultColor);
            subscriptionAmount2.setTextColor(defaultColor);
            separator1.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
            separator2.setBackgroundTintList(ColorStateList.valueOf(defaultColor));

            subscriptionOption3.setStrokeColor(selectedColor);
            subscriptionPeriod3.setTextColor(selectedColor);
            subscriptionAmount3.setTextColor(selectedColor);
            separator3.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            selectedPlan = "invoice_processing_fee";
        } else if (id == R.id.close_btn) {
            returnToHome();
        } else if (id == R.id.continue_btn) {
            makePurchase();
        } else if (id == R.id.restore_purchase) {
            restorePurchase();
        }
    }

    private void returnToHome() {
        customProgressLoader.showDialog();
        Intent intent = new Intent(PaymentActivity.this, DashboardActivity.class);
        intent.putExtra("isClosed", true);
        startActivity(intent);
        finish();
        customProgressLoader.hideDialog();
    }

    private void restorePurchase() {
        Purchases.getSharedInstance().restorePurchases(new ReceivePurchaserInfoListener() {
            @Override
            public void onReceived(@NonNull PurchaserInfo purchaserInfo) {
                if(purchaserInfo.getEntitlements().get("DocumentUpload").isActive()) {
                    if (purchaserInfo.getEntitlements().get("productIdenfier").equals("duke_monthly") || purchaserInfo.getEntitlements().get("productIdenfier").equals("ttt_monthly")) {
                        // Unlock that great "pro" content
                        Duke.subscriptionPlan.setMemberStatus(SubscriptionPlan.MemberStatus.MONTHLY_POD);
                        returnToHome();
                    } else {
                        Duke.subscriptionPlan.setMemberStatus(SubscriptionPlan.MemberStatus.ANNUALLY_POD);
                        returnToHome();
                    }
                }
            }

            @Override
            public void onError(@NonNull PurchasesError error) {
                Log.d("Purchase restored", error.getMessage());
            }
        });
    }

    public void makePurchase() {
        if(selectedPlan.equals("monthly")) {
            selectedPackage = availablePackages.get(0);
        } else if (selectedPlan.equals("annual")) {
            selectedPackage = currentOffering.getAnnual();
        } else {

            selectedPackage = currentOffering.getPackage("One-time");

//            List<Package> packages = currentOffering.getAvailablePackages();
//            System.out.println("## Available packages" + Arrays.asList(packages));
        }
        if (selectedPackage != null) {
            customProgressLoader.showDialog();
            Purchases.getSharedInstance().purchasePackage(
                    this,
                    selectedPackage,
                    new MakePurchaseListener() {
                        @Override
                        public void onError(@NonNull PurchasesError error, boolean userCancelled) {
                            customProgressLoader.hideDialog();
                            Log.d("Payment failed", error.toString());

                        }

                        @Override
                        public void onCompleted(@NonNull Purchase purchase, @NonNull PurchaserInfo purchaserInfo) {
                            if (purchaserInfo.getEntitlements().get("DocumentUpload").isActive()) {
                                // Unlock that great "pro" content
                                Log.d("Purchase successfull", purchaserInfo.toString());
                                if(purchaserInfo.getEntitlements().get("DocumentUpload").getProductIdentifier().equals("duke_monthly") || purchaserInfo.getEntitlements().get("DocumentUpload").getProductIdentifier().equals("ttt_monthly")){
                                    updatePayment("Premium_Monthly");
                                } else {
                                    updatePayment("Premium_Annually");
                                }

//                                if(purchaserInfo.getEntitlements().get("DocumentUpload").getProductIdentifier().equals("One-time")) {
//                                    updatePayment("Premium_Annually");
//                                }
                                customProgressLoader.hideDialog();
                            }
                            returnToHome();
                        }
                    }
            );
        }
    }

    private void updatePayment(String subscriptionType) {
        plan = new JsonObject();
        plan.addProperty("plan" ,subscriptionType);

//        updatePaymentStatus(new MemberStatusUpdateObserver() {
//            @Override
//            public void onChanged(String status, UpdatePaymentModel updatePaymentModel) {
//                if(status.toLowerCase().equals("success")) {
//                    Log.d("Purchase successfull", updatePaymentModel.getMsg());
//                } else {
//                    Log.d("Purchase failed", updatePaymentModel.getMsg());
//                }
//            }
//        });
    }

    public void setSubscriptionOptionColorScheme(int selectedColor, int defaultColor) {
        if(isPaywallOnetime) {
            subscriptionOption3.setStrokeColor(selectedColor);
            subscriptionPeriod3.setTextColor(selectedColor);
            subscriptionAmount3.setTextColor(selectedColor);
            separator3.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
        } else {
            subscriptionOption1.setStrokeColor(selectedColor);
            subscriptionOption2.setStrokeColor(defaultColor);
            subscriptionPeriod1.setTextColor(selectedColor);
            subscriptionPeriod2.setTextColor(defaultColor);
            subscriptionAmount1.setTextColor(selectedColor);
            subscriptionAmount2.setTextColor(defaultColor);
            separator1.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            separator2.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        }

    }

//    private void updatePaymentStatus(MemberStatusUpdateObserver memberStatusUpdateObserver) {
//
//        fileStatusViewModel.memberStatusUpdate(plan).observe(this, new Observer<UpdatePaymentModel>() {
//            @Override
//            public void onChanged(UpdatePaymentModel updatePaymentModel) {
//                try {
//                    String msg = "";
//                    if (updatePaymentModel != null && updatePaymentModel.getMsg() == null) {
//                        if(updatePaymentModel.getMsg().contentEquals("Subscription updated")) {
//                            msg = "SUCCESS";
//                        }
//                    } else {
//                        //Error
//                        msg = "ERROR";
//                    }
//                    memberStatusUpdateObserver.onChanged(msg, updatePaymentModel);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    /**Error occurred**/
//                    memberStatusUpdateObserver.onChanged("ERROR", updatePaymentModel);
//                }
//            }
//        });
//    }
}