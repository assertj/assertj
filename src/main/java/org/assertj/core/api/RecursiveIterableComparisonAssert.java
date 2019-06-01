package org.assertj.core.api;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.introspection.IntrospectionError;

import static org.assertj.core.util.IterableUtil.sizeOf;

public class RecursiveIterableComparisonAssert<SELF extends RecursiveIterableComparisonAssert<SELF>> extends RecursiveComparisonAssert<SELF> {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private AbstractIterableAssert iterableAssert;

  public RecursiveIterableComparisonAssert(Object actual, RecursiveComparisonConfiguration recursiveComparisonConfiguration, AbstractIterableAssert iterableAssert) {
    super(actual, recursiveComparisonConfiguration);
    this.iterableAssert = iterableAssert;
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
  }

  /**
   * By default, any type of iterables can be compared with each other. This may cause unexpected behavior when one of them is ordered and other is not.
   * For example, a List can be compared to a Set and even though they might have exact same elements, a Set will not preserve insertion order.
   * So the assertion will fail. To avoid these kind of surprises, comparison ordered can be ignored with calling {@link #ignoringActualIterableOrder ignoringActualIterableOrder}.
   * <p>
   * Example:
   * <pre><code class='java'>public class Person {
   *     String name;
   *     boolean hasPhd;
   * }
   *
   * Person sheldon = new People("Sheldon Cooper", true);
   * Person howard = new People("Howard Wolowitz", false);
   * Person penny = new People("Penny", false);
   *
   * List<Person> peopleList = Arrays.asList(sheldon, howard, penny);
   * Set<Person> peopleSet = new HashSet<>(peopleList);
   *
   * // assertion fails even though iterables contain the exact same elements because order is different.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .isEqualTo(peopleSet);
   *
   * // assertion succeeds because both iterables contain exact same items and comparison order is ignored.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .ignoringActualIterableOrder()
   *                       .isEqualTo(peopleArray);</code></pre>
   *
   * @return this {@link RecursiveIterableComparisonAssert} to chain other methods.
   */
  public SELF ignoringActualIterableOrder() {
    recursiveComparisonConfiguration.ignoreActualIterableOrder(true);
    return myself;
  }

  /**
   * By default, different type of iterables can be compared with each other. To change this behavior, use {@link #withStrictTypeCheckingOnActualIterable() withStrictTypeCheckingOnActualIterable}.
   * This method restricts the types of iterables to be the same.
   * <p>
   * Example:
   * <pre><code class='java'>public class Person {
   *     String name;
   *     boolean hasPhd;
   * }
   *
   * Person sheldon = new People("Sheldon Cooper", true);
   * Person howard = new People("Howard Wolowitz", false);
   * Person penny = new People("Penny", false);
   *
   * List<Person> peopleList = Arrays.asList(sheldon, howard, penny);
   * Set<Person> peopleSet = new LinkedHashSet<>(peopleList);
   *
   * // assertion succeeds because both iterables contain same items and both preserves insertion order.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .isEqualTo(peopleSet);
   *
   * // assertion fails because iterables types are restricted to be compatible, which in this case they are not.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .withStrictTypeCheckingOnActualIterable()
   *                       .isEqualTo(peopleSet);</code></pre>
   *
   * @return this {@link RecursiveIterableComparisonAssert} to chain other methods.
   */
  public SELF withStrictTypeCheckingOnActualIterable() {
    recursiveComparisonConfiguration.setStrictTypeCheckingOnActualIterable(true);
    return myself;
  }

  /**
   * By default, expected type can not be an array. This method allows to send arrays as parameters when asserting.
   * <p>
   * Example:
   * <pre><code class='java'>public class Person {
   *     String name;
   *     boolean hasPhd;
   * }
   *
   * Person sheldon = new People("Sheldon Cooper", true);
   * Person howard = new People("Howard Wolowitz", false);
   *
   * Person[] peopleArray = {sheldon, howard};
   * List<Person> peopleList = Arrays.asList(peopleArray);
   *
   * // assertion fails because expected object can't be of type Array.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .isEqualTo(peopleArray);
   *
   * // assertion succeeds because array type is explicitly made allowed.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .allowingArrayTypeForExpected()
   *                       .isEqualTo(peopleArray);</code></pre>
   *
   * @return this {@link RecursiveIterableComparisonAssert} to chain other methods.
   */
  public SELF allowingArrayTypeForExpected() {
    recursiveComparisonConfiguration.allowArrayTypeForExpected(true);
    return myself;
  }


  /**
   * Asserts that the iterable under test (actual) is equal to the given iterable when their elements are compared field by field recursively
   * (inherited fields are included in the comparison). If the comparison fails it will report all the differences found and which
   * effective {@link RecursiveComparisonConfiguration} was used to help users understand the failure.
   * <p>
   * All the comparison configuration behavior is documented in {@link RecursiveComparisonAssert#isEqualTo(Object) RecursiveComparisonAssert#isEqualTo}
   * is valid also for the comparison made on the elements of actual and expected iterables.
   * <p>
   * Additional behavior configurations for recursively comparing elements of iterables exist.
   * <p>
   * <strong>Strict/Lenient Actual/Expected Iterable type comparison</strong>
   * By default, different types of iterables, for example ArrayList(List) and LinkedHashSet(Ordered Set), can be compared.
   * To turn this behavior off, call {@link #withStrictTypeCheckingOnActualIterable() withStrictTypeCheckingOnActualIterable}.
   * Notice that this behavior is different than {@link #withStrictTypeChecking() withStrictTypeChecking}, where {@link #withStrictTypeChecking() withStrictTypeChecking}
   * forces type compatibility of <strong>elements contained in iterables</strong> and {@link #withStrictTypeCheckingOnActualIterable() withStrictTypeCheckingOnActualIterable}
   * forces type compatibility of the <strong>iterables themselves</strong>, not the elements contained within them.
   * <p>
   * <strong>Comparing ordered collections with unordered ones and vice/versa.</strong>
   * By default, {@link #isEqualTo(Iterable) isEqualTo} takes element order into consideration.
   * This means that a List and a Set can not be considered equals, even if they contain exactly the same elements.
   * To ignore the element order when comparing iterables, call {@link #ignoringActualIterableOrder() ignoringActualIterableOrder}.
   *
   * <pre><code class='java'>public class Person {
   *    String name;
   *    boolean hasPhd;
   * }
   *
   * public class Doctor {
   *    String name;
   *    boolean hasPhd;
   * }
   *
   * Person sheldon = new People("Sheldon", true);
   * Person leonard = new People("Leonard", true);
   * Person howard = new People("Howard", false);
   *
   * Doctor drSheldon = new Doctor("Sheldon", false);
   * Doctor drLeonard = new Doctor("Leonard", false);
   *
   *
   * List<Person> peopleList = Arrays.asList(sheldon, leonard, howard);
   * Set<Person> peopleLinkedSet = new LinkedHashSet<>(peopleList);
   *
   * // assertion succeeds because both iterables have same items in same order.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .isEqualTo(peopleLinkedSet);
   *
   *
   * Set<Person> peopleHashSet = new HashSet<>(peopleList);
   *
   * // assertion fails even though iterables have same items, the order is different.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .allowingArrayTypeForExpected()
   *                       .isEqualTo(peopleHashSet);
   *
   * // assertion succeeds because the order is ignored.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .ignoringActualIterableOrder()
   *                       .isEqualTo(peopleHashSet);
   *
   * // assertion fails because iterables are restricted to be compatible by {@link #withStrictTypeCheckingOnActualIterable() withStrictTypeCheckingOnActualIterable}.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .withStrictTypeCheckingOnActualIterable()
   *                       .isEqualTo(peopleLinkedSet);</code></pre>
   *
   * @param expected the object to compare {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  @SuppressWarnings("unchecked")
  public SELF isEqualTo(Iterable<?> expected) {
    if((actual == null && expected == null) || (actual == expected))
        return myself;

    if(actual == null || expected == null)
      throw new AssertionError("Iterables to compare can not be null.");

    if(recursiveComparisonConfiguration.isInStrictTypeCheckingOnActualMode() &&  !actual.getClass().isAssignableFrom(expected.getClass()))
      throw new AssertionError("types are not compatible.");

    if(sizeOf((Iterable<?>) actual) != sizeOf(expected))
      throw new AssertionError("Sizes are not the same.Actual is of size "
                                          + sizeOf((Iterable<?>) actual)
                                          + ", Expected is of size " + sizeOf(expected));

    boolean ignoreActualIterableOrder = recursiveComparisonConfiguration.getIgnoreActualIterableOrder();
    // order can not be ignored
    if(!ignoreActualIterableOrder)
      iterableAssert.isEqualTo(expected);
    else {
      // order can be ignored
      for(Object element : expected)
        iterableAssert.contains(element);
    }

    return myself;
  }

  /**
   * Asserts that the iterable under test (actual) is equal to the given array when their elements are compared field by field recursively
   * (inherited fields are included in the comparison). If the comparison fails it will report all the differences found and which
   * effective {@link RecursiveComparisonConfiguration} was used to help users understand the failure.
   * <p>
   * This comparison, i.e Iterable to Array, is not allowed by default. To enable it, call {@link #allowingArrayTypeForExpected() allowingArrayTypeForExpected}/
   * <p>
   * All the comparison configuration behavior is documented in {@link RecursiveIterableComparisonAssert#isEqualTo(Object) RecursiveComparisonAssert#isEqualTo}
   * is valid also for the comparison made on the elements of actual and expected iterables.
   *
   * <pre><code class='java'>public class Person {
   *     String name;
   *     boolean hasPhd;
   * }
   *
   * Person sheldon = new People("Sheldon Cooper", true);
   * Person howard = new People("Howard Wolowitz", false);
   *
   * Person[] peopleArray = {sheldon, howard};
   * List<Person> peopleList = Arrays.asList(peopleArray);
   *
   * // assertion fails because expected object can't be of type Array.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .isEqualTo(peopleArray);
   *
   * // assertion succeeds because array type is explicitly made allowed.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .allowingArrayTypeForExpected()
   *                       .isEqualTo(peopleArray);</code></pre>
   *
   * @param values the objects that must be contained in {@code actual}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  public SELF isEqualTo(Object... values) {
    if(!recursiveComparisonConfiguration.getAllowArrayTypeForExpected())
      throw new AssertionError("Type array is not acceptable when comparing iterables, try calling #allowingArrayTypeForExpected configuration.");
    return isEqualTo(Arrays.asList(values));
  }

  /**
   * Verifies that the actual iterable contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   *
   * // assertions will pass
   * assertThat(abc).usingRecursiveComparison().contains("b", "a");
   * assertThat(abc).usingRecursiveComparison().contains("b", "a", "b");
   *
   * // assertion will fail
   * assertThat(abc).usingRecursiveComparison().contains("d");</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values.
   */
  @SuppressWarnings("unchecked")
  public SELF contains(Object... values) {
    iterableAssert.contains(values);
    return myself;
  }

}
