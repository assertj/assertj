package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.groups.Tuple;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class Assertions_linesOf_Test {

  private static final String UTF_8 = "UTF-8";
  private static final List<String> EXPECTED_CONTENT = newArrayList("A text file encoded in UTF-8, with diacritics:", "é à");

  @Test
  public void should_read_lines_of_file_with_UTF8_charset() {
    File file = new File("src/test/resources/utf8.txt");
    assertThat(linesOf(file, UTF_8)).isEqualTo(EXPECTED_CONTENT);
    assertThat(linesOf(file, Charset.forName(UTF_8))).isEqualTo(EXPECTED_CONTENT);
  }

}
