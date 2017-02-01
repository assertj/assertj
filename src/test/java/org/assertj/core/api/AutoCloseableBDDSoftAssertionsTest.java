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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.util.DateUtil.parseDatetime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.data.MapEntry;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Lists;
import org.junit.Test;

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
    } catch (SoftAssertionError e) {
      List<String> errors = e.getErrors();
      assertThat(errors).hasSize(38);
      assertThat(errors.get(0)).isEqualTo("expected:<[1]> but was:<[0]>");

      assertThat(errors.get(1)).isEqualTo("expected:<[tru]e> but was:<[fals]e>");
      assertThat(errors.get(2)).isEqualTo("expected:<[tru]e> but was:<[fals]e>");
      assertThat(errors.get(3)).isEqualTo("expected:<[[tru]e]> but was:<[[fals]e]>");

      assertThat(errors.get(4)).isEqualTo("expected:<[1]> but was:<[0]>");
      assertThat(errors.get(5)).isEqualTo("expected:<0x0[3]> but was:<0x0[2]>");
      assertThat(errors.get(6)).isEqualTo("expected:<[[5]]> but was:<[[4]]>");

      assertThat(errors.get(7)).isEqualTo("expected:<'[B]'> but was:<'[A]'>");
      assertThat(errors.get(8)).isEqualTo("expected:<'[D]'> but was:<'[C]'>");
      assertThat(errors.get(9)).isEqualTo("expected:<['[F]']> but was:<['[E]']>");

      assertThat(errors.get(10)).isEqualTo("expected:<[b]> but was:<[a]>");

      assertThat(errors.get(11)).isEqualTo("expected:<java.lang.[String]> but was:<java.lang.[Object]>");

      assertThat(errors.get(12)).isEqualTo("expected:<[2000-01-01T00:00:01].000> but was:<[1999-12-31T23:59:59].000>");

      assertThat(errors.get(13)).isEqualTo("expected:<[7].0> but was:<[6].0>");
      assertThat(errors.get(14)).isEqualTo("expected:<[9].0> but was:<[8].0>");
      assertThat(errors.get(15)).isEqualTo("expected:<[1[1].0]> but was:<[1[0].0]>");

      assertThat(errors.get(16)).isEqualTo("expected:<File(b)> but was:<File(a)>");

      assertThat(errors.get(17)).isEqualTo("expected:<1[3].0f> but was:<1[2].0f>");
      assertThat(errors.get(18)).isEqualTo("expected:<1[5].0f> but was:<1[4].0f>");
      assertThat(errors.get(19)).isEqualTo("expected:<[1[7].0f]> but was:<[1[6].0f]>");

      assertThat(errors.get(20)).isEqualTo(String.format("%nInputStreams do not have same content:%n%n"
                                                         + "Changed content at line 1:%n"
                                                         + "expecting:%n"
                                                         + "  [\"B\"]%n"
                                                         + "but was:%n"
                                                         + "  [\"A\"]%n"));

      assertThat(errors.get(21)).isEqualTo("expected:<2[1]> but was:<2[0]>");
      assertThat(errors.get(22)).isEqualTo("expected:<2[3]> but was:<2[2]>");
      assertThat(errors.get(23)).isEqualTo("expected:<[2[5]]> but was:<[2[4]]>");

      assertThat(errors.get(24)).isEqualTo("expected:<[\"2[7]\"]> but was:<[\"2[6]\"]>");
      assertThat(errors.get(25)).isEqualTo(String.format("%nExpecting:%n" +
                                                         " <[\"28\"]>%n" +
                                                         "to contain:%n" +
                                                         " <[\"29\"]>%n" +
                                                         "but could not find:%n" +
                                                         " <[\"29\"]>%n"));
      assertThat(errors.get(26)).isEqualTo("expected:<[\"3[1]\"]> but was:<[\"3[0]\"]>");

      assertThat(errors.get(27)).isEqualTo("expected:<3[3]L> but was:<3[2]L>");
      assertThat(errors.get(28)).isEqualTo("expected:<3[5]L> but was:<3[4]L>");
      assertThat(errors.get(29)).isEqualTo("expected:<[3[7]L]> but was:<[3[6]L]>");

      assertThat(errors.get(30)).isEqualTo("expected:<{\"[40\"=\"41]\"}> but was:<{\"[38\"=\"39]\"}>");

      assertThat(errors.get(31)).isEqualTo("expected:<4[3]> but was:<4[2]>");
      assertThat(errors.get(32)).isEqualTo("expected:<4[5]> but was:<4[4]>");
      assertThat(errors.get(33)).isEqualTo("expected:<[4[7]]> but was:<[4[6]]>");

      assertThat(errors.get(34)).isEqualTo("expected:<\"4[9]\"> but was:<\"4[8]\">");

      assertThat(errors.get(35)).isEqualTo("expected:<5[1]> but was:<5[0]>");
      assertThat(errors.get(36)).isEqualTo("expected:<[5[3]]> but was:<[5[2]]>");
      assertThat(errors.get(37)).isEqualTo(String.format("%nExpecting message:%n"
                                                         + " <\"NullPointerException message\">%n"
                                                         + "but was:%n"
                                                         + " <\"IllegalArgumentException message\">"));
      return;
    }
    fail("Should not reach here");
  }

}
