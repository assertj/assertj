package org.assertj.core.internal.urls;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.error.uri.ShouldRepresentSamePrimaryRepresentationAs.shouldRepresentSamePrimaryResourceAs;
import static org.mockito.Mockito.verify;

public class Urls_assertRepresentSamePrimaryResource extends UrlsBaseTest {
  // @format:off
  @ParameterizedTest
  @CsvSource({
      "https://example.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?name=ferret&color=purple#hello",

      "https://example.com/path/to/page?color=purple, "+
      "https://example.com/path/to/page?name=ferret&color=purple",

      "https://example.com/path/to/page?color=purple&name=ferret#1, "+
      "https://example.com/path/to/page?name=ferret&color=purple#2",
  })
  // @format:on
  public void should_pass_if_have_same_primary_resource(String actual, String expected) throws MalformedURLException {
    // GIVEN
    URL actualURL = new URL(actual);
    URL expectedURL = new URL(expected);
    // THEN
    assertThat(actualURL).representsSamePrimaryResourceAs(expectedURL);
  }

  // @format:off
  @ParameterizedTest
  @CsvSource({
      "https://example2.com/path/to/page?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com/sunt/to/ting?color=purple&name=ferret#hello, " +
      "https://example.com/path/to/page?color=purple&name=ferret#hello",

      "https://example.com?color=purple&name=ferret#hello, "+
      "https://example.com/path/to/page?color=purple&name=ferret#hello",
  })
  // @format:on
  public void should_fail_if_have_different_primary_resource(String actual, String expected) throws MalformedURLException {
    // GIVEN
    URL actualURL = new URL(actual);
    URL expectedURL = new URL(expected);
    // WHEN
    String expectedResource = expectedURL.getProtocol() + "://" + expectedURL.getHost() + expectedURL.getPath();
    expectAssertionError(() -> urls.assertRepresentSamePrimaryResource(info, actualURL, expectedURL));
    // THEN
    verify(failures).failure(info, shouldRepresentSamePrimaryResourceAs(actualURL, expectedResource));
  }
}
