# PB138 team project - CIA
Costs and incomes accounting

[Project page](https://lizzardcorp.github.io/pb138project/)

[![Build Status](https://travis-ci.org/LizzardCorp/pb138project.svg?branch=master)](https://travis-ci.org/LizzardCorp/pb138project)

A Java school team project with native XML database.

Team members: 
* Peter Balčirák
* Pavel Vyskočil
* Dominik František Bučík
* Andrej Dravecký

# Prerequsities
`Install eXistDB on your machine. Create user admin with password admin1. Create collection called accounting.`

# Running the project
`Run the eXistDB server on localhost - port 8081`

`git clone https://github.com/LizzardCorp/pb138project.git`

`cd pb138project`

`mvn clean install`

`mvn jetty:run`

`access localhost:8082/accounting in your browser` 
