package com.qin.security;

import lombok.Data;

@Data
public class JsonBean {
    private String sign;
    private String nonce;
    private String timestamp;
    private String companyIdentity;
    private String incomingInspectionId;
    private String licensePlate;
    private String vin;
    private String vehicleType;
    private String engineNum;
    private String vehicleOwner;
    private String entrustRepair;
    private String contact;
    private String contactDetails;
    private String obd;
    private String carType;
    private String vehicleClassCode;
    private String drivingLicenseImg;
    private String color;
    private String brand;

}
