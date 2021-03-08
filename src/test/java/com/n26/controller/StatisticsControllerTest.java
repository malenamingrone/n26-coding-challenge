package com.n26.controller;

import com.n26.entity.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static com.n26.utils.DateTimeUtils.UTC;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StatisticsControllerTest extends ControllerTest {

    @Test
    public void testAddStatistics() {
        createTransaction("12.523", LocalDateTime.now(UTC));
        createTransaction("23.565", LocalDateTime.now(UTC));
        assertStatistics(2L, "36.09", "18.04", "23.57", "12.52");
    }

    @Test
    public void testClearStatistics() {
        createTransaction("12.523", LocalDateTime.now(UTC));
        assertEquals(1, transactionsCache.getTransactions().size());

        transactionService.deleteAll();
        assertClearStatistics();
    }

    @Test
    public void testRemoveStatistics() {
        Transaction transaction1 = createTransaction("100", LocalDateTime.now(UTC));
        Transaction transaction2 = createTransaction("50", LocalDateTime.now(UTC));
        assertStatistics(2L, "150.00", "75.00", "100.00", "50.00");

        transactionService.delete(transaction1);
        assertStatistics(1L, "50.00", "50.00", "50.00", "50.00");

        transactionService.delete(transaction2);
        assertClearStatistics();
    }

}
