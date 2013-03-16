## AssertJ's goal

AssertJ's ambition is to provide a rich and intuitive set of strongly typed assertions to use for unit testing (either with JUnit or TestNG). The idea is that, when writing unit tests, we should have at our disposal assertions specific to the type of the objects we are checking : you are checking a String ? use String specific assertions !  

AssertJ is composed of several modules :
* Core module : provide assertions for JDK types (String, Collections, File, Map ...). 
* Guava module provides assertions for Guava types. 
* Joda Time module provides assertions for Joda Time types. 

Assertion missing ? Please [fill a ticket](https://github.com/joel-costigliola/assertj-core/issues) ! 

What about my custom types ?   
AssertJ is easily extensible so it's simple to write assertions for your custom types, moreover, to ease your work, we provide assertions generator that can take a bunch of custom types and create for you specific assertions (you can use the assertion generator from eclipse or maven - WIP).  
check here : _TO COMPLETE_

AssertJ's assertions are super easy to write: just type **```assertThat```** followed the actual value and a dot, and any Java 
IDE will show you all the assertions available for the type of the object to verify. No more confusion about the 
order of the "expected" and "actual" values. Our assertions are very readable as well: they read very close to plain 
English, making it easier for non-technical people to read test code.  
A lot of efforts have to provide intuitive error messages showing as clearly as possible what the problem is.

### Example

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

## History

AssertJ is a fork of FEST Assert a great project I have contributed to during 3 years, so why having forked it ?  
Well the main reason is that FEST 2.0 will only provide a limited set of assertions (even less than FEST 1.x), and I felt on the contrary that it should have provided more assertions.  
This is why I forked FEST and created **AssertJ : to provide a rich set of assertions, simple and easy to use.** 

Another difference is that AssertJ will be more open than FEST regarding users demands and contributions, any resonable requests to enrich AssertJ assertions is welcome since we know it will be useful to someone.  
Said otherwise, AssertJ is **community driven**, we listen to our users because AssertJ is built for them. 

_Joel Costigliola  (AssertJ creator)_

## Documentation

For more details please visit :

* **[AssertJ wiki](https://github.com/joel-costigliola/AssertJ-core/wiki)** for the most up to date documentation.
