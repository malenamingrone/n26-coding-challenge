package com.n26.entity;

import com.n26.cache.StatisticsCache;

import java.math.BigDecimal;
import java.util.Objects;

public class Statistics {

    private String sum;
    private String avg;
    private String max;
    private String min;
    private long count;

    public Statistics(StatisticsCache statisticsCache) {
        this.sum = formatBigDecimal(statisticsCache.getSum());
        this.avg = formatBigDecimal(statisticsCache.getAvg());
        this.max = formatBigDecimal(statisticsCache.getMax());
        this.min = formatBigDecimal(statisticsCache.getMin());
        this.count = statisticsCache.getCount();
    }

    private String formatBigDecimal(BigDecimal bigDecimal) {
        return Objects.isNull(bigDecimal) ? null : bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
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
