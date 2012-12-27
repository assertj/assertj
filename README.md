Fest Assertions for Guava
=========================

Fest assertions for [Guava](http://code.google.com/p/guava-libraries/) provides assertions for Guava types like `Multimap` .

## Quick start

To start using Guava assertions, you just have to statically import `GUAVA.assertThat` and use your preferred IDE code completion after `assertThat.` !

Example : 

```java
import static org.fest.assertions.api.GUAVA.assertThat;
import static org.fest.assertions.api.GUAVA.entry;

Multimap<String, String> actual = ArrayListMultimap.create();
actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

assertThat(actual).containsKeys("Lakers", "Spurs");
assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
```

`assertThat` and `entry` are static import from `GUAVA` class (the equivalent of `Assertions` class in [Fest Assert Core](https://github.com/alexruiz/fest-assert-2.x/wiki)).

Fest assertions for Guava is available in Maven Central

```xml
 <dependency>
   <groupId>org.easytesting</groupId>
   <artifactId>fest-guava-assert</artifactId>
   <version>1.0</version>
 </dependency>
```

Note that you can find working example in [MultimapAssertionsExamples.java](https://github.com/joel-costigliola/fest-examples/blob/master/src/main/java/org/fest/assertions/examples/MultimapAssertionsExamples.java) from [fest-examples](https://github.com/joel-costigliola/fest-examples/) project.

## Using both FEST [Core assertions](https://github.com/alexruiz/fest-assert-2.x/wiki) and Guava assertions

You will have to make two static import : one for `Assertions.assertThat` to get **core** assertions and one `GUAVA.assertThat` for **Guava** assertions.

```java
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.GUAVA.assertThat;
...
// assertThat comes from org.fest.assertions.api.GUAVA.assertThat static import
assertThat(actual).hasSize(6);
assertThat(actual).containsKeys("Lakers", "Spurs");

// assertThat comes from org.fest.assertions.api.Assertions.assertThat static import
assertThat("hello world").startsWith("hello");
```

## Release notes history

2012-12-17 : 1.0 released
 
1.0 version only provides `Multimap` assertions but more will come in the future (contributions would be appreciated !).

