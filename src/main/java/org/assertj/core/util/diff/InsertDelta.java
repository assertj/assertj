/*
   Copyright 2010 Dmitry Naumenko (dm.naumenko@gmail.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.assertj.core.util.diff;

import java.util.List;

/**
 * Describes the add-delta between original and revised texts.
 * 
 * @author <a href="dm.naumenko@gmail.com">Dmitry Naumenko</a>
 * @param <T>
 *            The type of the compared elements in the 'lines'.
 */
public class InsertDelta<T> extends Delta<T> {

	/**
	 * Creates an insert delta with the two given chunks.
	 *
	 * @param original
	 *            The original chunk. Must not be {@code null}.
	 * @param revised
	 *            The original chunk. Must not be {@code null}.
	 */
	public InsertDelta(Chunk<T> original, Chunk<T> revised) {
		super(original, revised);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws PatchFailedException
	 */
	@Override
	public void applyTo(List<T> target) throws PatchFailedException {
		verify(target);
		int position = this.getOriginal().getPosition();
		List<T> lines = this.getRevised().getLines();
		for (int i = 0; i < lines.size(); i++) {
			target.add(position + i, lines.get(i));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void restore(List<T> target) {
		int position = getRevised().getPosition();
		int size = getRevised().size();
		for (int i = 0; i < size; i++) {
			target.remove(position);
		}
	}

	@Override
	public void verify(List<T> target) throws PatchFailedException {
		if (getOriginal().getPosition() > target.size()) {
			throw new PatchFailedException("Incorrect patch for delta: "
					+ "delta original position > target size");
		}

	}

	public TYPE getType() {
		return Delta.TYPE.INSERT;
	}

	@Override
	public String toString() {
		return "[InsertDelta, position: " + getOriginal().getPosition()
				+ ", lines: " + getRevised().getLines() + "]";
	}
}
