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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.data.TolkienCharacter.Race.ELF;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.data.TolkienCharacter.Race.MAN;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.Name.lastNameComparator;
import static org.assertj.core.test.Name.name;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.assertj.core.api.ClassAssertBaseTest.AnnotatedClass;
import org.assertj.core.api.ClassAssertBaseTest.AnotherAnnotation;
import org.assertj.core.api.ClassAssertBaseTest.MyAnnotation;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.api.test.ComparableExample;
import org.assertj.core.data.MapEntry;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacterAssert;
import org.assertj.core.test.Animal;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Name;
import org.assertj.core.test.Person;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.assertj.core.util.Lists;
import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

/**
 * Tests for <code>{@link SoftAssertions}</code>.
 *
 * @author Brian Laframboise
 */
@DisplayName("Soft assertions")
public class SoftAssertionsTest extends BaseAssertionsTest {

  private SoftAssertions softly;

  private CartoonCharacter homer;
  private CartoonCharacter fred;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter bart;

  private Map<String, Object> iterableMap;

  private static final ThrowingExtractor<Name, String, Exception> throwingFirstNameFunction = Name::getFirst;
  private static final ThrowingExtractor<Name, String, Exception> throwingLastNameFunction = Name::getLast;
  private static final Function<Name, String> firstNameFunction = Name::getFirst;
  private static final Function<Name, String> lastNameFunction = Name::getLast;

  private static final Function<? super CartoonCharacter, ? extends Collection<CartoonCharacter>> childrenExtractor = CartoonCharacter::getChildren;

  @BeforeEach
  public void setup() {
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(false);
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

    List<String> names = list("Dave", "Jeff");
    LinkedHashSet<String> jobs = newLinkedHashSet("Plumber", "Builder");
    Iterable<String> cities = list("Dover", "Boston", "Paris");
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
    assertThat(errors.get(0)).hasMessageStartingWith(format("%nExpecting map:%n"
                                                            + " <{\"54\"=\"55\"}>%n"
                                                            + "to contain:%n"
                                                            + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"
                                                            + "but could not find the following map entries:%n"
                                                            + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"));
    assertThat(errors.get(1)).hasMessageStartingWith(format("%nExpecting empty but was:<{\"54\"=\"55\"}>"));
  }

  @SuppressWarnings({ "unchecked", "deprecation" })
  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() throws MalformedURLException {
    try {
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
      softly.assertThat(list("28").iterator()).isExhausted();
      softly.assertThat(list("30")).isEqualTo(Lists.newArrayList("31"));

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

      softly.assertThat(LocalTime.of(12, 0)).isEqualTo(LocalTime.of(13, 0));
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
      softly.assertThat(URI.create("http://assertj.org:80").toURL()).hasNoPort();
      softly.assertThat(Duration.ofHours(10)).hasHours(5);
      softly.assertThat(Period.ofDays(1)).hasDays(2);

      softly.assertAll();
      fail("Should not reach here");
    } catch (MultipleFailuresError e) {
      List<String> errors = e.getFailures().stream().map(Object::toString).collect(toList());
      assertThat(errors).hasSize(55);
      assertThat(errors.get(0)).contains(format("%nExpecting:%n <0>%nto be equal to:%n <1>%nbut was not."));
      assertThat(errors.get(1)).contains(format("%nExpecting:%n <false>%nto be equal to:%n <true>%nbut was not."));
      assertThat(errors.get(2)).contains(format("%nExpecting:%n <false>%nto be equal to:%n <true>%nbut was not."));
      assertThat(errors.get(3)).contains(format("%nExpecting:%n <[false]>%nto be equal to:%n <[true]>%nbut was not."));

      assertThat(errors.get(4)).contains(format("%nExpecting:%n <0>%nto be equal to:%n <1>%nbut was not."));
      assertThat(errors.get(5)).contains(format("%nExpecting:%n <0x02>%nto be equal to:%n <0x03>%nbut was not."));
      assertThat(errors.get(6)).contains(format("%nExpecting:%n <[4]>%nto be equal to:%n <[5]>%nbut was not."));

      assertThat(errors.get(7)).contains(format("%nExpecting:%n <'A'>%nto be equal to:%n <'B'>%nbut was not."));
      assertThat(errors.get(8)).contains(format("%nExpecting:%n <'C'>%nto be equal to:%n <'D'>%nbut was not."));
      assertThat(errors.get(9)).contains(format("%nExpecting:%n <['E']>%nto be equal to:%n <['F']>%nbut was not."));

      assertThat(errors.get(10)).contains(format("%nExpecting:%n <a>%nto be equal to:%n <b>%nbut was not."));

      assertThat(errors.get(11)).contains(format("%nExpecting:%n <java.lang.Object>%nto be equal to:%n <java.lang.String>%nbut was not."));

      assertThat(errors.get(12)).contains(format("%nExpecting:%n <1999-12-31T23:59:59.000 (java.util.Date)>%nto be equal to:%n <2000-01-01T00:00:01.000 (java.util.Date)>%nbut was not."));

      assertThat(errors.get(13)).contains(format("%nExpecting:%n <6.0>%nto be equal to:%n <7.0>%nbut was not."));
      assertThat(errors.get(14)).contains(format("%nExpecting:%n <8.0>%nto be equal to:%n <9.0>%nbut was not."));
      assertThat(errors.get(15)).contains(format("%nExpecting:%n <[10.0]>%nto be equal to:%n <[11.0]>%nbut was not."));

      assertThat(errors.get(16)).contains(format("%nExpecting:%n <File(a)>%nto be equal to:%n <File(b)>%nbut was not."));

      assertThat(errors.get(17)).contains(format("%nExpecting:%n <12.0f>%nto be equal to:%n <13.0f>%nbut was not."));
      assertThat(errors.get(18)).contains(format("%nExpecting:%n <14.0f>%nto be equal to:%n <15.0f>%nbut was not."));
      assertThat(errors.get(19)).contains(format("%nExpecting:%n <[16.0f]>%nto be equal to:%n <[17.0f]>%nbut was not."));

      assertThat(errors.get(20)).contains(format("%nInputStreams do not have same content:%n%n"
                                                 + "Changed content at line 1:%n"
                                                 + "expecting:%n"
                                                 + "  [\"B\"]%n"
                                                 + "but was:%n"
                                                 + "  [\"A\"]%n"));

      assertThat(errors.get(21)).contains(format("%nExpecting:%n <20>%nto be equal to:%n <21>%nbut was not."));
      assertThat(errors.get(22)).contains(format("%nExpecting:%n <22>%nto be equal to:%n <23>%nbut was not."));
      assertThat(errors.get(23)).contains(format("%nExpecting:%n <[24]>%nto be equal to:%n <[25]>%nbut was not."));

      assertThat(errors.get(24)).contains(format("%nExpecting:%n <[\"26\"]>%nto be equal to:%n <[\"27\"]>%nbut was not."));
      assertThat(errors.get(25)).contains(format("Expecting the iterator under test to be exhausted"));
      assertThat(errors.get(26)).contains(format("%nExpecting:%n <[\"30\"]>%nto be equal to:%n <[\"31\"]>%nbut was not."));

      assertThat(errors.get(27)).contains(format("%nExpecting:%n <32L>%nto be equal to:%n <33L>%nbut was not."));
      assertThat(errors.get(28)).contains(format("%nExpecting:%n <34L>%nto be equal to:%n <35L>%nbut was not."));
      assertThat(errors.get(29)).contains(format("%nExpecting:%n <[36L]>%nto be equal to:%n <[37L]>%nbut was not."));

      assertThat(errors.get(30)).contains(format("%nExpecting:%n <{\"38\"=\"39\"}>%nto be equal to:%n <{\"40\"=\"41\"}>%nbut was not."));

      assertThat(errors.get(31)).contains(format("%nExpecting:%n <42>%nto be equal to:%n <43>%nbut was not."));
      assertThat(errors.get(31)).contains(format("%nExpecting:%n <42>%nto be equal to:%n <43>%nbut was not."));
      assertThat(errors.get(32)).contains(format("%nExpecting:%n <44>%nto be equal to:%n <45>%nbut was not."));
      assertThat(errors.get(33)).contains(format("%nExpecting:%n <[46]>%nto be equal to:%n <[47]>%nbut was not."));

      assertThat(errors.get(34)).contains(format("%nExpecting:%n <\"48\">%nto be equal to:%n <\"49\">%nbut was not."));

      assertThat(errors.get(35)).contains(format("%nExpecting:%n <50>%nto be equal to:%n <51>%nbut was not."));
      assertThat(errors.get(36)).contains(format("%nExpecting:%n <[52]>%nto be equal to:%n <[53]>%nbut was not."));
      assertThat(errors.get(37)).contains(format("%nExpecting message to be:%n"
                                                 + "  <\"NullPointerException message\">%n"
                                                 + "but was:%n"
                                                 + "  <\"IllegalArgumentException message\">"));
      assertThat(errors.get(38)).contains(format("%nExpecting message to be:%n"
                                                 + "  <\"something was good\">%n"
                                                 + "but was:%n"
                                                 + "  <\"something was wrong\">"));
      assertThat(errors.get(39)).contains(format("%nExpecting map:%n"
                                                 + " <{\"54\"=\"55\"}>%n"
                                                 + "to contain:%n"
                                                 + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"
                                                 + "but could not find the following map entries:%n"
                                                 + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"));

      assertThat(errors.get(40)).contains(format("%nExpecting:%n <12:00>%nto be equal to:%n <13:00>%nbut was not."));
      assertThat(errors.get(41)).contains(format("%nExpecting:%n <12:00Z>%nto be equal to:%n <13:00Z>%nbut was not."));

      assertThat(errors.get(42)).contains(format("%nExpecting:%n <Optional[not empty]>%nto be equal to:%n <\"empty\">%nbut was not."));
      assertThat(errors.get(43)).contains(format("%nExpecting:%n <OptionalInt[0]>%nto be equal to:%n <1>%nbut was not."));
      assertThat(errors.get(44)).contains(format("%nExpecting:%n <OptionalDouble[0.0]>%nto be equal to:%n <1.0>%nbut was not."));
      assertThat(errors.get(45)).contains(format("%nExpecting:%n <OptionalLong[0]>%nto be equal to:%n <1L>%nbut was not."));
      assertThat(errors.get(46)).contains("Expecting port of");
      assertThat(errors.get(47)).contains("to have failed");
      assertThat(errors.get(48)).contains(format("%nExpecting:%n  <given predicate>%n"
                                                 + "to accept <\"something else\"> but it did not."));

      assertThat(errors.get(49)).contains(format("%nExpecting:%n  <given predicate>%n"
                                                 + "to accept <2> but it did not."));

      assertThat(errors.get(50)).contains(format("%nExpecting:%n  <given predicate>%n"
                                                 + "to accept <2L> but it did not."));
      assertThat(errors.get(51)).contains(format("%nExpecting:%n  <given predicate>%n"
                                                 + "to accept <2.0> but it did not."));
      assertThat(errors.get(52)).contains(format("%nExpecting:%n"
                                                 + "  <http://assertj.org:80>%n"
                                                 + "not to have a port but had:%n"
                                                 + "  <80>"));
      assertThat(errors.get(53)).contains(format("%nExpecting Duration:%n"
                                                 + " <10H>%n"
                                                 + "to have 5L hours but had 10L"));
      assertThat(errors.get(54)).contains(format("%nExpecting Period:%n <P1D>%nto have 2 days but had 1"));
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_when_using_extracting_with_object() {
    // GIVEN
    Name name = name("John", "Doe");
    // WHEN
    softly.assertThat(name)
          .extracting("first", "last")
          .contains("John", "Doe");
    softly.assertThat(name)
          .extracting("first")
          .isEqualTo("John");
    softly.assertThat(name)
          .extracting("first", as(STRING))
          .startsWith("Jo");
    softly.assertThat(name)
          .extracting(Name::getFirst, Name::getLast)
          .contains("John", "Doe");
    softly.assertThat(name)
          .extracting(Name::getFirst)
          .isEqualTo("John");
    softly.assertThat(name)
          .extracting(Name::getFirst, as(STRING))
          .startsWith("Jo");
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
  }

  @Test
  public void should_pass_when_using_extracting_with_list() {
    // GIVEN
    List<Name> names = list(Name.name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .overridingErrorMessage("overridingErrorMessage with extracting(String)")
          .extracting("first")
          .contains("John")
          .contains("Jane")
          .contains("Foo1");

    softly.assertThat(names)
          .as("using extracting(Extractor)")
          .extracting(Name::getFirst)
          .contains("John")
          .contains("Jane")
          .contains("Foo2");

    softly.assertThat(names)
          .as("using extracting(..., Class)")
          .extracting("first", String.class)
          .contains("John")
          .contains("Jane")
          .contains("Foo3");

    softly.assertThat(names)
          .as("using extracting(...)")
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Jane", "Doe"))
          .contains(tuple("Foo", "4"));

    softly.assertThat(names)
          .as("using extractingResultOf(method, Class)")
          .extractingResultOf("getFirst", String.class)
          .contains("John")
          .contains("Jane")
          .contains("Foo5");

    softly.assertThat(names)
          .as("using extractingResultOf(method)")
          .extractingResultOf("getFirst")
          .contains("John")
          .contains("Jane")
          .contains("Foo6");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(6);
    assertThat(errorsCollected.get(0)).hasMessage("overridingErrorMessage with extracting(String)");
    assertThat(errorsCollected.get(1)).hasMessageContaining("Foo2");
    assertThat(errorsCollected.get(2)).hasMessageContaining("Foo3");
    assertThat(errorsCollected.get(3)).hasMessageContaining("Foo")
                                      .hasMessageContaining("4");
    assertThat(errorsCollected.get(4)).hasMessageContaining("Foo5");
    assertThat(errorsCollected.get(5)).hasMessageContaining("Foo6");
  }

  @Test
  public void should_pass_when_using_extracting_with_iterable() {

    Iterable<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));

    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      softly.assertThat(names)
            .as("using extracting()")
            .extracting("first")
            .contains("John")
            .contains("Jane");

      softly.assertThat(names)
            .as("using extracting(Extractor)")
            .extracting(Name::getFirst)
            .contains("John")
            .contains("Jane");

      softly.assertThat(names)
            .as("using extracting(..., Class)")
            .extracting("first", String.class)
            .contains("John")
            .contains("Jane");

      softly.assertThat(names)
            .as("using extracting(...)")
            .extracting("first", "last")
            .contains(tuple("John", "Doe"))
            .contains(tuple("Jane", "Doe"));

      softly.assertThat(names)
            .as("using extractingResultOf(method, Class)")
            .extractingResultOf("getFirst", String.class)
            .contains("John")
            .contains("Jane");

      softly.assertThat(names)
            .as("using extractingResultOf(method)")
            .extractingResultOf("getFirst")
            .contains("John")
            .contains("Jane");
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void should_pass_when_using_extracting_with_map() {
    // GIVEN
    Map<String, Object> map = mapOf(entry("name", "kawhi"), entry("age", 25));
    // WHEN
    softly.assertThat(map)
          .extractingByKeys("name", "age")
          .contains("kawhi", 25);
    softly.assertThat(map)
          .extractingByKey("name")
          .isEqualTo("kawhi");
    softly.assertThat(map)
          .extractingByKey("name", as(STRING))
          .startsWith("kaw");
    softly.assertThat(map)
          .extractingFromEntries(Map.Entry::getKey, Map.Entry::getValue)
          .contains(tuple("name", "kawhi"), tuple("age", 25));
    softly.assertThat(map)
          .extractingFromEntries(Map.Entry::getValue)
          .contains("kawhi", 25);
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
  }

  @Test
  public void should_work_when_using_extracting_with_array() {

    Name[] namesAsArray = { name("John", "Doe"), name("Jane", "Doe") };

    try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      softly.assertThat(namesAsArray)
            .as("using extracting()")
            .extracting("first")
            .contains("John")
            .contains("Jane");

      softly.assertThat(namesAsArray)
            .as("using extracting(Extractor)")
            .extracting(Name::getFirst)
            .contains("John")
            .contains("Jane");

      softly.assertThat(namesAsArray)
            .as("using extracting(..., Class)")
            .extracting("first", String.class)
            .contains("John")
            .contains("Jane");

      softly.assertThat(namesAsArray)
            .as("using extracting(...)")
            .extracting("first", "last")
            .contains(tuple("John", "Doe"))
            .contains(tuple("Jane", "Doe"));

      softly.assertThat(namesAsArray)
            .as("using extractingResultOf(method, Class)")
            .extractingResultOf("getFirst", String.class)
            .contains("John")
            .contains("Jane");

      softly.assertThat(namesAsArray)
            .as("using extractingResultOf(method)")
            .extractingResultOf("getFirst")
            .contains("John")
            .contains("Jane");
    }
  }

  @Test
  public void should_work_with_flat_extracting() {
    // GIVEN
    List<CartoonCharacter> characters = list(homer, fred);
    CartoonCharacter[] charactersAsArray = characters.toArray(new CartoonCharacter[0]);
    // WHEN
    softly.assertThat(characters)
          .as("using flatExtracting on Iterable")
          .overridingErrorMessage("error message")
          .flatExtracting(CartoonCharacter::getChildren)
          .containsAnyOf(homer, fred)
          .hasSize(123);
    softly.assertThat(charactersAsArray)
          .as("using flatExtracting on array")
          .overridingErrorMessage("error message")
          .flatExtracting(CartoonCharacter::getChildren)
          .containsAnyOf(homer, fred)
          .hasSize(456);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(4);
    assertThat(errorsCollected.get(0)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[using flatExtracting on array] error message");
    assertThat(errorsCollected.get(3)).hasMessage("[using flatExtracting on array] error message");
  }

  @Test
  public void should_work_with_flat_extracting_arrays() {
    // GIVEN
    CartoonCharacter[] charactersAsArray = array(homer, fred);
    // WHEN
    softly.assertThat(charactersAsArray)
          .as("using flatExtracting on array")
          .overridingErrorMessage("error message")
          .flatExtracting(CartoonCharacter::getChildren)
          .containsAnyOf(homer, fred)
          .hasSize(456);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessage("[using flatExtracting on array] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[using flatExtracting on array] error message");
  }

  @Test
  public void should_collect_all_errors_when_using_extracting() {
    // GIVEN
    List<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .overridingErrorMessage("error 1")
          .extracting("first")
          .contains("gandalf")
          .overridingErrorMessage("error 2")
          .contains("frodo");
    softly.assertThat(names)
          .overridingErrorMessage("error 3")
          .extracting("last")
          .isEmpty();
    // THEN
    assertThat(softly.errorsCollected()).extracting(Throwable::getMessage)
                                        .containsExactly("error 1", "error 2", "error 3");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_collect_all_errors_when_using_extracting_on_object() {
    // GIVEN
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    // WHEN
    softly.assertThat(frodo)
          .overridingErrorMessage("error 1")
          .extracting("name")
          .isEqualTo("Foo");
    softly.assertThat(frodo)
          .overridingErrorMessage("error 2")
          .extracting("name", "age")
          .contains("Frodo", 55);
    softly.assertThat(frodo)
          .overridingErrorMessage("error 3")
          .extracting("name", as(STRING))
          .startsWith("Bar");
    softly.assertThat(frodo)
          .overridingErrorMessage("error 4")
          .extracting(TolkienCharacter::getName,
                      character -> character.age,
                      character -> character.getRace())
          .containsExactly("Frodon", 33, HOBBIT);
    softly.assertThat(frodo)
          .overridingErrorMessage("error 5")
          .extracting(TolkienCharacter::getName)
          .isEqualTo("Foo");
    softly.assertThat(frodo)
          .overridingErrorMessage("error 6")
          .extracting(TolkienCharacter::getName, as(STRING))
          .startsWith("Bar");
    // THEN
    assertThat(softly.errorsCollected()).extracting(Throwable::getMessage)
                                        .containsExactly("error 1", "error 2", "error 3", "error 4", "error 5", "error 6");
  }

  @Test
  public void should_collect_all_errors_when_using_flat_extracting() {
    // GIVEN
    List<CartoonCharacter> characters = list(homer, fred);
    // WHEN
    softly.assertThat(characters)
          .overridingErrorMessage("error 1")
          .flatExtracting(CartoonCharacter::getChildren)
          .hasSize(0)
          .overridingErrorMessage("error 2")
          .isEmpty();
    // THEN
    assertThat(softly.errorsCollected()).extracting(Throwable::getMessage)
                                        .containsExactly("error 1", "error 2");
  }

  @Test
  public void should_collect_all_errors_when_using_filtering() {
    // GIVEN
    LinkedHashSet<CartoonCharacter> dads = newLinkedHashSet(homer, fred);
    // WHEN
    softly.assertThat(dads)
          .filteredOn("name", "Homer Simpson")
          .hasSize(10)
          .isEmpty();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageStartingWith(format("%nExpected size:<10> but was:<1> in:%n<[CartoonCharacter [name=Homer Simpson]]>"));
    assertThat(errorsCollected.get(1)).hasMessageStartingWith(format("%nExpecting empty but was:<[CartoonCharacter [name=Homer Simpson]]>"));
  }

  @Test
  public void should_collect_all_errors_when_using_predicate_filtering() {
    // GIVEN
    LinkedHashSet<CartoonCharacter> dads = newLinkedHashSet(homer, fred);
    // WHEN
    softly.assertThat(dads)
          .filteredOn(cartoonCharacter -> cartoonCharacter.getName().equals("Homer Simpson"))
          .hasSize(10)
          .isEmpty();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageStartingWith(format("%nExpected size:<10> but was:<1> in:%n<[CartoonCharacter [name=Homer Simpson]]>"));
    assertThat(errorsCollected.get(1)).hasMessageStartingWith(format("%nExpecting empty but was:<[CartoonCharacter [name=Homer Simpson]]>"));
  }

  @Test
  public void should_work_with_comparable() {
    // GIVEN
    ComparableExample example1 = new ComparableExample(0);
    ComparableExample example2 = new ComparableExample(0);
    ComparableExample example3 = new ComparableExample(123);
    // WHEN
    softly.assertThat(example1).isEqualByComparingTo(example2);
    softly.assertThat(example1).isEqualByComparingTo(example3);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("123");
  }

  @Test
  public void should_work_with_stream() {
    // WHEN
    softly.assertThat(Stream.of("a", "b", "c")).contains("a", "foo");
    softly.assertThat(IntStream.of(1, 2, 3)).as("IntStream").contains(2, 4, 6);
    softly.assertThat(LongStream.of(1, 2, 3)).contains(-1L, -2L, -3L);
    softly.assertThat(DoubleStream.of(1, 2, 3)).contains(10.0, 20.0, 30.0);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(4);
    assertThat(errorsCollected.get(0)).hasMessageContaining("foo");
    assertThat(errorsCollected.get(1)).hasMessageContaining("6")
                                      .hasMessageContaining("IntStream");
    assertThat(errorsCollected.get(2)).hasMessageContaining("-3");
    assertThat(errorsCollected.get(3)).hasMessageContaining("30.0");
  }

  @Test
  public void should_work_with_CompletionStage() {
    // GIVEN
    CompletionStage<String> completionStage = completedFuture("done");
    // WHEN
    softly.assertThat(completionStage).isDone();
    softly.assertThat(completionStage).hasNotFailed();
    softly.assertThat(completionStage).isCancelled();
    completionStage = null;
    softly.assertThat(completionStage).isNull();
    // THEN
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("cancelled");
  }

  @Test
  public void should_not_collect_AssertionError_from_CompletableFuture_succeedsWithin() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(10, MILLISECONDS));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  public void should_only_collect_error_from_chained_assertions_performed_after_succeedsWithin() {
    // GIVEN
    CompletableFuture<String> completableFuture = completedFuture("done");
    // WHEN
    softly.assertThat(completableFuture)
          .succeedsWithin(10, TimeUnit.MILLISECONDS)
          .isEqualTo("not done")
          .isEqualTo("not ok");
    // THEN
    assertThat(softly.errorsCollected()).hasSize(2);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("not done");
    assertThat(softly.errorsCollected().get(1)).hasMessageContaining("not ok");
  }

  @Test
  public void should_work_with_predicate() {
    // GIVEN
    Predicate<String> lowercasePredicate = s -> s.equals(s.toLowerCase());
    // WHEN
    softly.assertThat(lowercasePredicate).as("abc").accepts("a", "b", "c");
    softly.assertThat(lowercasePredicate).as("abC").accepts("a", "b", "C");
    // THEN
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("abC")
                                               .hasMessageContaining("C");
  }

  @Test
  void should_work_with_optional() {
    // GIVEN
    Optional<String> optional = Optional.of("Gandalf");
    // WHEN
    softly.assertThat(optional)
          .contains("Gandalf");
    softly.assertThat(optional)
          .map(String::length)
          .contains(7);
    softly.assertThat(optional)
          .get()
          .isEqualTo("Gandalf");
    softly.assertThat(optional)
          .get(as(STRING))
          .startsWith("Gan");
    // THEN
    softly.assertAll();
  }

  @Test
  void should_propagate_AssertionError_from_nested_proxied_calls() {
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).first();
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).first(as(STRING));
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).element(0);
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).element(0, as(STRING));
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).last();
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).last(as(STRING));
    // the nested proxied call to assertHasSize() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).singleElement();
    // the nested proxied call to assertHasSize() throw an Assertion error that must be propagated to the caller.
    softly.assertThat(emptyList()).singleElement(as(STRING));
    // nested proxied call to throwAssertionError when checking that is optional is present
    softly.assertThat(Optional.empty()).contains("Foo");
    // nested proxied call to isNotNull
    softly.assertThat((Predicate<String>) null).accepts("a", "b", "c");
    // nested proxied call to isCompleted
    softly.assertThat(new CompletableFuture<String>()).isCompletedWithValue("done");
    // nested proxied call to isEqualTo
    softly.assertThat(Duration.ofDays(1)).isZero();
    // nested proxied call to isLessThan
    softly.assertThat(Duration.ofDays(1)).isNegative();
    // nested proxied call to isGreaterThan
    softly.assertThat(Duration.ofDays(-1)).isPositive();
    // it must be caught by softly.assertAll()
    assertThat(softly.errorsCollected()).hasSize(14);
  }

  @Test
  public void check_477_bugfix() {
    // GIVEN
    TolkienCharacter frodo = TolkienCharacter.of("frodo", 33, HOBBIT);
    TolkienCharacter samnullGamgee = null;
    TolkienSoftAssertions softly = new TolkienSoftAssertions();
    // WHEN
    softly.assertThat(frodo).hasAge(10); // Expect failure - age will be 0 due to not being initialized.
    softly.assertThat(samnullGamgee).hasAge(11); // Expect failure - argument is null.
    // THEN
    assertThat(softly.errorsCollected()).hasSize(2);
  }

  public static class TolkienSoftAssertions extends SoftAssertions {

    public TolkienCharacterAssert assertThat(TolkienCharacter actual) {
      return proxy(TolkienCharacterAssert.class, TolkienCharacter.class, actual);
    }
  }

  @Test
  public void should_return_failure_after_fail() {
    // GIVEN
    String failureMessage = "Should not reach here";
    // WHEN
    softly.fail(failureMessage);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageStartingWith(failureMessage);
  }

  @Test
  public void should_return_failure_after_fail_with_parameters() {
    // GIVEN
    String failureMessage = "Should not reach %s or %s";
    // WHEN
    softly.fail(failureMessage, "here", "here");
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageStartingWith("Should not reach here or here");
  }

  @Test
  public void should_return_failure_after_fail_with_throwable() {
    // GIVEN
    String failureMessage = "Should not reach here";
    IllegalStateException realCause = new IllegalStateException();
    // WHEN
    softly.fail(failureMessage, realCause);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageStartingWith(failureMessage);
    assertThat(errorsCollected.get(0).getCause()).isEqualTo(realCause);
  }

  @Test
  public void should_return_failure_after_shouldHaveThrown() {
    // WHEN
    softly.shouldHaveThrown(IllegalArgumentException.class);
    // THEN
    assertThat(softly.wasSuccess()).isFalse();
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageStartingWith("IllegalArgumentException should have been thrown");
  }

  @Test
  public void should_assert_using_assertSoftly() {
    assertThatThrownBy(() -> assertSoftly(assertions -> {
      assertions.assertThat(true).isFalse();
      assertions.assertThat(42).isEqualTo("meaning of life");
      assertions.assertThat("red").isEqualTo("blue");
    })).as("it should call assertAll() and fail with multiple validation errors")
       .hasMessageContaining("meaning of life")
       .hasMessageContaining("blue");
  }

  @Test
  public void should_work_with_atomic() {
    // WHEN
    // simple atomic value
    softly.assertThat(new AtomicBoolean(true)).isTrue().isFalse();
    softly.assertThat(new AtomicInteger(1)).hasValueGreaterThan(0).hasNegativeValue();
    softly.assertThat(new AtomicLong(1L)).hasValueGreaterThan(0L).hasNegativeValue();
    softly.assertThat(new AtomicReference<>("abc")).hasValue("abc").hasValue("def");
    // atomic array value
    softly.assertThat(new AtomicIntegerArray(new int[] { 1, 2, 3 })).containsExactly(1, 2, 3).isEmpty();
    softly.assertThat(new AtomicLongArray(new long[] { 1L, 2L, 3L })).containsExactly(1L, 2L, 3L).contains(0);
    softly.assertThat(new AtomicReferenceArray<>(array("a", "b", "c"))).containsExactly("a", "b", "c").contains("123");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(7);
    assertThat(errorsCollected.get(0)).hasMessageContaining("false");
    assertThat(errorsCollected.get(1)).hasMessageContaining("0");
    assertThat(errorsCollected.get(2)).hasMessageContaining("0L");
    assertThat(errorsCollected.get(3)).hasMessageContaining("def");
    assertThat(errorsCollected.get(4)).hasMessageContaining("empty");
    assertThat(errorsCollected.get(5)).hasMessageContaining("0");
    assertThat(errorsCollected.get(6)).hasMessageContaining("123");
  }

  @Test
  public void should_fix_bug_1146() {
    // GIVEN
    Map<String, String> numbers = mapOf(entry("one", "1"),
                                        entry("two", "2"),
                                        entry("three", "3"));
    // THEN
    try (final AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
      softly.assertThat(numbers)
            .extracting("one", "two")
            .containsExactly("1", "2");
      softly.assertThat(numbers)
            .extracting("one")
            .isEqualTo("1");
    }
  }

  @Test
  public void iterable_soft_assertions_should_work_with_navigation_methods() {
    // GIVEN
    Iterable<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .as("size isGreaterThan(10)")
          .overridingErrorMessage("error message")
          .size()
          .isGreaterThan(10);
    softly.assertThat(names)
          .as("size isGreaterThan(22)")
          .overridingErrorMessage("error message")
          .size()
          .isGreaterThan(22)
          .returnToIterable()
          .as("should not be empty") // TODO returnToIterable() does not yet propagate assertion info
          .overridingErrorMessage("error message 2")
          .isEmpty();
    softly.assertThat(names)
          .as("first element")
          .overridingErrorMessage("error message")
          .first()
          .isNull();
    softly.assertThat(names)
          .as("first element as Name")
          .overridingErrorMessage("error message")
          .first(as(type(Name.class)))
          .isNull();
    softly.assertThat(names)
          .as("element(0)")
          .overridingErrorMessage("error message")
          .element(0)
          .isNull();
    softly.assertThat(names)
          .as("element(0) as Name")
          .overridingErrorMessage("error message")
          .element(0, as(type(Name.class)))
          .isNull();
    softly.assertThat(names)
          .as("last element")
          .overridingErrorMessage("error message")
          .last()
          .isNull();
    softly.assertThat(names)
          .as("last element as Name")
          .overridingErrorMessage("error message")
          .last(as(type(Name.class)))
          .isNull();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(9);
    assertThat(errorsCollected.get(0)).hasMessage("[size isGreaterThan(10)] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[size isGreaterThan(22)] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[should not be empty] error message 2");
    assertThat(errorsCollected.get(3)).hasMessage("[first element] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[first element as Name] error message");
    assertThat(errorsCollected.get(5)).hasMessage("[element(0)] error message");
    assertThat(errorsCollected.get(6)).hasMessage("[element(0) as Name] error message");
    assertThat(errorsCollected.get(7)).hasMessage("[last element] error message");
    assertThat(errorsCollected.get(8)).hasMessage("[last element as Name] error message");
  }

  @Test
  public void list_soft_assertions_should_work_with_navigation_methods() {
    // GIVEN
    List<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .as("size isGreaterThan(10)")
          .overridingErrorMessage("error message")
          .size()
          .isGreaterThan(10);
    softly.assertThat(names)
          .as("size isGreaterThan(22)")
          .overridingErrorMessage("error message")
          .size()
          .isGreaterThan(22)
          .returnToIterable()
          .as("shoud not be empty") // TODO returnToIterable() does not yet propagate assertion info
          .overridingErrorMessage("error message 2")
          .isEmpty();
    softly.assertThat(names)
          .as("first element")
          .overridingErrorMessage("error message")
          .first()
          .isNull();
    softly.assertThat(names)
          .as("first element as Name")
          .overridingErrorMessage("error message")
          .first(as(type(Name.class)))
          .isNull();
    softly.assertThat(names)
          .as("element(0)")
          .overridingErrorMessage("error message")
          .element(0)
          .isNull();
    softly.assertThat(names)
          .as("element(0) as Name")
          .overridingErrorMessage("error message")
          .element(0, as(type(Name.class)))
          .isNull();
    softly.assertThat(names)
          .as("last element")
          .overridingErrorMessage("error message")
          .last()
          .isNull();
    softly.assertThat(names)
          .as("last element as Name")
          .overridingErrorMessage("error message")
          .last(as(type(Name.class)))
          .isNull();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(9);
    assertThat(errorsCollected.get(0)).hasMessage("[size isGreaterThan(10)] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[size isGreaterThan(22)] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[shoud not be empty] error message 2");
    assertThat(errorsCollected.get(3)).hasMessage("[first element] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[first element as Name] error message");
    assertThat(errorsCollected.get(5)).hasMessage("[element(0)] error message");
    assertThat(errorsCollected.get(6)).hasMessage("[element(0) as Name] error message");
    assertThat(errorsCollected.get(7)).hasMessage("[last element] error message");
    assertThat(errorsCollected.get(8)).hasMessage("[last element as Name] error message");
  }

  @Test
  public void iterable_soft_assertions_should_work_with_singleElement_navigation() {
    // GIVEN
    Iterable<Name> names = list(name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .as("single element")
          .singleElement()
          .isNotNull();
    softly.assertThat(names)
          .as("single element")
          .overridingErrorMessage("error message")
          .singleElement()
          .isNull();
    softly.assertThat(names)
          .as("single element as Name")
          .overridingErrorMessage("error message")
          .singleElement(as(type(Name.class)))
          .isNull();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessage("[single element] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[single element as Name] error message");
  }

  @Test
  public void list_soft_assertions_should_work_with_singleElement_navigation() {
    // GIVEN
    List<Name> names = list(name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .as("single element")
          .singleElement()
          .isNotNull();
    softly.assertThat(names)
          .as("single element")
          .overridingErrorMessage("error message")
          .singleElement()
          .isNull();
    softly.assertThat(names)
          .as("single element as Name")
          .overridingErrorMessage("error message")
          .singleElement(as(type(Name.class)))
          .isNull();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessage("[single element] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[single element as Name] error message");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void iterable_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Iterable<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    Iterable<CartoonCharacter> characters = list(homer, fred);
    // WHEN
    softly.assertThat(names)
          .as("extracting(throwingFirstNameFunction)")
          .overridingErrorMessage("error message")
          .extracting(throwingFirstNameFunction)
          .contains("gandalf")
          .contains("frodo");
    softly.assertThat(names)
          .as("extracting(\"last\")")
          .overridingErrorMessage("error message")
          .extracting("last")
          .containsExactly("foo", "bar");
    softly.assertThat(characters)
          .as("using flatExtracting on Iterable")
          .overridingErrorMessage("error message")
          .flatExtracting(childrenExtractor)
          .hasSize(1)
          .containsAnyOf(homer, fred);
    softly.assertThat(characters)
          .as("using flatExtracting on Iterable with exception")
          .overridingErrorMessage("error message")
          .flatExtracting(CartoonCharacter::getChildrenWithException)
          .containsExactlyInAnyOrder(homer, fred);
    softly.assertThat(characters)
          .containsOnly(bart);
    softly.assertThat(characters)
          .containsOnlyOnce(maggie, bart);
    softly.assertThat(characters)
          .containsSequence(homer, bart);
    softly.assertThat(characters)
          .containsSubsequence(homer, maggie);
    softly.assertThat(characters)
          .doesNotContain(homer, maggie);
    softly.assertThat(characters)
          .doesNotContainSequence(fred);
    softly.assertThat(characters)
          .doesNotContainSubsequence(homer, fred);
    softly.assertThat(characters)
          .isSubsetOf(homer, bart);
    softly.assertThat(characters)
          .startsWith(fred);
    softly.assertThat(characters)
          .endsWith(bart);
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(firstNameFunction, lastNameFunction)")
          .extracting(firstNameFunction, lastNameFunction)
          .contains(tuple("John", "Doe"))
          .contains(tuple("Frodo", "Baggins"));
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(\"first\", \"last\")")
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Bilbo", "Baggins"));
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(firstNameFunction)")
          .extracting(firstNameFunction)
          .contains("John")
          .contains("sam");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(\"first\", String.class)")
          .extracting("first", String.class)
          .contains("John")
          .contains("Aragorn");
    softly.assertThat(names)
          .as("filteredOn(name -> name.first.startsWith(\"Jo\"))")
          .overridingErrorMessage("error message")
          .filteredOn(name -> name.first.startsWith("Jo"))
          .hasSize(123);
    softly.assertThat(names)
          .as("name.first.startsWith(\"Jo\")")
          .overridingErrorMessage("error message")
          .filteredOn(name -> name.first.startsWith("Jo"))
          .extracting(firstNameFunction)
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("flatExtracting with multiple Extractors")
          .flatExtracting(firstNameFunction, lastNameFunction)
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("flatExtracting with multiple ThrowingExtractors")
          .flatExtracting(throwingFirstNameFunction, throwingLastNameFunction)
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extractingResultOf(\"getFirst\")")
          .extractingResultOf("getFirst")
          .contains("John", "Jane")
          .contains("Sam", "Aragorn");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extractingResultOf(\"getFirst\", String.class)")
          .extractingResultOf("getFirst", String.class)
          .contains("John", "Jane")
          .contains("Messi", "Ronaldo");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("filteredOn with condition")
          .filteredOn(new Condition<>(name -> name.first.startsWith("Jo"), "startsWith Jo"))
          .hasSize(5);
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("filteredOn firstName in {John, Frodo}")
          .filteredOn("first", in("John", "Frodo"))
          .isEmpty();
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("filteredOn firstName = John")
          .filteredOn("first", "John")
          .isEmpty();
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("filteredOn firstName = null")
          .filteredOnNull("first")
          .isNotEmpty();
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("using flatExtracting(String... fieldOrPropertyNames)")
          .flatExtracting("first", "last")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.assertThat(characters)
          .as("using flatExtracting(String fieldOrPropertyName)")
          .overridingErrorMessage("error message")
          .flatExtracting("children")
          .contains(bart, maggie)
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .filteredOnAssertions(name -> assertThat(name.first).startsWith("Jo"))
          .as("filteredOn with consumer")
          .hasSize(5);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(33);
    assertThat(errorsCollected.get(0)).hasMessage("[extracting(throwingFirstNameFunction)] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[extracting(throwingFirstNameFunction)] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[extracting(\"last\")] error message");
    assertThat(errorsCollected.get(3)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(5)).hasMessage("[using flatExtracting on Iterable with exception] error message");
    assertThat(errorsCollected.get(6)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(7)).hasMessageContaining(maggie.toString());
    assertThat(errorsCollected.get(8)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(9)).hasMessageContaining(maggie.toString());
    assertThat(errorsCollected.get(10)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(11)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(12)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(13)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(14)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(15)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(16)).hasMessage("[extracting(firstNameFunction, lastNameFunction)] error message");
    assertThat(errorsCollected.get(17)).hasMessage("[extracting(\"first\", \"last\")] error message");
    assertThat(errorsCollected.get(18)).hasMessage("[extracting(firstNameFunction)] error message");
    assertThat(errorsCollected.get(19)).hasMessage("[extracting(\"first\", String.class)] error message");
    assertThat(errorsCollected.get(20)).hasMessage("[filteredOn(name -> name.first.startsWith(\"Jo\"))] error message");
    assertThat(errorsCollected.get(21)).hasMessage("[name.first.startsWith(\"Jo\")] error message");
    assertThat(errorsCollected.get(22)).hasMessage("[flatExtracting with multiple Extractors] error message");
    assertThat(errorsCollected.get(23)).hasMessage("[flatExtracting with multiple ThrowingExtractors] error message");
    assertThat(errorsCollected.get(24)).hasMessage("[extractingResultOf(\"getFirst\")] error message");
    assertThat(errorsCollected.get(25)).hasMessage("[extractingResultOf(\"getFirst\", String.class)] error message");
    assertThat(errorsCollected.get(26)).hasMessage("[filteredOn with condition] error message");
    assertThat(errorsCollected.get(27)).hasMessage("[filteredOn firstName in {John, Frodo}] error message");
    assertThat(errorsCollected.get(28)).hasMessage("[filteredOn firstName = John] error message");
    assertThat(errorsCollected.get(29)).hasMessage("[filteredOn firstName = null] error message");
    assertThat(errorsCollected.get(30)).hasMessage("[using flatExtracting(String... fieldOrPropertyNames)] error message");
    assertThat(errorsCollected.get(31)).hasMessage("[using flatExtracting(String fieldOrPropertyName)] error message");
    assertThat(errorsCollected.get(32)).hasMessage("[filteredOn with consumer] error message");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void list_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    List<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    List<CartoonCharacter> characters = list(homer, fred);
    // WHEN
    softly.assertThat(names)
          .as("extracting(throwingFirstNameFunction)")
          .overridingErrorMessage("error message")
          .extracting(throwingFirstNameFunction)
          .contains("gandalf")
          .contains("frodo");
    softly.assertThat(names)
          .as("extracting(\"last\")")
          .overridingErrorMessage("error message")
          .extracting("last")
          .containsExactly("foo", "bar");
    softly.assertThat(characters)
          .as("using flatExtracting on Iterable")
          .overridingErrorMessage("error message")
          .flatExtracting(childrenExtractor)
          .hasSize(1)
          .containsAnyOf(homer, fred);
    softly.assertThat(characters)
          .as("using flatExtracting on Iterable with exception")
          .overridingErrorMessage("error message")
          .flatExtracting(CartoonCharacter::getChildrenWithException)
          .containsExactlyInAnyOrder(homer, fred);
    softly.assertThat(characters)
          .containsOnly(bart);
    softly.assertThat(characters)
          .containsOnlyOnce(maggie, bart);
    softly.assertThat(characters)
          .containsSequence(homer, bart);
    softly.assertThat(characters)
          .containsSubsequence(homer, maggie);
    softly.assertThat(characters)
          .doesNotContain(homer, maggie);
    softly.assertThat(characters)
          .doesNotContainSequence(fred);
    softly.assertThat(characters)
          .doesNotContainSubsequence(homer, fred);
    softly.assertThat(characters)
          .isSubsetOf(homer, bart);
    softly.assertThat(characters)
          .startsWith(fred);
    softly.assertThat(characters)
          .endsWith(bart);
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(firstNameFunction, lastNameFunction)")
          .extracting(firstNameFunction, lastNameFunction)
          .contains(tuple("John", "Doe"))
          .contains(tuple("Frodo", "Baggins"));
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(\"first\", \"last\")")
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Bilbo", "Baggins"));
    softly.assertThat(names)
          .as("extracting(firstNameFunction)")
          .overridingErrorMessage("error message")
          .extracting(firstNameFunction)
          .contains("John")
          .contains("sam");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(\"first\", String.class)")
          .extracting("first", String.class)
          .contains("John")
          .contains("Aragorn");
    softly.assertThat(names)
          .as("filteredOn(name -> name.first.startsWith(\"Jo\"))")
          .overridingErrorMessage("error message")
          .filteredOn(name -> name.first.startsWith("Jo"))
          .hasSize(123);
    softly.assertThat(names)
          .as("name.first.startsWith(\"Jo\")")
          .overridingErrorMessage("error message")
          .filteredOn(name -> name.first.startsWith("Jo"))
          .extracting(firstNameFunction)
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("flatExtracting with multiple Extractors")
          .flatExtracting(firstNameFunction, lastNameFunction)
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("flatExtracting with multiple ThrowingExtractors")
          .flatExtracting(throwingFirstNameFunction, throwingLastNameFunction)
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extractingResultOf(\"getFirst\")")
          .extractingResultOf("getFirst")
          .contains("John", "Jane")
          .contains("Sam", "Aragorn");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extractingResultOf(\"getFirst\", String.class)")
          .extractingResultOf("getFirst", String.class)
          .contains("John", "Jane")
          .contains("Messi", "Ronaldo");
    softly.assertThat(names)
          .as("filteredOn with condition")
          .overridingErrorMessage("error message")
          .filteredOn(new Condition<>(name -> name.first.startsWith("Jo"), "startsWith Jo"))
          .hasSize(5);
    softly.assertThat(names)
          .as("filteredOn firstName in {John, Frodo}")
          .overridingErrorMessage("error message")
          .filteredOn("first", in("John", "Frodo"))
          .isEmpty();
    softly.assertThat(names)
          .as("filteredOn firstName = John")
          .overridingErrorMessage("error message")
          .filteredOn("first", "John")
          .isEmpty();
    softly.assertThat(names)
          .as("filteredOn firstName = null")
          .overridingErrorMessage("error message")
          .filteredOnNull("first")
          .isNotEmpty();
    softly.assertThat(names)
          .as("using flatExtracting(String... fieldOrPropertyNames)")
          .overridingErrorMessage("error message")
          .flatExtracting("first", "last")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.assertThat(characters)
          .as("using flatExtracting(String fieldOrPropertyName)")
          .overridingErrorMessage("error message")
          .flatExtracting("children")
          .contains(bart, maggie)
          .contains("Sauron");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .filteredOnAssertions(name -> assertThat(name.first).startsWith("Jo"))
          .as("filteredOn with consumer")
          .hasSize(5);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(33);
    assertThat(errorsCollected.get(0)).hasMessage("[extracting(throwingFirstNameFunction)] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[extracting(throwingFirstNameFunction)] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[extracting(\"last\")] error message");
    assertThat(errorsCollected.get(3)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(5)).hasMessage("[using flatExtracting on Iterable with exception] error message");
    assertThat(errorsCollected.get(6)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(7)).hasMessageContaining(maggie.toString());
    assertThat(errorsCollected.get(8)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(9)).hasMessageContaining(maggie.toString());
    assertThat(errorsCollected.get(10)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(11)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(12)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(13)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(14)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(15)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(16)).hasMessage("[extracting(firstNameFunction, lastNameFunction)] error message");
    assertThat(errorsCollected.get(17)).hasMessage("[extracting(\"first\", \"last\")] error message");
    assertThat(errorsCollected.get(18)).hasMessage("[extracting(firstNameFunction)] error message");
    assertThat(errorsCollected.get(19)).hasMessage("[extracting(\"first\", String.class)] error message");
    assertThat(errorsCollected.get(20)).hasMessage("[filteredOn(name -> name.first.startsWith(\"Jo\"))] error message");
    assertThat(errorsCollected.get(21)).hasMessage("[name.first.startsWith(\"Jo\")] error message");
    assertThat(errorsCollected.get(22)).hasMessage("[flatExtracting with multiple Extractors] error message");
    assertThat(errorsCollected.get(23)).hasMessage("[flatExtracting with multiple ThrowingExtractors] error message");
    assertThat(errorsCollected.get(24)).hasMessage("[extractingResultOf(\"getFirst\")] error message");
    assertThat(errorsCollected.get(25)).hasMessage("[extractingResultOf(\"getFirst\", String.class)] error message");
    assertThat(errorsCollected.get(26)).hasMessage("[filteredOn with condition] error message");
    assertThat(errorsCollected.get(27)).hasMessage("[filteredOn firstName in {John, Frodo}] error message");
    assertThat(errorsCollected.get(28)).hasMessage("[filteredOn firstName = John] error message");
    assertThat(errorsCollected.get(29)).hasMessage("[filteredOn firstName = null] error message");
    assertThat(errorsCollected.get(30)).hasMessage("[using flatExtracting(String... fieldOrPropertyNames)] error message");
    assertThat(errorsCollected.get(31)).hasMessage("[using flatExtracting(String fieldOrPropertyName)] error message");
    assertThat(errorsCollected.get(32)).hasMessage("[filteredOn with consumer] error message");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void object_array_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Name[] names = array(name("John", "Doe"), name("Jane", "Doe"));
    CartoonCharacter[] characters = array(homer, fred);
    // WHEN
    softly.assertThat(names)
          .as("extracting(Name::getFirst)")
          .overridingErrorMessage("error message")
          .extracting(Name::getFirst)
          .contains("gandalf")
          .contains("frodo");
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(\"last\")")
          .extracting("last")
          .containsExactly("foo", "bar");
    softly.assertThat(characters)
          .as("using flatExtracting on Iterable")
          .overridingErrorMessage("error message")
          .flatExtracting(CartoonCharacter::getChildren)
          .hasSize(1)
          .containsAnyOf(homer, fred);
    softly.assertThat(characters)
          .overridingErrorMessage("error message")
          .as("using flatExtracting on Iterable with exception")
          .flatExtracting(CartoonCharacter::getChildrenWithException)
          .containsExactlyInAnyOrder(homer, fred);
    softly.assertThat(characters)
          .containsOnly(bart);
    softly.assertThat(characters)
          .containsOnlyOnce(maggie, bart);
    softly.assertThat(characters)
          .containsSequence(homer, bart);
    softly.assertThat(characters)
          .containsSubsequence(homer, maggie);
    softly.assertThat(characters)
          .doesNotContain(homer, maggie);
    softly.assertThat(characters)
          .doesNotContainSequence(fred);
    softly.assertThat(characters)
          .doesNotContainSubsequence(homer, fred);
    softly.assertThat(characters)
          .isSubsetOf(homer, bart);
    softly.assertThat(characters)
          .startsWith(fred);
    softly.assertThat(characters)
          .endsWith(bart);
    softly.assertThat(names)
          .as("extracting(Name::getFirst, Name::getLast)")
          .overridingErrorMessage("error message")
          .extracting(Name::getFirst, Name::getLast)
          .contains(tuple("John", "Doe"))
          .contains(tuple("Frodo", "Baggins"));
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(\"first\", \"last\")")
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Bilbo", "Baggins"));
    softly.assertThat(names)
          .overridingErrorMessage("error message")
          .as("extracting(firstNameFunction)")
          .extracting(firstNameFunction)
          .contains("John")
          .contains("sam");
    softly.assertThat(names)
          .as("extracting(\"first\", String.class)")
          .overridingErrorMessage("error message")
          .extracting("first", String.class)
          .contains("John")
          .contains("Aragorn");
    softly.assertThat(names)
          .as("filteredOn(name -> name.first.startsWith(\"Jo\"))")
          .overridingErrorMessage("error message")
          .filteredOn(name -> name.first.startsWith("Jo"))
          .hasSize(123);
    softly.assertThat(names)
          .as("filteredOn + extracting")
          .overridingErrorMessage("error message")
          .filteredOn(name -> name.first.startsWith("Jo"))
          .extracting(firstNameFunction)
          .contains("Sauron");
    softly.assertThat(names)
          .as("extractingResultOf(\"getFirst\")")
          .overridingErrorMessage("error message")
          .extractingResultOf("getFirst")
          .contains("John", "Jane")
          .contains("Sam", "Aragorn");
    softly.assertThat(names)
          .as("extractingResultOf(\"getFirst\", String.class)")
          .overridingErrorMessage("error message")
          .extractingResultOf("getFirst", String.class)
          .contains("John", "Jane")
          .contains("Messi", "Ronaldo");
    softly.assertThat(names)
          .as("filteredOn with condition")
          .overridingErrorMessage("error message")
          .filteredOn(new Condition<>(name -> name.first.startsWith("Jo"), "startsWith Jo"))
          .hasSize(5);
    softly.assertThat(names)
          .as("filteredOn firstName in {John, Frodo}")
          .overridingErrorMessage("error message")
          .filteredOn("first", in("John", "Frodo"))
          .isEmpty();
    softly.assertThat(names)
          .as("filteredOn firstName = John")
          .overridingErrorMessage("error message")
          .filteredOn("first", "John")
          .isEmpty();
    softly.assertThat(names)
          .as("filteredOn firstName = null")
          .overridingErrorMessage("error message")
          .filteredOnNull("first")
          .isNotEmpty();
    softly.assertThat(characters)
          .as("using flatExtracting(String fieldOrPropertyName)")
          .overridingErrorMessage("error message")
          .flatExtracting("children")
          .contains(bart, maggie)
          .contains("Sauron");
    softly.assertThat(names)
          .filteredOnAssertions(name -> assertThat(name.first).startsWith("Jo"))
          .as("filteredOn with consumer")
          .hasSize(5);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(30);
    assertThat(errorsCollected.get(0)).hasMessage("[extracting(Name::getFirst)] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[extracting(Name::getFirst)] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[extracting(\"last\")] error message")
                                      .hasMessage("[extracting(\"last\")] error message");
    assertThat(errorsCollected.get(3)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[using flatExtracting on Iterable] error message");
    assertThat(errorsCollected.get(5)).hasMessage("[using flatExtracting on Iterable with exception] error message");
    assertThat(errorsCollected.get(6)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(7)).hasMessageContaining(maggie.toString());
    assertThat(errorsCollected.get(8)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(9)).hasMessageContaining(maggie.toString());
    assertThat(errorsCollected.get(10)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(11)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(12)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(13)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(14)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(15)).hasMessageContaining(bart.toString());
    assertThat(errorsCollected.get(16)).hasMessage("[extracting(Name::getFirst, Name::getLast)] error message");
    assertThat(errorsCollected.get(17)).hasMessage("[extracting(\"first\", \"last\")] error message");
    assertThat(errorsCollected.get(18)).hasMessage("[extracting(firstNameFunction)] error message");
    assertThat(errorsCollected.get(19)).hasMessage("[extracting(\"first\", String.class)] error message");
    assertThat(errorsCollected.get(20)).hasMessage("[filteredOn(name -> name.first.startsWith(\"Jo\"))] error message");
    assertThat(errorsCollected.get(21)).hasMessage("[filteredOn + extracting] error message");
    assertThat(errorsCollected.get(22)).hasMessage("[extractingResultOf(\"getFirst\")] error message");
    assertThat(errorsCollected.get(23)).hasMessage("[extractingResultOf(\"getFirst\", String.class)] error message");
    assertThat(errorsCollected.get(24)).hasMessage("[filteredOn with condition] error message");
    assertThat(errorsCollected.get(25)).hasMessage("[filteredOn firstName in {John, Frodo}] error message");
    assertThat(errorsCollected.get(26)).hasMessage("[filteredOn firstName = John] error message");
    assertThat(errorsCollected.get(27)).hasMessage("[filteredOn firstName = null] error message");
    assertThat(errorsCollected.get(28)).hasMessage("[using flatExtracting(String fieldOrPropertyName)] error message");
    assertThat(errorsCollected.get(29)).hasMessageContaining("filteredOn with consumer");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void class_soft_assertions_should_report_errors_on_final_methods() {
    // GIVEN
    Class<AnnotatedClass> actual = AnnotatedClass.class;
    // WHEN
    softly.assertThat(actual)
          .hasAnnotations(MyAnnotation.class, AnotherAnnotation.class)
          .hasAnnotations(SafeVarargs.class, VisibleForTesting.class);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("SafeVarargs")
                                      .hasMessageContaining("VisibleForTesting");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void object_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Name name = name("John", "Doe");
    Object alphabet = "abcdefghijklmnopqrstuvwxyz";
    Object vowels = list("a", "e", "i", "o", "u");
    // WHEN
    softly.assertThat(name)
          .as("extracting(\"first\", \"last\")")
          .overridingErrorMessage("error message")
          .extracting("first", "last")
          .contains("John")
          .contains("gandalf");
    softly.assertThat(name)
          .as("extracting(Name::getFirst, Name::getLast)")
          .overridingErrorMessage("error message")
          .extracting(Name::getFirst, Name::getLast)
          .contains("John")
          .contains("frodo");
    softly.assertThat(alphabet)
          .overridingErrorMessage("error message")
          .as("asString()")
          .asString()
          .startsWith("abc")
          .startsWith("123");
    softly.assertThat(vowels)
          .as("asList()")
          .overridingErrorMessage("error message")
          .asList()
          .startsWith("a", "e")
          .startsWith("1", "2");
    softly.assertThat(name)
          .as("extracting(Name::getFirst)")
          .overridingErrorMessage("error message")
          .extracting(Name::getFirst)
          .isEqualTo("Jack");
    softly.assertThat(name)
          .as("extracting(first)")
          .overridingErrorMessage("error message")
          .extracting("first")
          .isEqualTo("Jack");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(6);
    assertThat(errorsCollected.get(0)).hasMessage("[extracting(\"first\", \"last\")] error message");
    assertThat(errorsCollected.get(1)).hasMessage("[extracting(Name::getFirst, Name::getLast)] error message");
    assertThat(errorsCollected.get(2)).hasMessage("[asString()] error message");
    assertThat(errorsCollected.get(3)).hasMessage("[asList()] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[extracting(Name::getFirst)] error message");
    assertThat(errorsCollected.get(5)).hasMessage("[extracting(first)] error message");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void map_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Map<String, String> map = mapOf(entry("a", "1"), entry("b", "2"), entry("c", "3"));
    // WHEN
    softly.assertThat(map).contains(entry("abc", "ABC"), entry("def", "DEF")).isEmpty();
    softly.assertThat(map).containsAnyOf(entry("gh", "GH"), entry("ij", "IJ"));
    softly.assertThat(map).containsExactly(entry("kl", "KL"), entry("mn", "MN"));
    softly.assertThat(map).containsKeys("K1", "K2");
    softly.assertThat(map).containsOnly(entry("op", "OP"), entry("qr", "QR"));
    softly.assertThat(map).containsOnlyKeys("K3", "K4");
    softly.assertThat(map).containsValues("V1", "V2");
    softly.assertThat(map).doesNotContain(entry("a", "1"), entry("abc", "ABC"));
    softly.assertThat(map).doesNotContainKeys("a", "b");
    softly.assertThat(map)
          .as("extracting(\"a\", \"b\")")
          .overridingErrorMessage("error message")
          .extracting("a", "b")
          .contains("456");
    softly.assertThat(iterableMap)
          .as("flatExtracting(\"name\", \"job\", \"city\", \"rank\")")
          .overridingErrorMessage("error message")
          .flatExtracting("name", "job", "city", "rank")
          .contains("Unexpected", "Builder", "Dover", "Boston", "Paris", 1, 2, 3);
    softly.assertThat(map)
          .as("size()")
          .overridingErrorMessage("error message")
          .size()
          .isGreaterThan(1000);
    softly.assertThat(map).containsExactlyEntriesOf(mapOf(entry("kl", "KL"), entry("mn", "MN")));
    softly.assertThat(map).containsExactlyInAnyOrderEntriesOf(mapOf(entry("a", "1"), entry("b", "2")));
    softly.assertThat(map)
          .as("extracting(\"a\")")
          .overridingErrorMessage("error message")
          .extractingByKey("a")
          .isEqualTo("456");
    softly.assertThat(map)
          .as("extracting(\"a\") as string")
          .overridingErrorMessage("error message")
          .extractingByKey("a", as(STRING))
          .startsWith("456");
    // THEN
    List<Throwable> errors = softly.errorsCollected();
    assertThat(errors).hasSize(17);
    assertThat(errors.get(0)).hasMessageContaining("MapEntry[key=\"abc\", value=\"ABC\"]");
    assertThat(errors.get(1)).hasMessageContaining("empty");
    assertThat(errors.get(2)).hasMessageContaining("gh")
                             .hasMessageContaining("IJ");
    assertThat(errors.get(3)).hasMessageContaining("\"a\"=\"1\"");
    assertThat(errors.get(4)).hasMessageContaining("K2");
    assertThat(errors.get(5)).hasMessageContaining("OP");
    assertThat(errors.get(6)).hasMessageContaining("K4");
    assertThat(errors.get(7)).hasMessageContaining("V2");
    assertThat(errors.get(8)).hasMessageContaining("ABC");
    assertThat(errors.get(9)).hasMessageContaining("b");
    assertThat(errors.get(10)).hasMessage("[extracting(\"a\", \"b\")] error message");
    assertThat(errors.get(11)).hasMessage("[flatExtracting(\"name\", \"job\", \"city\", \"rank\")] error message");
    assertThat(errors.get(12)).hasMessage("[size()] error message");
    assertThat(errors.get(13)).hasMessageContaining("\"a\"=\"1\"");
    assertThat(errors.get(14)).hasMessageContaining("to contain only");
    assertThat(errors.get(15)).hasMessage("[extracting(\"a\")] error message");
    assertThat(errors.get(16)).hasMessage("[extracting(\"a\") as string] error message");
  }

  @Test
  public void map_soft_assertions_should_work_with_navigation_methods() {
    // GIVEN
    Map<String, String> map = mapOf(entry("a", "1"), entry("b", "2"), entry("c", "3"));
    // WHEN
    softly.assertThat(map)
          .as("navigate to size")
          .overridingErrorMessage("error message")
          .size()
          .isGreaterThan(10);
    softly.assertThat(map)
          .size()
          .isGreaterThan(1)
          // .as("returnToMap") TODO not yet supported
          .returnToMap()
          .as("returnToMap")
          .isEmpty();
    softly.assertThat(map)
          .size()
          .isGreaterThan(1)
          .returnToMap()
          .containsKey("nope")
          .as("check size after navigating back")
          .size()
          .isLessThan(2);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(4);
    assertThat(errorsCollected.get(0)).hasMessageContaining("[navigate to size] error message");
    assertThat(errorsCollected.get(1)).hasMessageContaining("returnToMap");
    assertThat(errorsCollected.get(2)).hasMessageContaining("nope");
    assertThat(errorsCollected.get(3)).hasMessageContaining("check size after navigating back");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void predicate_soft_assertions_should_report_errors_on_final_methods() {
    // GIVEN
    Predicate<MapEntry<String, String>> ballSportPredicate = sport -> sport.value.contains("ball");
    // WHEN
    softly.assertThat(ballSportPredicate)
          .accepts(entry("sport", "boxing"), entry("sport", "marathon"))
          .rejects(entry("sport", "football"), entry("sport", "basketball"));
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageContaining("boxing");
    assertThat(errorsCollected.get(1)).hasMessageContaining("basketball");
  }

  @Test
  void optional_soft_assertions_should_report_errors_on_methods_that_switch_the_object_under_test() {
    // GIVEN
    Optional<String> optional = Optional.of("Yoda");
    Function<String, Optional<String>> upperCaseOptional = s -> s == null ? Optional.empty() : Optional.of(s.toUpperCase());
    // WHEN
    softly.assertThat(optional)
          .as("map(String::length)")
          .overridingErrorMessage("error message")
          .map(String::length)
          .hasValue(4)
          .hasValue(777); // fail
    softly.assertThat(optional)
          .as("flatMap(upperCaseOptional)")
          .flatMap(upperCaseOptional)
          .contains("YODA")
          .contains("yoda") // fail
          .as("map(String::length) after flatMap(upperCaseOptional)")
          .map(String::length)
          .hasValue(4)
          .hasValue(888); // fail
    softly.assertThat(optional)
          .as("get()")
          .overridingErrorMessage("error message")
          .get()
          .isEqualTo("Yoda")
          .isEqualTo("Luke"); // fail
    softly.assertThat(optional)
          .as("get(as(STRING))")
          .overridingErrorMessage("error message")
          .get(as(STRING))
          .startsWith("Yo")
          .startsWith("Lu"); // fail
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(5);
    assertThat(errorsCollected.get(0)).hasMessage("[map(String::length)] error message");
    assertThat(errorsCollected.get(1)).hasMessageContaining("flatMap(upperCaseOptional)")
                                      .hasMessageContaining("yoda");
    assertThat(errorsCollected.get(2)).hasMessageContaining("map(String::length) after flatMap(upperCaseOptional)")
                                      .hasMessageContaining("888");
    assertThat(errorsCollected.get(3)).hasMessage("[get()] error message");
    assertThat(errorsCollected.get(4)).hasMessage("[get(as(STRING))] error message");
  }

  @Test
  void string_soft_assertions_should_report_errors_on_methods_that_switch_the_object_under_test() {
    // GIVEN
    String base64String = "QXNzZXJ0Sg==";
    // WHEN
    softly.assertThat(base64String)
          .as("decodedAsBase64()")
          .overridingErrorMessage("error message 1")
          .decodedAsBase64()
          .isEmpty();
    // THEN
    then(softly.errorsCollected()).extracting(Throwable::getMessage)
                                  .containsExactly("[decodedAsBase64()] error message 1");
  }

  @Test
  void byte_array_soft_assertions_should_report_errors_on_methods_that_switch_the_object_under_test() {
    // GIVEN
    byte[] byteArray = "AssertJ".getBytes();
    // WHEN
    softly.assertThat(byteArray)
          .as("encodedAsBase64()")
          .overridingErrorMessage("error message 1")
          .encodedAsBase64()
          .isEmpty();
    // THEN
    then(softly.errorsCollected()).extracting(Throwable::getMessage)
                                  .containsExactly("[encodedAsBase64()] error message 1");
  }

  @Test
  void should_work_with_string() {
    // GIVEN
    String base64String = "QXNzZXJ0Sg==";
    // WHEN
    softly.assertThat(base64String)
          .decodedAsBase64()
          .containsExactly("AssertJ".getBytes());
    // THEN
    softly.assertAll();
  }

  @Test
  void should_work_with_byte_array() {
    // GIVEN
    byte[] byteArray = "AssertJ".getBytes();
    // WHEN
    softly.assertThat(byteArray)
          .encodedAsBase64()
          .isEqualTo("QXNzZXJ0Sg==");
    // THEN
    softly.assertAll();
  }

  @Test
  public void soft_assertions_should_work_with_zipSatisfy() {
    // GIVEN
    List<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .as("zipSatisfy")
          .overridingErrorMessage("error message")
          .zipSatisfy(names, (n1, n2) -> softly.assertThat(n1).isNotEqualTo(n2));
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessage("[zipSatisfy] error message");
  }

  @Test
  public void bug_1209() {
    // GIVEN
    String string = "%%E";
    // WHEN
    softly.assertThat(string).matches("fffff");
    // THEN
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("%%E")
                                      .hasMessageContaining("to match pattern")
                                      .hasMessageContaining("fffff");
  }

  @Test
  public void should_keep_representation_after_changing_the_object_under_test() {
    // GIVEN
    List<Name> names = list(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.assertThat(names)
          .as("unicode")
          .withRepresentation(UNICODE_REPRESENTATION)
          .extracting(throwingFirstNameFunction)
          .contains("ó");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("unicode")
                                      .hasMessageContaining("\\u00f3");
  }

  @Test
  public void should_keep_registered_comparators_after_changing_the_iterable_under_test() {
    // GIVEN
    Iterable<Name> names = list(name("Manu", "Ginobili"), name("Magic", "Johnson"));
    // WHEN
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOn(string -> string.startsWith("Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOn(new Condition<>(string -> string.startsWith("Ma"), "starts with Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOnAssertions(string -> assertThat(string).startsWith("Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .usingElementComparator(lastNameComparator)
          .filteredOn("first", "Manu")
          .containsExactly(name("Whoever", "Ginobili"));
    softly.assertThat(names)
          .usingElementComparator(lastNameComparator)
          .filteredOn("first", not("Manu"))
          .containsExactly(name("Whoever", "Johnson"));
    softly.assertThat(array(name("John", null), name("Jane", "Doe")))
          .usingElementComparator(alwaysEqual())
          .filteredOnNull("last")
          .hasSize(1)
          .contains(name("Can be", "anybody"));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
  }

  @Test
  public void should_keep_registered_comparators_after_changing_the_list_under_test() {
    // GIVEN
    List<Name> names = list(name("Manu", "Ginobili"), name("Magic", "Johnson"));
    // WHEN
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOn(string -> string.startsWith("Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOn(new Condition<>(string -> string.startsWith("Ma"), "starts with Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOnAssertions(string -> assertThat(string).startsWith("Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .usingElementComparator(lastNameComparator)
          .filteredOn("first", "Manu")
          .containsExactly(name("Whoever", "Ginobili"));
    softly.assertThat(names)
          .usingElementComparator(lastNameComparator)
          .filteredOn("first", not("Manu"))
          .containsExactly(name("Whoever", "Johnson"));
    softly.assertThat(array(name("John", null), name("Jane", "Doe")))
          .usingElementComparator(alwaysEqual())
          .filteredOnNull("last")
          .hasSize(1)
          .contains(name("Can be", "anybody"));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
  }

  @Test
  public void should_keep_registered_comparators_after_changing_the_object_array_under_test() {
    // GIVEN
    Name[] names = array(name("Manu", "Ginobili"), name("Magic", "Johnson"));
    // WHEN
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOn(string -> string.startsWith("Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOn(new Condition<>(string -> string.startsWith("Ma"), "starts with Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .extracting(firstNameFunction)
          .usingElementComparator(CaseInsensitiveStringComparator.instance)
          .filteredOnAssertions(string -> assertThat(string).startsWith("Ma"))
          .containsExactly("MANU", "MAGIC");
    softly.assertThat(names)
          .usingElementComparator(lastNameComparator)
          .filteredOn("first", "Manu")
          .containsExactly(name("Whoever", "Ginobili"));
    softly.assertThat(names)
          .usingElementComparator(lastNameComparator)
          .filteredOn("first", not("Manu"))
          .containsExactly(name("Whoever", "Johnson"));
    softly.assertThat(array(name("John", null), name("Jane", "Doe")))
          .usingElementComparator(alwaysEqual())
          .filteredOnNull("last")
          .hasSize(1)
          .contains(name("Can be", "anybody"));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
  }

  @Test
  public void soft_assertions_should_work_with_satisfiesAnyOf() {
    // GIVEN
    TolkienCharacter legolas = TolkienCharacter.of("Legolas", 1000, ELF);
    Consumer<TolkienCharacter> isHobbit = tolkienCharacter -> assertThat(tolkienCharacter.getRace()).isEqualTo(HOBBIT);
    Consumer<TolkienCharacter> isMan = tolkienCharacter -> assertThat(tolkienCharacter.getRace()).isEqualTo(MAN);
    // WHEN
    softly.assertThat(legolas)
          .as("satisfiesAnyOf")
          .satisfiesAnyOf(isHobbit, isMan);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("[satisfiesAnyOf] ")
                                      .hasMessageContaining("HOBBIT")
                                      .hasMessageContaining("ELF")
                                      .hasMessageContaining("MAN");
  }

  @Test
  public void soft_assertions_should_work_with_assertThatObject() {
    // GIVEN
    TolkienCharacter legolas = TolkienCharacter.of("Legolas", 1000, ELF);
    Deque<TolkienCharacter> characters = new LinkedList<>(list(legolas));
    Consumer<Deque<TolkienCharacter>> isFirstHobbit = tolkienCharacters -> assertThat(tolkienCharacters.getFirst()
                                                                                                       .getRace()).isEqualTo(HOBBIT);
    Consumer<Deque<TolkienCharacter>> isFirstMan = tolkienCharacters -> assertThat(tolkienCharacters.getFirst()
                                                                                                    .getRace()).isEqualTo(MAN);
    // WHEN
    softly.assertThatObject(characters)
          .as("assertThatObject#satisfiesAnyOf")
          .satisfiesAnyOf(isFirstHobbit, isFirstMan);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("[assertThatObject#satisfiesAnyOf] ")
                                      .hasMessageContaining("HOBBIT")
                                      .hasMessageContaining("ELF")
                                      .hasMessageContaining("MAN");
  }

  @Nested
  class ExtractingFromEntries {

    // GIVEN
    Person aceVentura = new Person("ace ventura");
    Person david = new Person("david");

    Map<Person, List<Animal>> map = mapOf(MapEntry.entry(aceVentura, list(new Animal("spike"))),
                                          MapEntry.entry(david, list(new Animal("scoubi"), new Animal("peter"))));

    @Test
    @SuppressWarnings("unchecked")
    void should_pass_when_using_extractingFromEntries_with_map() {
      // WHEN
      softly.assertThat(map)
            .extractingFromEntries(Map.Entry::getKey)
            .containsExactlyInAnyOrder(aceVentura, david);

      softly.assertThat(map)
            .extractingFromEntries(Map.Entry::getKey, entry -> entry.getValue().size())
            .containsExactlyInAnyOrder(tuple(aceVentura, 1), tuple(david, 2));

      // THEN
      softly.assertAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    void should_collect_errors_when_using_extractingFromEntries_with_map() {
      // WHEN
      softly.assertThat(map)
            .extractingFromEntries(Map.Entry::getKey)
            .containsExactlyInAnyOrder(tuple(aceVentura), tuple(new Person("stranger")));

      softly.assertThat(map)
            .overridingErrorMessage("overridingErrorMessage with extractingFromEntries")
            .extractingFromEntries(entry -> entry.getKey().getName())
            .containsExactlyInAnyOrder(tuple("ace ventura", tuple("johnny")));

      softly.assertThat(map)
            .extractingFromEntries(Map.Entry::getKey, entry -> entry.getValue().size())
            .containsExactlyInAnyOrder(tuple(aceVentura, 10), tuple(david, 2));

      // THEN
      List<Throwable> errorsCollected = softly.errorsCollected();
      assertThat(errorsCollected).hasSize(3);
      assertThat(errorsCollected.get(0)).hasMessageFindingMatch("not found:.*stranger.*not expected:.*david");
      assertThat(errorsCollected.get(1)).hasMessage("overridingErrorMessage with extractingFromEntries");
      assertThat(errorsCollected.get(2)).hasMessageFindingMatch("not found:.*10.*not expected:.*1");
    }
  }

  @Test
  public void should_work_with_spliterator() {
    // GIVEN
    Spliterator<String> spliterator1 = Stream.of("a", "b", "c").spliterator();
    Spliterator<String> spliterator2 = Stream.of("a", "b", "c").spliterator();
    Spliterator<Integer> nullSpliterator = null;
    // WHEN
    softly.assertThat(spliterator1)
          .hasCharacteristics(Spliterator.SIZED) // OK
          .hasOnlyCharacteristics(Spliterator.IMMUTABLE);  // FAIL
    softly.assertThat(spliterator2).hasCharacteristics(Spliterator.DISTINCT);
    softly.assertThat(nullSpliterator).hasCharacteristics(Spliterator.DISTINCT);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(3);
    assertThat(errorsCollected.get(0)).hasMessageContaining("IMMUTABLE");
    assertThat(errorsCollected.get(1)).hasMessageContaining("DISTINCT");
    assertThat(errorsCollected.get(2)).hasMessageContaining("Expecting actual not to be null");
  }

  @Test
  void throwable_soft_assertions_should_work_with_navigation_method_get_cause() {
    // GIVEN
    IllegalArgumentException cause = new IllegalArgumentException("cause message");
    Throwable throwable = new Throwable("top level", cause);
    // WHEN
    softly.assertThat(throwable)
          .hasMessage("not top level message")
          .getCause()
          .hasMessage("not cause message");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageContaining("not top level message");
    assertThat(errorsCollected.get(1)).hasMessageContaining("not cause message");
  }

  @Test
  void throwable_soft_assertions_should_work_with_navigation_method_get_root_cause() {
    // GIVEN
    NullPointerException rootCause = new NullPointerException("root cause message");
    IllegalArgumentException cause = new IllegalArgumentException("cause message", rootCause);
    Throwable throwable = new Throwable("top level", cause);
    // WHEN
    softly.assertThat(throwable)
          .hasMessage("not top level message")
          .getRootCause()
          .hasMessage("not root cause message");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageContaining("not top level message");
    assertThat(errorsCollected.get(1)).hasMessageContaining("not root cause message");
  }
}
