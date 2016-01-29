package org.assertj.core.api.abstract_; //Make sure that package-private access is lost

import java.util.List;

import org.assertj.core.api.AbstractStandardSoftAssertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This tests that classes extended from {@link AbstractStandardSoftAssertions} will have access to the list of
 * collected errors that the various proxies have collected.
 */
public class SoftAssertionsErrorsCollectedTest{
	private final Object        objectForTesting = null;
	private final TestCollector softly           = new TestCollector();

	@Test
	public void return_empty_list_of_errors() throws Exception{
		softly.assertThat(objectForTesting).isNull(); //No errors to collect
		assertThat(softly.getErrors()).isEmpty();
	}

	@Test
	public void returns_nonempty_list_of_errors() throws Exception{
		softly.assertThat(objectForTesting).isNotNull(); //This should allow something to be collected
		assertThat(softly.getErrors()).hasAtLeastOneElementOfType(Throwable.class);
	}

	private class TestCollector extends AbstractStandardSoftAssertions{
		public List<Throwable> getErrors(){
			return errorsCollected();
		}
	}
}

