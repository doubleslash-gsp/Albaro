package com.example.jh.albaro.ServerData;

public class MemberInfo {
    private String result;
    private String email;
    private String year;
    private String month;
    private String day;
    private String sex;
    private String phone;
    private String color;


    public MemberInfo(String result) {
        this.result = result;
    }

    public MemberInfo(String result, String email){
        this.result = result;
        this.email = email;
    }

    public MemberInfo(String email, String year, String month, String day, String sex, String phone, String color){
        this.email = email;
        this.year = year;
        this.month = month;
        this.day = day;
        this.sex = sex;
        this.phone = phone;
        this.color = color;
    }

    public MemberInfo(String result, String email, String year, String month, String day, String sex, String phone, String color){
        this.result = result;
        this.email = email;
        this.year = year;
        this.month = month;
        this.day = day;
        this.sex = sex;
        this.phone = phone;
        this.color = color;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
