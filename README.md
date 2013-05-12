# AssertJ - a rich assertions library for java 

AssertJ provides a rich and intuitive set of strongly typed assertions to use for unit testing (either with JUnit or TestNG).  

* [AssertJ's goals](#goals)
* [Latest News](#news)
* [Quick start](#quickstart)
* [Assertions for your own custom types](#custom types-assertions)
* [Replacing JUnit assertions by AssertJ Assertions](#junit-to-assertj-assertions)
* [Why having forked Fest Assert ?](#fest-assertj)
* [Migrating from Fest assertions](#migrating-from-fest)
* [Documentation & Mailing list](#doc)
* [Contributing](#contributing)
* [Thanks](#thanks)

You can ask questions and make suggestions on [AssertJ google group](https://groups.google.com/forum/?fromgroups=#!forum/assertj).  
To directly jump to a more complete documentation please go to **[AssertJ wiki](https://github.com/joel-costigliola/AssertJ-core/wiki)**.

## <a name="goals"/>AssertJ's goals

AssertJ's ambition is to provide a rich and intuitive set of strongly typed assertions to use for unit testing.  
The idea is that, when writing unit tests, we should have at our disposal assertions specific to the type of the objects we are checking : you are checking a String ? use String specific assertions !  

AssertJ is composed of several modules :
* A core module (this one) to provide assertions for JDK types (String, Collections, File, Map ...). 
* A **[Guava module](https://github.com/joel-costigliola/assertj-guava#readme)** to provide assertions for Guava types (Multimap,Optional, ...).
* A **[Joda Time module](https://github.com/joel-costigliola/assertj-joda-time#readme)** to provide assertions for Joda Time types (DateTime, LocalDateTime).

Assertion missing ? Please [fill a ticket](https://github.com/joel-costigliola/assertj-core/issues) ! 

AssertJ's assertions are super easy to write: just type **```assertThat```** followed the actual value and a dot, and any Java 
IDE will show you all the assertions available for the type of the object to verify. No more confusion about the 
order of the "expected" and "actual" values. Our assertions are very readable as well: they read very close to plain 
English, making it easier for non-technical people to read test code.  
A lot of efforts have to provide intuitive error messages showing as clearly as possible what the problem is.

Note that AssertJ requires at least Java 6.

For more details check **[AssertJ wiki](https://github.com/joel-costigliola/assertj-core/wiki/)**.

## <a name="news"/>Latest News

To have details on the latest releases, please go to [**New and noteworthy**](https://github.com/joel-costigliola/assertj-core/wiki/New-and-noteworthy) wiki section. 

**2013-05-12 : AssertJ core 1.2.0 release**  
**2013-04-14 : AssertJ core 1.1.0 release**  
**2013-03-28 : AssertJ quickie presentation at Devoxx France !**  

**2013-03-26 - AssertJ train releases :**  
* **assertj-core 1.0.0**
* **assertj-guava 1.0.0**
* **assertj-joda-time 1.0.0**
* **assertj-assertions-generator 1.0.0**
* **assertj-assertions-generator-maven-plugin 1.0.0**

## <a name="quickstart"/>Quickstart

It is easy to start using AssertJ, it should take you less than a minute !

#### 1 - Get AssertJ core 

AssertJ core is available in Maven central repository.

```xml
<dependency>
   <groupId>org.assertj</groupId>
   <artifactId>assertj-core</artifactId>
   <version>1.2.0</version>
   <scope>test</scope>
</dependency>
```

### 2 - Add Assertions.* static import

```java
import static org.assertj.core.api.Assertions.*;
```
or the complete list
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

### 3 - Type **assertThat** followed by the actual value and a dot ... 

... and any Java IDE code completion will show you all the assertions available.

That's all !



### Some assertions examples

```java
import static org.assertj.core.api.Assertions.*;

// some of the assertions available for all types
assertThat(yoda).isInstanceOf(Jedi.class);
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
  // argggl ! fellowshipOfTheRing size is 9 and get(9) asks for the 10th element !
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

Having assertions for common types like List is great but it would so nice to have some for your own types !  

Well this is possible with AssertJ because it is easily extensible so it's simple to [write assertions for your custom types](https://github.com/joel-costigliola/assertj-core/wiki/Creating-specific-assertions).  
Moreover, to ease your work, we provide assertions generator that can take a bunch of custom types and create for you specific assertions, we provide : 
* a **[CLI assertions generator](https://github.com/joel-costigliola/assertj-assertions-generator#readme)** 
* A **[Maven plugin assertions generator](https://github.com/joel-costigliola/assertj-assertions-generator-maven-plugin#readme)**  
* An Eclipe plugin assertions generator (available soon)

## <a name="junit-to-assertj-assertions"/>Replacing JUnit assertions by AssertJ Assertions

To help you replace JUnit assertions by AssertJ ones, follow the steps described [**here**](https://github.com/joel-costigliola/assertj-core/wiki/Converting-JUnit-assertions-to-AssertJ-Assertions), they are based on regex search and replace.

## <a name="fest-assertj"/>Why having forked Fest Assert ?

AssertJ is a fork of FEST Assert a great project I have contributed to during 3 years, so why having forked it ?  

Well there are two main reasons : 
* FEST 2.0 will only provide a limited set of assertions, far less than Fest 2.0M10 and even less than FEST 1.x.  
* FEST is not enough open to users demands and contributions.

On the contrary **AssertJ goal is to provide a rich set of assertions** and any reasonable request to enrich AssertJ assertions is welcome as we know it will be useful to someone. Said otherwise, AssertJ is **community driven**, we listen to our users because AssertJ is built for them. 

If you feel that some assertion is missing, please [**fill a ticket**](https://github.com/joel-costigliola/assertj-core/issues) or even better make a contribution ! 

_Joel Costigliola  (AssertJ creator)_

## <a name="migrating-from-fest"/>Migrating from Fest assertions

As AssertJ starts where Fest 2.0M10 has left, migrating from Fest to AssertJ Assertions is easy, you only have to change your static import, just replace :

```java 
import static org.fest.assertions.api.Assertions
``` 

by :

```java 
import static org.assertj.core.api.Assertions
```

This should be all, if not please fill a ticket so that I can update this section.

If you are using Fest Assert 1.x, please read this [migration guide](https://github.com/joel-costigliola/assertj-core/wiki/Migrating-from-FEST-Assert-1.4).

## <a name="doc"/>Documentation & Mailing list

You can ask questions and make suggestions on [**AssertJ google group**](https://groups.google.com/forum/?fromgroups=#!forum/assertj).  
To directly jump to a more complete documentation please go to **[AssertJ wiki](https://github.com/joel-costigliola/AssertJ-core/wiki)**.

## <a name="contributing"/>Want to contribute ?

You are very welcome to contribute any missing useful assertions, please check the [contributor guidelines](CONTRIBUTING.md).

Special thanks to **William Delanoue** and **Mikhail Mazursky** for their contributions to assertj-core 1.2.0.

## <a name="Thanks"/>Thanks

Many thanks to Cloudbees for their FOSS program that allows AssertJ to have a **[Jenkins CI server](https://assertj.ci.cloudbees.com/#)**!

![cloudbees](src/site/resources/images/built-on-Dev@Cloud-Cloudbees.png)

