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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;

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
      softly.assertThat((byte) 0).isEqualTo((byte) 1);
      softly.assertThat(new byte[] { 0 }).isEqualTo(new byte[] { 1 });

      softly.assertThat(new Character((char) 0)).isEqualTo(new Character((char) 1));
      softly.assertThat((char) 0).isEqualTo((char) 1);
      softly.assertThat(new char[] { 0 }).isEqualTo(new char[] { 1 });

      softly.assertThat(new StringBuilder("a")).isEqualTo(new StringBuilder("b"));

      softly.assertThat(Object.class).isEqualTo(String.class);

      softly.assertThat(new Date(0)).isEqualTo(new Date(1));

      softly.assertThat(new Double(0d)).isEqualTo(new Double(1d));
      softly.assertThat(0d).isEqualTo(1d);
      softly.assertThat(new double[] { 0d }).isEqualTo(new double[] { 1d });

      softly.assertThat(new File("a")).isEqualTo(new File("b"));

      softly.assertThat(new Float(0f)).isEqualTo(new Float(1f));
      softly.assertThat(0f).isEqualTo(1f);
      softly.assertThat(new float[] { 0f }).isEqualTo(new float[] { 1f });

      softly.assertThat(new ByteArrayInputStream(new byte[0])).isEqualTo(new ByteArrayInputStream(new byte[1]));

      softly.assertThat(new Integer(0)).isEqualTo(new Integer(1));
      softly.assertThat(0).isEqualTo(1);
      softly.assertThat(new int[] { 0 }).isEqualTo(new int[] { 1 });

      softly.assertThat((Iterable<String>) Lists.newArrayList("a")).isEqualTo(Lists.newArrayList("b"));
      softly.assertThat(Lists.newArrayList("a").iterator()).isEqualTo(Lists.newArrayList("b").iterator());
      softly.assertThat(Lists.newArrayList("a")).isEqualTo(Lists.newArrayList("b"));

      softly.assertThat(new Long(0L)).isEqualTo(new Long(1L));
      softly.assertThat(0L).isEqualTo(1L);
      softly.assertThat(new long[] { 0L }).isEqualTo(new long[] { 1L });

      softly.assertThat(Maps.mapOf(MapEntry.entry("a", "b"))).isEqualTo(Maps.mapOf(MapEntry.entry("c", "d")));

      softly.assertThat(new Short((short) 0)).isEqualTo(new Short((short) 1));
      softly.assertThat((short) 0).isEqualTo((short) 1);
      softly.assertThat(new short[] { (short) 0 }).isEqualTo(new short[] { (short) 1 });

      softly.assertThat("a").isEqualTo("b");

      softly.assertThat(new Object()).isEqualTo(new Object());
      softly.assertThat(new Object[] { new Object() }).isEqualTo(new Object[] { new Object() });

      softly.assertAll();
      org.junit.Assert.fail("Should not reach here");
    } catch (SoftAssertionError e) {
      assertEquals(37, e.getErrors().size());
    }
  }

}
