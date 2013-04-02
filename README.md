AssertJ Assertions for Guava
=========================

AssertJ assertions for [Guava](http://code.google.com/p/guava-libraries/) provides assertions for Guava types like `Multimap`, `Table` or `Optional`.  

* [Quick start](#quickstart)
* [Latest news](#news)
* [Migrating from Fest Guava assertions](#migrating-from-fest)
* [Using both AssertJ Core assertions and Guava assertions](#core-and-guava-assertions)
* [Contributing](#contributing)

## <a name="quickstart"/>Quick start

To start using Guava assertions

1 - Add this dependency snippet to your project

```xml
 <dependency>
   <groupId>org.assertj</groupId>
   <artifactId>assertj-guava</artifactId>
   <version>1.0.0</version>
   <scope>test</scope>
 </dependency>
```

2 - statically import `GUAVA.assertThat` and use your preferred IDE code completion after `assertThat.`.

Example : 

```java
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.entry;

// Multimap assertions
Multimap<String, String> actual = ArrayListMultimap.create();
actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

assertThat(actual).containsKeys("Lakers", "Spurs");
assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));

// Optional assertions
Optional<String> optional = Optional.of("Test");
assertThat(optional).isPresent().contains("Test");
```

`assertThat` and `entry` are static import from `Assertions` class.

Note that you can find working examples in [assertj-examples](https://github.com/joel-costigliola/assertj-examples/blob/master/src/test/java/org/assertj/examples/guava) guava package.

## <a name="news"/>Latest News

**2013-03-26 : 1.0.0 release, the first release after Fest fork.**

New features since Fest fork : 
* Guava Table assertions 


## <a name="migrating-from-fest"/>Migrating from Fest Guava assertions

AssertJ Assertions for Guava is a fork form FEST Guava assertions and is part of AssertJ assertions portfolio.
The main reason for this fork is that FEST will only provide a small core of assertions in the future whereas I felt on the contrary that it should have provided more assertions.
Another reason is that AssertJ projects are also more opened to community contributions than FEST ones.

Migrating from Fest Guava to AssertJ Guava Assertions is super easy, you only have to change your static import.  

Just replace :

```java 
import static org.fest.assertions.api.GUAVA
``` 

by :

```java 
import static org.assertj.guava.api.Assertions
```

## <a name="core-and-guava-assertions"/>Using both AssertJ [Core assertions](https://github.com/joel-costigliola/assertj-core) and Guava assertions

You will have to make two static import : one for `Assertions.assertThat` to get **core** assertions and one `GUAVA.assertThat` for **Guava** assertions.

```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;
...
// assertThat comes from org.assertj.guava.api.Assertions.assertThat static import
Multimap<String, String> actual = ArrayListMultimap.create();
actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

assertThat(actual).hasSize(6);
assertThat(actual).containsKeys("Lakers", "Spurs");

// assertThat comes from org.assertj.core.api.Assertions.assertThat static import
assertThat("hello world").startsWith("hello");
```

## <a name="contributing"/>Contributing

Thanks for your interest ! Please check our [contributor's guidelines](CONTRIBUTING.md).

