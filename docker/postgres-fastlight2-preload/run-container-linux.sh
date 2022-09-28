#!/bin/bash
docker run \
	--name postgres-fastlight2-preload \
	--rm \
	--env POSTGRES_PASSWORD=postgres \
	--publish 5432:5432 \
	utel/postgres-fastlight2-preload

