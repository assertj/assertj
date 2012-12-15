package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;

import static org.fest.test.ExpectedException.none;

import org.junit.Before;
import org.junit.Rule;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.fest.test.ExpectedException;

public class MultimapAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Multimap<String, String> actual;

  public MultimapAssertBaseTest() {
    super();
  }

  @Before
  public void setUp() {
    actual = ArrayListMultimap.create();
    actual.putAll("Lakers", newArrayList("Kobe Bryant", "Magic Johnson", "Kareem Abdul Jabbar"));
    actual.putAll("Spurs", newArrayList("Tony Parker", "Tim Duncan", "Manu Ginobili"));
    actual.putAll("Bulls", newArrayList("Michael Jordan", "Scottie Pippen", "Derrick Rose"));
  }

}