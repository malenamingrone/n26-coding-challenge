package com.n26.validator;

import com.n26.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.n26.utils.DateTimeUtils.TIMESTAMP_FORMATTER;
import static com.n26.utils.DateTimeUtils.UTC;

/**
 * Performs input validation on the {@link Transaction} entity.
 */
public class TransactionValidator {

    private static final Logger logger = LoggerFactory.getLogger(TransactionValidator.class);

    /**
     * Throws {@link ResponseStatusException} with the following HTTP status codes:
     * <ul>
     *     <li>204 – if the transaction is older than 60 seconds.</li>
     *     <li>422 – if any of the fields are not parsable or the transaction date is in the future.</li>
     * </ul>
     *
     * @param transaction to be validated.
     */
    public static void validateTransaction(Transaction transaction) {
        validateAmount(transaction.getAmount());
        validateTimestamp(transaction.getTimestamp());
    }

    private static void validateAmount(String amount) {
        try {
            new BigDecimal(amount);
        } catch (NumberFormatException e) {
            logger.error("An error occurred parsing String amount to BigDecimal.", e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid amount format.", e);
        }
    }

    private static void validateTimestamp(String timestamp) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
        } catch (DateTimeException e) {
            logger.error("An error occurred parsing String timestamp to LocalDateTime.", e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid timestamp format.", e);
        }

        if (dateTime.isBefore(LocalDateTime.now(UTC).minus(60, ChronoUnit.SECONDS))) {
            logger.info("The transaction is older than 60 seconds.");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        if (dateTime.isAfter(LocalDateTime.now(UTC))) {
            logger.error("Future timestamp provided.");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Future transactions are not valid.");
        }
    }

}
