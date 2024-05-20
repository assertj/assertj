/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.Lists.list;

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
import org.assertj.core.internal.ChronoLocalDateTimeComparator;
import org.assertj.core.internal.ChronoZonedDateTimeByInstantComparator;
import org.assertj.core.internal.OffsetDateTimeByInstantComparator;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

class AutoCloseableSoftAssertionsTest {

  @Test
  void all_assertions_should_pass() {
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      softly.assertThat(1).isEqualTo(1);
      softly.assertThat(list(1, 2)).containsOnly(1, 2);
    }
  }

  @Test
  void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {

      softly.assertThat(BigDecimal.ZERO).isEqualTo(BigDecimal.ONE);

      softly.assertThat(Boolean.FALSE).isTrue();
      softly.assertThat(false).isTrue();
      softly.assertThat(new boolean[] { false }).isEqualTo(new boolean[] { true });

      softly.assertThat(Byte.valueOf((byte) 0)).isEqualTo((byte) 1);
      softly.assertThat((byte) 2).inHexadecimal().isEqualTo((byte) 3);
      softly.assertThat(new byte[] { 4 }).isEqualTo(new byte[] { 5 });

      softly.assertThat(Character.valueOf((char) 65)).isEqualTo(Character.valueOf((char) 66));
      softly.assertThat((char) 67).isEqualTo((char) 68);
      softly.assertThat(new char[] { 69 }).isEqualTo(new char[] { 70 });

      softly.assertThat(new StringBuilder("a")).isEqualTo(new StringBuilder("b"));

      softly.assertThat(Object.class).isEqualTo(String.class);

      softly.assertThat(parseDatetime("1999-12-31T23:59:59")).isEqualTo(parseDatetime("2000-01-01T00:00:01"));

      softly.assertThat(Double.valueOf(6.0d)).isEqualTo(Double.valueOf(7.0d));
      softly.assertThat(8.0d).isEqualTo(9.0d);
      softly.assertThat(new double[] { 10.0d }).isEqualTo(new double[] { 11.0d });

      softly.assertThat(new File("a"))
            .overridingErrorMessage(shouldBeEqualMessage("File(a)", "File(b)"))
            .isEqualTo(new File("b"));

      softly.assertThat(Float.valueOf(12f)).isEqualTo(Float.valueOf(13f));
      softly.assertThat(14f).isEqualTo(15f);
      softly.assertThat(new float[] { 16f }).isEqualTo(new float[] { 17f });

      softly.assertThat(new ByteArrayInputStream(new byte[] { (byte) 65 }))
            .hasSameContentAs(new ByteArrayInputStream(new byte[] { (byte) 66 }));

      softly.assertThat(Integer.valueOf(20)).isEqualTo(Integer.valueOf(21));
      softly.assertThat(22).isEqualTo(23);
      softly.assertThat(new int[] { 24 }).isEqualTo(new int[] { 25 });

      softly.assertThat((Iterable<String>) list("26")).isEqualTo(list("27"));
      softly.assertThat(list("28").iterator()).isExhausted();
      softly.assertThat(list("30")).isEqualTo(list("31"));

      softly.assertThat(Long.valueOf(32L)).isEqualTo(Long.valueOf(33L));
      softly.assertThat(34L).isEqualTo(35L);
      softly.assertThat(new long[] { 36L }).isEqualTo(new long[] { 37L });

      softly.assertThat(mapOf(entry("38", "39"))).isEqualTo(mapOf(entry("40", "41")));

      softly.assertThat(Short.valueOf((short) 42)).isEqualTo(Short.valueOf((short) 43));
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

    } catch (MultipleFailuresError e) {
      List<String> errors = e.getFailures().stream().map(Object::toString).collect(toList());
      assertThat(errors).hasSize(49);

      assertThat(errors.get(0)).contains(format(shouldBeEqualMessage("0", "1")));

      assertThat(errors.get(1)).contains(format("%nExpecting value to be true but was false"));
      assertThat(errors.get(2)).contains(format("%nExpecting value to be true but was false"));
      assertThat(errors.get(3)).contains(shouldBeEqualMessage("[false]", "[true]"));

      assertThat(errors.get(4)).contains(shouldBeEqualMessage("0", "1"));
      assertThat(errors.get(5)).contains(shouldBeEqualMessage("0x02", "0x03"));
      assertThat(errors.get(6)).contains(shouldBeEqualMessage("[4]", "[5]"));

      assertThat(errors.get(7)).contains(shouldBeEqualMessage("'A'", "'B'"));
      assertThat(errors.get(8)).contains(shouldBeEqualMessage("'C'", "'D'"));
      assertThat(errors.get(9)).contains(shouldBeEqualMessage("['E']", "['F']"));

      assertThat(errors.get(10)).contains(shouldBeEqualMessage("a", "b"));

      assertThat(errors.get(11)).contains(shouldBeEqualMessage("java.lang.Object", "java.lang.String"));

      assertThat(errors.get(12)).contains(shouldBeEqualMessage("1999-12-31T23:59:59.000 (java.util.Date)",
                                                               "2000-01-01T00:00:01.000 (java.util.Date)"));

      assertThat(errors.get(13)).contains(shouldBeEqualMessage("6.0", "7.0"));
      assertThat(errors.get(14)).contains(shouldBeEqualMessage("8.0", "9.0"));
      assertThat(errors.get(15)).contains(shouldBeEqualMessage("[10.0]", "[11.0]"));

      assertThat(errors.get(16)).contains(shouldBeEqualMessage("File(a)", "File(b)"));

      assertThat(errors.get(17)).contains(shouldBeEqualMessage("12.0f", "13.0f"));
      assertThat(errors.get(18)).contains(shouldBeEqualMessage("14.0f", "15.0f"));
      assertThat(errors.get(19)).contains(shouldBeEqualMessage("[16.0f]", "[17.0f]"));

      assertThat(errors.get(20)).contains(String.format("%nInputStreams do not have same content:%n%n"
                                                        + "Changed content at line 1:%n"
                                                        + "expecting:%n"
                                                        + "  [\"B\"]%n"
                                                        + "but was:%n"
                                                        + "  [\"A\"]%n"));

      assertThat(errors.get(21)).contains(shouldBeEqualMessage("20", "21"));
      assertThat(errors.get(22)).contains(shouldBeEqualMessage("22", "23"));
      assertThat(errors.get(23)).contains(shouldBeEqualMessage("[24]", "[25]"));

      assertThat(errors.get(24)).contains(shouldBeEqualMessage("[\"26\"]", "[\"27\"]"));
      assertThat(errors.get(25)).contains(format("Expecting the iterator under test to be exhausted"));
      assertThat(errors.get(26)).contains(shouldBeEqualMessage("[\"30\"]", "[\"31\"]"));

      assertThat(errors.get(27)).contains(shouldBeEqualMessage("32L", "33L"));
      assertThat(errors.get(28)).contains(shouldBeEqualMessage("34L", "35L"));
      assertThat(errors.get(29)).contains(shouldBeEqualMessage("[36L]", "[37L]"));

      assertThat(errors.get(30)).contains(shouldBeEqualMessage("{\"38\"=\"39\"}", "{\"40\"=\"41\"}"));

      assertThat(errors.get(31)).contains(shouldBeEqualMessage("42", "43"));
      assertThat(errors.get(32)).contains(shouldBeEqualMessage("44", "45"));
      assertThat(errors.get(33)).contains(shouldBeEqualMessage("[46]", "[47]"));

      assertThat(errors.get(34)).contains(shouldBeEqualMessage("\"48\"", "\"49\""));

      assertThat(errors.get(35)).contains(shouldBeEqualMessage("50", "51"));
      assertThat(errors.get(36)).contains(shouldBeEqualMessage("[52]", "[53]"));
      assertThat(errors.get(37)).contains(format("%nExpecting message to be:%n"
                                                 + "  \"NullPointerException message\"%n"
                                                 + "but was:%n"
                                                 + "  \"IllegalArgumentException message\""));
      assertThat(errors.get(38)).contains(format("%nExpecting message to be:%n"
                                                 + "  \"something was good\"%n"
                                                 + "but was:%n"
                                                 + "  \"something was wrong\""));
      assertThat(errors.get(39)).contains(shouldBeEqualMessage("Optional[bad option]", "Optional[good option]"));
      assertThat(errors.get(40)).contains(shouldBeEqualMessage("2015-01-01 (java.time.LocalDate)",
                                                               "2015-01-02 (java.time.LocalDate)"));
      assertThat(errors.get(41)).contains(shouldBeEqualMessage("2015-01-01T23:59:59 (java.time.LocalDateTime)",
                                                               "2015-01-01T23:59 (java.time.LocalDateTime)")
                                          + format("%nwhen comparing values using '%s'",
                                                   ChronoLocalDateTimeComparator.getInstance()));
      assertThat(errors.get(42)).contains(shouldBeEqualMessage("2015-01-01T23:59:59Z (java.time.ZonedDateTime)",
                                                               "2015-01-01T23:59Z (java.time.ZonedDateTime)")
                                          + format("%nwhen comparing values using '%s'",
                                                   ChronoZonedDateTimeByInstantComparator.getInstance()));
      assertThat(errors.get(43)).contains(shouldBeEqualMessage("OptionalInt[0]", "1"));
      assertThat(errors.get(44)).contains(shouldBeEqualMessage("OptionalDouble[0.0]", "1.0"));
      assertThat(errors.get(45)).contains(shouldBeEqualMessage("OptionalLong[0]", "1L"));

      assertThat(errors.get(46)).contains(shouldBeEqualMessage("12:00", "13:00"));
      assertThat(errors.get(47)).contains(shouldBeEqualMessage("12:00Z", "13:00Z"));

      assertThat(errors.get(48)).contains(shouldBeEqualMessage(OffsetDateTime.MIN.toString() + " (java.time.OffsetDateTime)",
                                                               OffsetDateTime.MAX.toString() + " (java.time.OffsetDateTime)")
                                          + format("%nwhen comparing values using '%s'",
                                                   OffsetDateTimeByInstantComparator.getInstance()));
      return;
    }
    fail("Should not reach here");
  }

}
