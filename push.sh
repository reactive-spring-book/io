#!/usr/bin/env bash
mvn clean spring-javaformat:apply package && git commit -am polish && git push