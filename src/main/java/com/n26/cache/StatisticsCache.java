package com.n26.cache;

import com.n26.entity.Transaction;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static java.util.Comparator.comparing;

/**
 * In memory cache for statistics.
 */
@Component
public class StatisticsCache {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;

    private final Set<Transaction> transactions;

    public StatisticsCache(TransactionsCache transactionsCache) {
        this.transactions = transactionsCache.getTransactions();
    }

    @PostConstruct
    private void init() {
        clear();
    }

    private void clear() {
        count = 0L;
        sum = ZERO;
        avg = ZERO;
        max = ZERO;
        min = ZERO;
    }

    public synchronized void update(Operation operation, BigDecimal amount) {
        switch (operation) {
            case ADD:
                addValue(amount);
                break;
            case REMOVE:
                removeValue(amount);
                break;
            case CLEAR:
                clear();
                break;
            default:
                break;
        }
    }

    public enum Operation {
        ADD, REMOVE, CLEAR
    }

    private void addValue(BigDecimal amount) {
        count += 1L;
        sum = sum.add(amount);
        avg = calculateAverage();
        boolean isFirstTransaction = transactions.size() == 1;
        if (isFirstTransaction || isGreaterThan(max, amount)) {
            max = amount;
        }
        if (isFirstTransaction || isLessThan(min, amount)) {
            min = amount;
        }
    }

    private void removeValue(BigDecimal amount) {
        count -= 1L;
        sum = sum.subtract(amount);
        avg = calculateAverage();
        if (max.equals(amount)) {
            max = calculateMax();
        }
        if (min.equals(amount)) {
            min = calculateMin();
        }
    }

    private BigDecimal calculateMax() {
        Optional<Transaction> max = transactions.parallelStream().max(comparing(Transaction::getBigDecimalAmount));
        return max.isPresent() ? max.get().getBigDecimalAmount() : ZERO;
    }

    private BigDecimal calculateMin() {
        Optional<Transaction> min = transactions.parallelStream().min(comparing(Transaction::getBigDecimalAmount));
        return min.isPresent() ? min.get().getBigDecimalAmount() : ZERO;
    }

    private BigDecimal calculateAverage() {
        if (count == 0L) {
            return ZERO;
        }
        return sum.divide(BigDecimal.valueOf(count), BigDecimal.ROUND_HALF_UP, RoundingMode.HALF_EVEN);
    }

    private boolean isLessThan(BigDecimal currentValue, BigDecimal newValue) {
        return currentValue == null || newValue.compareTo(currentValue) < 0;
    }

    private boolean isGreaterThan(BigDecimal currentValue, BigDecimal newValue) {
        return currentValue == null || newValue.compareTo(currentValue) > 0;
    }

    public synchronized HashMap<String, BigDecimal> getValues() {
        HashMap<String, BigDecimal> values = new HashMap<>();
        values.put("sum", sum);
        values.put("avg", avg);
        values.put("max", max);
        values.put("min", min);
        values.put("count", new BigDecimal(count));
        return values;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "sum=" + sum +
                ", avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}
