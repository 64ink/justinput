/*
 * Copyright 2018 No Face Press, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nofacepress.justinput.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.nofacepress.justinput.JustInput;

public class JustInputTest {

	public static final String INPUT_FILENAME = "test_data.txt";
	public static final String RESOURCE_DATA = "resourceXYZ";
	public static final String FILE_DATA = "fileXYZ";

	public String suck(Reader input) throws IOException {
		int ch;
		StringBuffer sb = new StringBuffer();
		while ((ch = input.read()) != -1) {
			sb.append((char) ch);
		}
		input.close();
		return sb.toString();

	}

	@Test(expected = IOException.class)
	public void testFromBadClasspath() throws IOException {
		new JustInput(JustInput.PREFIX_CLASSPATH + INPUT_FILENAME + "BAD").getReader();
	}

	@Test(expected = IOException.class)
	public void testFromBadExplicitFile() throws IOException {
		new JustInput(JustInput.PREFIX_FILE + INPUT_FILENAME + "BAD").getReader();
	}

	@Test(expected = IOException.class)
	public void testFromBadFile() throws IOException {
		new JustInput(INPUT_FILENAME + "BAD").getReader();
	}

	@Test(expected = IOException.class)
	public void testFromBadUrlFile() throws IOException {
		String path = "file:///" + System.getProperty("user.dir") + "/" + INPUT_FILENAME + "BAD";
		new JustInput(path).getReader();
	}

	@Test
	public void testFromClasspath() throws IOException {
		Reader input = new JustInput(JustInput.PREFIX_CLASSPATH + INPUT_FILENAME).getReader();
		String s = suck(input);
		assertEquals(s, RESOURCE_DATA);
	}

	@Test(expected = IOException.class)
	public void testFromEmptyPath() throws IOException {
		new JustInput("").getReader();
	}

	@Test
	public void testFromExplicitFile() throws IOException {
		Reader input = new JustInput(JustInput.PREFIX_FILE + INPUT_FILENAME).getReader();
		String s = suck(input);
		assertEquals(s, FILE_DATA);
	}

	@Test
	public void testFromFile() throws IOException {
		Reader input = new JustInput(INPUT_FILENAME).getReader();
		String s = suck(input);
		assertEquals(s, FILE_DATA);
	}

	@Test(expected = IOException.class)
	public void testFromNull() throws IOException {
		new JustInput(null).getReader();
	}

	@Test
	public void testFromUrlFile() throws IOException {
		String path = "file:///" + System.getProperty("user.dir") + "/" + INPUT_FILENAME;
		Reader input = new JustInput(path).getReader();
		String s = suck(input);
		assertEquals(s, FILE_DATA);
	}

}
