package com.n26.controller;

import com.n26.entity.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;

import static com.n26.utils.DateTimeUtils.ONE_MINUTE_MILLIS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionsControllerTest  extends ControllerTest {

    @Autowired
    private TransactionController transactionController;

    @Test
    public void testCreateTransaction() {
        Transaction transaction = newTransaction("67", Instant.now(Clock.systemUTC()));
        transactionController.createTransaction(transaction);
        assertEquals(1, transactionsCache.getTransactions().size());
        assertTrue(transactionsCache.getTransactions().contains(transaction));
    }

    @Test
    public void testDeleteAllTransactions() {
        transactionController.createTransaction(newTransaction("67", Instant.now(Clock.systemUTC())));
        transactionController.createTransaction(newTransaction("76", Instant.now(Clock.systemUTC())));
        transactionController.deleteTransactions();
        assertEquals(0, transactionsCache.getTransactions().size());
    }

    @Test
    public void testDeletionAfterOneMinute() throws InterruptedException {
        transactionController.createTransaction(newTransaction("67", Instant.now(Clock.systemUTC())));
        Thread.sleep(ONE_MINUTE_MILLIS);
        assertClearStatistics();
    }

}
