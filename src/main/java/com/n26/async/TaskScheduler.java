package com.n26.async;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class TaskScheduler {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Creates and executes a one-shot action that becomes enabled after the given delay.
     *
     * @param task to be executed at specific time.
     * @param delay the time from now to delay execution.
     * @param timeUnit the time unit of the delay parameter.
     */
    public void schedule(Runnable task, long delay, TimeUnit timeUnit) {
        executor.schedule(task, delay, timeUnit);
    }

}