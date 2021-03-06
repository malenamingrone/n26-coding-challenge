package com.n26.cache;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

    @PostConstruct
    private void init() {
        clear();
    }

    public void clear() {
        count = 0L;
        sum = ZERO;
        avg = ZERO;
        max = ZERO;
        min = ZERO;
    }

    public void addValue(BigDecimal amount, boolean isFirstTransaction) {
        count += 1L;
        sum = sum.add(amount);
        avg = calculateAverage();
        if (isFirstTransaction || isGreaterThan(max, amount)) {
            max = amount;
        }
        if (isFirstTransaction || isLessThan(min, amount)) {
            min = amount;
        }
    }

    public void removeValue(BigDecimal amount) {
        count -= 1L;
        sum = sum.subtract(amount);
        avg = calculateAverage();
        if (isGreaterThan(max, amount)) {
            max = amount;
        }
        if (isLessThan(min, amount)) {
            min = amount;
        }
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


    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
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
