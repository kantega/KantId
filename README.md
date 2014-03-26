## KantId - extendable ligthweigth library for Id Numbers.

*25.03.2014 version 0.1-SNAPSHOT*

#### SUMMARY
***

This project provides a simple api to manage National Id Numbers. We found out that the use of a functional approach to tackle the problem would result in a much lighter and flexible api, therefor we decided to use java 8

At the moment there is an implementation for 4 nordic countries:
* Denmark
* Finland
* Norway
* Sweden

For each of these countries the library provides:
* Validation
* Gender calculation
* Birthday calculation
* Age calculation

#### USAGE
#####_Get an instance either from constructor or factory:_
```java
forId("13020955966");
new NorwegianIdNumber("13020955966");
new NorwegianIdNumber("13020955966", LOCALE_NOR);
```
#####_Use methods of selected implementation, and instance:_
```java
forId("13020955966").isValid(NorwegianIdNumber::valid)
forId("540629-7407").isValid(FinnishIdNumber::Valid)

forId("13020955966", LOCALE_NOR).gender(NorwegianIdNumber::gender)
swedishIdNumber.gender(SwedishIdNumber::gender)

forId("1406108548").birthday(DanishIdNumber::birthday)
danishIdNumber.age(DanishIdNumber::birthday)
```

#### REQUIREMENTS
***
- JDK 8

#####_For building_
- Gradle v. 1.9 or newer (optional, you can also just use gradlew (*nix) or gradlew.bat (win)


#### INSTALLATION
***
#####_From source code_
* Clone git repository at: https://github.com/kantega/KantId.git
* Compile with Java 8 and Gradle: "gradle build"

#####_From repository_

#### HOW TO EXTEND LIBRARY
***
#####_By extending IdNumber_

#####_By extending LocalIdNumber_

#### DOCUMENTATION
***
* [JavaDoc](http://kantega.github.io/KantId/kantid/docs/javadoc/index.html)
* Wiki pages for [National identification numbers](http://en.wikipedia.org/wiki/National_identification_number)
* [Norway](http://en.wikipedia.org/wiki/National_identification_number#Norway)
* [Sweden](http://en.wikipedia.org/wiki/National_identification_number#Sweden)
* [Finland](http://en.wikipedia.org/wiki/National_identification_number#Finland)
* [Denmark](http://en.wikipedia.org/wiki/National_identification_number#Denmark)

#### LICENSE
***
Published under [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

#### COPYRIGHT
***
[Kantega AS 2014](http://www.kantega.no)


