package com.example.myapplication.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class InputDialog extends AlertDialog {

    private OnClickListener mOnPositiveClick, mOnNegativeClick;

    private EditText mEditText;

    private int mInputType;

    public InputDialog(@NonNull Context context) {
        super(context);
    }

    private void setKeyboardVisible(){
        Window dialogWindow = getWindow();
        if(dialogWindow != null) dialogWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void addPositiveButton(int buttonText, OnClickListener onClickListener){
        mOnPositiveClick = onClickListener;
        setButton(BUTTON_POSITIVE, getContext().getString(buttonText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mOnPositiveClick != null) mOnPositiveClick.onClick(InputDialog.this);
                dismiss();
            }
        });
    }

    public void addNegativeButton(int buttonText, OnClickListener onClickListener){
        mOnNegativeClick = onClickListener;
        setButton(BUTTON_NEGATIVE, getContext().getString(buttonText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mOnNegativeClick != null) mOnNegativeClick.onClick(InputDialog.this);
                dismiss();
            }
        });
    }

    public void addNegativeButton(int buttonText){
        addNegativeButton(buttonText, new OnClickListener() {
            @Override
            public void onClick(InputDialog textInput) {
                dismiss();
            }
        });
    }

    public void setTitle(int title){
        setTitle(getContext().getString(title));
    }

    public void setMessage(int message){
        setMessage(getContext().getString(message));
    }

    @Override
    public void dismiss() {
        mOnPositiveClick = null;
        mOnNegativeClick = null;
        super.dismiss();
    }

    @Override
    public void show() {
        View view = createView(getContext());
        setView(view);
        setKeyboardVisible();
        super.show();
    }

    private View createView(Context context){
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(params);
        view.setPaddingRelative(36, 0, 36, 0);

        mEditText = createTextInput(context, mInputType);

        view.addView(mEditText);

        return view;
    }

    public void setInputType(int inputType){
        mInputType = inputType;
    }

    private EditText createTextInput(Context context, int inputType){
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        EditText textInput = new EditText(context);
        textInput.setLayoutParams(params);
        textInput.setInputType(inputType);
        textInput.setSingleLine();
        textInput.setImeOptions(EditorInfo.IME_ACTION_DONE);

        return textInput;
    }

    public String getText(){
        return mEditText.getText().toString();
    }

    public interface OnClickListener{
        void onClick(InputDialog textInput);
    }
}
