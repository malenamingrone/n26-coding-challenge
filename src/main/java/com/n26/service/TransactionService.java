package com.n26.service;

import com.n26.entity.Transaction;

/**
 * Performs operations on {@link Transaction} entity.
 */
public interface TransactionService {

    /**
     * Creates a transaction in the system.
     *
     * @param transaction to be created.
     */
    void save(Transaction transaction);

    /**
     * Deletes every transaction stored at that moment.
     */
    void deleteAll();

}
