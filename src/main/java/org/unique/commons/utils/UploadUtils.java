package org.unique.commons.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * upload utils
 * @author biezhi
 * @since 1.0
 */
public class UploadUtils {

    /**
     * 文件允许格式
     */
    public static final String[] FILE_TYPE = { ".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv" };

    /**
     * 图片允许格式
     */
    public static final String[] PHOTO_TYPE = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
    
    public static final String MULTIPART = "multipart/";

    public static boolean isFileType(final String fileName, final String[] typeArray) {
        for (String type : typeArray) {
            if (fileName.toLowerCase().endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归获得目录的所有地址
     */
    public static List<java.io.File> getFiles(final String realpath, final List<File> files, final String[] fileType) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files, fileType);
                } else {
                    if (isFileType(file.getName(), fileType)) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

    /**
     * 得到文件上传的相对路径
     */
    public static String getUploadPath(final String fileName, final long time) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
        String uploadPath = "/upload/" + formater.format(new Date()) + "/" + time + getFileExt(fileName);
        File dir = new File(getWebRoot() + uploadPath);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return uploadPath;
    }

    /**
     * 获取项目物理路径
     */
    public static String getWebRoot() {
        File f = new File("");
        return f.getAbsoluteFile().getAbsoluteFile().getPath();
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExt(final String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 删除物理文件
     */
    public static void deleteFile(final String path) {
        File file = new File(getWebRoot() + path);
        file.delete();
    }
    
    public static boolean isMultipartContent(final HttpServletRequest request){
    	if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
    	String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART)) {
            return true;
        }
    	return false;
    }
}
