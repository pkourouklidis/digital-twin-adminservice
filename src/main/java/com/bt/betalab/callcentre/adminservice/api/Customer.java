/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 06/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

public class Customer {
    private boolean isHappyToWait;
    private boolean isHappyToWaitForService;
    private boolean isUnderstanding;
    private boolean isHappy;

    public boolean isHappyToWait() {
        return isHappyToWait;
    }

    public void setHappyToWait(boolean happyToWait) {
        isHappyToWait = happyToWait;
    }

    public boolean isHappyToWaitForService() {
        return isHappyToWaitForService;
    }

    public void setHappyToWaitForService(boolean happyToWaitForService) {
        isHappyToWaitForService = happyToWaitForService;
    }

    public boolean isUnderstanding() {
        return isUnderstanding;
    }

    public void setUnderstanding(boolean understanding) {
        isUnderstanding = understanding;
    }

    public boolean isHappy() {
        return isHappy;
    }

    public void setHappy(boolean happy) {
        isHappy = happy;
    }
}
