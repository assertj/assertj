FEST-Assert provides a fluent interface for assertions.

Example:

```
int removed = employees.removeFired();
assertThat(removed).isZero();
 
List<Employee> newEmployees = employees.hired(TODAY);
assertThat(newEmployees).hasSize(6)
                        .contains(frodo, sam);
 
assertThat(yoda).isInstanceOf(Jedi.class)
                .isEqualTo(foundJedi)
                .isNotEqualTo(foundSith);
```

The 2.x branch is an attempt to learn from mistakes made in the the 1.x release, in terms of readability and extensibility.

FEST's assertions are incredibly easy to write: just type **```assertThat```** followed the actual value and a dot, and any Java 
IDE will show you all the assertions available for the type of the object to verify. No more confusion about the 
order of the "expected" and "actual" values. Our assertions are very readable as well: they read very close to plain 
English, making it easier for non-technical people to read test code.

For more details please visit :

* **[FEST-Assert's wiki](https://github.com/alexruiz/fest-assert-2.x/wiki)** for the most up to date documentation.

Many thanks to Cloudbees for their FOSS program that allows FEST to have a **[Jenkins CI server](https://fest.ci.cloudbees.com/#)**!

![cloudbees](/alexruiz/fest-assert-2.x/raw/master/src/site/resources/images/built-on-Dev@Cloud-Cloudbees.png)
