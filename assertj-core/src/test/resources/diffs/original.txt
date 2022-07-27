/*
    Copyright 2009 Dmitry Naumenko (dm.naumenko@gmail.com)
    
    This file is part of Java Diff Utills Library.

    Java Diff Utills Library is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Java Diff Utills Library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Java Diff Utills Library.  If not, see <http://www.gnu.org/licenses/>.
*/
package difflib;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import difflib.myers.*;

/**
 * Implements the difference and patching engine
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @version 0.4.1
 */
public class DiffUtils {
	private static DiffAlgorithm defaultDiffAlgorithm = new MyersDiff();
	private static Pattern unifiedDiffChunkRe = 
		Pattern.compile("@@\\s+-(?:(\\d+)(?:,(\\d+))?)\\s+\\+(?:(\\d+)(?:,(\\d+))?)\\s+@@");
	
	/**
	 * Compute the difference between the original and revised texts with default diff algorithm 
	 * 
	 * @param original the original text
	 * @param revised the revised text
	 * @return the patch describing the difference between the original and revised texts 
	 */
	public static Patch diff(List<?> original, List<?> revised) {
		return DiffUtils.diff(original, revised, defaultDiffAlgorithm);
	}
	
	/**
	 * Compute the difference between the original and revised texts with given diff algorithm 
	 * 
	 * @param original the original text
	 * @param revised the revised text
	 * @param algorithm the given algorithm
	 * @return the patch describing the difference between the original and revised texts
	 */
	public static Patch diff(List<?> original, List<?> revised, DiffAlgorithm algorithm) {
		return algorithm.diff(original, revised);
	}
	
	/**
	 * Patch the original text with given patch
	 * 
	 * @param original the original text
	 * @param patch the given patch
	 * @return the revised text
	 * @throws PatchFailedException if can't apply patch
	 */
	public static List<?> patch(List<?> original, Patch patch) throws PatchFailedException {
		return patch.applyTo(original);
	}
	
	/**
	 * Unpatch the revised text for a given patch
	 * 
	 * @param revised the revised text
	 * @param patch the given patch
	 * @return the original text
	 */
	public static List<?> unpatch(List<?> revised, Patch patch) {
		return patch.restore(revised);
	}
	
	/**
	 * Parse the given text in unified format and creates the list of deltas for it.
	 * 
	 * @param diff the text in unified format 
	 * @return the patch with deltas.
	 */
	public static Patch parseUnifiedDiff(List<String> diff) {
		boolean inPrelude = true;
		List<Object[]> rawChunk = new ArrayList<Object[]>();
		Patch patch = new Patch();
		
		int old_ln = 0, old_n = 0, new_ln = 0, new_n = 0;
		String tag = "", rest = "";
		for (String line: diff) {
			// Skip leading lines until after we've seen one starting with '+++'
			if (inPrelude) {
				if (line.startsWith("+++")) {
					inPrelude = false;
				}
				continue;
			}
			Matcher m = unifiedDiffChunkRe.matcher(line);
			if (m.find()) {
				// Process the lines in the previous chunk
				if (rawChunk.size() != 0) {
					List<String> oldChunkLines = new ArrayList<String>();
					List<String> newChunkLines = new ArrayList<String>();
					
					for (Object[] raw_line: rawChunk) {
						tag = (String)raw_line[0];
						rest = (String)raw_line[1];
						if (tag.equals(" ") || tag.equals("-")) {
							oldChunkLines.add(rest);
						}
						if (tag.equals(" ") || tag.equals("+")) {
							newChunkLines.add(rest);
						}
					}
					patch.addDelta(new ChangeDelta(new Chunk(old_ln - 1, old_n, oldChunkLines),  
							new Chunk(new_ln - 1, new_n, newChunkLines)));
					rawChunk.clear();
				}
				// Parse the @@ header
				old_ln = m.group(1) == null ? 1 : Integer.parseInt(m.group(1));
				old_n  = m.group(2) == null ? 1 : Integer.parseInt(m.group(2));
				new_ln = m.group(3) == null ? 1 : Integer.parseInt(m.group(3));
				new_n  = m.group(4) == null ? 1 : Integer.parseInt(m.group(4));
				old_ln = Integer.parseInt(m.group(1));
				
				if (old_ln == 0) {
					old_ln += 1; 
				}
				if (new_ln == 0) {
					new_ln += 1;
				}
			} else {
				if (line.length() > 0) {
					tag  = line.substring(0, 1);
					rest = line.substring(1);
					if (tag.equals(" ") || tag.equals("+") || tag.equals("-")) {
						rawChunk.add(new Object[] {tag, rest});
					}
				}
			}
		}
		
		// Process the lines in the last chunk
		if (rawChunk.size() != 0) {
			List<String> oldChunkLines = new ArrayList<String>();
			List<String> newChunkLines = new ArrayList<String>();
			
			for (Object[] raw_line: rawChunk) {
				tag = (String)raw_line[0];
				rest = (String)raw_line[1];
				if (tag.equals(" ") || tag.equals("-")) {
					oldChunkLines.add(rest);
				} 
				if (tag.equals(" ") || tag.equals("+")) {
					newChunkLines.add(rest);
				}
			}
			
			patch.addDelta(new ChangeDelta(new Chunk(old_ln - 1, old_n, oldChunkLines),  
					new Chunk(new_ln - 1, new_n, newChunkLines)));
			rawChunk.clear();
		}
		
		return patch;
	}
	
}
