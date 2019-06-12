package org.assertj.core.api;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.introspection.IntrospectionError;

import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.Lists.list;
import static java.lang.String.format;

public class RecursiveIterableComparisonAssert<SELF extends RecursiveIterableComparisonAssert<SELF, ACTUAL>,
                                              ACTUAL extends Iterable<?>> extends RecursiveComparisonAssert<SELF, ACTUAL> {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private AbstractIterableAssert iterableAssert;

  public RecursiveIterableComparisonAssert(ACTUAL actual, RecursiveComparisonConfiguration recursiveComparisonConfiguration, AbstractIterableAssert iterableAssert) {
    super(actual, recursiveComparisonConfiguration);
    this.iterableAssert = iterableAssert;
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
  }

  /**
   * By default, iterables can't be compared to arrays, this method allows it.
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
   *                       .allowingComparingIterableWithArray()
   *                       .isEqualTo(peopleArray);</code></pre>
   *
   * @return this {@link RecursiveIterableComparisonAssert} to chain other methods.
   */
  public SELF allowingComparingIterableWithArray() {
    recursiveComparisonConfiguration.allowComparingIterableWithArray(true);
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
   * To enforce strict type checking, call {@link #withStrictTypeChecking}.
   * Note that calling {@link #withStrictTypeChecking}, in addition to enforcing type compatibility of Iterables themselves,
   * it also enforces type compatibility of the elements contained in those Iterables. For example:
   *
   * <pre><code class='java'> ArrayList<Person> arrayList = new ArrayList<>();
   * LinkedList<Person> linkedList = new LinkedList<>();
   *
   * // assertion fails because strict type check is enforced and ArrayList and LinkedList aren't subtypes of each other.
   * assertThat(arrayList).usingRecursiveComparison()
   *                      .withStrictTypeChecking()
   *                      .isEqualTo(linkedList);
   *
   * LinkedList<Person> listPerson = new LinkedList<>();
   * LinkedList<PersonDTO> listPersonDTO = new LinkedList<>();
   *
   * // assertion fails because strict type check is enforced and although LinkedLists are compatible, Person and PersonDTO are not.
   * assertThat(listPerson).usingRecursiveComparison()
   *                      .withStrictTypeChecking()
   *                      .isEqualTo(listPersonDTO);</code></pre>
   *
   * <p>
   * <strong>Comparing ordered collections with unordered ones and vice/versa.</strong>
   * By default, {@link #isEqualTo(Iterable) isEqualTo} takes element order into consideration.
   * This means that a List and a Set can not be considered equals, even if they contain exactly the same elements.
   * To ignore the element order when comparing iterables, call {@link #ignoringCollectionOrder}.
   * Note that calling {@link #ignoringCollectionOrder} also causes the order of any fields/properties that are iterables/collections to be ignored.
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
   * Set<Person> peopleHashSet = new HashSet<>(peopleList);
   *
   * // assertion fails even though iterables have same items, the order is different.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .isEqualTo(peopleHashSet);
   *
   * // assertion succeeds because the order is ignored.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .ignoringCollectionOrder()
   *                       .isEqualTo(peopleHashSet);
   *
   * // assertion fails because iterables are restricted to be compatible by {@link #withStrictTypeChecking}.
   * assertThat(peopleList).usingRecursiveComparison()
   *                       .withStrictTypeChecking()
   *                       .isEqualTo(peopleLinkedSet);</code></pre>
   *
   * @param expected the object to compare {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  @SuppressWarnings("unchecked")
  public SELF isEqualTo(Iterable<?> expected) {
    // whether both actual and expected is null or referencing to same object.
    boolean sameReferenceOrNull = checkIfExpectedIsSameReferenceOrNull(expected);
    if(sameReferenceOrNull)
      return myself;

    // actual and expected are not null and not referencing the same object. We can now start recursive comparison.
    if(recursiveComparisonConfiguration.isInStrictTypeCheckingMode() &&  !actual.getClass().isAssignableFrom(expected.getClass()))
      throw new AssertionError(format("Comparison enforces strict type checking and fields are considered different since %s is not a subtype of %s.",
                                  actual.getClass().getName(), expected.getClass().getName()));

    if(sizeOf(actual) != sizeOf(expected))
      throw new AssertionError(format("actual and expected values are %s of different size, actual size=%s when expected size=%s%nActual: %s%nExpected: %s",
                                  "iterables", sizeOf(actual), sizeOf(expected), actual, expected));

    boolean ignoreCollectionOrder = recursiveComparisonConfiguration.getIgnoreCollectionOrder();
    // comparison order can not be ignored
    if(!ignoreCollectionOrder)
        iterableAssert.isEqualTo(expected);
    else {
      // comparison order can be ignored
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
   * This comparison, Iterable to Array, is not allowed by default. To enable it, call {@link #allowingComparingIterableWithArray}.
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
   *                       .allowingComparingIterableWithArray()
   *                       .isEqualTo(peopleArray);</code></pre>
   *
   * @param values the objects that must be contained in {@code actual}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  public SELF isEqualTo(Object... values) {
    if(!recursiveComparisonConfiguration.getAllowComparingIterableWithArray())
      throw new AssertionError("By default Iterable can't be compared to array, call allowingComparingIterableWithArray to allow it.");
    return isEqualTo(list(values));
  }

  /**
   * Verifies that the actual iterable contains the given values, in any order.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = new ArrayList("a", "b", "c");
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
