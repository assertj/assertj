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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.condition;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;

/**
 * Building block to define precise conditions on complex objects.
 * <p>
 * It allows to create readable assertions and produces beautiful assertion error messages.
 * <p>
 * Example:
 * <pre><code class='java'> import static org.assertj.core.condition.NestableCondition.nestable;
 * import static org.assertj.core.condition.VerboseCondition.verboseCondition;
 *
 * class Customer {
 *    final String name;
 *    final Address address;
 *
 *    Customer(String name, Address address) {
 *      this.name = name;
 *      this.address = address;
 *    }
 * }
 *
 * class Address {
 *    final String firstLine;
 *    final String postcode;
 *
 *    Address(String firstLine, String postcode) {
 *      this.firstLine = firstLine;
 *      this.postcode = postcode;
 *    }
 *  }
 *
 * static Condition&lt;Customer&gt; name(String expected) {
 *   return new Condition&lt;&gt;(
 *      it -> expected.equals(it.name),
 *      "name: " + expected
 *   );
 * }
 *
 * static Condition&lt;Customer&gt; customer(Condition&lt;Customer&gt;... conditions) {
 *   return nestable("person", conditions);
 * }
 *
 * static Condition&lt;Address&gt; firstLine(String expected) {
 *   return new Condition&lt;&gt;(
 *      it -> expected.equals(it.firstLine),
 *      "first line: " + expected
 *   );
 * }
 *
 * static Condition&lt;Address&gt; postcode(String expected) {
 *   return new Condition&lt;&gt;(
 *      it -> expected.equals(it.postcode),
 *      "postcode: " + expected
 *   );
 * }
 *
 * static Condition&lt;Customer&gt; address(Condition&lt;Address&gt;... conditions) {
 *   return nestable(
 *      "address",
 *      customer -> customer.address,
 *      conditions
 *   );
 * }</code></pre>
 *
 *   And assertions can be written like:
 * <pre><code class='java'> assertThat(customer).is(
 *   customer(
 *     name("John"),
 *     address(
 *       firstLine("3"),
 *       postcode("KM3 8SP")
 *     )
 *   )
 * ); </code></pre>
 * leads to an easy-to-read assertion error:
 * <pre><code class='text'> Expecting actual:
 *   org.assertj.core.condition.Customer@27ff5d15
 * to be:
 *   [✗] person:[
 *       [✓] name: John,
 *       [✗] address:[
 *         [✗] first line: 3,
 *         [✓] postcode: KM3 8SP
 *       ]
 *   ]</code></pre>
 * For an even better assertion error, see <code>{@link VerboseCondition}</code>.
 *
 * @author Alessandro Ciccimarra
 *
 * @param <ACTUAL> the type of object this condition accepts ({@literal Customer} in the example)
 * @param <NESTED> the type of object nested into {@literal ACTUAL} ({@literal Address} in the example)
 */
public class NestableCondition<ACTUAL, NESTED> extends Join<ACTUAL> {
  private final String descriptionPrefix;

  /**
   * Creates a new <code>{@link NestableCondition}</code>
   * @param descriptionPrefix the prefix to use to build the description
   * @param extractor a function to extract the nested object of type {@literal T} from an object fo type {@literal K}
   * @param conditions conditions to be checked
   * @param <ACTUAL> the type of object the resulting condition accepts
   * @param <NESTED> the type of object nested into {@literal K}
   * @return the nestable condition
   */
  @SafeVarargs
  public static <ACTUAL, NESTED> Condition<ACTUAL> nestable(String descriptionPrefix,
                                                            Function<? super ACTUAL, ? extends NESTED> extractor,
                                                            Condition<? super NESTED>... conditions) {
    return new NestableCondition<>(descriptionPrefix, stream(conditions), extractor);
  }

  /**
   * Creates a new <code>{@link NestableCondition}</code>
   * @param descriptionPrefix the prefix to use to build the description
   * @param conditions conditions to be checked
   * @param <ACTUAL> the type of object the resulting condition accepts
   * @return the nestable condition
   */
  @SafeVarargs
  public static <ACTUAL> Condition<ACTUAL> nestable(String descriptionPrefix, Condition<? super ACTUAL>... conditions) {
    return new NestableCondition<ACTUAL, ACTUAL>(descriptionPrefix, stream(conditions));
  }

  private NestableCondition(String descriptionPrefix, Stream<? extends Condition<? super NESTED>> conditions,
                            Function<? super ACTUAL, ? extends NESTED> extractor) {
    super(compose(conditions, extractor));
    this.descriptionPrefix = descriptionPrefix;
  }

  private NestableCondition(String descriptionPrefix, Stream<? extends Condition<? super ACTUAL>> conditions) {
    super(conditions.collect(toList()));
    this.descriptionPrefix = descriptionPrefix;
  }

  @Override
  public boolean matches(ACTUAL value) {
    return conditions.stream().allMatch(condition -> condition.matches(value));
  }

  @Override
  public String descriptionPrefix() {
    return descriptionPrefix;
  }

  private static <ACTUAL, NESTED> List<Condition<? super ACTUAL>> compose(Stream<? extends Condition<? super NESTED>> conditions,
                                                                          Function<? super ACTUAL, ? extends NESTED> extractor) {
    return conditions.map(condition -> compose(condition, extractor)).collect(toList());
  }

  private static <ACTUAL, NESTED> Condition<ACTUAL> compose(Condition<? super NESTED> condition,
                                                            Function<? super ACTUAL, ? extends NESTED> extractor) {
    return new Condition<ACTUAL>(condition.description()) {
      @Override
      public boolean matches(ACTUAL value) {
        return condition.matches(extractor.apply(value));
      }

      @Override
      public Description conditionDescriptionWithStatus(ACTUAL actual) {
        return condition.conditionDescriptionWithStatus(extractor.apply(actual));
      }
    };
  }
}
