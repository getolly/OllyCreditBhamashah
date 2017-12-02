package com.ollycredit.utils.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.ColorRes;
import android.support.design.BuildConfig;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ollycredit.R;

import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class HelperClass {


    public static void showSnackBar(Context context, String message) {
        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.black));
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


    public static void showActionableSnackBar(View view, String message, String actionText, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.setAction(actionText, onClickListener);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.GRAY);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public static void delayAction(final Activity activity, final long delayTime, final Runnable runnable) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(delayTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (activity != null)
                        activity.runOnUiThread(runnable);
                }
            }
        }.start();
    }

    public static HashMap<String, String> additionParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("app_version", String.valueOf(BuildConfig.VERSION_CODE));
        params.put("app_ip_address", HelperClass.getIPAddress(true));
        params.put("user_agent", "android");
        params.put("user_agent_version", String.valueOf(android.os.Build.VERSION.SDK_INT));
        params.put("app_package", BuildConfig.APPLICATION_ID);
        return params;
    }


    /**
     * Get IP activity_address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return activity_address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        if (useIPv4) {
                            if ((addr instanceof Inet4Address))
                                return addr.getHostAddress().toUpperCase();
                        } else {
                            if ((addr instanceof Inet6Address))
                                return addr.getHostAddress().toUpperCase();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Utils", ex.getMessage());
        }
        return "";
    }


    public static boolean isLocationService(final Context context) {
        boolean gps_enabled = false;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return gps_enabled;
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void hideKeyboard(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getRootView().getWindowToken(), 0);
    }

    public static long getSaturdayDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String month = "", day = "";
        if ((c.get(Calendar.MONTH) + 1) > 9)
            month += String.valueOf(c.get(Calendar.MONTH) + 1);
        else month += "0" + String.valueOf(c.get(Calendar.MONTH) + 1);
        if (c.get(Calendar.DAY_OF_MONTH) > 9)
            day += String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        else day += "0" + String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String date = day + "-" + month + "-" + c.get(Calendar.YEAR);
        System.out.println("Saturday : " + date);
        return c.getTimeInMillis() / 1000;
    }

    public static String getSundayDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR, 1);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String month = "", day = "";
        if ((c.get(Calendar.MONTH) + 1) > 9)
            month += String.valueOf(c.get(Calendar.MONTH) + 1);
        else month += "0" + String.valueOf(c.get(Calendar.MONTH) + 1);
        if (c.get(Calendar.DAY_OF_MONTH) > 9)
            day += String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        else day += "0" + String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String date = day + "-" + month + "-" + c.get(Calendar.YEAR);
        System.out.println("Sunday : " + date);
        return date;
    }

    public static String getNextSaturdayDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR, 1);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String month = "", day = "";
        if ((c.get(Calendar.MONTH) + 1) > 9)
            month += String.valueOf(c.get(Calendar.MONTH) + 1);
        else month += "0" + String.valueOf(c.get(Calendar.MONTH) + 1);
        if (c.get(Calendar.DAY_OF_MONTH) > 9)
            day += String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        else day += "0" + String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String date = day + "-" + month + "-" + c.get(Calendar.YEAR);
        System.out.println("Next Saturday : " + date);
        return date;
    }

    public static String getNexSundayDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.WEEK_OF_YEAR, 2);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String month = "", day = "";
        if ((c.get(Calendar.MONTH) + 1) > 9)
            month += String.valueOf(c.get(Calendar.MONTH) + 1);
        else month += "0" + String.valueOf(c.get(Calendar.MONTH) + 1);
        if (c.get(Calendar.DAY_OF_MONTH) > 9)
            day += String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        else day += "0" + String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String date = day + "-" + month + "-" + c.get(Calendar.YEAR);
        System.out.println("Next Sunday : " + date);
        return date;
    }

    public static void showToast(Context context, String data) {
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }


    public static String getDateToday() {
        String day = "", month = "", year = "";
        Calendar calendar;
        calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) > 9)
            day += String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        else day += "0" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if ((calendar.get(Calendar.MONTH) + 1) > 9)
            month += String.valueOf(calendar.get(Calendar.MONTH) + 1);
        else month += "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
        year += String.valueOf(calendar.get(Calendar.YEAR));
        return year + "-" + month + "-" + day;
    }


    public static String getCurrentTime() {
        String current_time = "", current_hour = "", current_mins = "";
        Calendar calendar;
        calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10)
            current_hour = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        else
            current_hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        if (calendar.get(Calendar.MINUTE) < 10)
            current_mins = "0" + calendar.get(Calendar.MINUTE);
        else
            current_mins = Integer.toString(calendar.get(Calendar.MINUTE));
        current_time = current_hour + ":" + current_mins;
        return current_time;
    }

    public static String getTimeFromEpoch(Long epoch) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(epoch * 1000L);
        c.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        //System.out.println("Epoch for getTime: " + epoch * 1000L);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int AM_PM = c.get(Calendar.AM_PM);
        String hour_string = "", min_string = "", AM_PM_string = "";

        if (hour == 0) {
            hour_string = "12";
        } else if (Integer.toString(hour).length() == 1) hour_string = "0" + Integer.toString(hour);
        else hour_string = Integer.toString(hour);

        if (Integer.toString(min).length() == 1) min_string = "0" + Integer.toString(min);
        else min_string = Integer.toString(min);

        if (AM_PM == 0)
            AM_PM_string = "AM";
        else
            AM_PM_string = "PM";

        return hour_string + ":" + min_string + " " + AM_PM_string;
    }

    public static String getDateFromEpoch(Long epoch) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(epoch * 1000L);
        c.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        System.out.println("Epoch for getDate: " + epoch * 1000L);

        String day_string = "", month_string = "";

        if (Integer.toString(day).length() == 1) day_string = "0" + Integer.toString(day);
        else day_string = Integer.toString(day);

        if (Integer.toString(month).length() == 1) month_string = "0" + Integer.toString(month);
        else month_string = Integer.toString(month);

        return day_string + "-" + month_string + "-" + year;
    }

    public static String getTodaysDate() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        String day_string = "", month_string = "";

        if (Integer.toString(day).length() == 1) day_string = "0" + Integer.toString(day);
        else day_string = Integer.toString(day);

        if (Integer.toString(month).length() == 1) month_string = "0" + Integer.toString(month);
        else month_string = Integer.toString(month);

        return day_string + "-" + month_string + "-" + year;
    }

    public static String converPickerTime(int hour, int min) {

        String time12hour = null;

        try {
            String time = hour + ":" + min;
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date dateObj = sdf.parse(time);
            time12hour = new SimpleDateFormat("K:mm").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time12hour;
    }


    public static String converPickerDate(int day, int month, int year) {

        month += 1;
        String day_string = "", month_string = "";

        if (Integer.toString(day).length() == 1) day_string = "0" + Integer.toString(day);
        else day_string = Integer.toString(day);

        if (Integer.toString(month).length() == 1) month_string = "0" + Integer.toString(month);
        else month_string = Integer.toString(month);

        return day_string + "/" + month_string + "/" + year;
    }


    public static String creditNumberFormat(String cardNumber) {

        String input = cardNumber;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                result.append("\t\t");
            }
            result.append(input.charAt(i));
        }
        System.out.println(result.toString());

        return result.toString();
    }

    public static String getTomorrowsDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 1);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        String day_string = "", month_string = "";

        if (Integer.toString(day).length() == 1) day_string = "0" + Integer.toString(day);
        else day_string = Integer.toString(day);

        if (Integer.toString(month).length() == 1) month_string = "0" + Integer.toString(month);
        else month_string = Integer.toString(month);

        return day_string + "-" + month_string + "-" + year;
    }

    public static String getDisplayDateEventList(String parsed_date) {
        String date = parsed_date;
        try {
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.sss");
            Date MyDate = newDateFormat.parse(parsed_date);
            newDateFormat.applyPattern("d MMM yyyy");
            date = newDateFormat.format(MyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String detDisplayDate(String parsed_date) {
        String date = parsed_date;
        try {
            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.sss");
            Date MyDate = newDateFormat.parse(parsed_date);
            newDateFormat.applyPattern("EEEE d MMM");
            date = newDateFormat.format(MyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTodaysEpochTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 1);
        return Long.toString(c.getTimeInMillis());
    }


    public static String formatDate(String dt) {
        String dayOfTheWeek = "", month = "", dayno = "", yearno = "";

        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            inFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = inFormat.parse(dt);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            dayOfTheWeek = outFormat.format(date);
            SimpleDateFormat outFormat1 = new SimpleDateFormat("MMM");
            month = outFormat1.format(date);
            SimpleDateFormat outFormat2 = new SimpleDateFormat("d");
            dayno = outFormat2.format(date);
            SimpleDateFormat outFormat3 = new SimpleDateFormat("y");
            yearno = outFormat3.format(date);
        } catch (Exception e) {
        }
        return " " + dayno + " " + month + " " + yearno;

    }

    public static String calenderDate(String parsed_date) {

        String date = parsed_date;

        try {

            SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date MyDate = newDateFormat.parse(parsed_date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(MyDate);

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String day_string = "", month_string = "";

            if (Integer.toString(day).length() == 1) day_string = "0" + Integer.toString(day);
            else day_string = Integer.toString(day);

            if (Integer.toString(month).length() == 1) month_string = "0" + Integer.toString(month);
            else month_string = Integer.toString(month);

            date = day_string + "-" + month_string + "-" + year;


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;


    }


    public static long getEpochfromTime(int a, int b, int c, int d) {

        Calendar date = new GregorianCalendar();
// reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, a);
        date.set(Calendar.MINUTE, b);
        date.set(Calendar.SECOND, c);
        date.set(Calendar.MILLISECOND, d);


        return (date.getTimeInMillis() / 1000);

    }


    public static long getDateTomorrow(int a, int b, int c, int d) {
        Calendar calendar;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, a);
        calendar.set(Calendar.MINUTE, b);
        calendar.set(Calendar.SECOND, c);
        calendar.set(Calendar.MILLISECOND, d);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        return calendar.getTimeInMillis() / 1000;

    }

    public static long getSaturday() {
        Calendar date;
        date = Calendar.getInstance();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);


        int gap = 7 - date.get((Calendar.DAY_OF_WEEK));

        if (gap == 0)
            gap = gap + 7;
        System.out.println("Saturday" + gap);

        date.add(Calendar.DAY_OF_YEAR, gap);


        return (date.getTimeInMillis() / 1000);

    }

    public static long getSundayEnd() {
        Calendar date;
        date = Calendar.getInstance();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);


        int gap = 7 - date.get((Calendar.DAY_OF_WEEK));

        if (gap == 0)
            gap = gap + 7;
        gap += 1;
        System.out.println("Sunday" + gap);

        date.add(Calendar.DAY_OF_YEAR, gap);


        return (date.getTimeInMillis() / 1000);

    }

    public static String getDateAsName(long timeStamp) {
        java.text.DateFormat objFormatter = new SimpleDateFormat("dd MMM");
        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;
    }


    public static String getTimein24HrFormat(long timeStamp) {
        java.text.DateFormat objFormatter = new SimpleDateFormat("hh:mm aa");
        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTimeInMillis(timeStamp * 1000);//edit
        String result = objFormatter.format(objCalendar.getTime());
        objCalendar.clear();
        return result;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String toRupeesFormat(String amount) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(amount));
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }
}