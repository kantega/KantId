## KantId - extendable lightweight library for Id Numbers.

[![Build Status](https://travis-ci.org/kantega/KantId.svg?branch=master)](https://travis-ci.org/kantega/KantId)

*03.04.2014 version 0.2-SNAPSHOT*

#### SUMMARY
***

This project provides a simple API to manage National Id numbers. The API uses the Java 8 functional programming paradigm which results in a much lighter, flexible and extensible API.

At the moment there is an implementation for 5 nordic countries and Ireland:
* [Denmark](http://kantega.github.io/KantId/kantid/docs/javadoc/no/kantega/id/dk/DanishIdNumber.html)
* [Finland](http://kantega.github.io/KantId/kantid/docs/javadoc/no/kantega/id/fin/FinnishIdNumber.html)
* [Norway](http://kantega.github.io/KantId/kantid/docs/javadoc/no/kantega/id/no/NorwegianIdNumber.html)
* [Sweden](http://kantega.github.io/KantId/kantid/docs/javadoc/no/kantega/id/se/SwedishIdNumber.html)
* [Iceland](http://kantega.github.io/KantId/kantid/docs/javadoc/no/kantega/id/is/IcelandishIdNumber.html)
* [Ireland](http://kantega.github.io/KantId/kantid/docs/javadoc/no/kantega/id/ie/PersonalPublicServiceNumber.html)

For each of these countries the library provides:
* Validation
* Gender calculation
* Birthday calculation
* Age calculation

#### USAGE
##### Get an instance either from constructor or factory:
```java
forId("13020955966");
new NorwegianIdNumber("13020955966");
new NorwegianIdNumber("13020955966", LOCALE_NOR);
```
##### Use methods of selected implementation, and instance:
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

##### For building
- Gradle v. 1.9 or newer (optional, you can also just use gradlew (*nix) or gradlew.bat (win)


#### INSTALLATION
***
##### From source code
* Clone git repository at: https://github.com/kantega/KantId.git
* Compile with Java 8 and Gradle: "gradle build"

##### From repository
* _Gradle_: group: 'no.kantega', name: 'KantId', version: '0.1'
* _Maven_:
```xml
<dependency>
    <groupId>no.kantega</groupId>
    <artifactId>KantId</artifactId>
    <version>0.1</version>
</dependency>
```

#### HOW TO EXTEND LIBRARY
***
One of our main object is to encourage further API development by addition of more countries or functionality. We have attempted to make the API easy to extend by leveraging the Java 8 functional paradigm. Here follows two ways to extend the API:
#####_By extending IdNumber class_
* Create a class with a proper constructor or factory method for new instances.
 ```java
    public MyIdNumber(final String idToken) {
        super(idToken);
    }
    public static MyIdNumber forId(String idToken) {
        return new MyIdNumber(idToken);
    }

 ```
* Implement methods you want to use with your class, this doesn't mean that you 
  have to override methods! __*Just implement needed functionality.*__ Use optionally static methods
  for more fluent usage pattern.
```java
    public static Optional<Gender> gender(IdNumber idNumber) {
        char genderBit = idNumber.getIdToken().charAt(GENDER_BIT);
        if (isDigit(genderBit)) {
            return genderBit % 2 == 0 ? Optional.of(FEMALE) : Optional.of(MALE);
        }
        return empty();
    }

```
* See usage patterns above, use your class via static methods or instance methods.

##### By extending LocalIdNumber
* Implement _supports(Locale locale)_ method in no.kantega.id.api.LocalIdNumber to support your locale(s).
```java
    @Override
    protected boolean supports(Locale locale) {
        return locale != null && MY_COUNTRY.equals(locale.getCountry());
    }

```


#### DOCUMENTATION
***
* [JavaDoc](http://kantega.github.io/KantId/kantid/docs/javadoc/index.html)
* Wiki pages for [National identification numbers](http://en.wikipedia.org/wiki/National_identification_number)
  * [Norway](http://en.wikipedia.org/wiki/National_identification_number#Norway)
  * [Sweden](http://en.wikipedia.org/wiki/National_identification_number#Sweden)
  * [Finland](http://en.wikipedia.org/wiki/National_identification_number#Finland)
  * [Denmark](http://en.wikipedia.org/wiki/National_identification_number#Denmark) (official [documentation](https://cpr.dk/media/167692/personnummeret%20i%20cpr.pdf))
  * [Iceland](http://en.wikipedia.org/wiki/Kennitala)
  * [Ireland](http://en.wikipedia.org/wiki/National_identification_number#Ireland)
  
#### LICENSE
***
Published under [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

#### COPYRIGHT
***
[Kantega AS 2014](http://www.kantega.no)


