#=============================================================================
# Dockerfile to build PostgreSQL instance for testing transactions
#=============================================================================

FROM  postgres

ENV   POSTGRES_PASSWORD=postgres

WORKDIR /docker-entrypoint-initdb.d

COPY  database-setup.sql .
COPY  postgres-transactions-spring-data-jdbc.sql .
COPY  postgres-transactions-spring-data-jdbc-testdata.sql .
