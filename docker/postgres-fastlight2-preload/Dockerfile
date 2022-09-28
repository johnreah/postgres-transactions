#=============================================================================
# Dockerfile to build Postgres instance with binary loading of FastLight data
# john.reah@utel.co.uk
#=============================================================================

FROM	postgres

ENV	POSTGRES_PASSWORD=postgres

WORKDIR	/docker-entrypoint-initdb.d

COPY	init-database.sh .
COPY	dump.pgdata .
