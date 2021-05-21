package org.assertj.core.error.uri;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import java.net.URL;

public class ShouldRepresentSamePrimaryRepresentationAs extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_PRIMARY_RESOURCE = "%nExpecting the primary resource of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";

  public static ErrorMessageFactory shouldRepresentSamePrimaryResourceAs(URL actual, String expectedResource) {
    return new ShouldRepresentSamePrimaryRepresentationAs(actual, expectedResource);
  }

  private ShouldRepresentSamePrimaryRepresentationAs(URL actual, String expectedResource) {
    super(SHOULD_HAVE_PRIMARY_RESOURCE, actual, expectedResource,
          actual.getProtocol() + "://" + actual.getHost() + actual.getPath());
  }

}
