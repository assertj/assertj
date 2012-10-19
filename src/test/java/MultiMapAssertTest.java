import static org.fest.assertions.api.MultimapAssertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class MultiMapAssertTest {

	private Multimap<Integer, List<String>> map;

	@Before
	public void init_fibo_map() {
		map = HashMultimap.create();
		map.put(5, Lists.newArrayList("3", "2"));
		map.put(8, Lists.newArrayList("5", "3"));
		map.put(13, Lists.newArrayList("5", "8"));
		map.put(13, Lists.newArrayList("3", "2", "8"));
		map.put(21, Lists.newArrayList("13", "8"));

	}

	@Test
	public void should_has_a_key() {
		assertThat(map).containsKey(5);
	}

	@Test(expected = AssertionError.class)
	public void should_not_have_a_key() {
		assertThat(map).containsKey(6);
	}

}
