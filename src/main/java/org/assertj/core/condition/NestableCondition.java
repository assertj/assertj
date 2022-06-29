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
 * Copyright 2012-2022 the original author or authors.
 */

package org.assertj.core.condition;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;

/**
 * Building block to define a precise soft assertion about a complex object.
 * It allows to create readable assertions and produces beautiful assertion error messages.
 * <p>
 *   <pre><code class='java'>
 *   Example:
 *   class Customer {
 *      final String name;
 *      final Address address;
 *
 *      Customer(String name, Address address) {
 *        this.name = name;
 *        this.address = address;
 *      }
 *   }
 *
 *   class Address {
 *      final String firstLine;
 *      final String postcode;
 *
 *      Address(String firstLine, String postcode) {
 *        this.firstLine = firstLine;
 *        this.postcode = postcode;
 *      }
 *    }
 *
 *   static Condition{@literal <Customer>} name(String expected) {
 *     return new Condition<>(
 *        it -> expected.equals(it.name),
 *        "name: " + expected
 *     );
 *   }
 *
 *   static Condition{@literal <Customer>} customer(Condition{@literal <Customer>}... conditions) {
 *     return nestable("person", conditions);
 *   }
 *
 *   static Condition{@literal <Address>} firstLine(String expected) {
 *     return new Condition<>(
 *        it -> expected.equals(it.firstLine),
 *        "first line: " + expected
 *     );
 *   }
 *
 *   static Condition{@literal <Address>} postcode(String expected) {
 *     return new Condition<>(
 *        it -> expected.equals(it.postcode),
 *        "postcode: " + expected
 *     );
 *   }
 *
 *   static Condition{@literal <Customer>} address(Condition{@literal <Address>}... conditions) {
 *     return nestable(
 *        "address",
 *        customer -> customer.address,
 *        conditions
 *     );
 *   }
 *   </code></pre>
 *
 *   And assertions can be written like:
 *   <pre><code class='java'>
 *       assertThat(customer).is(
 *            customer(
 *                name("John"),
 *                address(
 *                  firstLine("3"),
 *                  postcode("KM3 8SP")
 *                )
 *             )
 *       );
 *   </code></pre>
 *   which leads to a easy to read assertion error:
 *   <pre>
 *   Expecting actual:
 *      org.assertj.core.condition.Customer@27ff5d15
 *   to be:
 *    [✗] person:[
 *        [✓] name: John,
 *        [✗] address:[
 *          [✗] first line: 3,
 *          [✓] postcode: KM3 8SP
 *        ]
 *    ]
 *    </pre>
 * For an even better assertion error, see <code>{@link VerboseCondition}</code>.
 * </p>
 *
 * @param <K> the type of object this condition accepts ({@literal Customer} in the example)
 * @param <T> the type of object nested into {@literal K} ({@literal Address} in the example)
 *
 * @author Alessandro Ciccimarra
 */
public class NestableCondition<K, T> extends Join<K> {
  private final String descriptionPrefix;

  /**
   * Creates a new <code>{@link NestableCondition}</code>
   * @param descriptionPrefix the prefix to use to build the description
   * @param f a function to extract the nested object of type {@literal T} from an object fo type {@literal K}
   * @param conditions conditions to be checked
   * @return the nestable condition
   * @param <K> the type of object the resulting condition accepts
   * @param <T> the type of object nested into {@literal K}
   */
  @SafeVarargs
  public static <K, T> Condition<K> nestable(String descriptionPrefix, Function<K, T> f, Condition<T>... conditions) {
    return new NestableCondition<>(descriptionPrefix, Arrays.stream(conditions), f);
  }

  /**
   * Creates a new <code>{@link NestableCondition}</code>
   * @param descriptionPrefix the prefix to use to build the description
   * @param conditions conditions to be checked
   * @return the nestable condition
   * @param <K> the type of object the resulting condition accepts
   */
  @SafeVarargs
  public static <K> Condition<K> nestable(String descriptionPrefix, Condition<K>... conditions) {
    return new NestableCondition<>(descriptionPrefix, Arrays.stream(conditions));
  }

  private NestableCondition(String descriptionPrefix, Stream<Condition<T>> conditions, Function<K, T> f) {
    super(compose(conditions, f));
    this.descriptionPrefix = descriptionPrefix;
  }

  private NestableCondition(String descriptionPrefix, Stream<Condition<K>> conditions) {
    super(conditions.collect(toList()));
    this.descriptionPrefix = descriptionPrefix;
  }

  @Override
  public boolean matches(K value) {
    return conditions.stream().allMatch(condition -> condition.matches(value));
  }

  @Override
  public String descriptionPrefix() {
    return descriptionPrefix;
  }

  private static <K, T> List<Condition<K>> compose(Stream<Condition<T>> conditions, Function<K, T> f) {
    return conditions.map(it -> compose(it, f)).collect(toList());
  }

  private static <K, T> Condition<K> compose(Condition<T> condition, Function<K, T> f) {
    return new Condition<>() {
      @Override
      public boolean matches(K value) {
        return condition.matches(f.apply(value));
      }

      @Override
      public Description conditionDescriptionWithStatus(K actual) {
        return condition.conditionDescriptionWithStatus(f.apply(actual));
      }
    };
  }
}
