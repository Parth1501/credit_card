package com.example.credit_card.utils;

import android.util.Log;

import com.example.credit_card.model.CardDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.credit_card.utils.Constants.CARD_LENGTH;
import static com.example.credit_card.utils.Constants.INVALID_CARD_NUMBER;
import static com.example.credit_card.utils.Constants.INVALID_DATE;
import static com.example.credit_card.utils.Constants.INVALID_FIRST_NAME;
import static com.example.credit_card.utils.Constants.INVALID_LAST_NAME;
import static com.example.credit_card.utils.Constants.INVALID_SECURITY_CODE;
import static com.example.credit_card.utils.Constants.SECURITY_CODE_LENGTH;
import static com.example.credit_card.utils.Constants.VALID;

public class ValidateData {
    private static  String TAG="VALIDATE DATA---------------------------->";
    public static int validateCardDetails(CardDetails details) {
        String card_number=details.getCard_number();
        String date=details.getDate();
        String security_code=details.getSecurity_code();
        String first_name=details.getFirst_name();
        String last_name=details.getLast_name();
        boolean is_valid_card_number=isValidCardNumber(card_number);
        if(!is_valid_card_number)
            return INVALID_CARD_NUMBER;
        boolean is_valid_date=isValidDate(date);
        if(!is_valid_date)
            return INVALID_DATE;
        boolean is_valid_security_code=isValidSecurityCode(security_code,card_number.length());
        if(!is_valid_security_code)
            return INVALID_SECURITY_CODE;
        boolean is_valid_first_name=isValidName(first_name);
        if(!is_valid_first_name)
            return INVALID_FIRST_NAME;
        boolean is_valid_last_name=isValidName(last_name);
        if(!is_valid_last_name)
            return INVALID_LAST_NAME;
        return VALID;
    }

    private static boolean isValidName(String str) {
        str=str.toLowerCase();
        if(str.length()==0)
            return false;
        for(int i=0;i<str.length();i++) {
            if(str.charAt(i)<'a'||str.charAt(i)>'z')
                return false;
        }
        return true;
    }

    private static boolean isValidSecurityCode(String security_code,int card_number_length) {
        //AMERICAN EXPRESS HAS 4 DIGIT SECURITY CODE
        if(card_number_length==CARD_LENGTH-1) {
            return security_code.length() == SECURITY_CODE_LENGTH;
        }
        //OTHER CARD HAS 3 DIGIT SECURITY CODE
        else if(card_number_length==card_number_length) {
            return security_code.length() == SECURITY_CODE_LENGTH - 1;
        }
        return true;
    }

    private static boolean isValidDate(String date) {
        int len=date.length();
        if(len!=5)
            return false;
        int start=0;
        //  MM/YY
        int exp_month,exp_year;
        try {
            exp_month = Integer.parseInt(date.substring(start, start + 2));
            exp_year = Integer.parseInt(date.substring(len - 2, len));
        }
        catch (Exception e) {
            return false;
        }
        int current_year=Integer.parseInt(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2,4));
        int current_month=Calendar.getInstance().get(Calendar.MONTH);
        if(current_month>12)
            return false;
        if(exp_year==current_year) {
            return current_month <= exp_month;
        }
        else return exp_year >= current_year;

    }


    private static boolean isValidCardNumber(String str) {
        Log.i(TAG, "isValidCardNumber: "+str.length());
        if(str.length()!=CARD_LENGTH&&str.length()!=CARD_LENGTH-1) {
            return false;
        }

        char ch=str.charAt(0);
        //    3 - American Express
        //    4 - Visa
        //    5 - MasterCard
        //    6 - Discover Card

        if((str.length()==CARD_LENGTH&&!(ch=='4'||ch=='5'||ch=='6')))
            return false;
        else if(str.length()==CARD_LENGTH-1&&ch!='3')
            return false;

        return luhnAlgorithm(str);
    }
    private static boolean luhnAlgorithm(String str) {
        int len=str.length();
        List<Integer> numbers=new ArrayList<>();
        try {
            for(int i=0;i<len;i++) {
                char ch=str.charAt(i);
                Integer temp=Integer.parseInt(String.valueOf(ch));
                numbers.add(temp);
            }
        }
        catch (Exception e) {
                return false;
        }
        for(int i=len-2;i>=0;i-=2) {
            int double_val= numbers.get(i) *2;
            int new_value=0;
            while(double_val!=0) {
                new_value+=double_val%10;
                double_val/=10;
            }
            numbers.set(i,new_value);

        }
        int sum=0;
        for(int i=0;i<len;i++) {
            sum+=numbers.get(i);
        }
        Log.i(TAG, "isValidCardNumber: "+sum );
        return sum % 10 == 0;
    }

}
