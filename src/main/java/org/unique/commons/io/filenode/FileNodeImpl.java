package org.unique.commons.io.filenode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import org.unique.commons.tools.Validate;

public final class FileNodeImpl extends AbstractFileNode {
	
    private final File file;

    public FileNodeImpl(File file) {
        this.file = file;
        this.relativePathName = file.getPath();
    }

    public FileNodeImpl(URL url) {
    	
        Validate.notNull(url);

        String file = url.getPath();
        try {
            file = URLDecoder.decode(file, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        this.file = new File(file);
        this.relativePathName = file;
    }

    @Override
    public InputStream openStream() throws FileNodeNotFoundException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileNodeNotFoundException(e);
        }
    }

    @Override
    public File getFile() {
        return file;
    }
    
    @Override
    public URL getURL() {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public boolean exist() {
        return file.exists();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public long length() {
        return file.length();
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
