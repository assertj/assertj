package org.assertj.core.error;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ElementsShouldNoneSatisfy.noneElementsShouldSatisfy;
import static org.assertj.core.util.Lists.newArrayList;

public class ElementsShouldNoneSatisfy_create_Test {

    @Test
    public void should_create_error_message() {
      ErrorMessageFactory factory = noneElementsShouldSatisfy(newArrayList("Luke", "Yoda"), "Yoda",
                                                          "Yoda violates some restrictions");
      String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
      assertThat(message).isEqualTo(format("[Test] %n" +
                                           "Expecting none element of:%n" +
                                           "  <[\"Luke\", \"Yoda\"]>%n" +
                                           "to satisfy given requirements, but this element did:%n" +
                                           "  <\"Yoda\"> %n" +
                                           "Details: \"Yoda violates some restrictions\""));
    }

    @Test
    public void should_create_error_message_any() {
      ErrorMessageFactory factory = noneElementsShouldSatisfy(newArrayList("Luke", "Yoda"));
      String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
      assertThat(message).isEqualTo(format("[Test] %n" +
                                           "Expecting none element of:%n" +
                                           "  <[\"Luke\", \"Yoda\"]>%n" +
                                           "to satisfy the given assertions requirements but one did."));
    }
}
