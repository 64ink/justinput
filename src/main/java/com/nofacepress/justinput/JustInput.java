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
package com.nofacepress.justinput;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class JustInput {
	public static final String PREFIX_CLASSPATH = "classpath:";
	public static final String PREFIX_FILE = "file:";
	private static final String PATTERN_URL = "://";

	/**
	 * Creates an input based on a generic path string from a file, class resource,
	 * network URL.
	 * 
	 * If path starts with "classpath:XYZ", then XYZ will be loaded from the class
	 * path.
	 * 
	 * If path is a URL which contains "://", then it will loaded via URL.
	 * 
	 * If path starts with "file:XYZ", then XYZ will be loaded from the local file
	 * system.
	 * 
	 * Otherwise, the entire path is consider as file:path and loaded from the file
	 * system. If still not found, this class path is checked as a fallback.
	 * 
	 * @param path        the path
	 * @param classLoader alternative class loader or null for default.
	 * @return the input stream
	 * @throws IOException on error
	 */
	public static InputStream newInputStream(String path, ClassLoader classLoader) throws IOException {
		if (path == null || path.isEmpty()) {
			throw new IOException("Cannot load from an empty path.");
		}
		InputStream input = null;
		if (path.startsWith(PREFIX_CLASSPATH)) {
			String resource = path.substring(PREFIX_CLASSPATH.length());
			URL resourceUrl = classLoader.getResource(resource);
			if (resourceUrl == null) {
				throw new IOException("Unable to find class resource '" + resource + "'");
			}
			URLConnection connection = resourceUrl.openConnection();
			input = connection.getInputStream();
		} else if (path.contains(PATTERN_URL)) {
			URL url = new URL(path);
			URLConnection connection = url.openConnection();
			input = connection.getInputStream();
		} else if (path.contains(PREFIX_FILE)) {
			String resource = path.substring(PREFIX_FILE.length());
			File file = new File(resource);
			if (file.exists()) {
				input = new FileInputStream(file);
			}
		} else {
			File file = new File(path);
			if (file.exists()) {
				input = new FileInputStream(file);
			} else {
				URL resourceUrl = classLoader.getResource(path);
				if (resourceUrl != null) {
					URLConnection connection = resourceUrl.openConnection();
					input = connection.getInputStream();
				}
			}
		}

		// last try to use a URL in case it is some odd protocol
		if (input == null) {
			try {
				URL url = new URL(path);
				URLConnection connection = url.openConnection();
				input = connection.getInputStream();
			} catch (MalformedURLException e) {
				throw new IOException("Unable to find file or resource '" + path + "'");
			}
		}

		return new BufferedInputStream(input);

	}

	/**
	 * Creates a URL based on a generic path string from a file, class resource,
	 * network URL. Be aware that the URL may not point to anything since it was
	 * never opened like in the other methods.
	 * 
	 * If path starts with "classpath:XYZ", then XYZ will be loaded from the class
	 * path.
	 * 
	 * If path is a URL which contains "://", then it will loaded via URL.
	 * 
	 * If path starts with "file:XYZ", then XYZ will be loaded from the local file
	 * system.
	 * 
	 * Otherwise, the entire path is consider as file:path and loaded from the file
	 * system. If still not found, this class path is checked as a fallback.
	 * 
	 * @param path        the path
	 * @param classLoader alternative class loader or null for default.
	 * @return the URL
	 * @throws IOException on error
	 */
	public static URL newUrl(String path, ClassLoader classLoader) throws IOException {
		if (path == null || path.isEmpty()) {
			throw new IOException("Cannot load from an empty path.");
		}
		if (path.startsWith(PREFIX_CLASSPATH)) {
			String resource = path.substring(PREFIX_CLASSPATH.length());
			URL resourceUrl = classLoader.getResource(resource);
			if (resourceUrl == null) {
				throw new IOException("Unable to find class resource '" + resource + "'");
			}
			return resourceUrl;
		}

		if (path.contains(PATTERN_URL)) {
			return new URL(path);
		}

		if (path.contains(PREFIX_FILE)) {
			String resource = path.substring(PREFIX_FILE.length());
			File file = new File(resource);
			if (file.exists()) {
				return file.toURI().toURL();
			}
		} else {
			File file = new File(path);
			if (file.exists()) {
				return file.toURI().toURL();
			}

			URL resourceUrl = classLoader.getResource(path);
			if (resourceUrl != null) {
				return resourceUrl;
			}
		}

		// last try to use a URL in case it is some odd protocol
		try {
			return new URL(path);
		} catch (MalformedURLException e) {
			throw new IOException("Unable to find file or resource '" + path + "'");
		}

	}

	/**
	 * Creates an reader based on a generic path string from a file, class resource,
	 * network URL.
	 * 
	 * If path starts with "classpath:XYZ", then XYZ will be loaded from the class
	 * path.
	 * 
	 * If path is a URL which contains "://", then it will loaded via URL.
	 * 
	 * If path starts with "file:XYZ", then XYZ will be loaded from the local file
	 * system.
	 * 
	 * Otherwise, the entire path is consider as file:path and loaded from the file
	 * system. If still not found, this class path is checked as a fallback.
	 * 
	 * @param path        the path
	 * @param classLoader alternative class loader or null for default.
	 * @return the URL
	 * @throws IOException on error
	 */
	public static Reader newReader(String path, ClassLoader classLoader) throws IOException {
		return new InputStreamReader(newInputStream(path, classLoader));
	}

	private final String path;

	/**
	 * Creates an input based on a generic path string from a file, class resource,
	 * network URL.
	 * 
	 * If path starts with "classpath:XYZ", then XYZ will be loaded from the class
	 * path.
	 * 
	 * If path is a URL which contains "://", then it will loaded via URL.
	 * 
	 * If path starts with "file:XYZ", then XYZ will be loaded from the local file
	 * system.
	 * 
	 * Otherwise, the entire path is consider as file:path and loaded from the file
	 * system. If still not found, this class path is checked as a fallback.
	 * 
	 * @param path the path
	 */
	public JustInput(String path) {
		this.path = path;
	}

	/**
	 * @return an InputStream
	 * @throws IOException on error
	 */
	public InputStream getInputStream() throws IOException {
		return newInputStream(path, getClass().getClassLoader());
	}

	/**
	 * @return a Reader
	 * @throws IOException on error
	 */
	public Reader getReader() throws IOException {
		return newReader(path, getClass().getClassLoader());
	}

	/**
	 * @return a Reader
	 * @throws IOException on error
	 */
	public URL getUrl() throws IOException {
		return newUrl(path, getClass().getClassLoader());
	}

}