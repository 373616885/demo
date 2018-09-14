package com.qin.observer.callback;

/**
 * 回调者（用于调用回调函数的类）
 */
public class Caller {

    /**
     * 回调模式其实就是传递一个接口
     * 然后在接口里执行传入的实现
     */
    public void call(ICallBack callBack){
        System.out.println("start...");
        callBack.callBack();
        System.out.println("end...");
    }
}
