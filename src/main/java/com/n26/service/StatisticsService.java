package com.n26.service;

import com.n26.entity.Statistics;

/**
 * Performs operations on {@link Statistics} entity.
 */
public interface StatisticsService {

    Statistics getLatestStatistics();

}
