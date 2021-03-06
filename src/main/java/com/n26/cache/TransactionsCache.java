package com.n26.cache;

import com.n26.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory cache for transactions.
 */
@Component
public class TransactionsCache {

    private final Set<Transaction> transactions = ConcurrentHashMap.newKeySet();

    public Set<Transaction> getTransactions() {
        return transactions;
    }

}