package com.n26.validation;

import com.n26.entity.Transaction;
import com.n26.validator.TransactionValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

import static com.n26.utils.DateTimeUtils.TIMESTAMP_FORMATTER;

public class TransactionValidationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOlderThanOneMinuteTransaction() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Response status 204");
        TransactionValidator.validateTransaction(new Transaction("12.3343", "2018-07-17T09:59:51.312Z"));
    }

    @Test
    public void testNotParsableAmountTransaction() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Response status 422");
        TransactionValidator.validateTransaction(new Transaction("One hundred", TIMESTAMP_FORMATTER.format(Instant.now())));
    }

    @Test
    public void testNotParsableTimestampTransaction() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Response status 422");
        TransactionValidator.validateTransaction(new Transaction("12.3343", "2020-03-06"));
    }

    @Test
    public void testFutureTimestampTransaction() {
        thrown.expect(ResponseStatusException.class);
        thrown.expectMessage("Response status 422");
        TransactionValidator.validateTransaction(new Transaction("12.3343", TIMESTAMP_FORMATTER.format(Instant.now().plusSeconds(1))));
    }

}
