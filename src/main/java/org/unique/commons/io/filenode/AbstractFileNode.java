package org.unique.commons.io.filenode;

import java.io.File;
import java.net.URL;

import org.unique.commons.tools.PathUtils;


public abstract class AbstractFileNode implements FileNode {
	
	protected String relativePathName;

	@Override
	public String getRelativePathName() {
		return this.relativePathName;
	}

	@Override
	public void setRelativePathName(String relativePathName) {
		this.relativePathName = relativePathName;
	}

	@Override
	public File getFile() throws UnsupportedOperationException {
		return PathUtils.urlAsFile(getURL());
	}

	@Override
	public URL getURL() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

	@Override
	public String getFileName() {
		int slash = relativePathName.lastIndexOf('/');
        if (slash >= 0) {
            return relativePathName.substring(slash + 1);
        }
        return relativePathName;
	}

	@Override
	public boolean exist() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public long length() {
		return -1;
	}

	@Override
	public long lastModified() {
		return 0;
	}
	
}
