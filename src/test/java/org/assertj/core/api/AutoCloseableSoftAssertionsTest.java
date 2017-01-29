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
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.DateUtil.parseDatetime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.data.MapEntry;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Lists;
import org.junit.Test;

public class AutoCloseableSoftAssertionsTest {

  @Test
  public void all_assertions_should_pass() {
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      softly.assertThat(1).isEqualTo(1);
      softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    }
  }
 
  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() {
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {

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
            .overridingErrorMessage("expected:<File(b)> but was:<File(a)>")
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
      softly.assertThat(Lists.newArrayList("28").iterator()).contains("29");
      softly.assertThat(Lists.newArrayList("30")).isEqualTo(Lists.newArrayList("31"));

      softly.assertThat(new Long(32L)).isEqualTo(new Long(33L));
      softly.assertThat(34L).isEqualTo(35L);
      softly.assertThat(new long[] { 36L }).isEqualTo(new long[] { 37L });

      softly.assertThat(Maps.mapOf(MapEntry.entry("38", "39"))).isEqualTo(Maps.mapOf(MapEntry.entry("40", "41")));

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
      softly.assertThatThrownBy(new ThrowingCallable() {
        @Override
        public void call() throws Exception {
          throw new Exception("something was wrong");
        }

      }).hasMessage("something was good");
      softly.assertThat(Optional.of("bad option")).isEqualTo(Optional.of("good option"));
      softly.assertThat(LocalDate.of(2015, 1, 1)).isEqualTo(LocalDate.of(2015, 1, 2));
      softly.assertThat(LocalDateTime.of(2015, 1, 1, 23, 59, 59)).isEqualTo(LocalDateTime.of(2015, 1, 1, 23, 59, 0));
      softly.assertThat(ZonedDateTime.of(2015, 1, 1, 23, 59, 59, 0, UTC)).isEqualTo(ZonedDateTime.of(2015, 1, 1, 23,
                                                                                                     59, 0, 0, UTC));

      softly.assertThat(OptionalInt.of(0)).isEqualTo(1);
      softly.assertThat(OptionalDouble.of(0.0)).isEqualTo(1.0);
      softly.assertThat(OptionalLong.of(0L)).isEqualTo(1L);

      softly.assertThat(LocalTime.of(12, 0)).isEqualTo(LocalTime.of(13, 0));
      softly.assertThat(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC))
            .isEqualTo(OffsetTime.of(13, 0, 0, 0, ZoneOffset.UTC));
      softly.assertThat(OffsetDateTime.MIN).isEqualTo(OffsetDateTime.MAX);

    } catch (SoftAssertionError e) {
      List<String> errors = e.getErrors();
      assertThat(errors).hasSize(49);

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

      assertThat(errors.get(20)).startsWith(String.format("%nInputStreams do not have same content:%n%n"
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
      assertThat(errors.get(39)).startsWith("expected:<Optional[[goo]d option]> but was:<Optional[[ba]d option]>");
      assertThat(errors.get(40)).startsWith("expected:<2015-01-0[2]> but was:<2015-01-0[1]>");
      assertThat(errors.get(41)).startsWith("expected:<2015-01-01T23:59[]> but was:<2015-01-01T23:59[:59]>");
      assertThat(errors.get(42)).startsWith("expected:<2015-01-01T23:59[]Z> but was:<2015-01-01T23:59[:59]Z>");

      assertThat(errors.get(43)).startsWith("expected:<[1]> but was:<[OptionalInt[0]]>");
      assertThat(errors.get(44)).startsWith("expected:<[1.0]> but was:<[OptionalDouble[0.0]]>");
      assertThat(errors.get(45)).startsWith("expected:<[1L]> but was:<[OptionalLong[0]]>");

      assertThat(errors.get(46)).startsWith("expected:<1[3]:00> but was:<1[2]:00>");
      assertThat(errors.get(47)).startsWith("expected:<1[3]:00Z> but was:<1[2]:00Z>");

      assertThat(errors.get(48)).startsWith("expected:<[+999999999-12-31T23:59:59.999999999-]18:00> but was:<[-999999999-01-01T00:00+]18:00>");

      return;
    }
    fail("Should not reach here");
  }

}
