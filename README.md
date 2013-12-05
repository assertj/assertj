# AssertJ - a rich assertions library for java 

AssertJ provides a rich and intuitive set of strongly-typed assertions to use for unit testing (either with JUnit or TestNG).  

* [AssertJ's goals](#goals)
* [Latest News](#news)
* [Javadoc](#javadoc)
* [Quick start](#quickstart)
* [Assertions for your own custom types](#custom types-assertions)
* [Replacing JUnit assertions by AssertJ Assertions](#junit-to-assertj-assertions)
* [Why have we forked FEST Assert?](#fest-assertj)
* [Migrating from FEST Assertions](#migrating-from-fest)
* [Documentation & Mailing list](#doc)
* [Contributing](#contributing)
* [Thanks](#thanks)

You can ask questions and make suggestions on [AssertJ google group](https://groups.google.com/forum/?fromgroups=#!forum/assertj).  
To directly jump to a more complete documentation please go to **[AssertJ wiki](https://github.com/joel-costigliola/AssertJ-core/wiki)**.

## <a name="goals"/>AssertJ's goals

AssertJ's ambition is to provide a rich and intuitive set of strongly-typed assertions to use for unit testing.  
The idea is that, when writing unit tests, we should have at our disposal assertions specific to the type of the objects
we are checking. If you're checking the value of a String, you use String-specific assertions. Checking the value of
a Map? Use the Map-specific assertions, which make it easy to check on the contents of the map.

AssertJ is composed of several modules:
* A core module (this one) to provide assertions for JDK types (String, Collections, File, Map ...) - see [AssertJ core javadoc](http://joel-costigliola.github.io/assertj/core/api/index.html). 
* A **[Guava module](https://github.com/joel-costigliola/assertj-guava#readme)** to provide assertions for Guava types (Multimap,Optional, ...) - see [AssertJ Guava javadoc](http://joel-costigliola.github.io/assertj/guava/api/index.html).
* A **[Joda Time module](https://github.com/joel-costigliola/assertj-joda-time#readme)** to provide assertions for Joda Time types (DateTime, LocalDateTime) - see [AssertJ Joda Time javadoc](http://joel-costigliola.github.io/assertj/joda-time/api/index.html).

Assertion missing? Please [create an issue](https://github.com/joel-costigliola/assertj-core/issues)! 

AssertJ's assertions are super easy to write: just type **```assertThat```** followed by the actual value in parentheses and then a dot,
and any Java IDE will show you all the assertions available for the type of the object to verify. No more confusion about the 
order of the "expected" and "actual" values. Our assertions are very readable as well: they read very close to plain 
English, making it easier for non-technical people to read test code.
A lot of effort have been done to provide intuitive error messages showing as clearly as possible what the problem is.

Note that AssertJ requires at least Java 6.

For more details check **[AssertJ wiki](https://github.com/joel-costigliola/assertj-core/wiki/)**.
AssertJ core javadoc is published [here](http://joel-costigliola.github.io/assertj/core/api/index.html).

## <a name="news"/>Latest News

To read details on the latest releases, please go to [**New and noteworthy**](https://github.com/joel-costigliola/assertj-core/wiki/New-and-noteworthy) wiki section. 

**2013-11-06 : AssertJ core 1.5.0 release.**  
**2013-09-15 : AssertJ core 1.4.0 release**  
**2013-06-30 : AssertJ core 1.3.0 release (with [Javadoc API published](http://joel-costigliola.github.io/assertj/core/api/index.html))**  
**2013-05-12 : AssertJ core 1.2.0 release**  
**2013-04-14 : AssertJ core 1.1.0 release**  

**2013-03-28 : AssertJ quickie presentation at Devoxx France!**  

**2013-03-26 - AssertJ train releases:**  
* **assertj-core 1.0.0**
* **assertj-guava 1.0.0**
* **assertj-joda-time 1.0.0**
* **assertj-assertions-generator 1.0.0**
* **assertj-assertions-generator-maven-plugin 1.0.0**

## <a name="javadoc"/>Javadoc of latest release

Latest javadoc release : [**AssertJ Core javadoc**](http://joel-costigliola.github.io/assertj/core/api/index.html).

## <a name="quickstart"/>Quickstart

It is easy to start using AssertJ, it should take you less than a minute.

#### 1 - Get AssertJ core 

AssertJ core is available in Maven central repository:

```xml
<dependency>
   <groupId>org.assertj</groupId>
   <artifactId>assertj-core</artifactId>
   <version>1.5.0</version>
   <scope>test</scope>
</dependency>
```

### 2 - Add Assertions.* static import

```java
import static org.assertj.core.api.Assertions.*;
```
or the complete list, or any single one that you want to use:
```java
import static org.assertj.core.api.Assertions.assertThat;  // main one
import static org.assertj.core.api.Assertions.atIndex; // for List assertions
import static org.assertj.core.api.Assertions.entry;  // for Map assertions
import static org.assertj.core.api.Assertions.extractProperty; // for Iterable/Array assertions
import static org.assertj.core.api.Assertions.fail; // use when making exception tests
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown; // idem
import static org.assertj.core.api.Assertions.filter; // for Iterable/Array assertions
import static org.assertj.core.api.Assertions.offset; // for floating number assertions
import static org.assertj.core.api.Assertions.anyOf; // use with Condition
import static org.assertj.core.api.Assertions.contentOf; // use with File assertions
import static org.assertj.core.api.Assertions.tuple; // use with extracting feature
```

You can even configure your IDE, so that when you type `asse` and trigger code completion, it will suggest `assertThat`.

### 3 - Type **assertThat** followed by the actual value (in parentheses) and a dot ... 

... and any Java IDE code completion will show you all the assertions available.

That's all!

If you want to bookmark AssertJ core javadoc, it is published [here](http://joel-costigliola.github.io/assertj/core/api/index.html).

### Some assertions examples

```java
import static org.assertj.core.api.Assertions.*;

// some of the assertions available for all types
assertThat(bilboBaggins).isInstanceOf(Hobbit.class);
assertThat(frodo.getName()).isEqualTo("Frodo");
assertThat(frodo).isNotEqualTo(sauron);
assertThat(frodo).isIn(fellowshipOfTheRing);
assertThat(sauron).isNotIn(fellowshipOfTheRing);

// String specific assertions
assertThat(frodo.getName()).startsWith("Fro").endsWith("do")
                           .isEqualToIgnoringCase("frodo");

// collection specific assertions
assertThat(fellowshipOfTheRing).hasSize(9)
                               .contains(frodo, sam)
                               .doesNotContain(sauron);
// Exception/Throwable specific assertions
try {
  fellowshipOfTheRing.get(9); 
  // fellowshipOfTheRing size is 9 and get(9) asks for the 10th element!
  fail("Expected an IndexOutOfBoundsException because we asked for the 10th element of a 9 entry list.");
} catch (Exception e) {
  assertThat(e).isInstanceOf(IndexOutOfBoundsException.class)
               .hasMessage("Index: 9, Size: 9")
               .hasNoCause();
}

// Map specific assertions, ringBearers is a Map of Ring -> TolkienCharacter
assertThat(ringBearers).hasSize(4)
                       .includes(entry(Ring.oneRing, frodo), entry(Ring.nenya, galadriel))
                       .excludes(entry(Ring.oneRing, aragorn));
```

## <a name="custom types-assertions"/>Assertions for your own custom types 

Having assertions for common types like `List` is great, but you might want some that are specific to your own types.  

This is possible with AssertJ because it is easily extensible so it's simple to [write assertions for your custom types](https://github.com/joel-costigliola/assertj-core/wiki/Creating-specific-assertions).  
Moreover, to ease your work, we provide assertions generator that can take a set of custom types and create specific assertions. The tools provided are: 
* a **[CLI assertions generator](https://github.com/joel-costigliola/assertj-assertions-generator#readme)** 
* A **[Maven plugin assertions generator](https://github.com/joel-costigliola/assertj-assertions-generator-maven-plugin#readme)**  
* An Eclipse plugin assertions generator (available soon)

## <a name="junit-to-assertj-assertions"/>Replacing JUnit assertions by AssertJ Assertions

To help you replace JUnit assertions by AssertJ ones, follow the steps described [**here**](https://github.com/joel-costigliola/assertj-core/wiki/Converting-JUnit-assertions-to-AssertJ-Assertions), they are based on regex search and replace.

## <a name="fest-assertj"/>Why have we forked FEST Assert?

AssertJ is a fork of FEST Assert, a great project that I have contributed to during 3 years, so why have I forked it?  

There are two main reasons: 
* FEST 2.0 will only provide a limited set of assertions, far fewer than found in Fest 2.0M10 and even fewer than FEST 1.x.  
* FEST is not enough open to users demands and contributions.

On the other hand, **AssertJ's goal is to provide a rich set of assertions** and any reasonable request to enrich AssertJ assertions is welcome as we know it will be useful to someone. Said otherwise, AssertJ is **community driven**, we listen to our users because AssertJ is built for them. 

If you feel that an assertion is missing, please [**create an issue**](https://github.com/joel-costigliola/assertj-core/issues), or even better make a contribution ! 

_Joel Costigliola (AssertJ creator)_

## <a name="migrating-from-fest"/>Migrating from FEST Assertions

As AssertJ starts where Fest 2.0M10 left off, migrating from FEST to AssertJ Assertions is easy: just change your static import, from:

```java 
import static org.fest.assertions.api.Assertions...
``` 

to:

```java 
import static org.assertj.core.api.Assertions...
```

And that should be all you need to do. If something's missing or doesn't work, please create an issue so that I can update this section.

For linux users, a quick way to do that is to use something like:

```bash
find . -name "*.java" -exec sed -i "s/org.fest.assertions.api.Assertions/org.assertj.core.api.Assertions/g" '{}' \;
```

If you are using FEST Assert 1.x, please read the [migration guide](https://github.com/joel-costigliola/assertj-core/wiki/Migrating-from-FEST-Assert-1.4).

## <a name="doc"/>Documentation & Mailing list

You can ask questions and make suggestions on [**AssertJ google group**](https://groups.google.com/forum/?fromgroups=#!forum/assertj).  
To directly jump to a more complete documentation please go to **[AssertJ wiki](https://github.com/joel-costigliola/AssertJ-core/wiki)**.

## <a name="contributing"/>Want to contribute?

You are encouraged to contribute any missing, useful assertions. To do so, please read the [contributor guidelines](CONTRIBUTING.md).

Special thanks to **Mariusz Smykula** for their contributions to **assertj-core 1.6.0**.

Special thanks to **William Delanoue**, **Jean Christophe Gay**, **Tomasz Bartczak**, **Michaël Bitard** and **Michał Piotrkowski** for their contributions to **assertj-core 1.5.0**.

Special thanks to **Brian Laframboise**, **William Delanoue**, **Marcus Klimstra**, **Ted Young**, **Piotr Betkier**, **Marcin Mikosik** and  **Jean Christophe Gay** for their contributions to **assertj-core 1.4.0**.

Special thanks to **William Delanoue**, **Jean Christophe Gay** and **Mikhail Mazursky** for their contributions to **assertj-core 1.3.0**.

Special thanks to **William Delanoue** and **Mikhail Mazursky** for their contributions to **assertj-core 1.2.0**.

## <a name="Thanks"/>Thanks

Many thanks to Cloudbees for their FOSS program that allows AssertJ to have a **[Jenkins CI server](https://assertj.ci.cloudbees.com/#)**.

![cloudbees](src/site/resources/images/built-on-Dev@Cloud-Cloudbees.png)

Thanks also to SonarQube for AssertJ Core **[quality reports](http://nemo.sonarqube.org/dashboard/index/528057)**.

![SonarQube](src/site/resources/images/sonar.png)
