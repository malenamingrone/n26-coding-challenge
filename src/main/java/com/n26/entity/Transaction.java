package com.n26.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.n26.utils.DateTimeUtils.TIMESTAMP_FORMATTER;

public class Transaction {

    private String amount;
    private String timestamp;

    public Transaction(String amount, String timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getEpochTimestamp() {
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount='" + amount + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
