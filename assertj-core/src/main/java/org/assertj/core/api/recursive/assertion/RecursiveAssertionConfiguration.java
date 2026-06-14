/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.recursive.assertion;

import static java.lang.String.format;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_VALUES_ONLY;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_VALUE_ONLY;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import org.assertj.core.api.RecursiveAssertionAssert;
import org.assertj.core.api.recursive.AbstractRecursiveOperationConfiguration;

// Add support for comparedFields

/**
 * Configuration for recursive assertions.
 *
 * @since 3.24.0
 */
public class RecursiveAssertionConfiguration extends AbstractRecursiveOperationConfiguration {

  private boolean ignorePrimitiveFields;
  private final boolean skipJavaLibraryTypeObjects;
  private IterableAssertionPolicy iterableAssertionPolicy;
  private MapAssertionPolicy mapAssertionPolicy;
  private OptionalAssertionPolicy optionalAssertionPolicy;
  private boolean ignoreAllNullFields;
  private RecursiveAssertionIntrospectionStrategy introspectionStrategy;

  private RecursiveAssertionConfiguration(Builder builder) {
    super(builder);
    this.ignorePrimitiveFields = builder.ignorePrimitiveFields;
    this.skipJavaLibraryTypeObjects = builder.skipJavaLibraryTypeObjects;
    this.mapAssertionPolicy = builder.mapAssertionPolicy;
    this.iterableAssertionPolicy = builder.iterableAssertionPolicy;
    this.optionalAssertionPolicy = builder.optionalAssertionPolicy;
    this.ignoreAllNullFields = builder.ignoreAllNullFields;
    this.introspectionStrategy = builder.introspectionStrategy;
  }

  /**
   * Choose between running the {@link Predicate} in use over the primitive fields of an object in an object tree or not,
   * by default asserting over primitives is <em>enabled</em>.
   * <p>
   * For example, consider the following class:
   * <pre><code class='java'> class Example {
   *    public int primitiveField;
   *    public String objectField;
   *  } </code></pre>
   * <p>
   * By default, the assertion being applied recursively is applied to <code>primitiveField</code> and to
   * <code>objectField</code>. If ignoring primitives it set to true, the assertion will only be applied to <code>objectField</code>.
   * <p>
   * If you elect to assert over primitives then it is your own responsibility as a developer to ensure that your
   * {@link Predicate} can handle (boxed) primitive arguments.</p>
   */
  public void ignorePrimitiveFields(boolean ignorePrimitiveFields) {
    this.ignorePrimitiveFields = ignorePrimitiveFields;
  }

  /**
   * Makes the recursive assertion to ignore all null fields.
   * <p>
   * Example: see {@link RecursiveAssertionAssert#ignoringAllNullFields()}
   *
   * @param ignoreAllNullFields whether to ignore empty optional fields in the recursive comparison
   */
  public void ignoreAllNullFields(boolean ignoreAllNullFields) {
    this.ignoreAllNullFields = ignoreAllNullFields;
  }

  /**
   * Makes the recursive assertion to ignore the specified fields in the object under test.
   * <p>
   * When a field is ignored, all its fields are ignored too.
   * <p>
   * Example: see {@link RecursiveAssertionAssert#ignoringFields(String...)}
   *
   * @param fieldsToIgnore the fields to ignore in the object under test.
   */
  @Override
  public void ignoreFields(String... fieldsToIgnore) {
    super.ignoreFields(fieldsToIgnore);
  }

  /**
   * Makes the recursive assertion to ignore the fields matching the specified regexes in the object under test.
   * <p>
   * When a field is ignored, all its fields are ignored too.
   * <p>
   * Example: see {@link RecursiveAssertionAssert#ignoringFieldsMatchingRegexes(String...)}
   *
   * @param regexes regexes used to ignore fields in the assertion.
   */
  @Override
  public void ignoreFieldsMatchingRegexes(String... regexes) {
    super.ignoreFieldsMatchingRegexes(regexes);
  }

  /**
   * Makes the recursive assertion to ignore the object under test fields of the given types.
   * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example if a field is a subtype of an ignored type it is not ignored.
   * <p>
   * If some object under test fields are null it is not possible to evaluate their types and thus these fields are not ignored.
   * <p>
   * When a field is ignored, all its fields are ignored too.
   * <p>
   * Example: see {@link RecursiveAssertionAssert#ignoringFieldsOfTypes(Class[])}
   *
   * @param types the types we want to ignore in the object under test fields.
   */
  @Override
  public void ignoreFieldsOfTypes(Class<?>... types) {
    super.ignoreFieldsOfTypes(types);
  }

  /**
   * Makes the recursive assertion to use the specified {@link OptionalAssertionPolicy}.
   *
   * @param optionalAssertionPolicy the {@link OptionalAssertionPolicy} to use.
   */
  public void setOptionalAssertionPolicy(OptionalAssertionPolicy optionalAssertionPolicy) {
    this.optionalAssertionPolicy = optionalAssertionPolicy;
  }

  /**
   * Makes the recursive assertion to use the specified {@link MapAssertionPolicy}.
   *
   * @param mapAssertionPolicy the {@link MapAssertionPolicy} to use.
   */
  public void setMapAssertionPolicy(MapAssertionPolicy mapAssertionPolicy) {
    this.mapAssertionPolicy = mapAssertionPolicy;
  }

  /**
   * Makes the recursive assertion to use the specified {@link IterableAssertionPolicy}.
   *
   * @param iterableAssertionPolicy the {@link IterableAssertionPolicy} to use.
   */
  public void setIterableAssertionPolicy(IterableAssertionPolicy iterableAssertionPolicy) {
    this.iterableAssertionPolicy = iterableAssertionPolicy;
  }

  /**
   * Makes the recursive assertion to use the specified {@link CollectionAssertionPolicy}.
   *
   * @param collectionAssertionPolicy the {@link CollectionAssertionPolicy} to use.
   * @deprecated use {@link #setIterableAssertionPolicy(IterableAssertionPolicy)} instead.
   */
  public void setCollectionAssertionPolicy(CollectionAssertionPolicy collectionAssertionPolicy) {
    switch (collectionAssertionPolicy) {
    case ELEMENTS_ONLY:
      iterableAssertionPolicy = IterableAssertionPolicy.ELEMENTS_ONLY;
      break;
    case COLLECTION_OBJECT_ONLY:
      iterableAssertionPolicy = IterableAssertionPolicy.ITERABLE_OBJECT_ONLY;
      break;
    case COLLECTION_OBJECT_AND_ELEMENTS:
      iterableAssertionPolicy = IterableAssertionPolicy.ITERABLE_OBJECT_AND_ELEMENTS;
      break;
    }
  }

  /**
   * Defines how objects are introspected in the recursive assertion.
   * <p>
   * Default to {@link DefaultRecursiveAssertionIntrospectionStrategy} that introspects all fields (including inherited ones).
   *
   * @param introspectionStrategy the {@link RecursiveAssertionIntrospectionStrategy} to use
   */
  public void setIntrospectionStrategy(RecursiveAssertionIntrospectionStrategy introspectionStrategy) {
    this.introspectionStrategy = introspectionStrategy;
  }

  @Override
  public String toString() {
    CONFIGURATION_PROVIDER.representation();
    StringBuilder description = new StringBuilder();
    describeIgnoreAllNullFields(description);
    describeIgnoredFields(description);
    describeIgnoredFieldsRegexes(description);
    describeIgnoredFieldsOfTypes(description);
    describeIgnorePrimitiveFields(description);
    describeSkipJCLTypeObjects(description);
    describeIterableAssertionPolicy(description);
    describeMapAssertionPolicy(description);
    describeOptionalAssertionPolicy(description);
    describeIntrospectionStrategy(description);
    return description.toString();
  }

  boolean shouldIgnorePrimitiveFields() {
    return ignorePrimitiveFields;
  }

  boolean shouldSkipJavaLibraryTypeObjects() {
    return skipJavaLibraryTypeObjects;
  }

  CollectionAssertionPolicy getCollectionAssertionPolicy() {
    switch (iterableAssertionPolicy) {
    case ELEMENTS_ONLY:
      return CollectionAssertionPolicy.ELEMENTS_ONLY;
    case ITERABLE_OBJECT_ONLY:
      return CollectionAssertionPolicy.COLLECTION_OBJECT_ONLY;
    case ITERABLE_OBJECT_AND_ELEMENTS:
      return CollectionAssertionPolicy.COLLECTION_OBJECT_AND_ELEMENTS;
    }
    return CollectionAssertionPolicy.ELEMENTS_ONLY;
  }

  IterableAssertionPolicy getIterableAssertionPolicy() {
    return iterableAssertionPolicy;
  }

  private static IterableAssertionPolicy toIterableAssertionPolicy(CollectionAssertionPolicy policy) {
    switch (policy) {
    case ELEMENTS_ONLY:
      return IterableAssertionPolicy.ELEMENTS_ONLY;
    case COLLECTION_OBJECT_ONLY:
      return IterableAssertionPolicy.ITERABLE_OBJECT_ONLY;
    case COLLECTION_OBJECT_AND_ELEMENTS:
      return IterableAssertionPolicy.ITERABLE_OBJECT_AND_ELEMENTS;
    }
    return IterableAssertionPolicy.ELEMENTS_ONLY;
  }

  MapAssertionPolicy getMapAssertionPolicy() {
    return mapAssertionPolicy;
  }

  OptionalAssertionPolicy getOptionalAssertionPolicy() {
    return optionalAssertionPolicy;
  }

  RecursiveAssertionIntrospectionStrategy getIntrospectionStrategy() {
    return introspectionStrategy;
  }

  boolean shouldIgnoreMap() {
    return mapAssertionPolicy == MAP_VALUES_ONLY;
  }

  boolean shouldIgnoreOptional() {
    return optionalAssertionPolicy == OPTIONAL_VALUE_ONLY;
  }

  boolean shouldIgnoreContainer() {
    return iterableAssertionPolicy == IterableAssertionPolicy.ELEMENTS_ONLY;
  }

  boolean shouldIgnoreAllNullFields() {
    return this.ignoreAllNullFields;
  }

  private void describeIgnoreAllNullFields(StringBuilder description) {
    if (shouldIgnoreAllNullFields()) description.append(format("- all null fields were ignored in the assertion%n"));
  }

  private void describeIgnorePrimitiveFields(StringBuilder description) {
    if (shouldIgnorePrimitiveFields())
      description.append(format("- primitive fields were ignored in the recursive assertion%n"));
  }

  private void describeSkipJCLTypeObjects(StringBuilder description) {
    if (!shouldSkipJavaLibraryTypeObjects())
      description.append(format("- fields from Java Class Library types (java.* or javax.*) were included in the recursive assertion%n"));
    else
      description.append(format("- fields from Java Class Library types (java.* or javax.*) were excluded in the recursive assertion%n"));
  }

  private void describeIterableAssertionPolicy(StringBuilder description) {
    description.append(format("- the iterable/collection assertion policy was %s%n", getIterableAssertionPolicy().name()));
  }

  private void describeMapAssertionPolicy(StringBuilder description) {
    description.append(format("- the map assertion policy was %s%n", getMapAssertionPolicy().name()));
  }

  private void describeOptionalAssertionPolicy(StringBuilder description) {
    description.append(format("- the optional assertion policy was %s%n", getOptionalAssertionPolicy().name()));
  }

  private void describeIgnoredFieldsOfTypes(StringBuilder description) {
    if (!getIgnoredTypes().isEmpty())
      description.append(format("- the following types were ignored in the assertion: %s%n", describeIgnoredTypes()));
  }

  private void describeIntrospectionStrategy(StringBuilder description) {
    description.append(format("- the introspection strategy used was: %s%n", introspectionStrategy.getDescription()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RecursiveAssertionConfiguration that = (RecursiveAssertionConfiguration) o;
    return shouldIgnoreAllNullFields() == that.shouldIgnoreAllNullFields()
           && java.util.Objects.equals(getIgnoredFields(), that.getIgnoredFields())
           && java.util.Objects.equals(getIgnoredFieldsRegexes(), that.getIgnoredFieldsRegexes())
           && shouldIgnorePrimitiveFields() == that.shouldIgnorePrimitiveFields()
           && shouldSkipJavaLibraryTypeObjects() == that.shouldSkipJavaLibraryTypeObjects()
           && getCollectionAssertionPolicy() == that.getCollectionAssertionPolicy()
           && getIterableAssertionPolicy() == that.getIterableAssertionPolicy()
           && getOptionalAssertionPolicy() == that.getOptionalAssertionPolicy()
           && getMapAssertionPolicy() == that.getMapAssertionPolicy();
  }

  @Override
  public int hashCode() {
    return Objects.hash(shouldIgnoreAllNullFields(), getIgnoredFields(), getIgnoredFieldsRegexes(), getIgnoredTypes(),
                        shouldIgnorePrimitiveFields(), shouldSkipJavaLibraryTypeObjects(), getCollectionAssertionPolicy(),
                        getIterableAssertionPolicy(), getOptionalAssertionPolicy(), getMapAssertionPolicy());
  }

  /**
   * @return A {@link Builder} that will assist the developer in creating a valid instance of {@link RecursiveAssertionConfiguration}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder for {@link RecursiveAssertionConfiguration}
   *
   * @since 3.24.0
   */
  public static class Builder extends AbstractBuilder<Builder> {
    private boolean ignorePrimitiveFields = false;
    private boolean skipJavaLibraryTypeObjects = true;
    private IterableAssertionPolicy iterableAssertionPolicy = IterableAssertionPolicy.ELEMENTS_ONLY;
    private MapAssertionPolicy mapAssertionPolicy = MAP_VALUES_ONLY;
    private OptionalAssertionPolicy optionalAssertionPolicy = OPTIONAL_VALUE_ONLY;
    private boolean ignoreAllNullFields;
    private RecursiveAssertionIntrospectionStrategy introspectionStrategy = new DefaultRecursiveAssertionIntrospectionStrategy();

    private Builder() {
      super(Builder.class);
    }

    /**
     * Makes the recursive assertion to ignore the specified fields in the object under test.
     * <p>
     * Example:
     * <pre><code class='java'> class Person {
     *   String name;
     *   String occupation;
     *   int age;
     *   Address address = new Address();
     * }
     *
     * class Address {
     *   int number;
     *   String street;
     * }
     *
     * Person sherlock = new Person("Sherlock", "Detective", 60);
     * sherlock.address.street = "Baker Street";
     * sherlock.address.number = 221;
     *
     * RecursiveAssertionConfiguration config = RecursiveAssertionConfiguration.builder()
     *                                                                         .withIgnoredFields("address", "age")
     *                                                                         .build();
     *
     * // assertion succeeds Person has only String fields except for address and age
     * assertThat(sherlock).usingRecursiveAssertion(config)
     *                     .allFieldsSatisfy(field -> field instanceof String);
     *
     * // assertion fails because of age, address and address.number fields
     * assertThat(sherlock).usingRecursiveComparison()
     *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
     *
     * @param fieldsToIgnore the fields to ignore in the object under test.
     * @return this builder.
     */
    @Override
    public Builder withIgnoredFields(String... fieldsToIgnore) {
      return super.withIgnoredFields(fieldsToIgnore);
    }

    /**
     * Makes the recursive assertion to ignore the fields matching the specified regexes in the object under test.
     * <p>
     * Example:
     * <pre><code class='java'> class Person {
     *   String name;
     *   String occupation;
     *   int age;
     *   Address address = new Address();
     * }
     *
     * class Address {
     *   int number;
     *   String street;
     * }
     *
     * Person sherlock = new Person("Sherlock", "Detective", 60);
     * sherlock.address.street = "Baker Street";
     * sherlock.address.number = 221;
     *
     * RecursiveAssertionConfiguration config = RecursiveAssertionConfiguration.builder()
     *                                                                         .withIgnoredFieldsMatchingRegexes("ad.*", "ag.")
     *                                                                         .build();
     *
     * // assertion succeeds Person has only String fields except for address and age
     * assertThat(sherlock).usingRecursiveAssertion(config)
     *                     .allFieldsSatisfy(field -> field instanceof String);
     *
     * // assertion fails because of age, address and address.number fields as by default no fields are ignored
     * assertThat(sherlock).usingRecursiveComparison()
     *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
     *
     * @param regexes regexes used to ignore fields in the assertion.
     * @return this builder.
     */
    @Override
    public Builder withIgnoredFieldsMatchingRegexes(String... regexes) {
      return super.withIgnoredFieldsMatchingRegexes(regexes);
    }

    /**
     * Makes the recursive assertion to ignore all null fields.
     * <p>
     * Example:
     * <pre><code class='java'> class Person {
     *   String name;
     *   String occupation;
     *   Address address;
     * }
     *
     * class Address {
     *   int number;
     *   String street;
     * }
     *
     * Person sherlock = new Person("Sherlock", "Detective");
     * sherlock.address = null;
     *
     * RecursiveAssertionConfiguration config = RecursiveAssertionConfiguration.builder()
     *                                                                         .withIgnoreAllNullFields(true)
     *                                                                         .build();
     *
     * // assertion succeeds as name and home.address.street fields are ignored in the comparison
     * assertThat(noName).usingRecursiveAssertion(config)
     *                  .allFieldsSatisfy(field -> field instanceof String);
     *
     * // assertion fails as name and home.address.street fields are populated for sherlock but not for noName.
     * assertThat(sherlock).usingRecursiveComparison()
     *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
     *
     * @param ignoreAllNullFields whether to ignore empty optional fields in the recursive comparison
     * @return This builder.
     */
    public Builder withIgnoreAllNullFields(boolean ignoreAllNullFields) {
      this.ignoreAllNullFields = ignoreAllNullFields;
      return this;
    }

    /**
     * Choose between running the {@link Predicate} in use over the primitive fields of an object in an object tree or not,
     * by default asserting over primitives is <em>enabled</em>.
     * <p>
     * For example, consider the following class:
     * <pre><code class='java'> class Example {
     *    public int primitiveField;
     *    public String objectField;
     *  } </code></pre>
     * <p>
     * By default, the assertion being applied recursively is applied to <code>primitiveField</code> and to
     * <code>objectField</code>. If ignoring primitives it set to true, the assertion will only be applied to <code>objectField</code>.
     * <p>
     * If you elect to assert over primitives then it is your own responsibility as a developer to ensure that your
     * {@link Predicate} can handle (boxed) primitive arguments.</p>
     *
     * @param ignorePrimitiveFields <code>true</code> to avoid asserting over primitives, <code>false</code> to enable.
     * @return This builder.
     */
    public Builder withIgnorePrimitiveFields(final boolean ignorePrimitiveFields) {
      this.ignorePrimitiveFields = ignorePrimitiveFields;
      return this;
    }

    /**
     * <p>Choose whether or not, while recursively applying a {@link Predicate} to an object tree, the recursion will dive into
     * types defined in the Java Class Library. That is to say, whether or not to recurse into objects whose classes are
     * declared in a package starting with java.* or javax.* .</p>
     * <p>Consider the following example:</p>
     * <pre><code style='java'> class Example {
     *   String s = "Don't look at me!";
     * }
     *
     * assertThat(new Example()).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myPredicate(o)); </code></pre>
     *
     * <p>With no recursion into Java Class Library types, <code>myPredicate()</code> is applied to the field <code>s</code>
     * but not to the internal fields of {@link String}. With recursion into Java standard types active, the internal
     * fields of String will be examined as well.</p>
     * <p>By default, recursion into Java Class Library types is <em>disabled</em>.
     *
     * @param recursionIntoJavaClassLibraryTypes <code>true</code> to enable recursion into Java Class Library types, <code>false</code> to disable it. Defaults to <code>false</code>.
     * @return This builder.
     */
    public Builder withRecursionIntoJavaClassLibraryTypes(final boolean recursionIntoJavaClassLibraryTypes) {
      this.skipJavaLibraryTypeObjects = !recursionIntoJavaClassLibraryTypes;
      return this;
    }

    /**
     * Makes the recursive assertion to ignore the object under test fields of the given types.
     * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example if a field is a subtype of an ignored type it is not ignored.
     * <p>
     * If some object under test fields are null it is not possible to evaluate their types and thus these fields are not ignored.
     * <p>
     * Example:
     * <pre><code class='java'> class Person {
     *   String name;
     *   String occupation;
     *   Address address = new Address();
     * }
     *
     * class Address {
     *   int number;
     *   String street;
     * }
     *
     * Person sherlock = new Person("Sherlock", "Detective");
     * sherlock.address.street = "Baker Street";
     * sherlock.address.number = 221;
     *
     * RecursiveAssertionConfiguration config = RecursiveAssertionConfiguration.builder()
     *                                                                         .withIgnoredFieldsOfTypes(Address.class)
     *                                                                         .build();
     *
     * // assertion succeeds Person has only String fields except for address
     * assertThat(sherlock).usingRecursiveAssertion(config)
     *                     .allFieldsSatisfy(field -> field instanceof String);
     *
     * // assertion fails because of address and address.number fields as the default config does not ignore any types.
     * assertThat(sherlock).usingRecursiveComparison()
     *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
     *
     * @param types the types we want to ignore in the object under test fields.
     * @return This builder.
     */
    @Override
    public Builder withIgnoredFieldsOfTypes(Class<?>... types) {
      return super.withIgnoredFieldsOfTypes(types);
    }

    /**
     * <p>Selects the {@link IterableAssertionPolicy} to use for recursive application of a {@link Predicate} to an object tree.
     * If not set, defaults to {@link IterableAssertionPolicy#ELEMENTS_ONLY}.</p>
     * <p>Note that for the purposes of recursive asserting, an array counts as an iterable, so this policy is applied to
     * arrays as well as children of {@link Iterable}.
     *
     * @param policy The policy to use for iterables and arrays in recursive asserting.
     * @return This builder.
     */
    public Builder withIterableAssertionPolicy(IterableAssertionPolicy policy) {
      iterableAssertionPolicy = policy;
      return this;
    }

    /**
     * <p>Selects the {@link CollectionAssertionPolicy} to use for recursive application of a {@link Predicate} to an object tree.
     * If not set, defaults to {@link CollectionAssertionPolicy#COLLECTION_OBJECT_AND_ELEMENTS}.</p>
     * <p>Note that for the purposes of recursive asserting, an array counts as a collection, so this policy is applied to
     * arrays as well as children of {@link Collection}.
     *
     * @param policy The policy to use for collections and arrays in recursive asserting.
     * @return This builder.
     * @deprecated use {@link #withIterableAssertionPolicy(IterableAssertionPolicy)} instead.
     */
    public Builder withCollectionAssertionPolicy(CollectionAssertionPolicy policy) {
      iterableAssertionPolicy = toIterableAssertionPolicy(policy);
      return this;
    }

    /**
     * <p>Selects the {@link MapAssertionPolicy} to use for recursive application of a {@link Predicate} to an object tree.
     * If not set, defaults to {@link MapAssertionPolicy#MAP_OBJECT_AND_ENTRIES}.</p>
     *
     * @param policy The policy to use for maps in recursive asserting.
     * @return This builder.
     */
    public Builder withMapAssertionPolicy(MapAssertionPolicy policy) {
      mapAssertionPolicy = policy;
      return this;
    }

    /**
     * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy}.
     * <p>
     * See {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy} for the different possible policies, by default
     * {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy#OPTIONAL_VALUE_ONLY} is used.
     *
     * @param policy the {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy} to use.
     * @return This builder.
     */
    public Builder withOptionalAssertionPolicy(RecursiveAssertionConfiguration.OptionalAssertionPolicy policy) {
      optionalAssertionPolicy = policy;
      return this;
    }

    /**
     * Defines how objects are introspected in the recursive assertion.
     * <p>
     * Default to {@link DefaultRecursiveAssertionIntrospectionStrategy} that introspects all fields (including inherited ones).
     *
     * @param introspectionStrategy the {@link RecursiveAssertionIntrospectionStrategy} to use
     * @return This builder.
     */
    public Builder withIntrospectionStrategy(RecursiveAssertionIntrospectionStrategy introspectionStrategy) {
      this.introspectionStrategy = introspectionStrategy;
      return this;
    }

    public RecursiveAssertionConfiguration build() {
      return new RecursiveAssertionConfiguration(this);
    }
  }

  /**
   * Possible policies to use regarding iterables (including arrays) when recursively asserting over the fields of an object tree.
   *
   * @author bzt
   */
  public enum IterableAssertionPolicy {
    /**
     * Apply the {@link Predicate} (recursively) to the elements of the iterable/array but not the iterable/array itself.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new ArrayList&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the two strings {@code "Hello"} and {@code "Salut"} but
     * not to the {@code greetings} field.
     */
    ELEMENTS_ONLY,

    /**
     * Apply the {@link Predicate} to the iterable/array only but not to its elements.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new ArrayList&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} ArrayList field
     * but not to the two strings {@code "Hello"} and {@code "Salut"}.
     */
    ITERABLE_OBJECT_ONLY,

    /**
     * Apply the {@link Predicate} to the iterable/array as well as to (recursively) its elements.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new ArrayList&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} ArrayList field and
     * to the two strings {@code "Hello"} and {@code "Salut"}.
     */
    ITERABLE_OBJECT_AND_ELEMENTS
  }

  /**
   * Possible policies to use regarding collections (including arrays) when recursively asserting over the fields of an object tree.
   *
   * @author bzt
   * @deprecated use {@link IterableAssertionPolicy} instead.
   */
  public enum CollectionAssertionPolicy {
    /**
     * Apply the {@link Predicate} (recursively) to the elements of the collection/array but not the collection/array itself.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new ArrayList&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the two strings {@code "Hello"} and {@code "Salut"} but
     * not to the {@code greetings} field.
     */
    ELEMENTS_ONLY,

    /**
     * Apply the {@link Predicate} to the collection/array only but not to its elements.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new ArrayList&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} ArrayList field
     * but not to the two strings {@code "Hello"} and {@code "Salut"}.
     */
    COLLECTION_OBJECT_ONLY,

    /**
     * Apply the {@link Predicate} to the collection/array as well as to (recursively) its elements.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new ArrayList&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} ArrayList field and
     * to the two strings {@code "Hello"} and {@code "Salut"}.
     */
    COLLECTION_OBJECT_AND_ELEMENTS
  }

  /**
   * Possible policies to use regarding maps when recursively asserting over the fields of an object tree.
   *
   * @author bzt
   */
  public enum MapAssertionPolicy {
    /**
     * Apply the {@link Predicate} to the map but not to its entries.
     * <p>
     * Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   Map&lt;String, String&gt; greetings = new HashMap&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.put("english", "Hi");
     * parent.greetings.put("french", "Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field)); </code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} field
     * but not to the objects contained in the {@code greetings} map entries.
     */
    MAP_OBJECT_ONLY,

    /**
     * Apply the {@link Predicate} (recursively) to the map values but not to the map itself or its keys.
     * <p>
     * <pre><code style='java'> class Parent {
     *   Map&lt;String, String&gt; greetings = new HashMap&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.put("english", "Hi");
     * parent.greetings.put("french", "Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field)); </code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} map values {@code "Hi"}
     * and {@code "Salut"} but not to the {@code greetings} field itself or its keys.
     */
    MAP_VALUES_ONLY,

    /**
     * Apply the {@link Predicate} to the map as well as (recursively) to its keys and values.
     * <p>
     * Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   Map&lt;String, String&gt; greetings = new HashMap&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.put("english", "Hi");
     * parent.greetings.put("french", "Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field)); </code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greetings} field and also to
     * the keys and values of the {@code greetings} map: {@code "english", "Hi", "french"} and {@code "Salut"}.
     */
    MAP_OBJECT_AND_ENTRIES
  }

  /**
   * Possible policies to use regarding optionals when recursively asserting over the fields of an object tree.
   */
  public enum OptionalAssertionPolicy {
    /**
     * Apply the {@link Predicate} (recursively) to the value of the optional field but not the optional field.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   Optional&lt;String&gt; greeting = Optional.of("Hi");
     * }
     *
     * Parent parent = new Parent();
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the optional value "Hi" but not to the {@code greeting} Optional field.
     */
    OPTIONAL_VALUE_ONLY,

    /**
     * Apply the {@link Predicate} to the optional field but not to its value.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   Optional&lt;String&gt; greeting = Optional.of("Hi");
     * }
     *
     * Parent parent = new Parent();
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greeting} Optional field but not to the optional value "Hi".
     */
    OPTIONAL_OBJECT_ONLY,

    /**
     * Apply the {@link Predicate} to the optional field as well as to (recursively) its value.
     * <p>
     * Consider the following example:
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; greetings = new Optional&lt;&gt;();
     * }
     *
     * Parent parent = new Parent();
     * parent.greetings.add("Hello");
     * parent.greetings.add("Salut");
     *
     * assertThat(parent).usingRecursiveAssertion()
     *                   .allFieldsSatisfy(field -> myPredicate(field));</code></pre>
     * <p>
     * With this policy, <code>myPredicate(field)</code> is applied to the {@code greeting} Optional field and its value "Hi".
     */
    OPTIONAL_OBJECT_AND_VALUE
  }
}
