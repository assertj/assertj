Fest Assertions for Guava
=========================

Fest assertions for Guava provides assertions for Guava types like `Multimap`.

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

`assertThat` and `entry` are static import from GUAVA class (the equivalent of Assertions class in Fest Assert Core).

Typically, you will use `Assertions.assertThat` static import to express assertions on basic types (from fest-assert-core) and `GUAVA.assertThat` for Guava types.

Fest assertions for Guava is available in Maven Central

```xml
 <dependency>
   <groupId>org.easytesting</groupId>
   <artifactId>fest-guava-assert</artifactId>
   <version>1.0</version>
 </dependency>
```

Release notes : 
- 2012-12-17 fest-guava-assert 1.0 released
 

1.0 version only provides `Multimap` assertions but more will come in the future (contributions would be appreciated !).
