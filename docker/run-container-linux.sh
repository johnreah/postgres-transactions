#!/bin/bash
docker run \
	--name postgres-transaction-test \
	--rm \
	--env POSTGRES_PASSWORD=postgres \
	--publish 5432:5432 \
	fastlight/postgres-transaction-test

