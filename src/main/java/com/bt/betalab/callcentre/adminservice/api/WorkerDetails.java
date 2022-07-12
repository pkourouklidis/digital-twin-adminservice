/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

public class WorkerDetails {
    private boolean isSkilled;
    private boolean isFast;

    public WorkerDetails(boolean isSkilled, boolean isFast) {
        this.isSkilled = isSkilled;
        this.isFast = isFast;
    }

    public boolean isSkilled() {
        return isSkilled;
    }

    public void setSkilled(boolean skilled) {
        isSkilled = skilled;
    }

    public boolean isFast() {
        return isFast;
    }

    public void setFast(boolean fast) {
        isFast = fast;
    }
}
