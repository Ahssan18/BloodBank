package com.ommi.bloodbank.Models;

public class ModelMessage {


    private String status;
    private String name;

    public String getMessegeStatus() {
        return MessegeStatus;
    }

    public void setMessegeStatus(String messegeStatus) {
        MessegeStatus = messegeStatus;
    }

    public String getLastMessege() {
        return lastMessege;
    }

    public void setLastMessege(String lastMessege) {
        this.lastMessege = lastMessege;
    }

    private String lastMessege;
    private String MessegeStatus;

    public ModelMessage() {
    }

    private String message;
    private String date;
    private String receiver;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private String sender;

    public ModelMessage(String receiver, String status, String name, String message, String date, String sender) {
        this.receiver = receiver;
        this.status = status;
        this.name = name;
        this.message = message;
        this.date = date;
        this.sender = sender;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
