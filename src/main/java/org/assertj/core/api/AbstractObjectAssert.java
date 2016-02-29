/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.extractor.Extractors.byName;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for all implementations of assertions for {@link Object}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 * @author Libor Ondrusek
 */
public abstract class AbstractObjectAssert<S extends AbstractObjectAssert<S, A>, A> extends AbstractAssert<S, A> {

  private Map<String, Comparator<?>> comparatorByPropertyOrField = new HashMap<>();
  private Map<Class<?>, Comparator<?>> comparatorByType = new HashMap<>();

  protected AbstractObjectAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Assert that the actual object is equal to the given one by comparing actual's properties/fields with other's
   * <b>not null</b> properties/fields only (including inherited ones).
   * <p/>
   * It means that if an actual field is not null and the corresponding field in other is null, this field will be
   * ignored in comparison, but the opposite will make assertion fail (null field in actual, not null in other) as 
   * the field is used in the performed comparison and the values differ.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible</b> fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * The objects to compare can be of different types but the properties/fields used in comparison must exist in both, 
   * for example if actual object has a name String field, it is expected other object to also have one.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter mysteriousHobbit = new TolkienCharacter(null, 33, HOBBIT);
   * 
   * // Null fields in other/expected object are ignored, the mysteriousHobbit has null name thus name is ignored
   * assertThat(frodo).isEqualToIgnoringNullFields(mysteriousHobbit); // OK
   * 
   * // ... but the lenient equality is not reversible !
   * assertThat(mysteriousHobbit).isEqualToIgnoringNullFields(frodo); // FAIL</code></pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws NullPointerException if the actual or other object is {@code null}.
   * @throws AssertionError if the actual and the given object are not lenient equals.
   * @throws IntrospectionError if one of actual's field to compare can't be found in the other object.
   */
  public S isEqualToIgnoringNullFields(Object other) {
    objects.assertIsEqualToIgnoringNullFields(info, actual, other, comparatorByPropertyOrField, comparatorByType);
    return myself;
  }

  /**
   * Assert that the actual object is equal to the given one using a property/field by property/field comparison <b>on the given properties/fields only</b>
   * (fields can be inherited fields or nested fields). This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * If an object has a field and a property with the same name, the property value will be used over the  field.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * The objects to compare can be of different types but the properties/fields used in comparison must exist in both, 
   * for example if actual object has a name String field, it is expected the other object to also have one.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT);
   * 
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;race&quot;); // OK
   * 
   * // they are also equals when comparing only race name (nested field).
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;race.name&quot;); // OK
   * 
   * // ... but not when comparing both name and race
   * assertThat(frodo).isEqualToComparingOnlyGivenFields(sam, &quot;name&quot;, &quot;race&quot;); // FAIL</code></pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @param propertiesOrFieldsUsedInComparison properties/fields used in comparison.
   * @throws NullPointerException if the actual or other is {@code null}.
   * @throws AssertionError if the actual and the given objects are not equals property/field by property/field on given fields.
   * @throws IntrospectionError if one of actual's property/field to compare can't be found in the other object.
   * @throws IntrospectionError if a property/field does not exist in actual.
   */
  public S isEqualToComparingOnlyGivenFields(Object other, String... propertiesOrFieldsUsedInComparison) {
    objects.assertIsEqualToComparingOnlyGivenFields(info, actual, other, comparatorByPropertyOrField, comparatorByType,
                                                    propertiesOrFieldsUsedInComparison);
    return myself;
  }

  /**
   * Assert that the actual object is equal to the given one by comparing their properties/fields <b>except for the given ones</b>
   * (inherited ones are taken into account). This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the property/field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * If an object has a field and a property with the same name, the property value will be used over the  field.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * The objects to compare can be of different types but the properties/fields used in comparison must exist in both, 
   * for example if actual object has a name String field, it is expected the other object to also have one.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   * 
   * // frodo and sam are equals when ignoring name and age since the only remaining field is race which they share as HOBBIT.
   * assertThat(frodo).isEqualToIgnoringGivenFields(sam, "name", "age"); // OK
   * 
   * // ... but they are not equals if only age is ignored as their names differ.
   * assertThat(frodo).isEqualToIgnoringGivenFields(sam, "age"); // FAIL</code></pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @param propertiesOrFieldsToIgnore ignored properties/fields to ignore in comparison.
   * @throws NullPointerException if the actual or given object is {@code null}.
   * @throws AssertionError if the actual and the given objects are not equals property/field by property/field after ignoring given fields.
   * @throws IntrospectionError if one of actual's property/field to compare can't be found in the other object.
   */
  public S isEqualToIgnoringGivenFields(Object other, String... propertiesOrFieldsToIgnore) {
    objects.assertIsEqualToIgnoringGivenFields(info, actual, other, comparatorByPropertyOrField, comparatorByType,
                                               propertiesOrFieldsToIgnore);
    return myself;
  }

  /**
   * Assert that actual object is equal to the given object based on a property/field by property/field comparison (including
   * inherited ones). This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * <p/>
   * Note that comparison is <b>not</b> recursive, if one of the field is an Object, it will be compared to the other
   * field using its <code>equals</code> method.
   * <p/>
   * If an object has a field and a property with the same name, the property value will be used over the  field.
   * <p/>
   * Private fields are used in comparison but this can be disabled using
   * {@link Assertions#setAllowComparingPrivateFields(boolean)}, if disabled only <b>accessible </b>fields values are
   * compared, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p/>
   * The objects to compare can be of different types but the properties/fields used in comparison must exist in both, 
   * for example if actual object has a name String field, it is expected the other object to also have one.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> // equals not overridden in TolkienCharacter 
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * 
   * // Fail as equals compares object references
   * assertThat(frodo).isEqualsTo(frodoClone);
   * 
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(frodo).isEqualToComparingFieldByField(frodoClone);</code></pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if the actual and the given objects are not equals property/field by property/field.
   * @throws IntrospectionError if one of actual's property/field to compare can't be found in the other object.
   */
  public S isEqualToComparingFieldByField(Object other) {
    objects.assertIsEqualToIgnoringGivenFields(info, actual, other, comparatorByPropertyOrField, comparatorByType);
    return myself;
  }

  /**
   * Allows to define a comparator which is used to compare the values of properties or fields with the given names.
   * A typical usage is for comparing double/float fields with a given precision.
   * <p> 
   * Comparators added by this method have precedence on comparators added by {@link #usingComparatorForType}.
   * <p>
   * Example:
   * <p>
   * <pre><code class='java'> public class TolkienCharacter {
   *   private String name;
   *   private double size;
   *   // constructor omitted
   * }
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   * 
   * Comparator&lt;Double&gt; closeEnough = new Comparator&lt;Double&gt;() {
   *   double precision = 0.5;
   *   public int compare(Double d1, Double d2) {
   *     return Math.abs(d1 -d2) <= precision ? 0 : 1;
   *   }
   * };
   * 
   * // assertions will pass
   * assertThat(frodo).usingComparatorForFields(closeEnough, &quot;size&quot;)
   *                  .isEqualToComparingFieldByField(tallerFrodo);
   *                  
   * assertThat(frodo).usingComparatorForFields(closeEnough, &quot;size&quot;)
   *                  .isEqualToIgnoringNullFields(tallerFrodo);
   *                  
   * assertThat(frodo).usingComparatorForFields(closeEnough, &quot;size&quot;)
   *                  .isEqualToIgnoringGivenFields(tallerFrodo);
   *                  
   * assertThat(frodo).usingComparatorForFields(closeEnough, &quot;size&quot;)
   *                  .isEqualToComparingOnlyGivenFields(tallerFrodo);
   *                  
   * // assertion will fail
   * assertThat(frodo).usingComparatorForFields(closeEnough, &quot;size&quot;)
   *                  .isEqualToComparingFieldByField(reallyTallFrodo);</code></pre>
   * </p>
   * @param comparator the {@link java.util.Comparator} to use
   * @param comparator the names of the properties and/or fields the comparator should be used for
   * @return {@code this} assertions object
   */
  public <T> S usingComparatorForFields(Comparator<T> comparator, String... propertiesOrFields) {
    for (String propertyOrField : propertiesOrFields) {
      comparatorByPropertyOrField.put(propertyOrField, comparator);
    }
    return myself;
  }

  /**
   * Allows to define a comparator which is used to compare the values of properties or fields with the given type.
   * A typical usage is for comparing fields of numeric type at a given precision.
   * <p>
   * Comparators added by {@link #usingComparatorForFields} have precedence on comparators added by this method.
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   private String name;
   *   private double size;
   *   // constructor omitted
   * }
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   * 
   * Comparator&lt;Double&gt; closeEnough = new Comparator&lt;Double&gt;() {
   *   double precision = 0.5;
   *   public int compare(Double d1, Double d2) {
   *     return Math.abs(d1 -d2) <= precision ? 0 : 1;
   *   }
   * };
   * 
   * // assertions will pass
   * assertThat(frodo).usingComparatorForType(closeEnough, Double.class)
   *                  .isEqualToComparingFieldByField(tallerFrodo);
   *                  
   * assertThat(frodo).usingComparatorForType(closeEnough, Double.class)
   *                  .isEqualToIgnoringNullFields(tallerFrodo);
   *                  
   * assertThat(frodo).usingComparatorForType(closeEnough, Double.class)
   *                  .isEqualToIgnoringGivenFields(tallerFrodo);
   *                  
   * assertThat(frodo).usingComparatorForType(closeEnough, Double.class)
   *                  .isEqualToComparingOnlyGivenFields(tallerFrodo);
   *                  
   * // assertion will fail
   * assertThat(frodo).usingComparatorForType(closeEnough, Double.class)
   *                  .isEqualToComparingFieldByField(reallyTallFrodo);</code></pre>
   * </p>
   * @param comparator the {@link java.util.Comparator} to use
   * @param comparator the {@link java.lang.Class} of the type the comparator should be used for
   * @return {@code this} assertions object
   */
  public <T> S usingComparatorForType(Comparator<T> comparator, Class<T> type) {
    comparatorByType.put(type, comparator);
    return myself;
  }

  /**
   * Assert that the actual object has the specified field or property.
   * <p/>
   * Private fields are matched by default but this can be changed by calling {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p/>
   *
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *
   *   private String name;
   *   private int age;
   *   // constructor omitted
   *      
   *   public String getName() {
   *     return this.name;
   *   }
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33);
   * 
   * // assertions will pass :
   * assertThat(frodo).hasFieldOrProperty("name")
   *                  .hasFieldOrProperty("age"); // private field are matched by default
   *         
   * // assertions will fail :
   * assertThat(frodo).hasFieldOrProperty("not_exists");
   * assertThat(frodo).hasFieldOrProperty(null);
   * // disable looking for private fields
   * Assertions.setAllowExtractingPrivateFields(false);
   * assertThat(frodo).hasFieldOrProperty("age"); </code></pre>
   *
   * @param name the field/property name to check 
   * @throws AssertionError if the actual object is {@code null}.
   * @throws IllegalArgumentException if name is {@code null}.
   * @throws AssertionError if the actual object has not the given field/property
   */
  public S hasFieldOrProperty(String name) {
    objects.assertHasFieldOrProperty(info, actual, name);
    return myself;
  }

  /**
   * Assert that the actual object has the specified field or property with the given value.
   * <p/>
   * Private fields are matched by default but this can be changed by calling {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p/>
   *
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   private String name;
   *   private int age;
   *   // constructor omitted
   *
   *   public String getName() {
   *     return this.name;
   *   }
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33);
   * TolkienCharacter noname = new TolkienCharacter(null, 33);
   *
   * // assertions will pass :
   * assertThat(frodo).hasFieldOrProperty("name", "Frodo");
   * assertThat(frodo).hasFieldOrProperty("age", 33);
   * assertThat(noname).hasFieldOrProperty("name", null);
   *
   * // assertions will fail :
   * assertThat(frodo).hasFieldOrProperty("name", "not_equals"); 
   * assertThat(frodo).hasFieldOrProperty(null, 33);             
   * assertThat(frodo).hasFieldOrProperty("age", null);          
   * assertThat(noname).hasFieldOrProperty("name", "Frodo");
   * // disable extracting private fields
   * Assertions.setAllowExtractingPrivateFields(false);
   * assertThat(frodo).hasFieldOrProperty("age", 33); </code></pre>
   *
   * @param name the field/property name to check 
   * @param value the field/property expected value 
   * @throws AssertionError if the actual object is {@code null}.
   * @throws IllegalArgumentException if name is {@code null}.
   * @throws AssertionError if the actual object has not the given field/property
   * @throws AssertionError if the actual object has the given field/property but not with the expected value
   *
   * @see AbstractObjectAssert#hasFieldOrProperty(java.lang.String)
   */
  public S hasFieldOrPropertyWithValue(String name, Object value) {
    objects.assertHasFieldOrPropertyWithValue(info, actual, name, value);
    return myself;
  }

  /**
   * Extract the values of given fields/properties from the object under test into an array, this new array becoming
   * the object under test. 
   * <p/>
   * If you extract "id", "name" and "email" fields/properties then the array will contain the id, name and email values 
   * of the object under test, you can then perform array assertions on the extracted values.
   * <p/>
   * Nested fields/properties are supported, specifying "adress.street.number" is equivalent to get the value 
   * corresponding to actual.getAdress().getStreet().getNumber()  
   * <p/>
   * Private fields can be extracted unless you call {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p/>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race fields (Race having a name field)
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   * 
   * // let's verify Frodo's name, age and race name:
   * assertThat(frodo).extracting(&quot;name&quot;, &quot;age&quot;, &quot;race.name&quot;)
   *                  .containsExactly(&quot;Frodo&quot;, 33, "Hobbit");</code></pre>
   * 
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field is not accessible (i.e. does not exist) an IntrospectionError is thrown.
   * <p/>
   * Note that the order of extracted property/field values is consistent with the iteration order of the array under
   * test.
   *
   * @param propertiesOrFields the properties/fields to extract from the initial array under test
   * @return a new assertion object whose object under test is the array containing the extracted properties/fields values
   * @throws IntrospectionError if one of the given name does not match a field or property
   */
  public AbstractObjectArrayAssert<?, Object> extracting(String... propertiesOrFields) {
    Tuple values = byName(propertiesOrFields).extract(actual);
    return new ObjectArrayAssert<Object>(values.toArray());
  }

  /**
   * Assert that actual object is equal to the given object based on recursive a property/field by property/field comparison (including
   * inherited ones). This can be handy if <code>equals</code> implementation of objects to compare does not suit you.
   * <p/>
   * Note that the recursive property/field comparison is stopped for fields with a custom <code>equals</code> implementation.
   * <p/>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p/>
   * The objects to compare can be of different types but must have the same properties/fields.
   * For example if actual object has a name String field, it is expected the other object to also have one.
   * <p/>
   * 
   * Example:
   * <pre><code class='java'> public class Person {
   *   public String name;
   *   public Home home = new Home();
   * }
   *
   * public class Home {
   *   public Address address = new Address();
   * }
   *
   * public static class Address {
   *   public int number = 1;
   * }
   * 
   * Person jack = new Person();
   * jack.name = "Jack";
   * jack.home.address.number = 1;
   * 
   * Person jackClone = new Person();
   * jackClone.name = "Jack";
   * jackClone.home.address.number = 1;
   * 
   * // will fail as equals compares object references
   * assertThat(jack).isEqualsTo(jackClone);
   * 
   * // jack and jackClone are equals when doing a recursive field by field comparison
   * assertThat(jack).isEqualToComparingFieldByFieldRecursively(jackClone);</code></pre>
   * 
   * @param other the object to compare {@code actual} to.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if the actual and the given objects are not deeply equal property/field by property/field.
   * @throws IntrospectionError if one property/field to compare can not be found.
   */
  public S isEqualToComparingFieldByFieldRecursively(Object other) {
    objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other);
    return myself;
  }
}
