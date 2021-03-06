package com.n26;

import com.n26.entity.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args) {
        Set<Transaction> map = ConcurrentHashMap.newKeySet();

        Transaction e = new Transaction("12.234", "2021-05-17T09:59:51.312Z");
        Transaction e1 = new Transaction("12.234", "2021-05-17T09:59:51.312Z");
        System.out.println(map.add(e));
        System.out.println(map.add(e1));

        System.out.println(map.remove(e));
        System.out.println(map.size());


        BigDecimal sum = new BigDecimal("11");
        BigDecimal count = new BigDecimal("3");
        BigDecimal divide = sum.divide(count, BigDecimal.ROUND_HALF_UP, RoundingMode.HALF_EVEN);
        System.out.println(divide);
        System.out.println(divide.setScale(2, BigDecimal.ROUND_HALF_UP));


        System.out.println(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());

        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        System.out.println(simpleDateFormat.format(calendar.getTime()));
    }
}


//        System.out.println("timestamp " + transaction.getTimestamp());
//
//        TimeZone timeZone = TimeZone.getTimeZone("UTC");
//        Calendar calendar = Calendar.getInstance(timeZone);
//        calendar.setTimeInMillis(transaction.getEpochTimestamp() + 5000);
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//        simpleDateFormat.setTimeZone(timeZone);
//
//        System.out.println("scheduled time: " + simpleDateFormat.format(calendar.getTime()));