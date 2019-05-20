package com.gradle.sample.closure;

/**
 * @author qinjp
 * @date 2019-05-20
 **/
public class DoubleColon {
    public static void printStr(String str) {
        System.out.println("printStr : " + str);
    }

    public void toUpper(){
        System.out.println("toUpper : " + this.toString());
    }

    public void toLower(String str){
        System.out.println("toLower : " + str);
    }

    public Integer toInt(String str){
        System.out.println("toInt : " + str);
        return 1;
    }
}
