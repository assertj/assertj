package org.assertj.core.util.diff;

import java.io.File;

/**
 * Test constants
 * @author simon.mittermueller@gmail.com
 *
 */
public final class TestConstants {

	private TestConstants() {
		// prevent construction.
	}
	
	/** File separator. */
	public static final String FS = File.separator;
	
	/** The base resource path. */
	public static String BASE_FOLDER_RESOURCES = "src" + FS + "test" + FS + "resources";
	
	/** The base folder containing the test files. Ends with {@link #FS}. */
	public static final String MOCK_FOLDER = BASE_FOLDER_RESOURCES + FS + "diffs" + FS;
	
}
