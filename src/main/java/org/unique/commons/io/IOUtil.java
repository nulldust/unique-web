package org.unique.commons.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.zip.ZipFile;

/**
 * io util
 * 
 * @author biezhi
 * @since 1.0
 */
public class IOUtil {

	private static final int DEFAULT_BUFFER_SIZE = 8192;

	public static void write(byte[] data, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(data);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			closeQuietly(os);
		}
	}

	public static void write(char[] data, File file, String charsetName) {
		write(data, file, Charset.forName(charsetName));
	}

	public static void write(char[] data, File file, Charset charset) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(new String(data).getBytes(charset));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			closeQuietly(os);
		}
	}

	public static void write(String data, File file, String charsetName) {
		write(data, file, Charset.forName(charsetName));
	}

	public static void write(String data, File file, Charset charset) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			os.write(data.getBytes(charset));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		} finally {
			closeQuietly(os);
		}
	}

	public static long copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copy(InputStream input, Writer output, String charsetName)
			throws IOException {
		return copy(new InputStreamReader(input, Charset.forName(charsetName)),
				output);
	}

	public static long copy(InputStream input, Writer output, Charset charset)
			throws IOException {
		return copy(new InputStreamReader(input, charset), output);
	}

	public static long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static void closeQuietly(ZipFile obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (IOException e) {
		}
	}

	public static void closeQuietly(Socket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
		}
	}

	public static void closeQuietly(ServerSocket socket) {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
		}
	}

	public static void closeQuietly(Selector selector) {
		try {
			if (selector != null) {
				selector.close();
			}
		} catch (IOException e) {
		}
	}

	public static void closeQuietly(URLConnection conn) {
		if (conn != null) {
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection) conn).disconnect();
			}
		}
	}

	public static void closeQuietly(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
