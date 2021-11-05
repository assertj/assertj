package org.assertj.core.api.recursive.assertion;

import org.assertj.core.api.BDDAssertions;
import org.assertj.core.api.recursive.FieldLocation;
import org.assertj.core.test.jdk11.Jdk11;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

public class RecursiveAssertionDriver_MapHandlingTest extends AbstractRecursiveAssertionDriverTestBase {

  @Test
  public void should_assert_over_map_but_not_keys_or_values_when_policy_is_map_object_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
      .withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_ONLY)
      .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = classWithAMap();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(2).contains(FieldLocation.rootFieldLocation(),
                                                         FieldLocation.rootFieldLocation().field("map"));
  }

  @Test
  public void should_assert_over_values_but_not_keys_or_map_object_when_policy_is_values_only() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
      .withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy.VALUES_ONLY)
      .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = classWithAMap();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(3).contains(FieldLocation.rootFieldLocation(),
                                                         FieldLocation.rootFieldLocation().field("map").field("VAL[value0]"),
                                                         FieldLocation.rootFieldLocation().field("map").field("VAL[value1]")
                                                        );
  }

  @Test
  public void should_assert_over_map_object__keys_and_values_when_policy_is_map_object_and_entries() {
    // GIVEN
    RecursiveAssertionConfiguration configuration = RecursiveAssertionConfiguration.builder()
      .withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_AND_ENTRIES)
      .build();
    RecursiveAssertionDriver objectUnderTest = new RecursiveAssertionDriver(configuration);
    Object testObject = classWithAMap();
    // WHEN
    List<FieldLocation> failedFields = objectUnderTest.assertOverObjectGraph(failingMockPredicate, testObject);
    // THEN
    then(failedFields).hasSize(6).contains(FieldLocation.rootFieldLocation(),
                                                         FieldLocation.rootFieldLocation().field("map"),
                                                         FieldLocation.rootFieldLocation().field("map").field("KEY[key0]"),
                                                         FieldLocation.rootFieldLocation().field("map").field("VAL[value0]"),
                                                         FieldLocation.rootFieldLocation().field("map").field("KEY[key1]"),
                                                         FieldLocation.rootFieldLocation().field("map").field("VAL[value1]")
                                                        );
  }

  private Object classWithAMap() {
    return new ClassWithMap();
  }

  class ClassWithMap {
    private Map<String, String> map = Jdk11.Map.of("key0", "value0", "key1", "value1");
  }
}
