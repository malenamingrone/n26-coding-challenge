package com.n26.controller;

import com.n26.cache.TransactionsCache;
import com.n26.entity.Statistics;
import com.n26.entity.Transaction;
import com.n26.service.TransactionService;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.n26.utils.DateTimeUtils.TIMESTAMP_FORMATTER;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ControllerTest {

    @Autowired
    protected StatisticsController statisticsController;

    @Autowired
    protected TransactionService transactionService;

    @Autowired
    protected TransactionsCache transactionsCache;

    @After
    public void tearDown() {
        transactionService.deleteAll();
    }

    protected Transaction createTransaction(String amount, LocalDateTime timestamp) {
        Transaction transaction = newTransaction(amount, timestamp.toInstant(ZoneOffset.UTC));
        transactionService.save(transaction);
        return transaction;
    }

    protected Transaction newTransaction(String amount, Instant timestamp) {
        return new Transaction(amount, TIMESTAMP_FORMATTER.format(timestamp));
    }

    protected void assertStatistics(long count, String sum, String avg, String max, String min) {
        Statistics statistics = statisticsController.getStatistics();
        assertEquals(sum, statistics.getSum());
        assertEquals(avg, statistics.getAvg());
        assertEquals(max, statistics.getMax());
        assertEquals(min, statistics.getMin());
        assertEquals(count, statistics.getCount());
        assertEquals(count, transactionsCache.getTransactions().size());
    }

    protected void assertClearStatistics() {
        Statistics statistics = statisticsController.getStatistics();
        assertEquals("0.00", statistics.getSum());
        assertEquals("0.00", statistics.getAvg());
        assertEquals("0.00", statistics.getMax());
        assertEquals("0.00", statistics.getMin());
        assertEquals(0L, statistics.getCount());
        assertEquals(0L, transactionsCache.getTransactions().size());
    }

}
