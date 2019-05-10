package com.example.tr.instantcool2.JavaBean;

/**
 * Created by TR on 2017/10/11.
 */

public class MyAccount {

    private String AccountName;

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getAccountPwd() {
        return AccountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        AccountPwd = accountPwd;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String AccountPwd;
    private String Name;

}
