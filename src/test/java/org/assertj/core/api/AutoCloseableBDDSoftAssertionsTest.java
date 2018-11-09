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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.Lists.list;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.assertj.core.data.MapEntry;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

public class AutoCloseableBDDSoftAssertionsTest {

  @Test
  public void all_assertions_should_pass() {
    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then(1).isEqualTo(1);
      softly.then(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    }
  }

  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() {
    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {

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
            .overridingErrorMessage(format("%nExpecting:%n <File(a)>%nto be equal to:%n <File(b)>%nbut was not."))
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
      softly.then(list("28").iterator()).isExhausted();
      softly.then(list("30")).isEqualTo(Lists.newArrayList("31"));

      softly.then(new Long(32L)).isEqualTo(new Long(33L));
      softly.then(34L).isEqualTo(35L);
      softly.then(new long[] { 36L }).isEqualTo(new long[] { 37L });

      softly.then(mapOf(MapEntry.entry("38", "39"))).isEqualTo(mapOf(MapEntry.entry("40", "41")));

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

      softly.then(Optional.of("not empty")).isEqualTo("empty");
      softly.then(OptionalInt.of(0)).isEqualTo(1);
      softly.then(OptionalDouble.of(0.0)).isEqualTo(1.0);
      softly.then(OptionalLong.of(0L)).isEqualTo(1L);
      softly.then(LocalTime.of(12, 00)).isEqualTo(LocalTime.of(13, 00));
      softly.then(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC)).isEqualTo(OffsetTime.of(13, 0, 0, 0, ZoneOffset.UTC));
      softly.then(OffsetDateTime.MIN).isEqualTo(OffsetDateTime.MAX);

    } catch (MultipleFailuresError e) {
      List<String> errors = e.getFailures().stream().map(Object::toString).collect(toList());
      assertThat(errors).hasSize(45);

      assertThat(errors.get(0)).contains(format("%nExpecting:%n <1>%nto be equal to:%n <0>%nbut was not."));

      assertThat(errors.get(1)).contains(format("%nExpecting:%n <true>%nto be equal to:%n <false>%nbut was not."));
      assertThat(errors.get(2)).contains(format("%nExpecting:%n <true>%nto be equal to:%n <false>%nbut was not."));
      assertThat(errors.get(3)).contains(format("%nExpecting:%n <[true]>%nto be equal to:%n <[false]>%nbut was not."));

      assertThat(errors.get(4)).contains(format("%nExpecting:%n <1>%nto be equal to:%n <0>%nbut was not."));
      assertThat(errors.get(5)).contains(format("%nExpecting:%n <0x03>%nto be equal to:%n <0x02>%nbut was not."));
      assertThat(errors.get(6)).contains(format("%nExpecting:%n <[5]>%nto be equal to:%n <[4]>%nbut was not."));

      assertThat(errors.get(7)).contains(format("%nExpecting:%n <'B'>%nto be equal to:%n <'A'>%nbut was not."));
      assertThat(errors.get(8)).contains(format("%nExpecting:%n <'D'>%nto be equal to:%n <'C'>%nbut was not."));
      assertThat(errors.get(9)).contains(format("%nExpecting:%n <['F']>%nto be equal to:%n <['E']>%nbut was not."));

      assertThat(errors.get(10)).contains(format("%nExpecting:%n <b>%nto be equal to:%n <a>%nbut was not."));

      assertThat(errors.get(11)).contains(format("%nExpecting:%n <java.lang.String>%nto be equal to:%n <java.lang" +
        ".Object>%nbut was not."));

      assertThat(errors.get(12)).contains(format("%nExpecting:%n <2000-01-01T00:00:01.000>%nto be equal to:%n <1999-12-31T23:59:59.000>%nbut was not."));

      assertThat(errors.get(13)).contains(format("%nExpecting:%n <7.0>%nto be equal to:%n <6.0>%nbut was not."));
      assertThat(errors.get(14)).contains(format("%nExpecting:%n <9.0>%nto be equal to:%n <8.0>%nbut was not."));
      assertThat(errors.get(15)).contains(format("%nExpecting:%n <[11.0]>%nto be equal to:%n <[10.0]>%nbut was not."));

      assertThat(errors.get(16)).contains(format("%nExpecting:%n <File(a)>%nto be equal to:%n <File(b)>%nbut was not" +
        "."));

      assertThat(errors.get(17)).contains(format("%nExpecting:%n <13.0f>%nto be equal to:%n <12.0f>%nbut was not."));
      assertThat(errors.get(18)).contains(format("%nExpecting:%n <15.0f>%nto be equal to:%n <14.0f>%nbut was not."));
      assertThat(errors.get(19)).contains(format("%nExpecting:%n <[17.0f]>%nto be equal to:%n <[16.0f]>%nbut was not" +
        "."));

      assertThat(errors.get(20)).contains(format("%nInputStreams do not have same content:%n%n"
                                                 + "Changed content at line 1:%n"
                                                 + "expecting:%n"
                                                 + "  [\"B\"]%n"
                                                 + "but was:%n"
                                                 + "  [\"A\"]%n"));

      assertThat(errors.get(21)).contains(format("%nExpecting:%n <21>%nto be equal to:%n <20>%nbut was not."));
      assertThat(errors.get(22)).contains(format("%nExpecting:%n <23>%nto be equal to:%n <22>%nbut was not."));
      assertThat(errors.get(23)).contains(format("%nExpecting:%n <[25]>%nto be equal to:%n <[24]>%nbut was not."));

      assertThat(errors.get(24)).contains(format("%nExpecting:%n <[\"27\"]>%nto be equal to:%n <[\"26\"]>%nbut was " +
        "not."));
      assertThat(errors.get(25)).contains(format("Expecting the iterator under test to be exhausted"));
      assertThat(errors.get(26)).contains(format("%nExpecting:%n <[\"31\"]>%nto be equal to:%n <[\"30\"]>%nbut was " +
        "not."));

      assertThat(errors.get(27)).contains(format("%nExpecting:%n <33L>%nto be equal to:%n <32L>%nbut was not."));
      assertThat(errors.get(28)).contains(format("%nExpecting:%n <35L>%nto be equal to:%n <34L>%nbut was not."));
      assertThat(errors.get(29)).contains(format("%nExpecting:%n <[37L]>%nto be equal to:%n <[36L]>%nbut was not."));

      assertThat(errors.get(30)).contains(format("%nExpecting:%n <{\"40\"=\"41\"}>%nto be equal to:%n <{\"38\"=\"39\"}>%nbut was not."));

      assertThat(errors.get(31)).contains(format("%nExpecting:%n <43>%nto be equal to:%n <42>%nbut was not."));
      assertThat(errors.get(32)).contains(format("%nExpecting:%n <45>%nto be equal to:%n <44>%nbut was not."));
      assertThat(errors.get(33)).contains(format("%nExpecting:%n <[47]>%nto be equal to:%n <[46]>%nbut was not."));

      assertThat(errors.get(34)).contains(format("%nExpecting:%n <\"49\">%nto be equal to:%n <\"48\">%nbut was not."));

      assertThat(errors.get(35)).contains(format("%nExpecting:%n <51>%nto be equal to:%n <50>%nbut was not."));
      assertThat(errors.get(36)).contains(format("%nExpecting:%n <[53]>%nto be equal to:%n <[52]>%nbut was not."));
      assertThat(errors.get(37)).contains(format("%nExpecting message:%n"
                                                 + " <\"NullPointerException message\">%n"
                                                 + "but was:%n"
                                                 + " <\"IllegalArgumentException message\">"));

      assertThat(errors.get(38)).contains(format("%nExpecting:%n <\"empty\">%nto be equal to:%n <Optional[not empty]>%nbut was not."));
      assertThat(errors.get(39)).contains(format("%nExpecting:%n <1>%nto be equal to:%n <OptionalInt[0]>%nbut was not" +
        "."));
      assertThat(errors.get(40)).contains(format("%nExpecting:%n <1.0>%nto be equal to:%n <OptionalDouble[0.0]>%nbut was not."));
      assertThat(errors.get(41)).contains(format("%nExpecting:%n <1L>%nto be equal to:%n <OptionalLong[0]>%nbut was not."));

      assertThat(errors.get(42)).contains(format("%nExpecting:%n <13:00>%nto be equal to:%n <12:00>%nbut was not."));
      assertThat(errors.get(43)).contains(format("%nExpecting:%n <13:00Z>%nto be equal to:%n <12:00Z>%nbut was not."));
      assertThat(errors.get(44)).contains(format("%nExpecting:%n <+999999999-12-31T23:59:59.999999999-18:00>%nto be equal to:%n <-999999999-01-01T00:00+18:00>%nbut was not."));
      return;
    }
    fail("Should not reach here");
  }

}
