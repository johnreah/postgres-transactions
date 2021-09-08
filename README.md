# Postgres Transactions

This is a set of demo projects intended to illustrate and compare some different ways
of implementing transactions in a Java application with a PostgreSQL database.

The code has been kept as simple as possible to illustrate the principles involved, but it
it does include concepts such as Entities, Repositories/DAOs and Service classes. The
majority of the code exists as JUnit tests, and it concentrates on comparing the
behaviour with and without transactions in place. The projects are organised as
separate projects under an aggregator parent POM, so they can be run with
`mvn clean test` either as individual projects or all together, though it will
probably be more interesting to run or step through the tests one by one in an IDE.

Because this project is concerned specifically with PostgreSQL the tests are all
integration tests rather than unit tests, and they expect a running instance of
PostgreSQL to be available. This is provided by files in the `docker` subdirectory.

These command line examples assume a Windows development machine running a WSL2 Linux
instance with Docker Desktop installed. To build and run the Docker image for the Postgres
database:
```
cd docker
./build.sh
./run-container-linux.sh
```
