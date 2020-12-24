package com.qin.security;

import cn.hutool.json.JSONUtil;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;


public class HttpTest {

    public static void main(String[] args) {

        String jsonString = "{\n" +
                "\t\"sign\": \"f161603b2f7a5f63123853d26790851e\",\n" +
                "\t\"nonce\": \"54321\",\n" +
                "\t\"timestamp\": \"1608816830\",\n" +
                "\t\"companyIdentity\":\"91310113674582238P\",\n" +
                "\t\"incomingInspectionId\": \"1\",\n" +
                "\t\"licensePlate\": \"苏E5EY29\",\n" +
                "\t\"vin\": \"LSY8AACG1GK065095\",\n" +
                "\t\"vehicleType\": \"阁瑞斯\",\n" +
                "\t\"engineNum\": \"9077356(2TR)\",\n" +
                "\t\"vehicleOwner\": \"张家港市锦丰镇人民医院\",\n" +
                "\t\"entrustRepair\": \"张家港市锦丰镇人民医院\",\n" +
                "\t\"contact\": \"严建华\",\n" +
                "\t\"contactDetails\": \"13962295825\",\n" +
                "\t\"obd\": \"0\",\n" +
                "\t\"carType\": \"阁瑞斯\",\n" +
                "\t\"vehicleClassCode\": \"C\",\n" +
                "\t\"drivingLicenseImg\": \"a/b.jpg\",\n" +
                "\t\"color\": \"黑\",\n" +
                "\t\"brand\": \"华晨金杯\"\n" +
                "}";
        JsonBean jsonBean = JSONUtil.toBean(jsonString,JsonBean.class);
        HttpResult.Body body = OkHttps.sync("http://182.254.133.51:8085/repair/car/upload")
                .addHeader("Content-Type","application/json")
//                .addBodyPara("sign", "f161603b2f7a5f63123853d26790851e")
//                .addBodyPara("nonce", "f161603b2f7a5f63123853d26790851e")
//                .addBodyPara("vehicleClassCode", "C")
                .setBodyPara(jsonBean)
                .bodyType("json")
                .post( )
                .getBody();
        System.out.println(body);
    }
}
