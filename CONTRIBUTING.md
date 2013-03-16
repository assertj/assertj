Contributing
============

Thank you for your interest in contributing to AssertJ assertions !  
We appreciate your effort and to make sure that your pull request is easy to review, we ask you to make note of the following guidelines :

* Use **[AssertJ code Eclipse formatting preferences](src/formatters/assertj-eclipse-formatter.xml)** (for Idea users, it is possible to import it)
* Write complete Javadocs on each assertion methods including a code example.
* Write one JUnit test class for each assertion method with the following naming convention : `<AssertClass>_<assertion>_Test`. 
* Unit tests method naming convention is underscore based (like python) and not camel case, we find it is much readable for long test names !
* Successfull assertion unit test method name must start with : `should_pass_...`.
* Failing assertion unit test method name must start with : `should_fail_...`.
* If possible, add a (fun) code example in [assertj-examples](https://github.com/joel-costigliola/assertj-examples) and use it in the javadoc. 

[assertj-examples](https://github.com/joel-costigliola/assertj-examples) shows how to use efficiently Assert4J through fun unit test examples, it can be seen as AssertJ living documention.

Naming convetions on some examples : 

Here some of `ThrowableAssert` assertions : `hasMessage`, `hasNoCause`, `hasMessageContaining`, for each of them we have a test class, note the naming convention : 
* `ThrowableAssert_hasMessage_Test`
* `ThrowableAssert_hasNoCause_Test`
* `ThrowableAssert_hasMessageContaining_Test`

Let's look at `Throwables_assertHasNoCause_Test` tests method names (underscore based only):
* `should_pass_if_actual_has_no_cause`
* `should_fail_if_actual_is_null`
* `should_fail_if_actual_has_a_cause`

A good javadoc example taken from [`ObjectAssert.isEqualsToByComparingFields`](src/main/java/org/assertj/core/assertions/api/ObjectAssert.java) including :
* assertion description
* a code example showing how to use the assertion
* parameters description (if any)
* exceptions description

```
/**
 * Assert that the actual object is lenient equals to given one by comparing only actual and <b>not null</b> other
 * fields (including inherited fields).
 * <p>
 * It means that if an actual field is not null and the corresponding field in other is null, field will be ignored by
 * lenient comparison, but the inverse will make assertion fail (null field in actual, not null in other).
 * 
 * <pre>
 * Example: 
 * 
 * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT); 
 * TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT); 
 * 
 * // Null fields in other/expected object are ignored, the mysteriousHobbit has null name thus name is ignored
 * assertThat(frodo).isLenientEqualsToByIgnoringNullFields(mysteriousHobbit); //=> OK
 * 
 * // ... but the lenient equality is not reversible !
 * assertThat(mysteriousHobbit).isLenientEqualsToByIgnoringNullFields(frodo); //=> FAIL
 * 
 * </pre>
 * 
 * @param other the object to compare {@code actual} to.
 * @throws NullPointerException if the actual type is {@code null}.
 * @throws NullPointerException if the other type is {@code null}.
 * @throws AssertionError if the actual and the given object are not lenient equals.
 * @throws AssertionError if the other object is not an instance of the actual type.
 */
```

Not all AssertJ javadoc meets this standard but this is something we are working on.
