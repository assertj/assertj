package org.assertj.core.api;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Static {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#asInstanceOf(InstanceOfAssertFactory)}.
 *
 * @since 3.13.0
 */
public interface InstanceOfAssertFactories {

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<Predicate, PredicateAssert<Object>> PREDICATE = new InstanceOfAssertFactory<>(Predicate.class,
                                                                                                        PredicateAssert::new);

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <T> InstanceOfAssertFactory<Predicate, PredicateAssert<T>> predicate(Class<T> type) {
    return new InstanceOfAssertFactory<>(Predicate.class, PredicateAssert<T>::new);
  }

  InstanceOfAssertFactory<IntPredicate, IntPredicateAssert> INT_PREDICATE = new InstanceOfAssertFactory<>(IntPredicate.class,
                                                                                                          IntPredicateAssert::new);

  InstanceOfAssertFactory<LongPredicate, LongPredicateAssert> LONG_PREDICATE = new InstanceOfAssertFactory<>(LongPredicate.class,
                                                                                                             LongPredicateAssert::new);

  InstanceOfAssertFactory<DoublePredicate, DoublePredicateAssert> DOUBLE_PREDICATE = new InstanceOfAssertFactory<>(DoublePredicate.class,
                                                                                                                   DoublePredicateAssert::new);

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> COMPLETABLE_FUTURE = new InstanceOfAssertFactory<>(CompletableFuture.class,
                                                                                                                                 CompletableFutureAssert::new);

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class, CompletableFutureAssert<RESULT>::new);
  }

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> COMPLETION_STAGE = new InstanceOfAssertFactory<>(CompletionStage.class,
                                                                                                                             CompletableFutureAssert::new);

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class, CompletableFutureAssert<RESULT>::new);
  }

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<Optional, OptionalAssert<Object>> OPTIONAL = new InstanceOfAssertFactory<>(Optional.class,
                                                                                                     OptionalAssert::new);

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<Optional, OptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new InstanceOfAssertFactory<>(Optional.class, OptionalAssert<VALUE>::new);
  }

  InstanceOfAssertFactory<OptionalDouble, OptionalDoubleAssert> OPTIONAL_DOUBLE = new InstanceOfAssertFactory<>(OptionalDouble.class,
                                                                                                                OptionalDoubleAssert::new);

  InstanceOfAssertFactory<OptionalInt, OptionalIntAssert> OPTIONAL_INT = new InstanceOfAssertFactory<>(OptionalInt.class,
                                                                                                       OptionalIntAssert::new);

  InstanceOfAssertFactory<OptionalLong, OptionalLongAssert> OPTIONAL_LONG = new InstanceOfAssertFactory<>(OptionalLong.class,
                                                                                                          OptionalLongAssert::new);

  InstanceOfAssertFactory<BigDecimal, BigDecimalAssert> BIG_DECIMAL = new InstanceOfAssertFactory<>(BigDecimal.class,
                                                                                                    BigDecimalAssert::new);

  InstanceOfAssertFactory<BigInteger, BigIntegerAssert> BIG_INTEGER = new InstanceOfAssertFactory<>(BigInteger.class,
                                                                                                    BigIntegerAssert::new);

  InstanceOfAssertFactory<URI, UriAssert> URI = new InstanceOfAssertFactory<>(URI.class,
                                                                              UriAssert::new);

  InstanceOfAssertFactory<URL, UrlAssert> URL = new InstanceOfAssertFactory<>(URL.class,
                                                                              UrlAssert::new);

  InstanceOfAssertFactory<Boolean, BooleanAssert> BOOLEAN = new InstanceOfAssertFactory<>(Boolean.class,
                                                                                          BooleanAssert::new);

  InstanceOfAssertFactory<boolean[], BooleanArrayAssert> BOOLEAN_ARRAY = new InstanceOfAssertFactory<>(boolean[].class,
                                                                                                       BooleanArrayAssert::new);

  InstanceOfAssertFactory<Byte, ByteAssert> BYTE = new InstanceOfAssertFactory<>(Byte.class,
                                                                                 ByteAssert::new);

  InstanceOfAssertFactory<byte[], ByteArrayAssert> BYTE_ARRAY = new InstanceOfAssertFactory<>(byte[].class,
                                                                                              ByteArrayAssert::new);

  InstanceOfAssertFactory<Character, CharacterAssert> CHARACTER = new InstanceOfAssertFactory<>(Character.class,
                                                                                                CharacterAssert::new);

  InstanceOfAssertFactory<char[], CharArrayAssert> CHAR_ARRAY = new InstanceOfAssertFactory<>(char[].class,
                                                                                              CharArrayAssert::new);

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<Class, ClassAssert> CLASS = new InstanceOfAssertFactory<>(Class.class,
                                                                                    ClassAssert::new);

  InstanceOfAssertFactory<Double, DoubleAssert> DOUBLE = new InstanceOfAssertFactory<>(Double.class,
                                                                                       DoubleAssert::new);

  InstanceOfAssertFactory<double[], DoubleArrayAssert> DOUBLE_ARRAY = new InstanceOfAssertFactory<>(double[].class,
                                                                                                    DoubleArrayAssert::new);

  InstanceOfAssertFactory<File, FileAssert> FILE = new InstanceOfAssertFactory<>(File.class,
                                                                                 FileAssert::new);

  InstanceOfAssertFactory<Integer, IntegerAssert> INTEGER = new InstanceOfAssertFactory<>(Integer.class,
                                                                                          IntegerAssert::new);

  InstanceOfAssertFactory<String, StringAssert> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                       StringAssert::new);

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<List, ListAssert<Object>> LIST = new InstanceOfAssertFactory<>(List.class,
                                                                                         ListAssert::new);

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, ListAssert<ELEMENT>::new);
  }

}
