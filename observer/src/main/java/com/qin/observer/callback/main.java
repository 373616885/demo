package com.qin.observer.callback;

public class main {

    public static void main(String[] args) {
        Caller call = new Caller();
        call.call(new ICallBack() {
            @Override
            public void callBack() {
                System.out.println("终于回调成功了！");
            }
        });




    }
}
