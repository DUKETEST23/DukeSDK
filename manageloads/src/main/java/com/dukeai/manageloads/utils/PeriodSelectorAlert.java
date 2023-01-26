package com.dukeai.manageloads.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dukeai.manageloads.R;
import com.dukeai.manageloads.interfaces.OnPeriodSubmitListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class PeriodSelectorAlert extends Dialog {

    ImageView closeIcon;
    Button submitButton;
    TextView fromDateError;
    TextView toDateError;
    //    TextInputEditText fromDateField;
//    TextInputEditText toDateField
    TextInputLayout fromDateField;
    TextInputLayout toDateField;
    TextInputLayout fromDateLayout;
    TextView tvFromDate;
    TextView tvToDate;

    Calendar fromCalendar = Calendar.getInstance();
    Calendar toCalender = Calendar.getInstance();

    boolean didSetFromDate = false;
    boolean didSetToDate = false;

    public PeriodSelectorAlert(final Context context, final OnPeriodSubmitListener periodSubmitListener, View.OnClickListener closeButtonClickListener) {
        super(context);
        View view = View.inflate(context, R.layout.custom_filter, null);
        initView(view);
        setContentView(view);
        this.setCancelable(false);

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    //
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        fromCalendar.set(Calendar.YEAR, year);
                        fromCalendar.set(Calendar.MONTH, monthOfYear);
                        fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        tvFromDate.setText(Utilities.getFormattedDate(fromCalendar));

                        fromDateError.setVisibility(View.GONE);
                        didSetFromDate = true;
                    }

                };

                DatePickerDialog dpDialog = new DatePickerDialog(context, date, fromCalendar
                        .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                        fromCalendar.get(Calendar.DAY_OF_MONTH));
                if (didSetToDate) {
                    dpDialog.getDatePicker().setMaxDate(toCalender.getTimeInMillis());
                } else {
                    dpDialog.getDatePicker().setMaxDate(new Date().getTime());
                }
                dpDialog.show();
            }

        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            toCalender.set(Calendar.YEAR, year);
                            toCalender.set(Calendar.MONTH, monthOfYear);
                            toCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            tvToDate.setText(Utilities.getFormattedDate(toCalender));


                            toDateError.setVisibility(View.GONE);
                            didSetToDate = true;
                        }

                    };

                    DatePickerDialog dpDialog = new DatePickerDialog(context, toDate, toCalender
                            .get(Calendar.YEAR), toCalender.get(Calendar.MONTH),
                            toCalender.get(Calendar.DAY_OF_MONTH));
                    if (didSetFromDate) {
                        dpDialog.getDatePicker().setMinDate(fromCalendar.getTimeInMillis());
                    }
                    dpDialog.getDatePicker().setMaxDate(new Date().getTime());
                    dpDialog.show();
                }
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (didSetFromDate && didSetToDate) {
                    periodSubmitListener.onSubmitClick(fromCalendar, toCalender);
                } else {

                    if (didSetToDate) {
                        toDateError.setVisibility(View.GONE);
                    } else {
                        toDateError.setVisibility(View.VISIBLE);
                    }

                    if (didSetFromDate) {
                        fromDateError.setVisibility(View.GONE);
                    } else {
                        fromDateError.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        closeIcon.setOnClickListener(closeButtonClickListener);


        toDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        toCalender.set(Calendar.YEAR, year);
                        toCalender.set(Calendar.MONTH, monthOfYear);
                        toCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        toDateField.getEditText().setText(Utilities.getFormattedDate(toCalender));
                        Log.d("PM => ", "To Date " + Utilities.getFormattedDate(toCalender));
                        toDateError.setVisibility(View.GONE);
                        didSetToDate = true;
                    }

                };

                DatePickerDialog dpDialog = new DatePickerDialog(context, toDate, toCalender
                        .get(Calendar.YEAR), toCalender.get(Calendar.MONTH),
                        toCalender.get(Calendar.DAY_OF_MONTH));
                if (didSetFromDate) {
                    dpDialog.getDatePicker().setMinDate(fromCalendar.getTimeInMillis());
                }
                dpDialog.getDatePicker().setMaxDate(new Date().getTime());
                dpDialog.show();
            }
        });

        fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    //
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        fromCalendar.set(Calendar.YEAR, year);
                        fromCalendar.set(Calendar.MONTH, monthOfYear);
                        fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        fromDateField.getEditText().setText(Utilities.getFormattedDate(fromCalendar));
                        fromDateError.setVisibility(View.GONE);
                        didSetFromDate = true;
                    }

                };

                DatePickerDialog dpDialog = new DatePickerDialog(context, date, fromCalendar
                        .get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH),
                        fromCalendar.get(Calendar.DAY_OF_MONTH));
                if (didSetToDate) {
                    dpDialog.getDatePicker().setMaxDate(toCalender.getTimeInMillis());
                } else {
                    dpDialog.getDatePicker().setMaxDate(new Date().getTime());
                }
                dpDialog.show();
            }
        });
        showPopup();
    }

    private void initView(View view) {
        closeIcon = view.findViewById(R.id.close_icon);
        fromDateLayout = view.findViewById(R.id.from_date_wrapper);
        tvFromDate = view.findViewById(R.id.tvFromDate);
        fromDateError = view.findViewById(R.id.from_date_error);
        toDateField = view.findViewById(R.id.to_date_wrapper);
        toDateError = view.findViewById(R.id.to_date_error);
        submitButton = view.findViewById(R.id.submit);
        tvFromDate = view.findViewById(R.id.tvFromDate);
        tvToDate = view.findViewById(R.id.tvToDate);

    }


    //    TODO: fromDateWrapper click
    public void onClickFrom(View view) {
        Toast.makeText(getContext(), "PM-Clicked", Toast.LENGTH_SHORT).show();
    }

    private void showPopup() {
        if (!this.isShowing()) {
            this.show();
        }
    }
}
