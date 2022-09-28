#!/bin/bash -xe
################################################################################
# Build docker image for postgres-fastlight2 with preloaded binary data
# john.reah@utel.co.uk
################################################################################

PG_IMAGE=docker.utel.co.uk/utel/postgres-fastlight2:latest
PG_CONTAINER=postgres-builder

set +e
docker stop $PG_CONTAINER
docker rm  $PG_CONTAINER
set -e

#docker pull $PG_IMAGE

# Start detached postgres container with fastlight data. Returns immediately but takes several minutes to initialise data from scripts.
docker run --name $PG_CONTAINER -d --rm --env POSTGRES_PASSWORD=postgres --publish 5432:5432 $PG_IMAGE

# Block until postgres log shows that initialisation phase is complete.
( docker logs -f -n0 $PG_CONTAINER 2>&1 & ) | grep -q "PostgreSQL init process complete; ready for start up."

# Dump the data from the container to a local binary file.
docker exec --user postgres postgres-builder pg_dump --format custom fastlight2 >dump.pgdata

