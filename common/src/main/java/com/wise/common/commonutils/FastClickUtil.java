package com.wise.common.commonutils;

/**
 * Created by sunpeng on 15/11/12.
 */
public class FastClickUtil {

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
