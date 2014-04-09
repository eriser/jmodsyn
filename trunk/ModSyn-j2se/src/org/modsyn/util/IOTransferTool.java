package org.modsyn.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Utility class to easily transfer resources. For example copying or
 * downloading files.
 * 
 * @author Erik Duijs
 */
public class IOTransferTool {

	private static final int DEFAULT_URL_CONNECT_TIME_OUT = 0;

	private static final int DEFAULT_URL_READ_TIME_OUT = 0;

	/**
	 * Internal buffer
	 */
	private final byte[] buffer;

	/**
	 * The size of the internal buffer
	 */
	private final int bufferSize;

	/**
	 * Time-out when opening a URL connection
	 */
	private int urlConnectTimeout = DEFAULT_URL_CONNECT_TIME_OUT;

	/**
	 * Time-out when reading from a URL connection
	 */
	private int urlReadTimeout = DEFAULT_URL_READ_TIME_OUT;

	/**
	 * Constructor
	 * 
	 * @param bufferSize
	 */
	public IOTransferTool(final int bufferSize) {
		this.bufferSize = bufferSize;
		this.buffer = new byte[bufferSize];
	}

	/**
	 * Constructor
	 */
	public IOTransferTool() {
		this(0x2000);
	}

	/**
	 * Set the connection time-out when opening a connection to a URL.
	 * 
	 * @param timeout
	 */
	public IOTransferTool setURLConnectTimeout(final int timeout) {
		this.urlConnectTimeout = timeout;
		return this;
	}

	/**
	 * Set the read time-out for a URL connection.
	 * 
	 * @param timeout
	 */
	public IOTransferTool setURLReadTimeout(final int timeout) {
		this.urlReadTimeout = timeout;
		return this;
	}

	/**
	 * Load a String from a URL using default character set.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public String loadString(final URL url) throws IOException {
		return loadString(url, null);
	}

	/**
	 * Load a String from a URL.
	 * 
	 * @param url
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public String loadString(final URL url, final String charsetName) throws IOException {
		InputStream is = null;
		try {
			is = openInputStream(url);
			return loadString(is, charsetName);
		} finally {
			close(is);
		}
	}

	/**
	 * Load a String from a File using default character set encoding.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String loadString(final File file) throws IOException {
		return loadString(file, null);
	}

	/**
	 * Load a String from a File.
	 * 
	 * @param file
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public String loadString(final File file, final String charsetName) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return loadString(is, charsetName);
		} finally {
			close(is);
		}
	}

	/**
	 * Load a String from an InputStream using default character set encoding.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public String loadString(final InputStream is) throws IOException {
		return loadString(is, null);
	}

	/**
	 * Load a String from an InputStream.
	 * 
	 * @param is
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public String loadString(final InputStream is, String charsetName) throws IOException {
		if (charsetName == null) {
			charsetName = Charset.defaultCharset().name();
		}
		return new String(loadBinary(is), charsetName);
	}

	/**
	 * Load a byte[] array from an InputStream.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public byte[] loadBinary(final InputStream is) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream(bufferSize);
		try {
			transfer(is, os);
		} finally {
			close(os);
		}

		return os.toByteArray();
	}

	/**
	 * Save a String to a File using default character set
	 * 
	 * @param s
	 * @param file
	 * @throws IOException
	 */
	public void saveString(final String s, final File file) throws IOException {
		saveString(s, null, file);
	}

	/**
	 * Save a String to a File
	 * 
	 * @param s
	 * @param charsetName
	 * @param file
	 * @throws IOException
	 */
	public void saveString(final String s, final String charsetName, final File file) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			saveString(s, charsetName, os);
		} finally {
			close(os);
		}
	}

	/**
	 * Save a String to an OutputStream using default character set
	 * 
	 * @param s
	 * @param os
	 * @throws IOException
	 */
	public void saveString(final String s, final OutputStream os) throws IOException {
		saveBinary(s.getBytes(), os);
	}

	/**
	 * Save a String to an OutputStream
	 * 
	 * @param s
	 * @param charsetName
	 * @param os
	 * @throws IOException
	 */
	public void saveString(final String s, String charsetName, final OutputStream os) throws IOException {
		if (charsetName == null) {
			charsetName = Charset.defaultCharset().name();
		}
		saveBinary(s.getBytes(charsetName), os);
	}

	/**
	 * Save a byte[] array to a File
	 * 
	 * @param b
	 * @param f
	 * @throws IOException
	 */
	public void saveBinary(final byte[] b, final File f) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			saveBinary(b, os);
		} finally {
			close(os);
		}
	}

	/**
	 * Save a byte[] array to an OutputStream
	 * 
	 * @param b
	 * @param os
	 * @throws IOException
	 */
	public void saveBinary(final byte[] b, final OutputStream os) throws IOException {
		final ByteArrayInputStream is = new ByteArrayInputStream(b);
		try {
			transfer(is, os);
		} finally {
			close(is);
		}
	}

	/**
	 * Transfer a resource from a URL to a File.
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public void transfer(final URL from, final File to) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(to);
			transfer(from, os);
		} finally {
			close(os);
		}
	}

	/**
	 * Transfer a resource from a URL to an OutputStream.
	 * 
	 * @param from
	 * @param os
	 * @throws IOException
	 */
	public void transfer(final URL from, final OutputStream os) throws IOException {
		InputStream is = null;
		try {
			is = openInputStream(from);
			transfer(is, os);
		} finally {
			close(os);
			close(is);
		}
	}

	/**
	 * Copy a single file
	 * 
	 * @param from
	 * @param os
	 * @throws IOException
	 */
	public void transfer(final File from, final OutputStream os) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(from);
			transfer(is, os);
		} finally {
			close(os);
			close(is);
		}
	}

	/**
	 * Copy a File.
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public void transfer(final File from, final File to) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(from);
			os = new FileOutputStream(to);
			transfer(is, os);
		} finally {
			close(os);
			close(is);
		}
	}

	/**
	 * Completely reads the InputStream and writes it to the File, and closes
	 * the stream when finished. It will make sure the streams will be buffered
	 * if necessary.
	 * 
	 * @param is
	 * @param to
	 * @throws IOException
	 */
	public void transfer(final InputStream is, final File to) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(to);
			transfer(is, os);
		} finally {
			close(os);
		}
	}

	/**
	 * Completely reads the InputStream and writes it to the OutputStream, and
	 * closes the streams when finished. It will ensure the streams will be
	 * buffered if they weren't already.
	 * 
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public void transfer(InputStream is, OutputStream os) throws IOException {
		if (!(is instanceof BufferedInputStream)) {
			is = new BufferedInputStream(is, bufferSize);
		}

		if (!(os instanceof BufferedOutputStream)) {
			os = new BufferedOutputStream(os, bufferSize);
		}

		int size;
		try {
			while ((size = is.read(buffer)) >= 0) {
				os.write(buffer, 0, size);
			}
		} finally {
			close(os);
			close(is);
		}
	}

	/**
	 * Close an InputStream, ignoring any IOExceptions. Good for closing streams
	 * in a finally block.
	 * 
	 * @param is
	 */
	private void close(final InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (final IOException ignored) {
			}
		}
	}

	/**
	 * Close an OutputStream, ignoring any IOExceptions. Good for closing
	 * streams in a finally block.
	 * 
	 * @param os
	 */
	private void close(final OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (final IOException ignored) {
			}
		}
	}

	/**
	 * Open an InputStream from a URL.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	protected InputStream openInputStream(final URL url) throws IOException {
		final URLConnection conn = url.openConnection();
		conn.setConnectTimeout(urlConnectTimeout);
		conn.setReadTimeout(urlReadTimeout);
		return conn.getInputStream();
	}
}
