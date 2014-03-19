KantId
======

This project provides a simple api to manage National Id Numbers.
We found out that the use of a functional approach to tackle the problem would result in a much lighter and flexible api, therefor we decided to use java 8.

Use cases are shown in the different tests. Take for example: [FinnishIdNumberTest.java](https://github.com/kantega/KantId/blob/master/kantid/src/test/java/no/kantega/id/fin/FinnishIdNumberTest.java) 

At the moment is provided implementation for all 4 Scandinavian countries:
* Denmark
* Finland
* Norway
* Sweden

For each of these countries is provided:
* validation
* gender calculation
* birthday calculation
* age cal


