package com.jekz.stepitup.util;

import java.text.NumberFormat;

/**
 * Created by evanalmonte on 12/19/17.
 */

public final class Formatter {
    private static NumberFormat numberFormat = NumberFormat.getInstance();

    public static String formatSteps(int steps) {
        return numberFormat.format(steps) + " steps";
    }
}
