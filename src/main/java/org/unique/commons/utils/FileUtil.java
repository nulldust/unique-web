/**
 * Copyright (c) 2014-2015, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unique.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 文件操作工具类
 * 
 * @author biezhi
 * @since 1.0
 */
public class FileUtil {

	public static final int BUFFER_SIZE = 4096;

	public static int copy(File in, File out) throws IOException {
		return copy(new BufferedInputStream(new FileInputStream(in)),
				new BufferedOutputStream(new FileOutputStream(out)));
	}

	public static void copy(byte[] in, File out) throws IOException {
		ByteArrayInputStream inStream = new ByteArrayInputStream(in);
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(
				out));
		copy(inStream, outStream);
	}

	public static byte[] copyToByteArray(File in) throws IOException {
		return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	public static void copy(byte[] in, OutputStream out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}

	public static int copy(Reader in, Writer out) throws IOException {
		try {
			int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}
	
	public static void copy(String in, Writer out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}
	
	/**
	 * 复制字符串
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}
	
	/**
	 * 一行一行读取
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		List<String> list = CollectionUtil.newArrayList();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}
	
	/**
	 * 读取文件
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		Reader in = new FileReader(file);
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}

	/**
	 * 读取文件
	 * @param file
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readFile(File file, String encoding)
			throws IOException {
		InputStream inputStream = new FileInputStream(file);
		return toString(encoding, inputStream);
	}
	
	/**
	 * 输出文本文件
	 * @param inputStream
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String toString(InputStream inputStream)
			throws UnsupportedEncodingException, IOException {
		Reader reader = new InputStreamReader(inputStream);
		StringWriter writer = new StringWriter();
		copy(reader, writer);
		return writer.toString();
	}

	/**
	 * 输出文本文件
	 * @param encoding
	 * @param inputStream
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static String toString(String encoding, InputStream inputStream)
			throws UnsupportedEncodingException, IOException {
		Reader reader = new InputStreamReader(inputStream, encoding);
		StringWriter writer = new StringWriter();
		copy(reader, writer);
		return writer.toString();
	}

	/**
	 * 保存文本文件
	 * @param file
	 * @param content
	 */
	public static void saveFile(File file, String content) {
		saveFile(file, content, null, false);
	}

	/**
	 * 保存文本文件
	 * @param file
	 * @param content
	 * @param append
	 */
	public static void saveFile(File file, String content, boolean append) {
		saveFile(file, content, null, append);
	}

	/**
	 * 保存文本文件
	 * @param file
	 * @param content
	 * @param encoding
	 */
	public static void saveFile(File file, String content, String encoding) {
		saveFile(file, content, encoding, false);
	}

	/**
	 * 保存文本文件
	 * @param file
	 * @param content
	 * @param encoding
	 * @param append
	 */
	public static void saveFile(File file, String content, String encoding, boolean append) {
		try {
			FileOutputStream output = new FileOutputStream(file, append);
			Writer writer = StringUtils.isBlank(encoding) ? new OutputStreamWriter(output) : new OutputStreamWriter(output, encoding);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到相对路径
	 */
	public static String getRelativePath(File baseDir, File file) {
		if (baseDir.equals(file)) {
			return "";
		}
		if (baseDir.getParentFile() == null) {
			return file.getAbsolutePath().substring(
					baseDir.getAbsolutePath().length());
		}
		return file.getAbsolutePath().substring(
				baseDir.getAbsolutePath().length() + 1);
	}

	/**
	 * 获取文件输入流
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getInputStream(String file)
			throws FileNotFoundException {
		InputStream inputStream = null;
		if (file.startsWith("classpath:")) {
			inputStream = FileUtil.class.getClassLoader().getResourceAsStream(
					file.substring("classpath:".length()));
		} else {
			inputStream = new FileInputStream(file);
		}
		return inputStream;
	}

	/**
	 * 创建文件夹
	 * @param dir
	 * @param file
	 * @return
	 */
	public static File mkdir(String dir, String file) {
		if (dir == null)
			throw new IllegalArgumentException("dir must be not null");
		File result = new File(dir, file);
		parnetMkdir(result);
		return result;
	}
	
	/**
	 * 创建文件夹
	 * @param outputFile
	 */
	public static void parnetMkdir(File outputFile) {
		if (outputFile.getParentFile() != null) {
			outputFile.getParentFile().mkdirs();
		}
	}
	
	/**
	 * 根据classLoader获取文件
	 * @param resourceName
	 * @return
	 * @throws IOException
	 */
	public static File getFileByClassLoader(String resourceName)
			throws IOException {
		Enumeration<URL> urls = FileUtil.class.getClassLoader().getResources(
				resourceName);
		while (urls.hasMoreElements()) {
			return new File(urls.nextElement().getFile());
		}
		throw new FileNotFoundException(resourceName);
	}

	/**
	 * 获取文件后缀
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = filename.lastIndexOf(".");
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index);
		}
	}

	/**
	 * 删除目录
	 * @param directory
	 * @throws IOException
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}

		cleanDirectory(directory);
		if (!directory.delete()) {
			String message = "Unable to delete directory " + directory + ".";
			throw new IOException(message);
		}
	}

	/**
	 * 删除文件
	 * @param file
	 * @return
	 */
	public static boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				cleanDirectory(file);
			}
		} catch (Exception e) {
		}

		try {
			return file.delete();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 清空目录文件
	 * @param directory
	 * @throws IOException
	 */
	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			String message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!directory.isDirectory()) {
			String message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) { // null if security restricted
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception) {
			throw exception;
		}
	}
	
	/**
	 * 删除目录
	 * @param file
	 * @throws IOException
	 */
	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			boolean filePresent = file.exists();
			if (!file.delete()) {
				if (!filePresent) {
					throw new FileNotFoundException("File does not exist: "
							+ file);
				}
				String message = "Unable to delete file: " + file;
				throw new IOException(message);
			}
		}
	}

	/**
	 * 判断文件是否存在
	 */
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}

	/**
	 * 判断是否是文件
	 * @param filePath
	 * @return
	 */
	public static boolean isFile(String filePath) {
		return new File(filePath).exists() && new File(filePath).isFile();
	}

	/**
	 * 判断是否是目录
	 * @param filePath
	 * @return
	 */
	public static boolean isDir(String filePath) {
		return new File(filePath).exists() && new File(filePath).isDirectory();
	}

	/**
	 * 获取文件后缀
	 */
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf("/") + 1);
	}

	/**
	 * 获取文件不带后缀的全路径
	 */
	public static String getNoSuffixFilePath(String filePath) {
		return filePath.substring(0, filePath.lastIndexOf("."));
	}
	
	/**
	 * 根据路径获取文件列表
	 * @param paths
	 * @return
	 */
	public static List<File> getFiles(String paths) {
		List<File> filesList = new ArrayList<File>();
		for (final String path : paths.split(File.pathSeparator)) {
			final File file = new File(path);
			if (file.isDirectory()) {
				recurse(filesList, file);
			} else {
				filesList.add(file);
			}
		}
		return filesList;
	}

	private static void recurse(List<File> filesList, File f) {
		File list[] = f.listFiles();
		for (File file : list) {
			if (file.isDirectory()) {
				recurse(filesList, file);
			} else {
				filesList.add(file);
			}
		}
	}

	/**
	 * List the content of the given jar
	 * 
	 * @param jarPath
	 * @return
	 * @throws IOException
	 */
	public static List<String> getJarContent(String jarPath) throws IOException {
		List<String> content = CollectionUtil.newArrayList();
		@SuppressWarnings("resource")
		JarFile jarFile = new JarFile(jarPath);
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry entry = (JarEntry) e.nextElement();
			String name = entry.getName();
			content.add(name);
		}
		return content;
	}

}