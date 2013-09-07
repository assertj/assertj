/*
 * Created on Sep 1, 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2013 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Dates.parseDatetime;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.data.MapEntry;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Lists;
import org.junit.Test;

/**
 * Tests for <code>{@link SoftAssertions}</code>.
 * 
 * @author Brian Laframboise
 */
public class SoftAssertionsTest {

  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() {
    try {
      SoftAssertions softly = new SoftAssertions();

      softly.assertThat(BigDecimal.ZERO).isEqualTo(BigDecimal.ONE);

      softly.assertThat(Boolean.FALSE).isTrue();
      softly.assertThat(false).isTrue();
      softly.assertThat(new boolean[] { false }).isEqualTo(new boolean[] { true });

      softly.assertThat(new Byte((byte) 0)).isEqualTo((byte) 1);
      softly.assertThat((byte) 2).isEqualTo((byte) 3);
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

      softly.assertThat(new File("a")).isEqualTo(new File("b"));

      softly.assertThat(new Float(12f)).isEqualTo(new Float(13f));
      softly.assertThat(14f).isEqualTo(15f);
      softly.assertThat(new float[] { 16f }).isEqualTo(new float[] { 17f });

      softly.assertThat(new ByteArrayInputStream(new byte[] { (byte) 65 })).hasContentEqualTo(
          new ByteArrayInputStream(new byte[] { (byte) 66 }));

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

      softly.assertAll();
      fail("Should not reach here");
    } catch (SoftAssertionError e) {
      List<String> errors = e.getErrors();
      assertThat(errors).hasSize(37);
      assertThat(errors.get(0)).isEqualTo("expected:<[1]> but was:<[0]>");

      assertThat(errors.get(1)).isEqualTo("expected:<[tru]e> but was:<[fals]e>");
      assertThat(errors.get(2)).isEqualTo("expected:<[tru]e> but was:<[fals]e>");
      assertThat(errors.get(3)).isEqualTo("expected:<[[tru]e]> but was:<[[fals]e]>");

      assertThat(errors.get(4)).isEqualTo("expected:<[1]> but was:<[0]>");
      assertThat(errors.get(5)).isEqualTo("expected:<[3]> but was:<[2]>");
      assertThat(errors.get(6)).isEqualTo("expected:<[[5]]> but was:<[[4]]>");

      assertThat(errors.get(7)).isEqualTo("expected:<[B]> but was:<[A]>");
      assertThat(errors.get(8)).isEqualTo("expected:<[D]> but was:<[C]>");
      assertThat(errors.get(9)).isEqualTo("expected:<[[F]]> but was:<[[E]]>");

      assertThat(errors.get(10)).isEqualTo("expected:<[b]> but was:<[a]>");

      assertThat(errors.get(11)).isEqualTo("expected:<java.lang.[String]> but was:<java.lang.[Object]>");

      assertThat(errors.get(12)).isEqualTo("expected:<[2000-01-01T00:00:01]> but was:<[1999-12-31T23:59:59]>");

      assertThat(errors.get(13)).isEqualTo("expected:<[7].0> but was:<[6].0>");
      assertThat(errors.get(14)).isEqualTo("expected:<[9].0> but was:<[8].0>");
      assertThat(errors.get(15)).isEqualTo("expected:<[1[1].0]> but was:<[1[0].0]>");

      // can't check complete error message as it displays the file's path that depend on where you run the test.
      assertThat(errors.get(16)).containsSequence("expected:<...", "[b]> but was:<...", "[a]>");

      assertThat(errors.get(17)).isEqualTo("expected:<1[3].0f> but was:<1[2].0f>");
      assertThat(errors.get(18)).isEqualTo("expected:<1[5].0f> but was:<1[4].0f>");
      assertThat(errors.get(19)).isEqualTo("expected:<[1[7].0]> but was:<[1[6].0]>");

      assertThat(errors.get(20)).isEqualTo(
          "\nInputStreams do not have equal content:" + System.getProperty("line.separator")
              + "line:<0>, expected:<B> but was:<A>");

      assertThat(errors.get(21)).isEqualTo("expected:<2[1]> but was:<2[0]>");
      assertThat(errors.get(22)).isEqualTo("expected:<2[3]> but was:<2[2]>");
      assertThat(errors.get(23)).isEqualTo("expected:<[2[5]]> but was:<[2[4]]>");

      assertThat(errors.get(24)).isEqualTo("expected:<['2[7]']> but was:<['2[6]']>");
      assertThat(errors.get(25)).isEqualTo(
          "\nExpecting:\n <['28']>\nto contain:\n <['29']>\nbut could not find:\n <['29']>\n");
      assertThat(errors.get(26)).isEqualTo("expected:<['3[1]']> but was:<['3[0]']>");

      assertThat(errors.get(27)).isEqualTo("expected:<3[3]L> but was:<3[2]L>");
      assertThat(errors.get(28)).isEqualTo("expected:<3[5]L> but was:<3[4]L>");
      assertThat(errors.get(29)).isEqualTo("expected:<[3[7]]> but was:<[3[6]]>");

      assertThat(errors.get(30)).isEqualTo("expected:<{'[40'='41]'}> but was:<{'[38'='39]'}>");

      assertThat(errors.get(31)).isEqualTo("expected:<4[3]> but was:<4[2]>");
      assertThat(errors.get(32)).isEqualTo("expected:<4[5]> but was:<4[4]>");
      assertThat(errors.get(33)).isEqualTo("expected:<[4[7]]> but was:<[4[6]]>");

      assertThat(errors.get(34)).isEqualTo("expected:<'4[9]'> but was:<'4[8]'>");

      assertThat(errors.get(35)).isEqualTo("expected:<5[1]> but was:<5[0]>");
      assertThat(errors.get(36)).isEqualTo("expected:<[5[3]]> but was:<[5[2]]>");
    }
  }

}
