package com.call.jupiter.recorder.Models;

import java.util.Date;

/**
 * Created by batuhan on 22.08.2018.
 */

public class RecordsModel {
    private String phoneNumber;
    private String recordDate;
    private String recordDuration;
    private String recordPath;
    private String recordFrom;

    public RecordsModel(String  mphoneNumber, String mrecordDate, String mrecordDuration, String mrecordPath, String mInOrOut){
        setPhoneNumber(mphoneNumber);
        setRecordDate(mrecordDate);
        setRecordDuration(mrecordDuration);
        setRecordPath(mrecordPath);
        setRecordFrom(mInOrOut);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordDuration() {
        return recordDuration;
    }

    public void setRecordDuration(String recordDuration) {
        this.recordDuration = recordDuration;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public String getRecordFrom() {
        return recordFrom;
    }

    public void setRecordFrom(String recordFrom) {
        this.recordFrom = recordFrom;
    }
}
