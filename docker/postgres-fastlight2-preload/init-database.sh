psql --command="create database fastlight2;"
psql --command="CREATE EXTENSION IF NOT EXISTS pg_trgm with schema pg_catalog;"
psql --dbname=fastlight2 --command="CREATE EXTENSION IF NOT EXISTS pg_trgm;"
pg_restore --dbname=fastlight2 --single-transaction < dump.pgdata
