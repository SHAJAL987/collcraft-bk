package com.middlewareExp.collcraft.utils;

import com.middlewareExp.collcraft.payload.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Utilities {
    private  int sequenceNumber;
    private  SimpleDateFormat ft3 = new SimpleDateFormat( "yyDDDHHmm");
    private  SimpleDateFormat ft4 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public  String getServiceID() {
        Date myDate = new Date();
        String sequenceString = "0000" + String.valueOf(sequenceNumber);
        if(sequenceNumber < 1000)
            sequenceNumber++;
        else
            sequenceNumber = 0;
        String serviceID = ft3.format(myDate).toString() + sequenceString.substring(sequenceString.length() - 3, sequenceString.length());
        return serviceID;
    }
    public  String getTimeStamp() {
        Date myDate = new Date();
        return ft4.format(myDate).toString();
    }

    public ResponseEntity handleException(Exception ex) {
        ex.printStackTrace();
        ResponseEntity resp = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        return resp;
    }

}
