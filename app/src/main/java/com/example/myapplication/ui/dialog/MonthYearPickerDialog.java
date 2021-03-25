package com.example.myapplication.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FwandroidDialogMonthYearPickerBinding;

import java.util.Calendar;


public class MonthYearPickerDialog extends AlertDialog {
    private String[] mMonths;
    private FwandroidDialogMonthYearPickerBinding mBinding;
//    private NumberPicker mMonthPicker, mYearPicker;
//    private OnClickListener mOnPositiveClick, mOnNegativeClick;

    public MonthYearPickerDialog(@NonNull Context context) {
        super(context);
        mMonths = context.getResources().getStringArray(R.array.fwandroid_month_array);
        setView(createView(context));
        initComponents();
    }

//    public void addPositiveButton(int buttonText, OnClickListener onClickListener){
//        mOnPositiveClick = onClickListener;
//        setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(buttonText), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if(mOnPositiveClick != null) mOnPositiveClick.onClick(dialogInterface, mMonthPicker.getValue(), mYearPicker.getValue());
//            }
//        });
//    }
//
//    public void addNegativeButton(int buttonText, OnClickListener onClickListener){
//        mOnNegativeClick = onClickListener;
//        setButton(AlertDialog.BUTTON_NEGATIVE, getContext().getString(buttonText), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if(mOnNegativeClick != null) mOnNegativeClick.onClick(dialogInterface, mMonthPicker.getValue(), mYearPicker.getValue());
//            }
//        });
//    }
//
//    public void setTitle(int title) {
//        setTitle(getContext().getString(title));
//    }
//
//    private View createView(Context context){
//        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//
//        LinearLayout view = new LinearLayout(context);
//        view.setOrientation(LinearLayout.HORIZONTAL);
//        view.setLayoutParams(params);
//        view.setPadding(16, 0, 16, 0);
//        view.setGravity(Gravity.CENTER);
//
//        mMonthPicker = new NumberPicker(context);
//        mMonthPicker.setLayoutParams(params);
//        mMonthPicker.setMinValue(0);
//        mMonthPicker.setMaxValue(mMonths.length - 1);
//        mMonthPicker.setDisplayedValues(mMonths);
//
//        mYearPicker = new NumberPicker(context);
//        mYearPicker.setLayoutParams(params);
//        mYearPicker.setLeft(32);
//        mYearPicker.setMinValue(2015);
//        mYearPicker.setMaxValue(2040);
//
//        view.addView(mMonthPicker);
//        view.addView(mYearPicker);
//
//        return view;
//    }

    private View createView(Context context){
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fwandroid_dialog_month_year_picker, null, false);
        return mBinding.getRoot();
    }

    public void setTitle(String title){
        mBinding.fwandroidMonthYearPickerTitle.setText(title);
    }

    public void setTitle(int title){
        mBinding.fwandroidMonthYearPickerTitle.setText(getContext().getString(title));
    }

    public void addOnClickListener(final OnClickListener onClickListener){
        mBinding.fwandroidMonthYearPickerButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(mBinding.fwandroidMonthYearPickerMonth.getValue(), mBinding.fwandroidMonthYearPickerYear.getValue());
                dismiss();
            }
        });
    }

    private void initComponents(){
        Calendar calendar = Calendar.getInstance();

        mBinding.fwandroidMonthYearPickerMonth.setMinValue(0);
        mBinding.fwandroidMonthYearPickerMonth.setMaxValue(mMonths.length - 1);
        mBinding.fwandroidMonthYearPickerMonth.setDisplayedValues(mMonths);
        mBinding.fwandroidMonthYearPickerMonth.setValue(calendar.get(Calendar.MONTH));

        mBinding.fwandroidMonthYearPickerYear.setMinValue(2015);
        mBinding.fwandroidMonthYearPickerYear.setMaxValue(2030);
        mBinding.fwandroidMonthYearPickerYear.setValue(calendar.get(Calendar.YEAR));
    }

    @Override
    public void dismiss() {
        mMonths = null;
        mBinding = null;
//        mMonthPicker = null;
//        mYearPicker = null;
//        mOnPositiveClick = null;
//        mOnNegativeClick = null;
        super.dismiss();
    }

    public interface OnClickListener{
        void onClick(int month, int year);
    }
}
