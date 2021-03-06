package com.n26.service.impl;

import com.n26.cache.StatisticsCache;
import com.n26.entity.Statistics;
import com.n26.service.StatisticsService;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsCache statisticsCache;

    public StatisticsServiceImpl(StatisticsCache statisticsCache) {
        this.statisticsCache = statisticsCache;
    }

    @Override
    public Statistics getLatestStatistics() {
        return new Statistics(statisticsCache);
    }

}
