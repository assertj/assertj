Fest Assertions for Guava
=========================

Fest assertions for Guava provides assertions for Guava types like `Multimap`.

Example : 

```java
Multimap<String, String> actual = ArrayListMultimap.create();
actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));

assertThat(actual).containsKeys("Lakers", "Spurs");
assertThat(actual).contains(entry("Lakers", "Kobe Bryant"), entry("Spurs", "Tim Duncan"));
```

`assertThat` and `entry` are static import from GUAVA class (the equivalent of Assertions class in Fest Assert Core).

Typically, you will use `Assertions.assertThat` static import to express assertions on basic types (from fest-assert-core) and `GUAVA.assertThat` for Guava types.

For the time being, only `Multimap` assertions are available but more will come in the future (contributions would be appreciated !).
