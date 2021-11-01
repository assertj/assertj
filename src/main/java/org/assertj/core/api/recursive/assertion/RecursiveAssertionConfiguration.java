/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.assertion;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.assertj.core.api.recursive.AbstractRecursiveOperationConfiguration;
import org.assertj.core.util.VisibleForTesting;

public class RecursiveAssertionConfiguration extends AbstractRecursiveOperationConfiguration {

  private final boolean assertOverPrimitiveFields;
  private final boolean skipJavaLibraryTypeObjects;
  private final CollectionAssertionPolicy collectionAssertionPolicy;
  private final MapAssertionPolicy mapAssertionPolicy;

  private RecursiveAssertionConfiguration(Builder builder) {
    super(builder);
    this.assertOverPrimitiveFields = builder.assertOverPrimitiveFields;
    this.skipJavaLibraryTypeObjects = builder.noJavaClassLibraryRecursion;
    this.collectionAssertionPolicy = builder.collectionAssertionPolicy;
    this.mapAssertionPolicy = builder.mapAssertionPolicy;
  }

  @VisibleForTesting
  boolean getAssertOverPrimitiveFields() {
    return assertOverPrimitiveFields;
  }
  
  @VisibleForTesting
  boolean isSkipJavaLibraryTypeObjects() {
    return skipJavaLibraryTypeObjects;
  }

  @VisibleForTesting
  CollectionAssertionPolicy getCollectionAssertionPolicy() {
    return collectionAssertionPolicy;
  }
  
  @VisibleForTesting
  MapAssertionPolicy getMapAssertionPolicy() {
    return mapAssertionPolicy;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends AbstractBuilder<Builder> {
    private boolean assertOverPrimitiveFields = true;
    private boolean noJavaClassLibraryRecursion = true;
    private CollectionAssertionPolicy collectionAssertionPolicy = CollectionAssertionPolicy.COLLECTION_OBJECT_AND_ELEMENTS;
    private MapAssertionPolicy mapAssertionPolicy = MapAssertionPolicy.MAP_OBJECT_AND_ENTRIES;

    private Builder() {
      super(Builder.class);
    }

    /**
     * <p>Choose between running the {@link Predicate} in use over the primitive fields of an object in an object tree or not.</p>
     * <p>For example, consider the following class:</p>
     * <pre><code class='java'> class Example {
     *    public int primitiveField;
     *    public String objectField;
     *  } </code>
     * </pre>
     * <p>With asserting over primitives set to true, the assertion being applied recursively will be applied to 
     * <code>primitiveField</code> and to <code>objectField</code>. If asserting over primitives it set to false, 
     * the assertion will only be applied to <code>objectField</code>. Of course, if you elect to assert over 
     * primitives then it is your own responsibility as a developer to ensure that your {@link Predicate} can handle 
     * (boxed) primitive arguments.</p>
     * <p>By default, asserting over primitives is <em>enabled</em>.
     *      
     * @param yesOrNo <code>true</code> to enable asserting over primitives, <code>false</code> to disable; defaults to <code>true</code>.
     * @return This builder.
     * @since 3.22.0
     */
    public Builder withAssertionOverPrimitiveFields(final boolean yesOrNo) {
      assertOverPrimitiveFields = yesOrNo;
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
     * assertThat(new Example()).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myPredicate(o)); </code>
     * </pre>
     * <p>With no recursion into Java Class Library types, <code>myPredicate()</code> will be applied to the field <code>s</code> 
     * but not to the internal fields of {@link String}. With recursion into Java standard types active, the internal 
     * fields of String will be examined as well.</p>
     * <p>By default, recursion into Java Class Library types is <em>disabled</em>. 
     * @param yesOrNo <code>true</code> to enable recursion into Java Class Library types, <code>false</code> to disable it. Defaults to <code>false</code>.  
     * @return This builder.
     * @since 3.22.0
     */
    public Builder withRecursionIntoJavaClassLibraryTypes(final boolean yesOrNo) {
      noJavaClassLibraryRecursion = !yesOrNo;
      return this;
    }
    
    /**
     * <p>Selects the {@link CollectionAssertionPolicy} to use for recursive application of a {@link Predicate} to an object tree. 
     * If not set, defaults to {@link CollectionAssertionPolicy#COLLECTION_OBJECT_AND_ELEMENTS}.</p>
     * <p>Note that for the purposes of recursive asserting, an array counts as a collection, so this policy will be applied to 
     * arrays as well as children of {@link Collection}.
     * @param policy The policy to use for collections and arrays in recursive asserting.
     * @return This builder.
     * @since 3.22.0
     */
    public Builder withCollectionAssertionPolicy(CollectionAssertionPolicy policy) {
      collectionAssertionPolicy = policy;
      return this;
    }

    /**
     * <p>Selects the {@link MapAssertionPolicy} to use for recursive application of a {@link Predicate} to an object tree. 
     * If not set, defaults to {@link MapAssertionPolicy#MAP_OBJECT_AND_ENTRIES}.</p>
     * @param policy The policy to use for maps in recursive asserting.
     * @return This builder.
     * @since 3.22.0
     */
    public Builder withMapAssertionPolicy(MapAssertionPolicy policy) {
      mapAssertionPolicy = policy;
      return this;
    }

    public RecursiveAssertionConfiguration build() {
      return new RecursiveAssertionConfiguration(this);
    }
  }
  
  /**
   * Possible policies to use regarding collections (including arrays) when recursively asserting over the fields of an object tree.
   * @author bzt
   *
   */
  public static enum CollectionAssertionPolicy {
    
    /**
     * <p>Apply the {@link Predicate} (recursively) to the elements of the collection object only. Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; myList = new ArrayList<>();
     * }
     * ...
     * 
     * Parent p = new Parent();
     * p.myList.add("Hello World!");
     * p.myList.add("Goodbye World!");
     * ...
     * 
     * assertThat(parent).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myComplicatedCheckOn(o)); </code>
     * </pre>
     * 
     * With this policy, <code>myComplicatedCheckOn(o)</code> will be applied to the two Strings "Hello World!" 
     * and "Goodbye World!", but not to the containing ArrayList object referenced by myList. 
     */
    ELEMENTS_ONLY,
    /**
     * <p>Apply the {@link Predicate} to the collection object only and not to its elements. Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; myList = new ArrayList<>();
     * }
     * ...
     * 
     * Parent p = new Parent();
     * p.myList.add("Hello World!");
     * p.myList.add("Goodbye World!");
     * ...
     * 
     * assertThat(parent).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myComplicatedCheckOn(o)); </code>
     * </pre>
     * 
     * With this policy, <code>myComplicatedCheckOn(o)</code> will be applied to the ArrayList object referenced
     * by myList but not to the two Strings "Hello World!" and "Goodbye World!".
     */
    COLLECTION_OBJECT_ONLY,
    /**
     * <p>Apply the {@link Predicate} to the collection object as well as to (recursively) its elements. Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   List&lt;String&gt; myList = new ArrayList<>();
     * }
     * ...
     * 
     * Parent p = new Parent();
     * p.myList.add("Hello World!");
     * p.myList.add("Goodbye World!");
     * ...
     * 
     * assertThat(parent).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myComplicatedCheckOn(o)); </code>
     * </pre>
     * 
     * With this policy, <code>myComplicatedCheckOn(o)</code> will be applied to the ArrayList object referenced
     * by myList and also on the two Strings "Hello World!" and "Goodbye World!".
     */
    COLLECTION_OBJECT_AND_ELEMENTS
  }
  
  /**
   * Possible policies to use regarding maps when recursively asserting over the fields of an object tree.
   * @author bzt
   */
  public static enum MapAssertionPolicy {
    
    /**
     * <p>Apply the {@link Predicate} to the map object but not to its elements. Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   Map&lt;String, String&gt; myMap = new HashMap<>();
     * }
     * ...
     * 
     * Parent p = new Parent();
     * p.myMap.put("Hello World!", "Goodbye World!");
     * ...
     * 
     * assertThat(parent).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myComplicatedCheckOn(o)); </code>
     * </pre>
     * 
     * With this policy, <code>myComplicatedCheckOn(o)</code> will be applied to the HashMap object referenced
     * by myMap but not to the objects contained in {@link Map.Entry} ( "Hello World!" , "Goodbye World!" ).
     */
    MAP_OBJECT_ONLY,
    /**
     * <p>Apply the {@link Predicate} (recursively) to the mapped values in the map object but not to the map object or the keys. 
     * Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   Map&lt;String, String&gt; myMap = new HashMap<>();
     * }
     * ...
     * 
     * Parent p = new Parent();
     * p.myMap.put("Hello World!", "Goodbye World!");
     * ...
     * 
     * assertThat(parent).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myComplicatedCheckOn(o)); </code>
     * </pre>
     * 
     * With this policy, <code>myComplicatedCheckOn(o)</code> will be applied to the "Goodbye World!" object, but not to the HashMap 
     * object referenced by myMap nor to the "Hello World!" object.
     */
    VALUES_ONLY,
    /**
     * <p>Apply the {@link Predicate} to the map object as well as (recursively) to its elements. Consider the following example:<p>
     * <pre><code style='java'> class Parent {
     *   Map&lt;String, String&gt; myMap = new HashMap<>();
     * }
     * ...
     * 
     * Parent p = new Parent();
     * p.myMap.put("Hello World!", "Goodbye World!");
     * ...
     * 
     * assertThat(parent).usingRecursiveAssertion(...).allFieldsSatisfy(o -> myComplicatedCheckOn(o)); </code>
     * </pre>
     * 
     * With this policy, <code>myComplicatedCheckOn(o)</code> will be applied to the HashMap object referenced
     * by myMap and also to the objects contained in {@link Map.Entry} ( "Hello World!" , "Goodbye World!" ).
     */
    MAP_OBJECT_AND_ENTRIES
    
  }
}
