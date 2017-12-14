# Introduction

This project aims at wrapping FlexMonster Java API into an executable JAR to:
* read data from a database (by specifying an SQL query)
* write the data compressed using FlexMonster API into a file

# Version details

* Uses Flexmonster compressor `2.409` compatible with `2.213`.

# How to use it

Use the executable jar file available at `dist/flexmonster-wrapper-1.1.jar`.

## Writing the to standard output

```
$ java -jar \
    -Ddatabase.user=postgres \
    -Ddatabase.password= \
    -Ddatabase.host=localhost \
    -Ddatabase.port=5432 \
    -Ddatabase.databaseName=engine \
    flexmonster-wrapper-1.1.jar \
    "SELECT * FROM x"
```

## Writing to a file

```
$ java -jar \
    -Ddatabase.user=postgres \
    -Ddatabase.password= \
    -Ddatabase.host=localhost \
    -Ddatabase.port=5432 \
    -Ddatabase.databaseName=engine \
    -Doutput.file=true \
    -Doutput.path=/home/gilbert/montagne.ocsv \
    flexmonster-wrapper-1.1.jar \
    "SELECT * FROM x"
```

# Parameters

All these parameters can be defined as System Properties (not Environment Variables) using the java `-D` argument. Example: `-Ddatabase.use=michel`.

| Variable | Default Value | Type | Required? | Description  | Example |
| ---- | ----- | ------ | ----- | ------ | ----- |
| `database.user` | `null`| `String` | Required | Database username | `coincoin` |
| `database.password` | `''`| `String` | Optional | Database password | `42424242` |
| `database.host` | `null` | `String` | Required | Database host | `127.0.0.1` |
| `database.port` | `null` | `Integer` | Required | Database port | `5432` |
| `database.driver` | `org.postgresql.Driver` | `String` | Optional | Driver that should be loaded using reflection to define which driver to use.  | `com.mysql.jdbc.Driver` |
| `database.provider` | `postgres` | `String` | Optional | What kind of database is this? (this value will be used as a suffix for the connection string) | `mysql` |
| `database.databaseName` | `postgresql` | `String` | Optional | What kind of database is this? (this value will be used as a suffix for the connection string) | `mysql` |
| `output.file` | `false` | `boolean` | Optional | If the output should be written to a file instead of the standard output | `true` |
| `output.path` | `null` | `String` | Optional | Where this program should write compressed data. This file should not exist yet. Should be used if and only if `output.file=true` | `/home/michel/output.ocsv` |

# Program argument

There is a single program argument, which is the query to execute on the database. *Do not forget the double quotes!*

# Exit code

* Success: `0`
* Failure: `1`

# Build

Run: `./gradlew build` and the resulting JAR file will be written at `build/libs/flexmonster-wrapper-1.1.jar`

# Side-notes

* PostgreSQL driver is already embedded in this executable.
* If you want to use another driver, please add the `-classpath <driver-jar-file>` argument to the command line.

# Changelog

## Version 1.1

* Allow to write to STDOUT instead of a given file (using `output.file=true`)