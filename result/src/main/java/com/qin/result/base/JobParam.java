package com.qin.result.base;

public class JobParam extends Job {

    /**
     *  that would work, but it's a but ugly
     */
    public String getPm() {
        return super.praram;
    }

    public void setPm(String praram) {
        super.praram = praram;
    }

}
