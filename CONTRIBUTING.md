Contributing
============

Thank you for your interest in contributing to FEST Guava assertions !  
We appreciate your effort, but to make sure that the inclusion of your patch is a smooth process, we ask that you make note of the following guidelines.

* Use **[FEST code formatting preferences](https://raw.github.com/alexruiz/fest-eclipse-preferences/master/formatter.xml)** (Eclipse)
* Write complete Javadocs on each assertion method including a code example.
* Write one JUnit test class for each assertion method. 
* Use FEST unit tests naming convention** that is underscore based and not camel case like the rest of the code.
* Successfull assertion unit test name should start with : `should_pass_...`.
* Failing assertion unit test name should start with : `should_fail_...`.

Example : 

Since `OptionalAssert` has three assertions : `isPresent`, `isAbsent`, `contains`, we have three test classes : 
* `OptionalAssert_isPresent_Test`
* `OptionalAssert_isAbsent_Test`
* `OptionalAssert_contains_Test`

Let's look at `OptionalAssert_contains_Test` tests names :
* `should_pass_when_actual_contains_expected_value`
* `should_fail_if_actual_is_null`
* `should_fail_when_option_does_not_contain_expected_value`
* `should_fail_when_optional_contains_nothing`
