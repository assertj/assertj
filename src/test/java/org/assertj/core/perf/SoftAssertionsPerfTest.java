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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.perf;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.api.BaseAssertionsTest;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.MapEntry;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * results in 3.9.0  : ~3000ms
 * results in 3.9.1+ : ~9300ms
 * results in 3.10.0 : ~6000ms
 * results in 3.10.0 with Raphael changes < 1.8.10: ~5500ms
 * results in 3.10.0 with 1.8.10: ~5100ms
 * results in 3.10.0 with 1.8.11: ~5000ms
 */
@Disabled
public class SoftAssertionsPerfTest extends BaseAssertionsTest {

  private SoftAssertions softly;

  private CartoonCharacter homer;
  private CartoonCharacter fred;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter bart;

  private Map<String, Object> iterableMap;

  private static long start;

  @BeforeAll
  public static void beforeAll() {
    start = System.currentTimeMillis();
  }

  @AfterAll
  public static void afterAll() {
    long duration = System.currentTimeMillis() - start;
    System.out.println("SoftAssertionsTest execution time (ms): " + duration);
  }

  @BeforeEach
  public void setup() {
    softly = new SoftAssertions();

    bart = new CartoonCharacter("Bart Simpson");
    lisa = new CartoonCharacter("Lisa Simpson");
    maggie = new CartoonCharacter("Maggie Simpson");

    homer = new CartoonCharacter("Homer Simpson");
    homer.getChildren().add(bart);
    homer.getChildren().add(lisa);
    homer.getChildren().add(maggie);

    CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
    fred = new CartoonCharacter("Fred Flintstone");
    fred.getChildren().add(pebbles);

    List<String> names = asList("Dave", "Jeff");
    LinkedHashSet<String> jobs = newLinkedHashSet("Plumber", "Builder");
    Iterable<String> cities = asList("Dover", "Boston", "Paris");
    int[] ranks = { 1, 2, 3 };

    iterableMap = new LinkedHashMap<>();
    iterableMap.put("name", names);
    iterableMap.put("job", jobs);
    iterableMap.put("city", cities);
    iterableMap.put("rank", ranks);
  }

  @Test
  public void all_assertions_should_pass() {
    softly.assertThat(1).isEqualTo(1);
    softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    softly.assertAll();
  }

  @Test
  public void all_assertions_should_pass2() {
    softly.assertThat(1).isEqualTo(1);
    softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    softly.assertAll();
  }

  @Test
  public void all_assertions_should_pass3() {
    softly.assertThat(1).isEqualTo(1);
    softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    softly.assertAll();
  }

  @Test
  public void all_assertions_should_pass4() {
    softly.assertThat(1).isEqualTo(1);
    softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    softly.assertAll();
  }

  @Test
  public void should_return_success_of_last_assertion() {
    softly.assertThat(true).isFalse();
    softly.assertThat(true).isEqualTo(true);
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  public void should_return_success_of_last_assertion_with_nested_calls() {
    softly.assertThat(true).isFalse();
    softly.assertThat(true).isTrue(); // isTrue() calls isEqualTo(true)
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  public void should_return_failure_of_last_assertion() {
    softly.assertThat(true).isTrue();
    softly.assertThat(true).isEqualTo(false);
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  public void should_return_failure_of_last_assertion_with_nested_calls() {
    softly.assertThat(true).isTrue();
    softly.assertThat(true).isFalse(); // isFalse() calls isEqualTo(false)
    assertThat(softly.wasSuccess()).isFalse();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_map_assertions() {
    // GIVEN
    Map<String, String> map = mapOf(MapEntry.entry("54", "55"));
    // WHEN
    softly.assertThat(map).contains(MapEntry.entry("1", "2")).isEmpty();
    // THEN
    List<Throwable> errors = softly.errorsCollected();
    assertThat(errors).hasSize(2);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() {
    // perform a bunch of soft assertions
    softly.assertThat(BigDecimal.ZERO).isEqualTo(BigDecimal.ONE);
    softly.assertThat(Boolean.FALSE).isTrue();
    softly.assertThat(false).isTrue();
    softly.assertThat(new boolean[] { false }).isEqualTo(new boolean[] { true });
    softly.assertThat(new Byte((byte) 0)).isEqualTo((byte) 1);
    softly.assertThat((byte) 2).inHexadecimal().isEqualTo((byte) 3);
    softly.assertThat(new byte[] { 4 }).isEqualTo(new byte[] { 5 });
    softly.assertThat(new Character((char) 65)).isEqualTo(new Character((char) 66));
    softly.assertThat((char) 67).isEqualTo((char) 68);
    softly.assertThat(new char[] { 69 }).isEqualTo(new char[] { 70 });
    softly.assertThat(new StringBuilder("a")).isEqualTo(new StringBuilder("b"));
    softly.assertThat(Object.class).isEqualTo(String.class);
    softly.assertThat(parseDatetime("1999-12-31T23:59:59")).isEqualTo(parseDatetime("2000-01-01T00:00:01"));
    softly.assertThat(new Double(6.0d)).isEqualTo(new Double(7.0d));
    softly.assertThat(8.0d).isEqualTo(9.0d);
    softly.assertThat(new double[] { 10.0d }).isEqualTo(new double[] { 11.0d });
    softly.assertThat(new File("a"))
          .overridingErrorMessage(format("%nExpecting:%n <File(a)>%nto be equal to:%n <File(b)>%nbut was not."))
          .isEqualTo(new File("b"));
    softly.assertThat(new Float(12f)).isEqualTo(new Float(13f));
    softly.assertThat(14f).isEqualTo(15f);
    softly.assertThat(new float[] { 16f }).isEqualTo(new float[] { 17f });
    softly.assertThat(new ByteArrayInputStream(new byte[] { (byte) 65 }))
          .hasSameContentAs(new ByteArrayInputStream(new byte[] { (byte) 66 }));
    softly.assertThat(new Integer(20)).isEqualTo(new Integer(21));
    softly.assertThat(22).isEqualTo(23);
    softly.assertThat(new int[] { 24 }).isEqualTo(new int[] { 25 });
    softly.assertThat((Iterable<String>) Lists.newArrayList("26")).isEqualTo(Lists.newArrayList("27"));
    softly.assertThat(Lists.newArrayList("28").iterator()).hasNext();
    softly.assertThat(Lists.newArrayList("30")).isEqualTo(Lists.newArrayList("31"));
    softly.assertThat(new Long(32L)).isEqualTo(new Long(33L));
    softly.assertThat(34L).isEqualTo(35L);
    softly.assertThat(new long[] { 36L }).isEqualTo(new long[] { 37L });
    softly.assertThat(mapOf(MapEntry.entry("38", "39"))).isEqualTo(mapOf(MapEntry.entry("40", "41")));
    softly.assertThat(new Short((short) 42)).isEqualTo(new Short((short) 43));
    softly.assertThat((short) 44).isEqualTo((short) 45);
    softly.assertThat(new short[] { (short) 46 }).isEqualTo(new short[] { (short) 47 });
    softly.assertThat("48").isEqualTo("49");
    softly.assertThat(new Object() {
      @Override
      public String toString() {
        return "50";
      }
    }).isEqualTo(new Object() {
      @Override
      public String toString() {
        return "51";
      }
    });
    softly.assertThat(new Object[] { new Object() {
      @Override
      public String toString() {
        return "52";
      }
    } }).isEqualTo(new Object[] { new Object() {
      @Override
      public String toString() {
        return "53";
      }
    } });

    final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("IllegalArgumentException message");
    softly.assertThat(illegalArgumentException).hasMessage("NullPointerException message");
    softly.assertThatThrownBy(() -> {
      throw new Exception("something was wrong");
    }).hasMessage("something was good");
    softly.assertThat(mapOf(MapEntry.entry("54", "55"))).contains(MapEntry.entry("1", "2"));
    softly.assertThat(LocalTime.of(12, 00)).isEqualTo(LocalTime.of(13, 00));
    softly.assertThat(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC))
          .isEqualTo(OffsetTime.of(13, 0, 0, 0, ZoneOffset.UTC));
    softly.assertThat(Optional.of("not empty")).isEqualTo("empty");
    softly.assertThat(OptionalInt.of(0)).isEqualTo(1);
    softly.assertThat(OptionalDouble.of(0.0)).isEqualTo(1.0);
    softly.assertThat(OptionalLong.of(0L)).isEqualTo(1L);
    softly.assertThat(URI.create("http://assertj.org")).hasPort(8888);
    softly.assertThat(CompletableFuture.completedFuture("done")).hasFailed();
    softly.assertThat((Predicate<String>) s -> s.equals("something")).accepts("something else");
    softly.assertThat((IntPredicate) s -> s == 1).accepts(2);
    softly.assertThat((LongPredicate) s -> s == 1).accepts(2);
    softly.assertThat((DoublePredicate) s -> s == 1).accepts(2);
    // assert everything, but catch the error since it is a perf test
    catchThrowable(() -> softly.assertAll());
  }

}
