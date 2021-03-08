# N26 Coding Challenge

We would like to have a RESTful API for our statistics. The main use case for the
API is to calculate realtime statistics for the last 60 seconds of transactions.

## Requirements
These are the additional requirements for the solution:
- You are free to choose any JVM language to complete the challenge in, but
your application has to run in Maven.
- The API has to be threadsafe with concurrent requests.
- The solution has to work without a database (this also applies to in-memory
databases).
- Your service must not store all transactions in memory for all time.
Transactions not necessary for correct calculation MUST be discarded.
- Unit tests are mandatory.
- mvn clean install and mvn clean integration-test must complete successfully.
- Please ensure that no changes are made to the src/it folder.
- In addition to passing the tests, the solution must be at a quality level that you
would be comfortable enough to put in production.

## Specs

#### POST​ ​/transactions

This endpoint is called to create a new transaction.

Body:
```
{
    "amount": "12.3343",
    "timestamp": "2018-07-17T09:59:51.312Z"
}
```
Where:
- **amount**: transaction amount; a string of arbitrary length that is parsable as a BigDecimal
- **timestamp**:  transaction time in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the UTC timezone (this is not the current timestamp)

Returns: Empty body with one of the following:
- **201** – in case of success
- **204** – if the transaction is older than 60 seconds
- **400** – if the JSON is invalid
- **422** – if any of the fields are not parsable, or the transaction date is in the future


#### GET /statistics
This endpoint returns the statistics computed on the transactions within the last 60 seconds.

Returns: 
```
{
    "sum": "1000.00",
    "avg": "100.53",
    "max": "200000.49",
    "min": "50.23",
    "count": 10
}
```
Where:

- **sum** - ​​​​a BigDecimal specifying the total sum of transaction value in the last 60 seconds 
- **avg** - ​​​​a BigDecimal specifying the average amount of transaction value in the last 60 seconds
- **max​​​** - a BigDecimal specifying single highest transaction value in the last 60 seconds
- **min​​​​​​** - a BigDecimal specifying single lowest transaction value in the last 60 seconds
- **count​​​​** - ​​a long specifying the total number of transactions that happened in the last 60 seconds

#### DELETE /transactions
This endpoint causes all existing transactions to be deleted.
The endpoint should accept an empty request body and return a 204 status code.

## Solution

To avoid storing unnecessary transactions, every time a transaction is made a task is scheduled to remove it from the in-memory storage and update the latest statistics.
The implementation consists of the following.

- All new transactions coming in from the POST /transactions are stored in a _Set_ that will serve as an in-memory cache. 
Statistics are calculated, and a task that deletes the transaction and updates the statistics is scheduled with a delay of 60 seconds after its timestamp.
- A call to GET /statistics reads from the statistics in-memory, which is constantly updated when transactions are created and deleted,
containing always the statistic based on the transactions of the last 60 seconds. 
- A call to DELETE /transactions will delete every transaction stored and clear the statistics as well.

## Data structure used to ensure concurrency

For the in-memory storage of transactions, a hash set backed by a concurrent hash map is used, which is achieved 
by`ConcurrentHashMap.newKeySet()` method. 

The `HashSet` offers constant time performance for the basic operations like add, remove, contains, and size. And the 
`ConcurrentHashMap` provides thread safety ensuring that concurrent updates to the transactions are managed correctly, 
and without synchronizing the whole map. Combining these two, we get fast and safe operations for the stored transactions.
