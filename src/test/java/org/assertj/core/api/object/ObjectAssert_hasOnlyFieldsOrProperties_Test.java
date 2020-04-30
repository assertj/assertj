package org.assertj.core.api.object;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.Test;
/**
 * Tests for <code>{@link ObjectAssert#hasOnlyFieldsOrProperties(String...)}</code>.
 *
 * @author Victor Wang
 */
public class ObjectAssert_hasOnlyFieldsOrProperties_Test extends ObjectAssertBaseTest {

  public static final String FIELD_NAME = "name"; // field in org.assertj.core.test.Person

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasOnlyFieldsOrProperties(FIELD_NAME);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasOnlyFieldsOrProperties(getInfo(assertions), getActual(assertions), FIELD_NAME);
  }

  @Test
  public void should_fail_if_fields_or_properties_does_not_exists() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      Jedi jedi = new Jedi("Yoda", "Blue");
      assertThat(jedi).hasOnlyFieldsOrProperties("name", "lightSaberColor");
    }).withMessage(format("%nExpecting%n  <Yoda the Jedi>%nto have  properties and a fields named <\"lightSaberColor, name\">"));
  }

  @Test
  public void should_fail_if_given_field_or_property_name_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      Jedi jedi = new Jedi("Yoda", "Blue");
      assertThat(jedi).hasOnlyFieldsOrProperties((String[]) null);
    }).withMessage("The name of the property/field to read should not be null");

  }
}
