package com.littlepay.demo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {

    public static Long DurationSec(LocalDateTime startTime, LocalDateTime endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime time1 = LocalDateTime.parse(df.format(startTime));
        LocalDateTime time2 = LocalDateTime.parse(df.format(endTime));
        return Duration.between(time1, time2).getSeconds();
    }

    public static double MaxChargeofIncompleteTrip(String stop) {
        return ("Stop2".equals(stop)) ? 5.5 : 7.3;
    }

    public static double ChargeofCompleteTrip(String stop1, String stop2) {
        int indexX = 0;
        int indexY = 0;

        double [][] ChargAmount = { {0, 3.25, 7.30},
                                    {3.25, 0, 5.50},
                                    {7.30, 5.50, 0} };
        if (stop1.equals("Stop1"))
            indexX = 0;
        if (stop1.equals("Stop2"))
            indexX= 1;
        if (stop1.equals("Stop3"))
            indexX =2;

        if (stop2.equals("Stop1"))
            indexY = 0;
        if (stop2.equals("Stop2"))
            indexY= 1;
        if (stop2.equals("Stop3"))
            indexY =2;

        return ChargAmount[indexX][indexY];
    }
}
