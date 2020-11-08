package com.ommi.bloodbank.Models;

public class ModelVotingRecord {
    String messegeKey;

    public ModelVotingRecord() {
    }

    public String getMessegeKey() {
        return messegeKey;
    }

    public void setMessegeKey(String messegeKey) {
        this.messegeKey = messegeKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVotingVal() {
        return VotingVal;
    }

    public void setVotingVal(String votingVal) {
        VotingVal = votingVal;
    }

    String userId;

    public ModelVotingRecord(String messegeKey, String userId, String votingVal) {
        this.messegeKey = messegeKey;
        this.userId = userId;
        this.VotingVal = votingVal;
    }

    String VotingVal;
}
