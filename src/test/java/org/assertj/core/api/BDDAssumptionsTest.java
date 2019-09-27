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
package org.assertj.core.api;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;

import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.assertj.core.api.BDDAssumptions.givenObject;

import java.io.ByteArrayInputStream​;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.willReturn;

/**
 * Tests for <code>{@link org.assertj.core.api.BDDAssumptions}</code>.
 *
 * @author Gonzalo Müller
 */
public class BDDAssumptionsTest {
  @Nested
  public class BDDAssumptions_given_boolean_Test {
    private final boolean actual = true;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isTrue())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isFalse())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Boolean_Test {
    private final Boolean actual = true;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isTrue())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isFalse())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_boolean_array_Test {
    private final boolean[] actual = { true, true };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(true))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains(false))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_byte_Test {
    private final byte actual = (byte) 1;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Byte_Test {
    private final Byte actual = (byte) 1;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_byte_array_Test {
    private final byte[] actual = { 1, 2 };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains((byte) 1))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains((byte) 0))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_short_Test {
    private final short actual = (short) 1;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Short_Test {
    private final Short actual = (short) 1;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_short_array_Test {
    private final short[] actual = { 1, 2 };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains((short) 1))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains((short) 0))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_int_Test {
    private final int actual = 1;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Integer_Test {
    private final Integer actual = 1;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_int_array_Test {
    private final int[] actual = { 1, 2 };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(1))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains(0))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_BigInteger_Test {
    private final BigInteger actual = BigInteger.valueOf(1L);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }


  @Nested
  public class BDDAssumptions_given_long_Test {
    private final long actual = 1L;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Long_Test {
    private final Long actual = 1L;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_long_array_Test {
    private final long[] actual = { 1, 2 };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(1L))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains(0L))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_float_Test {
    private final float actual = 1.0f;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Float_Test {
    private final Float actual = 1.0f;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_float_array_Test {
    private final float[] actual = { 1.0f, 2.0f };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(1.0f))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains(0.0f))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_double_Test {
    private final double actual = 1.0;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Double_Test {
    private final Double actual = 1.0;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_double_array_Test {
    private final double[] actual = { 1.0, 2.0 };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(1.0))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains(0.0f))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_BigDecimal_Test {
    private final BigDecimal actual = BigDecimal.valueOf(1.0);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isOne())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isZero())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_char_Test {
    private final char actual = 'A';

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isUpperCase())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isLowerCase())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Character_Test {
    private final Character actual = 'A';

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isUpperCase())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isLowerCase())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_char_array_Test {
    private final char[] actual = { 'A', 'B' };

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains('A'))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).contains('C'))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_CharSequence_Test {
    private final CharSequence actual = (CharSequence) "Yoda";

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isNotEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNullOrEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_String_Test {
    private final String actual = "Yoda";

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEqualTo("Yoda"))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNullOrEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_StringBuilder_Test {
    private final StringBuilder actual = new StringBuilder("Yoda");

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isNotEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNullOrEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_StringBuffer_Test {
    private final StringBuffer actual = new StringBuffer("Yoda");

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isNotEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNullOrEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Class_Test {
    private final Class<?> actual = Number.class;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isAssignableFrom(Long.class))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isInterface())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_generics_Test {
    private class Yoda {
      private int field = 1;
      public int getField() {
        return field;
      }
    }

    @Nested
    public class BDDAssumptions_given_T_Test {
      private final Yoda actual = new Yoda();

      @Test
      public void should_run_test_when_assumption_passes() {
        thenCode(() -> given(actual).hasNoNullFieldsOrProperties())
          .doesNotThrowAnyException();
      }

      @Test
      public void should_ignore_test_when_assumption_fails() {
        thenThrownBy(() -> given(actual).hasAllNullFieldsOrProperties())
          .isInstanceOf(AssumptionViolatedException.class);
      }
    }

    @Nested
    public class BDDAssumptions_given_array_T_Test {
      private final Yoda[] actual = { new Yoda(), new Yoda() };

      @Test
      public void should_run_test_when_assumption_passes() {
        thenCode(() -> given(actual).isNotEmpty())
          .doesNotThrowAnyException();
      }

      @Test
      public void should_ignore_test_when_assumption_fails() {
        thenThrownBy(() -> given(actual).isNullOrEmpty())
          .isInstanceOf(AssumptionViolatedException.class);
      }
    }

    @Nested
    public class BDDAssumptions_givenObject_Test {
      private final Yoda actual = new Yoda();

      @Test
      public void should_run_test_when_assumption_passes() {
        thenCode(() -> givenObject(actual).hasNoNullFieldsOrProperties())
          .doesNotThrowAnyException();
      }

      @Test
      public void should_ignore_test_when_assumption_fails() {
        thenThrownBy(() -> givenObject(actual).hasAllNullFieldsOrProperties())
          .isInstanceOf(AssumptionViolatedException.class);
      }
    }
  }

  @Nested
  public class BDDAssumptions_given_Comparable_Test {
    private class Yoda implements Comparable<Yoda> {
      @Override
      public int compareTo(Yoda to) {
        return 0;
      }
    }
    private final Yoda actual = new Yoda();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEqualByComparingTo(new Yoda()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEqualByComparingTo(new Yoda()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Throwable_Test {
    private final Throwable actual = new Exception("Yoda time");

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasMessage("Yoda time"))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasMessage(""))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_lambda_Test {

    @Nested
    public class BDDAssumptions_given_lambda__no_exception_required_Test {
      private final ThrowingCallable actual = () -> { /* some code */ };

      @Test
      public void should_run_test_when_assumption_passes() {
        thenCode(() -> given(actual).doesNotThrowAnyException())
          .doesNotThrowAnyException();
      }

      @Test
      public void should_ignore_test_when_assumption_fails() {
        thenThrownBy(() -> given(actual).hasMessage("Yoda time"))
          .isInstanceOf(AssumptionViolatedException.class);
      }
    }

    @Nested
    public class BDDAssumptions_given_lambda__exception_required_Test {
      private final ThrowingCallable actual = () -> {throw new Exception("Yoda time");};

      @Test
      public void should_run_test_when_assumption_passes() {
        thenCode(() -> given(actual).hasMessage("Yoda time"))
          .doesNotThrowAnyException();
      }

      @Test
      public void should_ignore_test_when_assumption_fails() {
        thenThrownBy(() -> given(actual).doesNotThrowAnyException())
          .isInstanceOf(AssumptionViolatedException.class);
      }
    }
  }

  @Nested
  public class BDDAssumptions_given_Iterable_Test {
    private final Iterable<Integer> actual = Arrays.asList(1, 2);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsOnly(2))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Iterator_Test {
    private final Iterator<Integer> actual = Arrays.asList(1, 2).iterator();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasNext())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isExhausted())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_List_Test {
    private final List<Integer> actual = Arrays.asList(1, 2);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsOnly(2))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Map_Test {
    private final Map<Integer, Integer> actual = Collections.singletonMap(1, 2);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).containsEntry(1, 2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsEntry(2, 1))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Predicate_Test {
    private final Predicate<Integer> actual = value -> value > 0;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).accepts(1, 2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).accepts(-2, -1))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_IntPredicate_Test {
    private final IntPredicate actual = value -> value > 0;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).accepts(1, 2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).accepts(-2, -1))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_LongPredicate_Test {
    private final LongPredicate actual = value -> value > 0;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).accepts(1, 2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).accepts(-2, -1))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_DoublePredicate_Test {
    private final DoublePredicate actual = value -> value > 0;

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).accepts(1.0, 2.0))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).accepts(-2.0, -1.0))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Optional_Test {
    private final Optional<?> actual = Optional.empty();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_OptionalInt_Test {
    private final OptionalInt actual = OptionalInt.empty();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_OptionalLong_Test {
    private final OptionalLong actual = OptionalLong.empty();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_OptionalDouble_Test {
    private final OptionalDouble actual = OptionalDouble.empty();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Stream_Test {
    private final Stream<Integer> actual = Stream.of(1, 2);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsOnly(2))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_IntStream_Test {
    private final IntStream actual = IntStream.of(1, 2);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(2))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsOnly(2))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_LongStream_Test {
    private final LongStream actual = LongStream.of(1L, 2L);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(2L))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsOnly(2L))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_DoubleStream_Test {
    private final DoubleStream actual = DoubleStream.of(1.0, 2.0);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).contains(2.0))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).containsOnly(2.0))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Future_Test {
    private final Future<Integer> actual = mock(Future.class);

    @BeforeEach
    public void beforeEach() {
      willReturn(true)
        .given(actual)
        .isCancelled();
    }

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isCancelled())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotCancelled())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_CompletableFuture_Test {
    private final CompletableFuture<Integer> actual = CompletableFuture.completedFuture​(1);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isDone())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotDone())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_CompletableStage_Test {
    private final CompletionStage<Integer> actual = CompletableFuture.completedFuture​(1);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isDone())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotDone())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicBoolean_Test {
    private final AtomicBoolean actual = new AtomicBoolean(true);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isTrue())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isFalse())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicInteger_Test {
    private final AtomicInteger actual = new AtomicInteger(1);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasNonNegativeValue())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasNegativeValue())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicIntegerArray_Test {
    private final AtomicIntegerArray actual = new AtomicIntegerArray(0);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicIntegerFieldUpdater_Test {
    private class Yoda {
      public volatile int field = 0;
    }
    private final AtomicIntegerFieldUpdater actual = AtomicIntegerFieldUpdater.newUpdater​(Yoda.class, "field");
    private final Yoda value = new Yoda();

    @BeforeEach
    public void beforeEach() {
      actual.set(value, 1);
    }

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasValue(1, value))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasValue(2, value))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicLong_Test {
    private final AtomicLong actual = new AtomicLong(1L);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasNonNegativeValue())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasNegativeValue())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicLongArray_Test {
    private final AtomicLongArray actual = new AtomicLongArray(0);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicLongFieldUpdater_Test {
    private class Yoda {
      public volatile long field = 0L;
    }
    private final AtomicLongFieldUpdater actual = AtomicLongFieldUpdater.newUpdater​(Yoda.class, "field");
    private final Yoda value = new Yoda();

    @BeforeEach
    public void beforeEach() {
      actual.set(value, 1L);
    }

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasValue(1L, value))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasValue(2L, value))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicReference_Test {
    private final AtomicReference actual = new AtomicReference("Yoda");

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasValue("Yoda"))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).doesNotHaveValue("Yoda"))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicReferenceArray_Test {
    private final AtomicReferenceArray actual = new AtomicReferenceArray(0);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isEmpty())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotEmpty())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicReferenceFieldUpdater_Test {
    private class Yoda {
      public volatile String field = "";
    }
    private final AtomicReferenceFieldUpdater actual = AtomicReferenceFieldUpdater.newUpdater​(Yoda.class, String.class, "field");
    private final Yoda value = new Yoda();

    @BeforeEach
    public void beforeEach() {
      actual.set(value, "Yoda");
    }

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasValue("Yoda", value))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasValue("", value))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicMarkableReference_Test {
    private final AtomicMarkableReference actual = new AtomicMarkableReference("Yoda", true);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isMarked())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isNotMarked())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_AtomicStampedReference_Test {
    private final AtomicStampedReference actual = new AtomicStampedReference("Yoda", 1);

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasStamp(1))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasStamp(0))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Date_Test {
    private final Date actual = Date.from(Instant.parse("2014-12-03T10:15:30Z"));

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBefore("2016-12-03T10:15:30Z"))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter("2016-12-03T10:15:30Z"))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_LocalDate_Test {
    private final LocalDate actual = LocalDate.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(LocalDate.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(LocalDate.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_LocalTime_Test {
    private final LocalTime actual = LocalTime.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(LocalTime.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(LocalTime.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_OffsetTime_Test {
    private final OffsetTime actual = OffsetTime.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(OffsetTime.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(OffsetTime.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_LocalDateTime_Test {
    private final LocalDateTime actual = LocalDateTime.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(LocalDateTime.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(LocalDateTime.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Instant_Test {
    private final Instant actual = Instant.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(Instant.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(Instant.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_OffsetDateTime_Test {
    private final OffsetDateTime actual = OffsetDateTime.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(OffsetDateTime.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(OffsetDateTime.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_ZonedDateTime_Test {
    private final ZonedDateTime actual = ZonedDateTime.now();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isBeforeOrEqualTo(ZonedDateTime.now()))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAfter(ZonedDateTime.now()))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_InputStream_Test {
    private final InputStream actual = new ByteArrayInputStream​("A".getBytes());

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).hasContent("A"))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).hasContent("B"))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_File_Test {
    private final File actual = new File("file.ext");

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isRelative())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAbsolute())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_Path_Test {
    private final Path actual = new File("file.ext").toPath();

    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(actual).isRelative())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(actual).isAbsolute())
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_URI_Test {
    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(new URI("http://assertj.org")).hasNoPort())
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(new URI("http://assertj.org")).hasPort(80))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }

  @Nested
  public class BDDAssumptions_given_URL_Test {
    @Test
    public void should_run_test_when_assumption_passes() {
      thenCode(() -> given(new URL("http://assertj.org")).hasProtocol("http"))
        .doesNotThrowAnyException();
    }

    @Test
    public void should_ignore_test_when_assumption_fails() {
      thenThrownBy(() -> given(new URL("http://assertj.org")).hasPort(80))
        .isInstanceOf(AssumptionViolatedException.class);
    }
  }
}
