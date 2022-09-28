#!/bin/bash
docker stop postgres-fastlight2-preload
docker image rm utel/postgres-fastlight2-preload
docker build -t utel/postgres-fastlight2-preload .
