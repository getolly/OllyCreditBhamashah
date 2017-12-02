package com.ollycredit.utils.helpers;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by ch8n on 20/7/17.
 */

public class TelephoneHelper {

    private static TelephoneHelper telephonyInfo;
    private String imeiSIM1;
    private String imeiSIM2;

    private boolean isSIM1Ready;
    private boolean isSIM2Ready;

    public String getPhoneSIM1() {
        return phoneSIM1;
    }

    public void setPhoneSIM1(String phoneSIM1) {
        this.phoneSIM1 = phoneSIM1;
    }

    public String getPhoneSIM2() {
        return phoneSIM2;
    }

    public void setPhoneSIM2(String phoneSIM2) {
        this.phoneSIM2 = phoneSIM2;
    }

    private String phoneSIM1;
    private String phoneSIM2;


    private TelephoneHelper() {

    }


    public String getImeiSIM1() {
        return imeiSIM1;
    }

    public void setImeiSIM1(String imeiSIM1) {
        this.imeiSIM1 = imeiSIM1;
    }

    public String getImeiSIM2() {
        return imeiSIM2;
    }

    public void setImeiSIM2(String imeiSIM2) {
        this.imeiSIM2 = imeiSIM2;
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public void setSIM1Ready(boolean SIM1Ready) {
        isSIM1Ready = SIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    public void setSIM2Ready(boolean SIM2Ready) {
        isSIM2Ready = SIM2Ready;
    }

    public boolean isDualSIM() {
        return imeiSIM2 != null;
    }

    public static TelephoneHelper getInstance(Context context) {

        if (telephonyInfo == null) {

            telephonyInfo = new TelephoneHelper();

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

            telephonyInfo.imeiSIM1 = telephonyManager.getDeviceId();
            telephonyInfo.imeiSIM2 = null;

            try {
                telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
                telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            } catch (GeminiMethodNotFoundException e) {
                e.printStackTrace();

                try {
                    telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                    telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    //Call here for next manufacturer's predicted method name if you wish

                    e1.printStackTrace();
                    try {
                        telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context, "getDeviceIdDs", 0);
                        telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdDs", 1);
                    } catch (GeminiMethodNotFoundException e2) {
                        e2.printStackTrace();
                    }
                }
            }

            telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
            telephonyInfo.isSIM2Ready = false;

            try {
                telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
                telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
            } catch (GeminiMethodNotFoundException e) {

                e.printStackTrace();

                try {
                    telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                    telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    //Call here for next manufacturer's predicted method name if you wish
                    e1.printStackTrace();
                }


            }
        }

        return telephonyInfo;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        String imei = null;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if (ob_phone != null) {
                imei = ob_phone.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imei;
    }

    private static boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                if (simState == TelephonyManager.SIM_STATE_READY) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    public static ArrayList<String> getTelephonyManagerMethodNamesForThisDevice(Context context) {

        ArrayList<String> methodNameList = new ArrayList<>();

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;

        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            Method[] methods = telephonyClass.getMethods();
            for (int idx = 0; idx < methods.length; idx++) {

                System.out.println("\n" + methods[idx] + " declared by " + methods[idx].getDeclaringClass());
                methodNameList.add(methods[idx].getDeclaringClass().toString());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return methodNameList;
    }


    private static class GeminiMethodNotFoundException extends Exception {

        private static final long serialVersionUID = -996812356902545308L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }
}
