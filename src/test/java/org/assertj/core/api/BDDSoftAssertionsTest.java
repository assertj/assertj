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
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.Name.name;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.DateUtil.parseDatetime;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
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
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.api.test.ComparableExample;
import org.assertj.core.data.MapEntry;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.Name;
import org.assertj.core.util.Lists;
import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

public class BDDSoftAssertionsTest extends BaseAssertionsTest {

  private BDDSoftAssertions softly;

  private CartoonCharacter homer;
  private CartoonCharacter fred;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter bart;

  private Map<String, Object> iterableMap;

  private ThrowingExtractor<Name, String, Exception> throwingFirstNameExtractor;
  private ThrowingExtractor<Name, String, Exception> throwingLastNameExtractor;
  private Extractor<Name, String> firstNameExtractor;
  private Extractor<Name, String> lastNameExtractor;
  private Function<Name, String> firstNameFunction;
  private Function<Name, String> lastNameFunction;

  private Extractor<? super CartoonCharacter, ? extends Collection<CartoonCharacter>> childrenExtractor;

  @BeforeEach
  public void setup() {
    softly = new BDDSoftAssertions();

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

    throwingFirstNameExtractor = Name::getFirst;
    throwingLastNameExtractor = Name::getLast;
    firstNameFunction = Name::getFirst;
    lastNameFunction = Name::getLast;
    firstNameExtractor = Name::getFirst;
    lastNameExtractor = Name::getLast;

    childrenExtractor = CartoonCharacter::getChildren;
  }

  @Test
  public void all_assertions_should_pass() {
    softly.then(1).isEqualTo(1);
    softly.then(Lists.newArrayList(1, 2)).containsOnly(1, 2);
    softly.assertAll();
  }

  @Test
  public void should_return_success_of_last_assertion() {
    softly.then(true).isFalse();
    softly.then(true).isEqualTo(true);
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  public void should_return_success_of_last_assertion_with_nested_calls() {
    softly.then(true).isFalse();
    softly.then(true).isTrue(); // isTrue() calls isEqualTo(true)
    assertThat(softly.wasSuccess()).isTrue();
  }

  @Test
  public void should_return_failure_of_last_assertion() {
    softly.then(true).isTrue();
    softly.then(true).isEqualTo(false);
    assertThat(softly.wasSuccess()).isFalse();
  }

  @Test
  public void should_return_failure_of_last_assertion_with_nested_calls() {
    softly.then(true).isTrue();
    softly.then(true).isFalse(); // isFalse() calls isEqualTo(false)
    assertThat(softly.wasSuccess()).isFalse();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_map_assertions() {
    // GIVEN
    Map<String, String> map = mapOf(MapEntry.entry("54", "55"));
    // WHEN
    softly.then(map).contains(MapEntry.entry("1", "2")).isEmpty();
    // THEN
    List<Throwable> errors = softly.errorsCollected();
    assertThat(errors).hasSize(2);
    assertThat(errors.get(0)).hasMessageContaining(format("Expecting:%n"
                                                          + " <{\"54\"=\"55\"}>%n"
                                                          + "to contain:%n"
                                                          + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"
                                                          + "but could not find:%n"
                                                          + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"));
    assertThat(errors.get(1)).hasMessageContaining(format("Expecting empty but was:<{\"54\"=\"55\"}>"));

  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_be_able_to_catch_exceptions_thrown_by_all_proxied_methods() throws URISyntaxException {
    // GIVEN
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
    softly.then(Lists.newArrayList("28").iterator()).contains("29");
    softly.then(Lists.newArrayList("30")).isEqualTo(Lists.newArrayList("31"));
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
    softly.thenThrownBy(() -> {
      throw new Exception("something was wrong");
    }).hasMessage("something was good");
    softly.then(mapOf(MapEntry.entry("54", "55"))).contains(MapEntry.entry("1", "2"));
    softly.then(LocalTime.of(12, 00)).isEqualTo(LocalTime.of(13, 00));
    softly.then(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC))
          .isEqualTo(OffsetTime.of(13, 0, 0, 0, ZoneOffset.UTC));
    softly.then(Optional.of("not empty")).isEqualTo("empty");
    softly.then(OptionalInt.of(0)).isEqualTo(1);
    softly.then(OptionalDouble.of(0.0)).isEqualTo(1.0);
    softly.then(OptionalLong.of(0L)).isEqualTo(1L);
    softly.then(new URI("http://assertj.org")).hasPort(8888);
    softly.then(CompletableFuture.completedFuture("done")).hasFailed();
    softly.then((Predicate<String>) s -> s.equals("something")).accepts("something else");
    softly.then((IntPredicate) s -> s == 1).accepts(2);
    softly.then((LongPredicate) s -> s == 1).accepts(2);
    softly.then((DoublePredicate) s -> s == 1).accepts(2);
    // WHEN
    MultipleFailuresError error = catchThrowableOfType(() -> softly.assertAll(), MultipleFailuresError.class);
    // THEN
    List<String> errors = error.getFailures().stream().map(Object::toString).collect(toList());
    assertThat(errors).hasSize(52);
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
    assertThat(errors.get(12)).contains(format("%nExpecting:%n <1999-12-31T23:59:59.000>%nto be equal to:%n <2000-01-01T00:00:01.000>%nbut was not."));
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
    assertThat(errors.get(25)).contains(format("%nExpecting:%n" +
                                               " <[\"28\"]>%n" +
                                               "to contain:%n" +
                                               " <[\"29\"]>%n" +
                                               "but could not find:%n" +
                                               " <[\"29\"]>%n"));
    assertThat(errors.get(26)).contains(format("%nExpecting:%n <[\"30\"]>%nto be equal to:%n <[\"31\"]>%nbut was not."));
    assertThat(errors.get(27)).contains(format("%nExpecting:%n <32L>%nto be equal to:%n <33L>%nbut was not."));
    assertThat(errors.get(28)).contains(format("%nExpecting:%n <34L>%nto be equal to:%n <35L>%nbut was not."));
    assertThat(errors.get(29)).contains(format("%nExpecting:%n <[36L]>%nto be equal to:%n <[37L]>%nbut was not."));
    assertThat(errors.get(30)).contains(format("%nExpecting:%n <{\"38\"=\"39\"}>%nto be equal to:%n <{\"40\"=\"41\"}>%nbut was not."));
    assertThat(errors.get(31)).contains(format("%nExpecting:%n <42>%nto be equal to:%n <43>%nbut was not."));
    assertThat(errors.get(32)).contains(format("%nExpecting:%n <44>%nto be equal to:%n <45>%nbut was not."));
    assertThat(errors.get(33)).contains(format("%nExpecting:%n <[46]>%nto be equal to:%n <[47]>%nbut was not."));
    assertThat(errors.get(34)).contains(format("%nExpecting:%n <\"48\">%nto be equal to:%n <\"49\">%nbut was not."));
    assertThat(errors.get(35)).contains(format("%nExpecting:%n <50>%nto be equal to:%n <51>%nbut was not."));
    assertThat(errors.get(36)).contains(format("%nExpecting:%n <[52]>%nto be equal to:%n <[53]>%nbut was not."));
    assertThat(errors.get(37)).contains(format("%nExpecting message:%n"
                                               + " <\"NullPointerException message\">%n"
                                               + "but was:%n"
                                               + " <\"IllegalArgumentException message\">"));
    assertThat(errors.get(38)).contains(format("%nExpecting message:%n"
                                               + " <\"something was good\">%n"
                                               + "but was:%n"
                                               + " <\"something was wrong\">"));
    assertThat(errors.get(39)).contains(format("%nExpecting:%n"
                                               + " <{\"54\"=\"55\"}>%n"
                                               + "to contain:%n"
                                               + " <[MapEntry[key=\"1\", value=\"2\"]]>%n"
                                               + "but could not find:%n"
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
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_when_using_extracting_with_object() {
    // GIVEN
    Name name = name("John", "Doe");
    // WHEN
    softly.then(name)
          .extracting("first", "last")
          .contains("John", "Doe");
    softly.then(name)
          .extracting(Name::getFirst, Name::getLast)
          .contains("John", "Doe");
    softly.then(name)
          .extracting(Name::getFirst)
          .isEqualTo("John");
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
  }

  @Test
  public void should_pass_when_using_extracting_with_list() {
    // GIVEN
    List<Name> names = asList(Name.name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.then(names)
          .extracting("first")
          .as("using extracting()")
          .contains("John")
          .contains("Jane")
          .contains("Foo1");

    softly.then(names)
          .extracting(Name::getFirst)
          .as("using extracting(Extractor)")
          .contains("John")
          .contains("Jane")
          .contains("Foo2");

    softly.then(names)
          .extracting("first", String.class)
          .as("using extracting(..., Class)")
          .contains("John")
          .contains("Jane")
          .contains("Foo3");

    softly.then(names)
          .extracting("first", "last")
          .as("using extracting(...)")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Jane", "Doe"))
          .contains(tuple("Foo", "4"));

    softly.then(names)
          .extractingResultOf("getFirst", String.class)
          .as("using extractingResultOf(method, Class)")
          .contains("John")
          .contains("Jane")
          .contains("Foo5");

    softly.then(names)
          .extractingResultOf("getFirst")
          .as("using extractingResultOf(method)")
          .contains("John")
          .contains("Jane")
          .contains("Foo6");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(6);
    assertThat(errorsCollected.get(0)).hasMessageContaining("Foo1");
    assertThat(errorsCollected.get(1)).hasMessageContaining("Foo2");
    assertThat(errorsCollected.get(2)).hasMessageContaining("Foo3");
    assertThat(errorsCollected.get(3)).hasMessageContaining("Foo")
                                      .hasMessageContaining("4");
    assertThat(errorsCollected.get(4)).hasMessageContaining("Foo5");
    assertThat(errorsCollected.get(5)).hasMessageContaining("Foo6");
  }

  @Test
  public void should_pass_when_using_extracting_with_iterable() {

    Iterable<Name> names = asList(name("John", "Doe"), name("Jane", "Doe"));

    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then(names)
            .extracting("first")
            .as("using extracting()")
            .contains("John")
            .contains("Jane");

      softly.then(names)
            .extracting(Name::getFirst)
            .as("using extracting(Extractor)")
            .contains("John")
            .contains("Jane");

      softly.then(names)
            .extracting("first", String.class)
            .as("using extracting(..., Class)")
            .contains("John")
            .contains("Jane");

      softly.then(names)
            .extracting("first", "last")
            .as("using extracting(...)")
            .contains(tuple("John", "Doe"))
            .contains(tuple("Jane", "Doe"));

      softly.then(names)
            .extractingResultOf("getFirst", String.class)
            .as("using extractingResultOf(method, Class)")
            .contains("John")
            .contains("Jane");

      softly.then(names)
            .extractingResultOf("getFirst")
            .as("using extractingResultOf(method)")
            .contains("John")
            .contains("Jane");
    }
  }

  @Test
  public void should_work_when_using_extracting_with_array() {

    Name[] namesAsArray = { name("John", "Doe"), name("Jane", "Doe") };

    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then(namesAsArray)
            .extracting("first")
            .as("using extracting()")
            .contains("John")
            .contains("Jane");

      softly.then(namesAsArray)
            .extracting(Name::getFirst)
            .as("using extracting(Extractor)")
            .contains("John")
            .contains("Jane");

      softly.then(namesAsArray)
            .extracting("first", String.class)
            .as("using extracting(..., Class)")
            .contains("John")
            .contains("Jane");

      softly.then(namesAsArray)
            .extracting("first", "last")
            .as("using extracting(...)")
            .contains(tuple("John", "Doe"))
            .contains(tuple("Jane", "Doe"));

      softly.then(namesAsArray)
            .extractingResultOf("getFirst", String.class)
            .as("using extractingResultOf(method, Class)")
            .contains("John")
            .contains("Jane");

      softly.then(namesAsArray)
            .extractingResultOf("getFirst")
            .as("using extractingResultOf(method)")
            .contains("John")
            .contains("Jane");
    }
  }

  @Test
  public void should_pass_when_using_extracting_with_iterator() {

    Iterator<Name> names = asList(name("John", "Doe"), name("Jane", "Doe")).iterator();

    try (AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then(names)
            .extracting("first")
            .as("using extracting()")
            .contains("John")
            .contains("Jane");
    }
  }

  @Test
  public void should_work_with_flat_extracting() {
    // GIVEN
    List<CartoonCharacter> characters = asList(homer, fred);
    CartoonCharacter[] charactersAsArray = characters.toArray(new CartoonCharacter[characters.size()]);
    // WHEN
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildren)
          .as("using flatExtracting on Iterable")
          .containsAnyOf(homer, fred)
          .hasSize(123);
    softly.then(charactersAsArray)
          .flatExtracting(CartoonCharacter::getChildren)
          .as("using flatExtracting on array")
          .containsAnyOf(homer, fred)
          .hasSize(456);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(4);
    assertThat(errorsCollected.get(0)).hasMessageContaining(homer.toString());
    assertThat(errorsCollected.get(1)).hasMessageContaining("123");
    assertThat(errorsCollected.get(2)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(3)).hasMessageContaining("456");
  }

  @Test
  public void should_collect_all_errors_when_using_extracting() {
    // GIVEN
    List<Name> names = asList(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.then(names)
          .extracting("first")
          .overridingErrorMessage("error 1")
          .contains("gandalf")
          .overridingErrorMessage("error 2")
          .contains("frodo");
    softly.then(names)
          .extracting("last")
          .overridingErrorMessage("error 3")
          .isEmpty();
    // THEN
    assertThat(softly.errorsCollected()).extracting(Throwable::getMessage)
                                        .containsExactly("error 1", "error 2", "error 3");
  }

  @Test
  public void should_collect_all_errors_when_using_flat_extracting() {
    // GIVEN
    List<CartoonCharacter> characters = asList(homer, fred);
    // WHEN
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildren)
          .overridingErrorMessage("error 1")
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
    softly.then(dads)
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
    softly.then(dads)
          .filteredOn(c -> c.getName().equals("Homer Simpson"))
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
    softly.then(example1).isEqualByComparingTo(example2);
    softly.then(example1).isEqualByComparingTo(example3);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(1);
    assertThat(errorsCollected.get(0)).hasMessageContaining("123");
  }

  @Test
  public void should_work_with_stream() {
    // WHEN
    softly.then(Stream.of("a", "b", "c")).contains("a", "foo");
    softly.then(IntStream.of(1, 2, 3)).contains(2, 4, 6);
    softly.then(LongStream.of(1, 2, 3)).contains(-1L, -2L, -3L);
    softly.then(DoubleStream.of(1, 2, 3)).contains(10.0, 20.0, 30.0);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(4);
    assertThat(errorsCollected.get(0)).hasMessageContaining("foo");
    assertThat(errorsCollected.get(1)).hasMessageContaining("6");
    assertThat(errorsCollected.get(2)).hasMessageContaining("-3");
    assertThat(errorsCollected.get(3)).hasMessageContaining("30.0");
  }

  @Test
  public void should_work_with_CompletionStage() {
    // GIVEN
    CompletionStage<String> completionStage = completedFuture("done");
    // WHEN
    softly.then(completionStage).isDone();
    softly.then(completionStage).hasNotFailed();
    softly.then(completionStage).isCancelled();
    completionStage = null;
    softly.then(completionStage).isNull();
    // THEN
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("cancelled");
  }

  @Test
  public void should_work_with_predicate() {
    // GIVEN
    Predicate<String> lowercasePredicate = s -> s.equals(s.toLowerCase());
    // WHEN
    softly.then(lowercasePredicate).accepts("a", "b", "c");
    softly.then(lowercasePredicate).accepts("a", "b", "C");
    // THEN
    assertThat(softly.errorsCollected()).hasSize(1);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("C");
  }

  @Test
  public void should_work_with_optional() {
    // GIVEN
    Optional<String> optional = Optional.of("Gandalf");
    // WHEN
    softly.then(optional).contains("Gandalf");
    // THEN
    softly.assertAll();
  }

  @Test
  public void should_work_with_optional_chained_with_map() {
    // GIVEN
    Optional<String> optional = Optional.of("Gandalf");
    // WHEN
    softly.then(optional)
          .contains("Gandalf")
          .map(String::length)
          .contains(7);
    // THEN
    softly.assertAll();
  }

  @Test
  public void should_collect_all_errors_when_using_map() {
    // GIVEN
    Optional<String> optional = Optional.of("Gandalf");
    // WHEN
    softly.then(optional)
          .contains("Sauron");
    softly.then(optional)
          .contains("Gandalf")
          .map(String::length)
          .contains(1);
    // THEN
    assertThat(softly.errorsCollected()).hasSize(2);
  }

  @Test
  public void should_collect_all_errors_when_using_flatMap() {
    // GIVEN
    Optional<String> optional = Optional.of("Gandalf");
    // WHEN
    softly.then(optional)
          .contains("Sauron");
    softly.then(optional)
          .flatMap(s -> Optional.of(s.length()))
          .contains(1);
    // THEN
    assertThat(softly.errorsCollected()).hasSize(2);
  }

  @Test
  public void should_propagate_AssertionError_from_nested_proxied_calls() {
    // the nested proxied call to isNotEmpty() throw an Assertion error that must be propagated to the caller.
    softly.then(asList()).first();
    // nested proxied call to throwAssertionError when checking that is optional is present
    softly.then(Optional.empty()).contains("Foo");
    // nested proxied call to isNotNull
    softly.then((Predicate<String>) null).accepts("a", "b", "c");
    // nested proxied call to isCompleted
    softly.then(new CompletableFuture<String>()).isCompletedWithValue("done");
    // it must be caught by softly.assertAll()
    assertThat(softly.errorsCollected()).hasSize(4);
  }

  // bug #447

  public class TolkienCharacter {
    String name;
    int age;
  }

  @Test
  public void check_477_bugfix() {
    // GIVEN
    TolkienCharacter frodo = new TolkienCharacter();
    TolkienCharacter samnullGamgee = null;
    TolkienSoftAssertions softly = new TolkienSoftAssertions();
    // WHEN
    softly.then(frodo).hasAge(10); // Expect failure - age will be 0 due to not being initialized.
    softly.then(samnullGamgee).hasAge(11); // Expect failure - argument is null.
    // THEN
    assertThat(softly.errorsCollected()).hasSize(2);
  }

  public static class TolkienCharacterAssert extends AbstractAssert<TolkienCharacterAssert, TolkienCharacter> {

    public TolkienCharacterAssert(TolkienCharacter actual) {
      super(actual, TolkienCharacterAssert.class);
    }

    public static TolkienCharacterAssert assertThat(TolkienCharacter actual) {
      return new TolkienCharacterAssert(actual);
    }

    // 4 - a specific assertion !
    public TolkienCharacterAssert hasName(String name) {
      // check that actual TolkienCharacter we want to make assertions on is not null.
      isNotNull();

      // check condition
      if (!Objects.equals(actual.name, name)) {
        failWithMessage("Expected character's name to be <%s> but was <%s>", name, actual.name);
      }

      // return the current assertion for method chaining
      return this;
    }

    // 4 - another specific assertion !
    public TolkienCharacterAssert hasAge(int age) {
      // check that actual TolkienCharacter we want to make assertions on is not null.
      isNotNull();

      // check condition
      if (actual.age != age) {
        failWithMessage("Expected character's age to be <%s> but was <%s>", age, actual.age);
      }

      // return the current assertion for method chaining
      return this;
    }
  }

  public static class TolkienSoftAssertions extends SoftAssertions {

    public TolkienCharacterAssert then(TolkienCharacter actual) {
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
    assertThatThrownBy(() -> {
      assertSoftly(assertions -> {
        assertions.assertThat(true).isFalse();
        assertions.assertThat(42).isEqualTo("meaning of life");
        assertions.assertThat("red").isEqualTo("blue");
      });
    }).as("it should call assertAll() and fail with multiple validation errors")
      .hasMessageContaining("meaning of life")
      .hasMessageContaining("blue");
  }

  @Test
  public void should_work_with_atomic() {
    // WHEN
    // simple atomic value
    softly.then(new AtomicBoolean(true)).isTrue().isFalse();
    softly.then(new AtomicInteger(1)).hasValueGreaterThan(0).hasNegativeValue();
    softly.then(new AtomicLong(1L)).hasValueGreaterThan(0L).hasNegativeValue();
    softly.then(new AtomicReference<>("abc")).hasValue("abc").hasValue("def");
    // atomic array value
    softly.then(new AtomicIntegerArray(new int[] { 1, 2, 3 })).containsExactly(1, 2, 3).isEmpty();
    softly.then(new AtomicLongArray(new long[] { 1L, 2L, 3L })).containsExactly(1L, 2L, 3L).contains(0);
    softly.then(new AtomicReferenceArray<>(array("a", "b", "c"))).containsExactly("a", "b", "c").contains("123");
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
    try (final AutoCloseableBDDSoftAssertions softly = new AutoCloseableBDDSoftAssertions()) {
      softly.then(numbers)
            .extracting("one", "two")
            .containsExactly("1", "2");
      softly.then(numbers)
            .extracting("one")
            .containsExactly("1");
    }
  }

  @Test
  public void iterable_soft_assertions_should_work_with_navigation_methods() {
    // GIVEN
    Iterable<Name> names = asList(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.then(names)
          .size()
          .isGreaterThan(10);
    softly.then(names)
          .size()
          .isGreaterThan(22)
          .returnToIterable()
          .isEmpty();
    softly.then(names)
          .first()
          .as("first element")
          .isNull();
    softly.then(names)
          .element(0)
          .as("element(0)")
          .isNull();
    softly.then(names)
          .last()
          .as("last element")
          .isNull();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(6);
    assertThat(errorsCollected.get(0)).hasMessageContaining("10");
    assertThat(errorsCollected.get(1)).hasMessageContaining("22");
    assertThat(errorsCollected.get(2)).hasMessageContaining("empty");
    assertThat(errorsCollected.get(3)).hasMessageContaining("first element");
    assertThat(errorsCollected.get(4)).hasMessageContaining("element(0)");
    assertThat(errorsCollected.get(5)).hasMessageContaining("last element");
  }

  @Test
  public void list_soft_assertions_should_work_with_navigation_methods() {
    // GIVEN
    List<Name> names = asList(name("John", "Doe"), name("Jane", "Doe"));
    // WHEN
    softly.then(names)
          .size()
          .isGreaterThan(10);
    softly.then(names)
          .size()
          .isGreaterThan(22)
          .returnToIterable()
          .isEmpty();
    softly.then(names)
          .first()
          .as("first element")
          .isNull();
    softly.then(names)
          .element(0)
          .as("element(0)")
          .isNull();
    softly.then(names)
          .last()
          .as("last element")
          .isNull();
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(6);
    assertThat(errorsCollected.get(0)).hasMessageContaining("10");
    assertThat(errorsCollected.get(1)).hasMessageContaining("22");
    assertThat(errorsCollected.get(2)).hasMessageContaining("empty");
    assertThat(errorsCollected.get(3)).hasMessageContaining("first element");
    assertThat(errorsCollected.get(4)).hasMessageContaining("element(0)");
    assertThat(errorsCollected.get(5)).hasMessageContaining("last element");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void iterable_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Iterable<Name> names = asList(name("John", "Doe"), name("Jane", "Doe"));
    Iterable<CartoonCharacter> characters = asList(homer, fred);
    softly.then(names)
          .extracting(throwingFirstNameExtractor)
          .contains("gandalf")
          .contains("frodo");
    softly.then(names)
          .extracting("last")
          .containsExactly("foo", "bar");
    softly.then(characters)
          .flatExtracting(childrenExtractor)
          .as("using flatExtracting on Iterable")
          .hasSize(1)
          .containsAnyOf(homer, fred);
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildrenWithException)
          .as("using flatExtracting on Iterable with exception")
          .containsExactlyInAnyOrder(homer, fred);
    softly.then(characters)
          .containsOnly(bart);
    softly.then(characters)
          .containsOnlyOnce(maggie, bart);
    softly.then(characters)
          .containsSequence(homer, bart);
    softly.then(characters)
          .containsSubsequence(homer, maggie);
    softly.then(characters)
          .doesNotContain(homer, maggie);
    softly.then(characters)
          .doesNotContainSequence(fred);
    softly.then(characters)
          .doesNotContainSubsequence(homer, fred);
    softly.then(characters)
          .isSubsetOf(homer, bart);
    softly.then(characters)
          .startsWith(fred);
    softly.then(characters)
          .endsWith(bart);
    softly.then(names)
          .extracting(firstNameFunction, lastNameFunction)
          .contains(tuple("John", "Doe"))
          .contains(tuple("Frodo", "Baggins"));
    softly.then(names)
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Bilbo", "Baggins"));
    softly.then(names)
          .extracting(firstNameExtractor)
          .contains("John")
          .contains("sam");
    softly.then(names)
          .extracting("first", String.class)
          .contains("John")
          .contains("Aragorn");
    softly.then(names)
          .filteredOn(name -> name.first.startsWith("Jo"))
          .hasSize(123);
    softly.then(names)
          .filteredOn(name -> name.first.startsWith("Jo"))
          .extracting(firstNameExtractor)
          .contains("Sauron");
    softly.then(names)
          .flatExtracting(firstNameExtractor, lastNameExtractor)
          .as("flatExtracting with multiple Extractors")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.then(names)
          .flatExtracting(throwingFirstNameExtractor, throwingLastNameExtractor)
          .as("flatExtracting with multiple ThrowingExtractors")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.then(names)
          .extractingResultOf("getFirst")
          .contains("John", "Jane")
          .contains("Sam", "Aragorn");
    softly.then(names)
          .extractingResultOf("getFirst", String.class)
          .contains("John", "Jane")
          .contains("Messi", "Ronaldo");
    softly.then(names)
          .filteredOn(new Condition<>(name -> name.first.startsWith("Jo"), "startsWith Jo"))
          .as("filteredOn with condition")
          .hasSize(5);
    softly.then(names)
          .filteredOn("first", in("John", "Frodo"))
          .as("filteredOn firstName in {John, Frodo}")
          .isEmpty();
    softly.then(names)
          .filteredOn("first", "John")
          .as("filteredOn firstName = John")
          .isEmpty();
    softly.then(names)
          .filteredOnNull("first")
          .as("filteredOn firstName = null")
          .isNotEmpty();
    softly.then(names)
          .flatExtracting("first", "last")
          .as("using flatExtracting(String... fieldOrPropertyNames)")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.then(characters)
          .flatExtracting("children")
          .as("using flatExtracting(String fieldOrPropertyName)")
          .contains(bart, maggie)
          .contains("Sauron");
    softly.then(names)
          .filteredOnAssertions(name -> assertThat(name.first).startsWith("Jo"))
          .as("filteredOn with consumer")
          .hasSize(5);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(33);
    assertThat(errorsCollected.get(0)).hasMessageContaining("gandalf");
    assertThat(errorsCollected.get(1)).hasMessageContaining("frodo");
    assertThat(errorsCollected.get(2)).hasMessageContaining("foo")
                                      .hasMessageContaining("bar");
    assertThat(errorsCollected.get(3)).hasMessageContaining("size");
    assertThat(errorsCollected.get(4)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(5)).hasMessageContaining(homer.toString());
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
    assertThat(errorsCollected.get(16)).hasMessageContaining("Baggins");
    assertThat(errorsCollected.get(17)).hasMessageContaining("Bilbo");
    assertThat(errorsCollected.get(18)).hasMessageContaining("sam");
    assertThat(errorsCollected.get(19)).hasMessageContaining("Aragorn");
    assertThat(errorsCollected.get(20)).hasMessageContaining("123");
    assertThat(errorsCollected.get(21)).hasMessageContaining("Sauron");
    assertThat(errorsCollected.get(22)).hasMessageContaining("flatExtracting with multiple Extractors");
    assertThat(errorsCollected.get(23)).hasMessageContaining("flatExtracting with multiple ThrowingExtractors");
    assertThat(errorsCollected.get(24)).hasMessageContaining("Sam");
    assertThat(errorsCollected.get(25)).hasMessageContaining("Ronaldo");
    assertThat(errorsCollected.get(26)).hasMessageContaining("filteredOn with condition");
    assertThat(errorsCollected.get(27)).hasMessageContaining("filteredOn firstName in {John, Frodo}");
    assertThat(errorsCollected.get(28)).hasMessageContaining("filteredOn firstName = John");
    assertThat(errorsCollected.get(29)).hasMessageContaining("filteredOn firstName = null");
    assertThat(errorsCollected.get(30)).hasMessageContaining("using flatExtracting(String... fieldOrPropertyNames)");
    assertThat(errorsCollected.get(31)).hasMessageContaining("using flatExtracting(String fieldOrPropertyName)");
    assertThat(errorsCollected.get(32)).hasMessageContaining("filteredOn with consumer");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void list_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    List<Name> names = asList(name("John", "Doe"), name("Jane", "Doe"));
    List<CartoonCharacter> characters = asList(homer, fred);
    // WHEN
    softly.then(names)
          .extracting(Name::getFirst)
          .contains("gandalf")
          .contains("frodo");
    softly.then(names)
          .extracting("last")
          .containsExactly("foo", "bar");
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildren)
          .as("using flatExtracting on Iterable")
          .hasSize(1)
          .containsAnyOf(homer, fred);
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildrenWithException)
          .as("using flatExtracting on Iterable with exception")
          .containsExactlyInAnyOrder(homer, fred);
    softly.then(characters)
          .containsOnly(bart);
    softly.then(characters)
          .containsOnlyOnce(maggie, bart);
    softly.then(characters)
          .containsSequence(homer, bart);
    softly.then(characters)
          .containsSubsequence(homer, maggie);
    softly.then(characters)
          .doesNotContain(homer, maggie);
    softly.then(characters)
          .doesNotContainSequence(fred);
    softly.then(characters)
          .doesNotContainSubsequence(homer, fred);
    softly.then(characters)
          .isSubsetOf(homer, bart);
    softly.then(characters)
          .startsWith(fred);
    softly.then(characters)
          .endsWith(bart);
    softly.then(names)
          .extracting(Name::getFirst, Name::getLast)
          .contains(tuple("John", "Doe"))
          .contains(tuple("Frodo", "Baggins"));
    softly.then(names)
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Bilbo", "Baggins"));
    softly.then(names)
          .extracting(firstNameExtractor)
          .contains("John")
          .contains("sam");
    softly.then(names)
          .extracting("first", String.class)
          .contains("John")
          .contains("Aragorn");
    softly.then(names)
          .filteredOn(name -> name.first.startsWith("Jo"))
          .hasSize(123);
    softly.then(names)
          .filteredOn(name -> name.first.startsWith("Jo"))
          .extracting(firstNameExtractor)
          .contains("Sauron");
    softly.then(names)
          .flatExtracting(firstNameExtractor, lastNameExtractor)
          .as("flatExtracting with multiple Extractors")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.then(names)
          .flatExtracting(throwingFirstNameExtractor, throwingLastNameExtractor)
          .as("flatExtracting with multiple ThrowingExtractors")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.then(names)
          .extractingResultOf("getFirst")
          .contains("John", "Jane")
          .contains("Sam", "Aragorn");
    softly.then(names)
          .extractingResultOf("getFirst", String.class)
          .contains("John", "Jane")
          .contains("Messi", "Ronaldo");
    softly.then(names)
          .filteredOn(new Condition<>(name -> name.first.startsWith("Jo"), "startsWith Jo"))
          .as("filteredOn with condition")
          .hasSize(5);
    softly.then(names)
          .filteredOn("first", in("John", "Frodo"))
          .as("filteredOn firstName in {John, Frodo}")
          .isEmpty();
    softly.then(names)
          .filteredOn("first", "John")
          .as("filteredOn firstName = John")
          .isEmpty();
    softly.then(names)
          .filteredOnNull("first")
          .as("filteredOn firstName = null")
          .isNotEmpty();
    softly.then(names)
          .flatExtracting("first", "last")
          .as("using flatExtracting(String... fieldOrPropertyNames)")
          .contains("John", "Jane", "Doe")
          .contains("Sauron");
    softly.then(characters)
          .flatExtracting("children")
          .as("using flatExtracting(String fieldOrPropertyName)")
          .contains(bart, maggie)
          .contains("Sauron");
    softly.then(names)
          .filteredOnAssertions(name -> assertThat(name.first).startsWith("Jo"))
          .as("filteredOn with consumer")
          .hasSize(5);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(33);
    assertThat(errorsCollected.get(0)).hasMessageContaining("gandalf");
    assertThat(errorsCollected.get(1)).hasMessageContaining("frodo");
    assertThat(errorsCollected.get(2)).hasMessageContaining("foo")
                                      .hasMessageContaining("bar");
    assertThat(errorsCollected.get(3)).hasMessageContaining("size");
    assertThat(errorsCollected.get(4)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(5)).hasMessageContaining(homer.toString());
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
    assertThat(errorsCollected.get(16)).hasMessageContaining("Baggins");
    assertThat(errorsCollected.get(17)).hasMessageContaining("Bilbo");
    assertThat(errorsCollected.get(18)).hasMessageContaining("sam");
    assertThat(errorsCollected.get(19)).hasMessageContaining("Aragorn");
    assertThat(errorsCollected.get(20)).hasMessageContaining("123");
    assertThat(errorsCollected.get(21)).hasMessageContaining("Sauron");
    assertThat(errorsCollected.get(22)).hasMessageContaining("flatExtracting with multiple Extractors");
    assertThat(errorsCollected.get(23)).hasMessageContaining("flatExtracting with multiple ThrowingExtractors");
    assertThat(errorsCollected.get(24)).hasMessageContaining("Sam");
    assertThat(errorsCollected.get(25)).hasMessageContaining("Ronaldo");
    assertThat(errorsCollected.get(26)).hasMessageContaining("filteredOn with condition");
    assertThat(errorsCollected.get(27)).hasMessageContaining("filteredOn firstName in {John, Frodo}");
    assertThat(errorsCollected.get(28)).hasMessageContaining("filteredOn firstName = John");
    assertThat(errorsCollected.get(29)).hasMessageContaining("filteredOn firstName = null");
    assertThat(errorsCollected.get(30)).hasMessageContaining("using flatExtracting(String... fieldOrPropertyNames)");
    assertThat(errorsCollected.get(31)).hasMessageContaining("using flatExtracting(String fieldOrPropertyName)");
    assertThat(errorsCollected.get(32)).hasMessageContaining("filteredOn with consumer");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void object_array_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Name[] names = array(name("John", "Doe"), name("Jane", "Doe"));
    CartoonCharacter[] characters = array(homer, fred);
    // WHEN
    softly.then(names)
          .extracting(Name::getFirst)
          .contains("gandalf")
          .contains("frodo");
    softly.then(names)
          .extracting("last")
          .containsExactly("foo", "bar");
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildren)
          .as("using flatExtracting on Iterable")
          .hasSize(1)
          .containsAnyOf(homer, fred);
    softly.then(characters)
          .flatExtracting(CartoonCharacter::getChildrenWithException)
          .as("using flatExtracting on Iterable with exception")
          .containsExactlyInAnyOrder(homer, fred);
    softly.then(characters)
          .containsOnly(bart);
    softly.then(characters)
          .containsOnlyOnce(maggie, bart);
    softly.then(characters)
          .containsSequence(homer, bart);
    softly.then(characters)
          .containsSubsequence(homer, maggie);
    softly.then(characters)
          .doesNotContain(homer, maggie);
    softly.then(characters)
          .doesNotContainSequence(fred);
    softly.then(characters)
          .doesNotContainSubsequence(homer, fred);
    softly.then(characters)
          .isSubsetOf(homer, bart);
    softly.then(characters)
          .startsWith(fred);
    softly.then(characters)
          .endsWith(bart);
    softly.then(names)
          .extracting(Name::getFirst, Name::getLast)
          .contains(tuple("John", "Doe"))
          .contains(tuple("Frodo", "Baggins"));
    softly.then(names)
          .extracting("first", "last")
          .contains(tuple("John", "Doe"))
          .contains(tuple("Bilbo", "Baggins"));
    softly.then(names)
          .extracting(firstNameExtractor)
          .contains("John")
          .contains("sam");
    softly.then(names)
          .extracting("first", String.class)
          .contains("John")
          .contains("Aragorn");
    softly.then(names)
          .filteredOn(name -> name.first.startsWith("Jo"))
          .hasSize(123);
    softly.then(names)
          .filteredOn(name -> name.first.startsWith("Jo"))
          .extracting(firstNameExtractor)
          .contains("Sauron");
    softly.then(names)
          .extractingResultOf("getFirst")
          .contains("John", "Jane")
          .contains("Sam", "Aragorn");
    softly.then(names)
          .extractingResultOf("getFirst", String.class)
          .contains("John", "Jane")
          .contains("Messi", "Ronaldo");
    softly.then(names)
          .filteredOn(new Condition<>(name -> name.first.startsWith("Jo"), "startsWith Jo"))
          .as("filteredOn with condition")
          .hasSize(5);
    softly.then(names)
          .filteredOn("first", in("John", "Frodo"))
          .as("filteredOn firstName in {John, Frodo}")
          .isEmpty();
    softly.then(names)
          .filteredOn("first", "John")
          .as("filteredOn firstName = John")
          .isEmpty();
    softly.then(names)
          .filteredOnNull("first")
          .as("filteredOn firstName = null")
          .isNotEmpty();
    softly.then(characters)
          .flatExtracting("children")
          .as("using flatExtracting(String fieldOrPropertyName)")
          .contains(bart, maggie)
          .contains("Sauron");
    softly.then(names)
          .filteredOnAssertions(name -> assertThat(name.first).startsWith("Jo"))
          .as("filteredOn with consumer")
          .hasSize(5);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(30);
    assertThat(errorsCollected.get(0)).hasMessageContaining("gandalf");
    assertThat(errorsCollected.get(1)).hasMessageContaining("frodo");
    assertThat(errorsCollected.get(2)).hasMessageContaining("foo")
                                      .hasMessageContaining("bar");
    assertThat(errorsCollected.get(3)).hasMessageContaining("size");
    assertThat(errorsCollected.get(4)).hasMessageContaining(fred.toString());
    assertThat(errorsCollected.get(5)).hasMessageContaining(homer.toString());
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
    assertThat(errorsCollected.get(16)).hasMessageContaining("Baggins");
    assertThat(errorsCollected.get(17)).hasMessageContaining("Bilbo");
    assertThat(errorsCollected.get(18)).hasMessageContaining("sam");
    assertThat(errorsCollected.get(19)).hasMessageContaining("Aragorn");
    assertThat(errorsCollected.get(20)).hasMessageContaining("123");
    assertThat(errorsCollected.get(21)).hasMessageContaining("Sauron");
    assertThat(errorsCollected.get(22)).hasMessageContaining("Sam");
    assertThat(errorsCollected.get(23)).hasMessageContaining("Ronaldo");
    assertThat(errorsCollected.get(24)).hasMessageContaining("filteredOn with condition");
    assertThat(errorsCollected.get(25)).hasMessageContaining("filteredOn firstName in {John, Frodo}");
    assertThat(errorsCollected.get(26)).hasMessageContaining("filteredOn firstName = John");
    assertThat(errorsCollected.get(27)).hasMessageContaining("filteredOn firstName = null");
    assertThat(errorsCollected.get(28)).hasMessageContaining("using flatExtracting(String fieldOrPropertyName)");
    assertThat(errorsCollected.get(29)).hasMessageContaining("filteredOn with consumer");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void class_soft_assertions_should_report_errors_on_final_methods() {
    // GIVEN
    Class<AnnotatedClass> actual = AnnotatedClass.class;
    // WHEN
    softly.then(actual)
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
    Object vowels = asList("a", "e", "i", "o", "u");
    // WHEN
    softly.then(name)
          .extracting("first", "last")
          .contains("John")
          .contains("gandalf");
    softly.then(name)
          .extracting(Name::getFirst, Name::getLast)
          .contains("John")
          .contains("frodo");
    softly.then(alphabet)
          .asString()
          .startsWith("abc")
          .startsWith("123");
    softly.then(vowels)
          .asList()
          .startsWith("a", "e")
          .startsWith("1", "2");
    softly.then(name)
          .as("extracting(Name::getFirst)")
          .overridingErrorMessage("error message")
          .extracting(Name::getFirst)
          .isEqualTo("Jack");
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(5);
    assertThat(errorsCollected.get(0)).hasMessageContaining("gandalf");
    assertThat(errorsCollected.get(1)).hasMessageContaining("frodo");
    assertThat(errorsCollected.get(2)).hasMessageContaining("123");
    assertThat(errorsCollected.get(3)).hasMessageContaining("\"1\", \"2\"");
    assertThat(errorsCollected.get(4)).hasMessage("[extracting(Name::getFirst)] error message");
  }

  // the test would fail if any method was not proxyable as the assertion error would not be softly caught
  @SuppressWarnings("unchecked")
  @Test
  public void map_soft_assertions_should_report_errors_on_final_methods_and_methods_that_switch_the_object_under_test() {
    // GIVEN
    Map<String, String> map = mapOf(entry("a", "1"), entry("b", "2"), entry("c", "3"));
    // WHEN
    softly.then(map).contains(entry("abc", "ABC"), entry("def", "DEF")).isEmpty();
    softly.then(map).containsAnyOf(entry("gh", "GH"), entry("ij", "IJ"));
    softly.then(map).containsExactly(entry("kl", "KL"), entry("mn", "MN"));
    softly.then(map).containsKeys("K1", "K2");
    softly.then(map).containsOnly(entry("op", "OP"), entry("qr", "QR"));
    softly.then(map).containsOnlyKeys("K3", "K4");
    softly.then(map).containsValues("V1", "V2");
    softly.then(map).doesNotContain(entry("a", "1"), entry("abc", "ABC"));
    softly.then(map).doesNotContainKeys("a", "b");
    softly.then(map).extracting("a", "b").contains("456");
    softly.then(iterableMap)
          .flatExtracting("name", "job", "city", "rank")
          .contains("Unexpected", "Builder", "Dover", "Boston", "Paris", 1, 2, 3);
    // softly.then(map).size().isGreaterThan(1000); not yet supported
    // THEN
    List<Throwable> errors = softly.errorsCollected();
    assertThat(errors).hasSize(12);
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
    assertThat(errors.get(10)).hasMessageContaining("456");
    assertThat(errors.get(11)).hasMessageContaining("Unexpected");
  }

  @Test
  public void map_soft_assertions_should_work_with_navigation_methods() {
    // GIVEN
    Map<String, String> map = mapOf(entry("a", "1"), entry("b", "2"), entry("c", "3"));
    // WHEN
    softly.then(map)
          .size()
          .isGreaterThan(10);
    softly.then(map)
          .size()
          .isGreaterThan(1)
          .returnToMap()
          .as("returnToMap")
          .isEmpty();
    softly.then(map)
          .size()
          .isGreaterThan(1)
          .returnToMap()
          .containsKey("nope")
          .size()
          .as("check size after navigating back")
          .isLessThan(2);
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(4);
    assertThat(errorsCollected.get(0)).hasMessageContaining("10");
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
    softly.then(ballSportPredicate)
          .accepts(entry("sport", "boxing"), entry("sport", "marathon"))
          .rejects(entry("sport", "football"), entry("sport", "basketball"));
    // THEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).hasSize(2);
    assertThat(errorsCollected.get(0)).hasMessageContaining("boxing");
    assertThat(errorsCollected.get(1)).hasMessageContaining("basketball");
  }

}
