package com.example.wallet.ui.models;

import java.util.Date;

public class ContactUI {
    private String whoWrite;
    private String whichReason;
    private String detail;
    private String date;

    public ContactUI(){
        this.whoWrite = "";
        this.whichReason = "";
        this.detail = "";
        this.date = (new Date()).toString();
    }
    public ContactUI(String whoWrite, String whichReason, String detail, String date) {
        this.whoWrite = whoWrite;
        this.whichReason = whichReason;
        this.detail = detail;
        this.date = date;
    }

    public boolean isValid(){
        return  !this.whoWrite.isEmpty() &&
            !this.whichReason.isEmpty() &&
            !this.detail.isEmpty() &&
            !this.date.isEmpty();
    }

    public String getWhoWrite() {
        return whoWrite;
    }

    public void setWhoWrite(String whoWrite) {
        this.whoWrite = whoWrite;
    }

    public String getWhichReason() {
        return whichReason;
    }

    public void setWhichReason(String whichReason) {
        this.whichReason = whichReason;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void clear(){
        this.whoWrite = "";
        this.whichReason = "";
        this.detail = "";
        this.date = "";
    }
}
