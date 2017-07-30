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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.DateUtil.parseDatetime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.test.ComparableExample;
import org.assertj.core.data.MapEntry;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

public class BDDSoftAssertionsTest extends BaseAssertionsTest {

  private BDDSoftAssertions softly;

  @Before
  public void setup() {
    softly = new BDDSoftAssertions();
  }

  @Test
  public void all_assertions_should_pass() {
    softly.then(1).isEqualTo(1);
    softly.then(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    softly.assertAll();
  }

  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() throws URISyntaxException {
    try {
      softly.then(BigDecimal.ZERO).isEqualTo(BigDecimal.ONE);

      softly.then(Boolean.FALSE).isTrue();
      softly.then(false).isTrue();
      softly.then(new boolean[] { false }).isEqualTo(new boolean[] { true });

      softly.then(new Byte((byte) 0)).isEqualTo((byte) 1);
      softly.then((byte) 2).inHexadecimal().isEqualTo((byte) 3);
      softly.then(new byte[] { 4 }).isEqualTo(new byte[] { 5 });

      softly.then(new Character((char) 65)).isEqualTo(new Character((char) 66));
      softly.then((char) 67).isEqualTo((char) 68);
      softly.then(new char[] { 69 }).isEqualTo(new char[] { 70 });

      softly.then(new StringBuilder("a")).isEqualTo(new StringBuilder("b"));

      softly.then(Object.class).isEqualTo(String.class);

      softly.then(parseDatetime("1999-12-31T23:59:59")).isEqualTo(parseDatetime("2000-01-01T00:00:01"));

      softly.then(new Double(6.0d)).isEqualTo(new Double(7.0d));
      softly.then(8.0d).isEqualTo(9.0d);
      softly.then(new double[] { 10.0d }).isEqualTo(new double[] { 11.0d });

      softly.then(new File("a"))
            .overridingErrorMessage("expected:<File(b)> but was:<File(a)>")
            .isEqualTo(new File("b"));

      softly.then(new Float(12f)).isEqualTo(new Float(13f));
      softly.then(14f).isEqualTo(15f);
      softly.then(new float[] { 16f }).isEqualTo(new float[] { 17f });

      softly.then(new ByteArrayInputStream(new byte[] { (byte) 65 }))
            .hasSameContentAs(new ByteArrayInputStream(new byte[] { (byte) 66 }));

      softly.then(new Integer(20)).isEqualTo(new Integer(21));
      softly.then(22).isEqualTo(23);
      softly.then(new int[] { 24 }).isEqualTo(new int[] { 25 });

      softly.then((Iterable<String>) Lists.newArrayList("26")).isEqualTo(Lists.newArrayList("27"));
      softly.then(Lists.newArrayList("28").iterator()).contains("29");
      softly.then(Lists.newArrayList("30")).isEqualTo(Lists.newArrayList("31"));

      softly.then(new Long(32L)).isEqualTo(new Long(33L));
      softly.then(34L).isEqualTo(35L);
      softly.then(new long[] { 36L }).isEqualTo(new long[] { 37L });

      softly.then(Maps.mapOf(MapEntry.entry("38", "39"))).isEqualTo(Maps.mapOf(MapEntry.entry("40", "41")));

      softly.then(new Short((short) 42)).isEqualTo(new Short((short) 43));
      softly.then((short) 44).isEqualTo((short) 45);
      softly.then(new short[] { (short) 46 }).isEqualTo(new short[] { (short) 47 });

      softly.then("48").isEqualTo("49");

      softly.then(new Object() {
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

      softly.then(new Object[] { new Object() {
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
      softly.then(illegalArgumentException).hasMessage("NullPointerException message");
      softly.thenThrownBy(new ThrowingCallable() {
        @Override
        public void call() throws Exception {
          throw new Exception("something was wrong");
        }

      }).hasMessage("something was good");

      softly.then(Optional.of("not empty")).isEqualTo("empty");
      // TODO should be caught : softly.assertThat(Optional.of("not empty")).isEmpty();
      softly.then(OptionalInt.of(0)).isEqualTo(1);
      softly.then(OptionalDouble.of(0.0)).isEqualTo(1.0);
      softly.then(OptionalLong.of(0L)).isEqualTo(1L);
      softly.then(new URI("http://assertj.org")).hasPort(8888);

      softly.then(LocalTime.of(12, 0)).isEqualTo(LocalTime.of(13, 0));
      softly.then(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC)).isEqualTo(OffsetTime.of(13, 0, 0, 0, ZoneOffset.UTC));
      softly.then(OffsetDateTime.MIN).isEqualTo(LocalDateTime.MAX);
      // softly.then(completedFuture("done")).hasFailed();
      softly.then((Predicate<String>) s -> s.equals("something")).accepts("something else");
      softly.then((IntPredicate) s -> s == 1).accepts(2);
      softly.then((LongPredicate) s -> s == 1).accepts(2);
      softly.then((DoublePredicate) s -> s == 1).accepts(2.0);

      softly.assertAll();

      fail("Should not reach here");

    } catch (SoftAssertionError e) {
      List<String> errors = e.getErrors();
      assertThat(errors).hasSize(51);

      assertThat(errors.get(0)).startsWith("expected:<[1]> but was:<[0]>");

      assertThat(errors.get(1)).startsWith("expected:<[tru]e> but was:<[fals]e>");
      assertThat(errors.get(2)).startsWith("expected:<[tru]e> but was:<[fals]e>");
      assertThat(errors.get(3)).startsWith("expected:<[[tru]e]> but was:<[[fals]e]>");

      assertThat(errors.get(4)).startsWith("expected:<[1]> but was:<[0]>");
      assertThat(errors.get(5)).startsWith("expected:<0x0[3]> but was:<0x0[2]>");
      assertThat(errors.get(6)).startsWith("expected:<[[5]]> but was:<[[4]]>");

      assertThat(errors.get(7)).startsWith("expected:<'[B]'> but was:<'[A]'>");
      assertThat(errors.get(8)).startsWith("expected:<'[D]'> but was:<'[C]'>");
      assertThat(errors.get(9)).startsWith("expected:<['[F]']> but was:<['[E]']>");

      assertThat(errors.get(10)).startsWith("expected:<[b]> but was:<[a]>");

      assertThat(errors.get(11)).startsWith("expected:<java.lang.[String]> but was:<java.lang.[Object]>");

      assertThat(errors.get(12)).startsWith("expected:<[2000-01-01T00:00:01].000> but was:<[1999-12-31T23:59:59].000>");

      assertThat(errors.get(13)).startsWith("expected:<[7].0> but was:<[6].0>");
      assertThat(errors.get(14)).startsWith("expected:<[9].0> but was:<[8].0>");
      assertThat(errors.get(15)).startsWith("expected:<[1[1].0]> but was:<[1[0].0]>");

      assertThat(errors.get(16)).startsWith("expected:<File(b)> but was:<File(a)>");

      assertThat(errors.get(17)).startsWith("expected:<1[3].0f> but was:<1[2].0f>");
      assertThat(errors.get(18)).startsWith("expected:<1[5].0f> but was:<1[4].0f>");
      assertThat(errors.get(19)).startsWith("expected:<[1[7].0f]> but was:<[1[6].0f]>");

      assertThat(errors.get(20)).startsWith(format("%nInputStreams do not have same content:%n%n"
                                                 + "Changed content at line 1:%n"
                                                 + "expecting:%n"
                                                 + "  [\"B\"]%n"
                                                 + "but was:%n"
                                                 + "  [\"A\"]%n"));

      assertThat(errors.get(21)).startsWith("expected:<2[1]> but was:<2[0]>");
      assertThat(errors.get(22)).startsWith("expected:<2[3]> but was:<2[2]>");
      assertThat(errors.get(23)).startsWith("expected:<[2[5]]> but was:<[2[4]]>");

      assertThat(errors.get(24)).startsWith("expected:<[\"2[7]\"]> but was:<[\"2[6]\"]>");
      assertThat(errors.get(25)).startsWith(format("%nExpecting:%n" +
                                                   " <[\"28\"]>%n" +
                                                   "to contain:%n" +
                                                   " <[\"29\"]>%n" +
                                                   "but could not find:%n" +
                                                   " <[\"29\"]>%n"));
      assertThat(errors.get(26)).startsWith("expected:<[\"3[1]\"]> but was:<[\"3[0]\"]>");

      assertThat(errors.get(27)).startsWith("expected:<3[3]L> but was:<3[2]L>");
      assertThat(errors.get(28)).startsWith("expected:<3[5]L> but was:<3[4]L>");
      assertThat(errors.get(29)).startsWith("expected:<[3[7]L]> but was:<[3[6]L]>");

      assertThat(errors.get(30)).startsWith("expected:<{\"[40\"=\"41]\"}> but was:<{\"[38\"=\"39]\"}>");

      assertThat(errors.get(31)).startsWith("expected:<4[3]> but was:<4[2]>");
      assertThat(errors.get(32)).startsWith("expected:<4[5]> but was:<4[4]>");
      assertThat(errors.get(33)).startsWith("expected:<[4[7]]> but was:<[4[6]]>");

      assertThat(errors.get(34)).startsWith("expected:<\"4[9]\"> but was:<\"4[8]\">");

      assertThat(errors.get(35)).startsWith("expected:<5[1]> but was:<5[0]>");
      assertThat(errors.get(36)).startsWith("expected:<[5[3]]> but was:<[5[2]]>");
      assertThat(errors.get(37)).startsWith(format("%nExpecting message:%n"
                                                 + " <\"NullPointerException message\">%n"
                                                 + "but was:%n"
                                                 + " <\"IllegalArgumentException message\">"));
      assertThat(errors.get(38)).startsWith(format("%nExpecting message:%n"
                                                 + " <\"something was good\">%n"
                                                 + "but was:%n"
                                                 + " <\"something was wrong\">"));

      assertThat(errors.get(39)).startsWith("expected:<[\"empty\"]> but was:<[Optional[not empty]]>");
      assertThat(errors.get(40)).startsWith("expected:<[1]> but was:<[OptionalInt[0]]>");
      assertThat(errors.get(41)).startsWith("expected:<[1.0]> but was:<[OptionalDouble[0.0]]>");
      assertThat(errors.get(42)).startsWith("expected:<[1L]> but was:<[OptionalLong[0]]>");
      assertThat(errors.get(43)).contains(format("%nExpecting port of"));
      assertThat(errors.get(44)).startsWith("expected:<1[3]:00> but was:<1[2]:00>");
      assertThat(errors.get(45)).startsWith("expected:<1[3]:00Z> but was:<1[2]:00Z>");
      assertThat(errors.get(46)).startsWith("expected:<[+999999999-12-31T23:59:59.999999999]> but was:<[-999999999-01-01T00:00+18:00]>");
      // assertThat(errors.get(47)).isEqualTo("");
      assertThat(errors.get(47)).startsWith(String.format("%nExpecting:%n  <given predicate>%n"
                                                        + "to accept <\"something else\"> but it did not."));

      assertThat(errors.get(48)).startsWith(String.format("%nExpecting:%n  <given predicate>%n"
                                                         + "to accept <2> but it did not."));

      assertThat(errors.get(49)).startsWith(String.format("%nExpecting:%n  <given predicate>%n"
                                                         + "to accept <2L> but it did not."));

      assertThat(errors.get(50)).startsWith(String.format("%nExpecting:%n  <given predicate>%n"
                                                         + "to accept <2.0> but it did not."));

    }
  }

  @Test
  public void should_work_with_comparable() throws Exception {
    ComparableExample example1 = new ComparableExample(0);
    ComparableExample example2 = new ComparableExample(0);
    softly.then(example1).isEqualByComparingTo(example2);
    softly.assertAll();
  }

  @Test
  public void should_work_with_atomic() throws Exception {
    // simple atomic value
    softly.then(new AtomicBoolean(true)).isTrue();
    softly.then(new AtomicInteger(1)).hasValueGreaterThan(0);
    softly.then(new AtomicLong(1L)).hasValueGreaterThan(0L);
    softly.then(new AtomicReference<>("abc")).hasValue("abc");
    // atomic array value
    softly.then(new AtomicIntegerArray(new int[] { 1, 2, 3 })).containsExactly(1, 2, 3);
    softly.then(new AtomicLongArray(new long[] {1L, 2L, 3L})).containsExactly(1L, 2L, 3L);
    softly.then(new AtomicReferenceArray<>(array("a", "b", "c"))).containsExactly("a", "b", "c");
    softly.assertAll();
  }
  
}
