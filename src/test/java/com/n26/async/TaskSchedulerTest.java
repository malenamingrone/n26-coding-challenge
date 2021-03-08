package com.n26.async;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class TaskSchedulerTest {

    private final TaskScheduler scheduler = new TaskScheduler();

    private boolean executed;

    @Before
    public void setUp() {
        executed = false;
    }

    @Test
    public void testSchedule() throws InterruptedException {
        scheduler.schedule(this::someScheduledTask, 2000L, TimeUnit.MILLISECONDS);
        Thread.sleep(2100L);
        assertTrue(executed);
    }

    private void someScheduledTask() {
        executed = true;
    }

}
