Contributing
============

Thank you for your interest in contributing to AssertJ assertions !  
We appreciate your effort and to make sure that your pull request is easy to review, we ask you to make note of the following guidelines :

* Use **[AssertJ code Eclipse formatting preferences](https://github.com/joel-costigliola/assertj-core/blob/master/src/ide-support/assertj-eclipse-formatter.xml)** (for Idea users, it is possible to import it)
* Write complete Javadocs on each assertion methods including a code example.
* Write one JUnit test class for each assertion method with the following naming convention : `<AssertClass>_<assertion>_Test`. 
* Unit tests method naming convention is underscore based (like python) and not camel case, we find it is much readable for long test names !
* Successfull assertion unit test method name must start with : `should_pass_...`.
* Failing assertion unit test method name must start with : `should_fail_...`.
* If possible, add a (fun) code example in [assertj-examples](https://github.com/joel-costigliola/assertj-examples) and use it in the javadoc. 

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
