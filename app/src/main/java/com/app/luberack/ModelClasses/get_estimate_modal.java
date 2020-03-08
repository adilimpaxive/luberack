package com.app.luberack.ModelClasses;

/**
 * Created by ahmad on 5/2/2018.
 */

public class get_estimate_modal {

    String  str1,str2;

    public get_estimate_modal() {
    }

    public get_estimate_modal(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;
    }


    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }
}
