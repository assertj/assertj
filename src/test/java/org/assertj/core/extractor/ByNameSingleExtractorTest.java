package org.assertj.core.extractor;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Rule;
import org.junit.Test;

public class ByNameSingleExtractorTest {
  private static final Employee yoda = new Employee(1L, new Name("Yoda"), 800);;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_extract_field_values_even_if_property_exist() {
    Object extractedValues = idExtractor().extract(yoda);

    assertThat(extractedValues).isEqualTo(1L);
  }

  @Test
  public void should_extract_property_values_when_no_public_field_match_given_name() {
    Object extractedValues = ageExtractor().extract(yoda);

    assertThat(extractedValues).isEqualTo(800);
  }

  @Test
  public void should_extract_pure_property_values() {
    Object extractedValues = adultExtractor().extract(yoda);

    assertThat(extractedValues).isEqualTo(true);
  }

  @Test
  public void should_throw_error_when_no_property_nor_public_field_match_given_name() {
    thrown.expect(IntrospectionError.class);

    new ByNameSingleExtractor("unknown").extract(yoda);
  }

  @Test
  public void should_throw_exception_when_given_name_is_null() {
    thrown.expectIllegalArgumentException("The name of the field/property to read should not be null");

    new ByNameSingleExtractor(null).extract(yoda);
  }

  @Test
  public void should_throw_exception_when_given_name_is_empty() {
    thrown.expectIllegalArgumentException("The name of the field/property to read should not be empty");

    new ByNameSingleExtractor("").extract(yoda);
  }

  @Test
  public void should_fallback_to_field_if_exception_has_been_thrown_on_property_access() throws Exception {
    Object extractedValue = nameExtractor().extract(employeeWithBrokenName("Name"));

    assertThat(extractedValue).isEqualTo(new Name("Name"));
  }

  @Test
  public void should_prefer_properties_over_fields() throws Exception {
    Object extractedValue = nameExtractor().extract(employeeWithOverridenName("Overriden Name"));

    assertThat(extractedValue).isEqualTo(new Name("Overriden Name"));
  }

  @Test
  public void should_throw_exception_if_property_cannot_be_extracted_due_to_runtime_exception_during_property_access()
      throws Exception {
    thrown.expect(IntrospectionError.class);

    Employee employee = brokenEmployee();
    adultExtractor().extract(employee);
  }

  @Test
  public void should_throw_exception_if_no_object_is_given() throws Exception {
    thrown.expect(IllegalArgumentException.class);

    idExtractor().extract(null);
  }

  private Employee employeeWithBrokenName(String name) {
    return new Employee(1L, new Name(name), 0) {

      @Override
      public Name getName() {
        throw new IllegalStateException();
      }
    };
  }

  private Employee employeeWithOverridenName(final String overridenName) {
    return new Employee(1L, new Name("Name"), 0) {

      @Override
      public Name getName() {
        return new Name(overridenName);
      }
    };
  }

  private Employee brokenEmployee() {
    return new Employee() {

      @Override
      public boolean isAdult() {
        throw new IllegalStateException();
      }
    };
  }

  private ByNameSingleExtractor idExtractor() {
    return new ByNameSingleExtractor("id");
  }

  private ByNameSingleExtractor ageExtractor() {
    return new ByNameSingleExtractor("age");
  }

  private ByNameSingleExtractor adultExtractor() {
    return new ByNameSingleExtractor("adult");
  }

  private ByNameSingleExtractor nameExtractor() {
    return new ByNameSingleExtractor("name");
  }

}
