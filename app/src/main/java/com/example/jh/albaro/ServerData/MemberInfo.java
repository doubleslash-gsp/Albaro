package com.example.jh.albaro.ServerData;

public class MemberInfo {
    private String result;
    private String email;
    private int year;
    private int month;
    private int day;
    private String sex;
    private  String phone;

    public MemberInfo() {
        // TODO Auto-generated constructor stub
    }

    public MemberInfo(String result) {
        this.result = result;
    }

    public MemberInfo(String result, String email){
        this.result = result;
        this.email = email;
    }


    public MemberInfo(String result, String email, int year, int month, int day, String sex, String phone) {
        this.result = result;
        this.email = email;
        this.year = year;
        this.month = month;
        this.day = day;
        this.sex = sex;
        this.phone = phone;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
