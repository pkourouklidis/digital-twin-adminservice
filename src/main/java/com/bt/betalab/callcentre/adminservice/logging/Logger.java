/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 04/07/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.logging;

import java.util.Calendar;
import java.util.Date;

public class Logger {

    public static void log(String statement, LogLevel level) {
        Date time = Calendar.getInstance().getTime();
        System.out.println("[" + time + "] " + level + ": " + statement);
    }
}
