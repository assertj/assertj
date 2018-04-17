package org.assertj.core.error;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.util.Lists.newArrayList;

public class NoElementsShouldSatisfy_create_Test {

    @Test
    public void should_create_error_message_any() {
      ErrorMessageFactory factory = NoElementsShouldSatisfy.noElementsShouldSatisfy(newArrayList("Luke", "Yoda"));
      String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
      assertThat(message).isEqualTo(format("[Test] %n" +
                                           "Expecting no elements of:%n" +
                                           "  <[\"Luke\", \"Yoda\"]>%n" +
                                           "to satisfy the given assertions requirements but one did."));
    }
}
