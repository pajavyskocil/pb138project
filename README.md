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
`Install eXistDB on your machine. Configure the access to database in config.conf file.`
`Install Maven on your machine`

# Running the project
`Run the eXistDB server (configuration for server can be changed in config.conf).`

`git clone https://github.com/LizzardCorp/pb138project.git`

`cd pb138project`

`mvn clean install`

`mvn jetty:run-war`

`access localhost:8082/accounting/ in your browser` 

# Configuring database
Database can be configured in the config.conf file. Shortcut to this file is in the root of the application.


