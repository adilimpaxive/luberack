package com.app.giftfcard;

import com.app.giftfcard.ModelClasses.VehicleData;

import java.util.ArrayList;

public class MyEvent {

    private ArrayList<VehicleData> message;

    public MyEvent(ArrayList<VehicleData> message) {
        this.message = message;
    }

    public ArrayList<VehicleData> getMessage() {
        return message;
    }
}