package org.fest.assertions.api;

import static com.google.common.collect.Lists.newArrayList;

import static org.fest.assertions.api.GUAVA.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class MultiMapAssertTest {

  private Multimap<Integer, List<String>> map;

  @Before
  public void init_fibo_map() {
    map = HashMultimap.create();
    map.put(5, newArrayList("3", "2"));
    map.put(8, newArrayList("5", "3"));
    map.put(13, newArrayList("5", "8"));
    map.put(13, newArrayList("3", "2", "8"));
    map.put(21, newArrayList("13", "8"));

  }

  @Test
  public void should_contain_key() {
    assertThat(map).containsKey(5);
  }

  @Test(expected = AssertionError.class)
  public void should_not_contain_key() {
    assertThat(map).containsKey(6);
  }

}
