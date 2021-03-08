package com.n26.service.impl;

import com.n26.async.TaskScheduler;
import com.n26.cache.StatisticsCache;
import com.n26.cache.TransactionsCache;
import com.n26.entity.Transaction;
import com.n26.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import static com.n26.utils.DateTimeUtils.ONE_MINUTE_MILLIS;
import static com.n26.utils.DateTimeUtils.UTC;

/**
 * {@inheritDoc}
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionsCache transactionsCache;
    private final StatisticsCache statisticsCache;

    private final TaskScheduler scheduler;

    public TransactionServiceImpl(TransactionsCache transactionsCache, StatisticsCache statisticsCache, TaskScheduler scheduler) {
        this.transactionsCache = transactionsCache;
        this.statisticsCache = statisticsCache;
        this.scheduler = scheduler;
    }

    /**
     * {@inheritDoc}
     * Schedules deletion 1 min after its timestamp, and update statistics cache values.
     *
     * @param transaction to be created.
     */
    @Override
    public void save(Transaction transaction) {
        scheduleDeletion(transaction);

        boolean isFirstTransaction = transactionsCache.getTransactions().isEmpty();

        transactionsCache.getTransactions().add(transaction);
        statisticsCache.addValue(transaction.getBigDecimalAmount(), isFirstTransaction);
    }

    /**
     * Schedules transaction deletion 1 min after its timestamp.
     *
     * @param transaction being processed.
     */
    private void scheduleDeletion(Transaction transaction) {
        long nowEpoch = LocalDateTime.now(UTC).toInstant(ZoneOffset.UTC).toEpochMilli();
        long delay = transaction.getEpochTimestamp() - nowEpoch + ONE_MINUTE_MILLIS;

        scheduler.schedule(() -> delete(transaction), delay, TimeUnit.MILLISECONDS);
    }

    /**
     * Deletes the transaction and removes its values from the statistics cache.
     *
     * @param transaction to be deleted.
     */
    @Override
    public void delete(Transaction transaction) {
        logger.info("Deleting " + transaction);
        transactionsCache.getTransactions().remove(transaction);
        logger.info("Updating statistics...");
        statisticsCache.removeValue(transaction.getBigDecimalAmount());
        logger.info("Statistics updated. " + statisticsCache);
    }

    @Override
    public void deleteAll() {
        statisticsCache.clear();
        transactionsCache.getTransactions().clear();
    }

}
