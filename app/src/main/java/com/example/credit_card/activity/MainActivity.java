package com.example.credit_card.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.credit_card.R;
import com.example.credit_card.model.CardDetails;
import com.example.credit_card.utils.ValidateData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.example.credit_card.utils.Constants.*;

import static com.example.credit_card.utils.Constants.INVALID_CARD_NUMBER;
import static com.example.credit_card.utils.Constants.INVALID_DATE;
import static com.example.credit_card.utils.Constants.INVALID_FIRST_NAME;
import static com.example.credit_card.utils.Constants.INVALID_LAST_NAME;
import static com.example.credit_card.utils.Constants.INVALID_SECURITY_CODE;
import static com.example.credit_card.utils.Constants.VALID;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout card_number,date,security_code,first_name,last_name;
    private MaterialButton submit_payment;
    private static final String TAG = "Main Activity----------------------------------->";
    private boolean back_space=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        submit_payment.setOnClickListener((view)->{
            String card_number_str=card_number.getEditText().getText().toString().trim();
            String date_str=date.getEditText().getText().toString().trim();
            String security_code_str=security_code.getEditText().getText().toString().trim();
            String first_name_str=first_name.getEditText().getText().toString().trim();
            String last_name_str=last_name.getEditText().getText().toString().trim();
            CardDetails details=new CardDetails();
            details.setCard_number(card_number_str);
            details.setDate(date_str);
            details.setSecurity_code(security_code_str);
            details.setFirst_name(first_name_str);
            details.setLast_name(last_name_str);
            int code= ValidateData.validateCardDetails(details);
            removePrevError();
            handelCase(code);
        });

    }




    private void removePrevError() {
        card_number.setErrorEnabled(false);
        security_code.setErrorEnabled(false);
        date.setErrorEnabled(false);
        first_name.setErrorEnabled(false);
        last_name.setErrorEnabled(false);


    }

    private void handelCase(int code) {
        switch (code) {
            case VALID:
                handleAlertDialog();
                break;
            case INVALID_CARD_NUMBER:
                card_number.setError(getString(R.string.valid_card_number));
                break;
            case INVALID_DATE:
                date.setError(getString(R.string.valid_date));
                break;
            case INVALID_SECURITY_CODE:
                security_code.setError(getString(R.string.valid_security_code));
                break;
            case INVALID_FIRST_NAME:
                first_name.setError(getString(R.string.valid_first_name));
                break;
            case INVALID_LAST_NAME:
                last_name.setError(getString(R.string.valid_last_name));
                break;

        }
    }

    private void handleAlertDialog() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
        dialog.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(R.string.payment_successful);
        AlertDialog alert=dialog.create();
        alert.show();

    }

    private void initViews() {
        card_number=findViewById(R.id.card_number);
        date=findViewById(R.id.date);
        security_code=findViewById(R.id.security_code);
        first_name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);
        submit_payment=findViewById(R.id.submit_payment);
        setEditTextListener();

    }

    private void setEditTextListener() {



        date.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==2&&!back_space) {
                    date.getEditText().setText(s + "/");
                    date.getEditText().setSelection(date.getEditText().getText().length());
                    back_space=true;
                }
                else if(s.length()==2&&back_space) {
                    date.getEditText().setText(s.subSequence(0,1));
                    date.getEditText().setSelection(date.getEditText().getText().length());
                    back_space=false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


    }
}