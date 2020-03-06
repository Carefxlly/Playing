package com.example.jingbin.cloudreader.tools;

/**
 * Created by surui29 on 2017/1/4.
 */

import android.util.Log;

/**
 * 日志相关类:默认是测试环境<br>
 */
public class LogUtil {

    private static boolean isShowLog = true;//是否输出日志，默认设为true输出，发布时设为false
    private static String tag = "surui";

    /**
     * verbose详细日志
     *
     * @param message 日志信息
     */
    public static void v1(String message) {

        if (isShowLog) {
            Log.v(tag, getDetailMessage(message));
        }
    }

    /**
     * verbose详细日志
     *
     * @param message 日志信息
     */
    public static void v(String message) {
        if (isShowLog) {
            String[] output = getTagAndDetailMessage(message);
            Log.v(output[0], output[1]);
        }
    }

    /**
     * error错误日志
     *
     * @param message 日志信息
     */
    public static void e1( String message) {

        if (isShowLog) {
            Log.e(tag, getDetailMessage(message));
        }
    }

    /**
     * error错误日志
     *
     * @param message 日志信息
     */
    public static void e(String message) {

        if (isShowLog) {
            String[] output = getTagAndDetailMessage(message);
            Log.e(output[0], output[1]);
        }
    }

    /**
     * info信息日志
     *
     * @param message 日志信息
     */
    public static void i1(String message) {

        if (isShowLog) {
            Log.i(tag, getDetailMessage(message));
        }
    }

    /**
     * info信息日志
     *
     * @param message 日志信息
     */
    public static void i(String message) {
        if (isShowLog) {
            String[] output = getTagAndDetailMessage(message);
            Log.i(output[0], output[1]);
        }
    }

    /**
     * debug调试日志
     *
     * @param message 日志信息
     */
    public static void d1(String message) {

        if (isShowLog) {
            Log.d(tag, getDetailMessage(message));
        }
    }

    /**
     * debug调试日志
     *
     * @param message 日志信息
     */
    public static void d(String message) {

        if (isShowLog) {
            String[] output = getTagAndDetailMessage(message);
            Log.d(output[0], output[1]);
        }
    }

    /**
     * warn警告日志
     *
     * @param message 日志信息
     */
    public static void w1(String message) {

        if (isShowLog) {
            Log.w(tag, getDetailMessage(message));
        }
    }

    /**
     * warn警告日志
     *
     * @param message 日志信息
     */
    public static void w(String message) {

        if (isShowLog) {
            String[] output = getTagAndDetailMessage(message);
            Log.w(output[0], output[1]);
        }
    }

    /**
     * 得到默认tag【类名】以及信息详情
     *
     * @param message 要显示的信息
     * @return 默认tag【类名】以及信息详情,默认信息详情【类名+方法名+行号+message】
     */
    private static String[] getTagAndDetailMessage(String message) {
        String output[] = new String[2];
        for (StackTraceElement ste : (new Throwable()).getStackTrace()) {
            //栈顶肯定是LogUtil这个类自己
            if (LogUtil.class.getName().equals(ste.getClassName())) {
                continue;
            }
            //栈顶的下一个就是需要调用这个类的地方
            else {
                int b = ste.getClassName().lastIndexOf(".") + 1;
                output[0] = ste.getClassName().substring(b);
                output[1] = "-->" + ste.getMethodName() + "():" + ste.getLineNumber()
                        + "-->" + message;
                break;
            }
        }
        return output;
    }

    /**
     * 得到一个信息的详细的情况【类名+方法名+行号】
     *
     * @param message 要显示的信息
     * @return 一个信息的详细的情况【类名+方法名+行号+message】
     */
    private static String getDetailMessage(String message) {
        String detailMessage = "";
        for (StackTraceElement ste : (new Throwable()).getStackTrace()) {
            //栈顶肯定是LogUtil这个类自己
            if (LogUtil.class.getName().equals(ste.getClassName())) {
                continue;
            }
            //栈顶的下一个就是需要调用这个类的地方[此处取出类名和方法名还有行号]
            else {
                int b = ste.getClassName().lastIndexOf(".") + 1;
                String TAG = ste.getClassName().substring(b);
                detailMessage = TAG + "-->" + ste.getMethodName() + "():" + ste.getLineNumber()
                        + "-->" + message;
                break;
            }
        }
        return detailMessage;
    }
}
