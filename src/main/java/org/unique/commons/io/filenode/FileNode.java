package org.unique.commons.io.filenode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public interface FileNode {

//	public static final String URL_PREFIX_CLASSPATH = "classpath:";
//    public static final String URL_PREFIX_FILE = "file:";
//    public static final String URL_PREFIX_JAR = "jar:";
//    public static final String URL_PREFIX_ZIP = "zip:";
//    public static final String URL_PROTOCOL_FILE = "file";
//    public static final String URL_PROTOCOL_JAR = "jar";
//    public static final String URL_PROTOCOL_ZIP = "zip";
//    public static final String URL_PROTOCOL_VFS = "vfs";
//    public static final String URL_SEPARATOR_JAR = "!/";

    /**
     * 代表 Resource 名称，默认是 url/file (包含路径)
     */
    public String getRelativePathName();
    
    public void setRelativePathName(String relativePathName);

    /**
     * 打开文件输入流.
     */
    public InputStream openStream() throws FileNotFoundException;

    /**
     * 文件对象.
     */
    public File getFile() throws UnsupportedOperationException;

    /**
     * URL 对象.
     */
    public URL getURL() throws UnsupportedOperationException;

    /**
     * 文件名(不包含路径)
     */
    public String getFileName();

    /**
     * 是否存在
     */
    public boolean exist();

    /**
     * 是否是一个目录
     */
    public boolean isDirectory();

    /**
     * 是否是一个文件
     */
    public boolean isFile();

    /**
     * 文件大小 (byte 长度).
     *
     * @return 如果文件不存在，返回 -1.
     */
    public long length();

    /**
     * 最后修改时间.
     *
     * @return 如果文件不存在，返回 0.
     */
    public long lastModified();
    
}
