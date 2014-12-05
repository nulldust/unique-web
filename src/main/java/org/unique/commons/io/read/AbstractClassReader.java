package org.unique.commons.io.read;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.Const;
import org.unique.commons.utils.CollectionUtil;
import org.unique.commons.utils.Validate;

public abstract class AbstractClassReader implements ClassReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractClassReader.class);
	
	@Override
	public List<Class<?>> getClass(String packageName, boolean recursive) {
		return this.getClassByAnnotation(packageName, null, null, recursive);
	}

	/**
	 * 默认实现以文件形式的读取
	 */
	@Override
	public List<Class<?>> getClass(String packageName, Class<?> parent, boolean recursive) {
        return this.getClassByAnnotation(packageName, parent,  null, recursive);
	}
	
	/**
	 * 根据条件获取class
	 * @param packageName
	 * @param packagePath
	 * @param parent
	 * @param annotation
	 * @param recursive
	 * @return
	 */
	private List<Class<?>> findClassByPackage(final String packageName, final String packagePath, final Class<?> parent, final Class<? extends Annotation> annotation, final boolean recursive) {
        List<Class<?>> classes = CollectionUtil.newArrayList();
		// 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if ((!dir.exists()) || (!dir.isDirectory())) {
        	LOGGER.warn("包 " + packageName + " 不是一个文件!");
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = accept(dir, recursive);
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
            	findClassByPackage(packageName + "." + file.getName(), file.getAbsolutePath(), parent, annotation, recursive);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                	Class<?> clazz = Class.forName(packageName + '.' + className);
                	if(null != parent){
                		if(null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent)){
                			classes.add(clazz);
                		}
                	}
                	if(null != annotation){
                		if(null != clazz.getAnnotation(annotation)){
                			classes.add(clazz);
                		}
                	}
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                	LOGGER.error("在扫描用户定义视图时从jar包获取文件出错，找不到.class类文件：" + e.getMessage());
                }
            }
        }
        return classes;
    }
	
	/**
	 * 过滤文件规则
	 * @param file
	 * @param recursive
	 * @return
	 */
	private File[] accept(File file, final boolean recursive){
		File[] dirfiles = file.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
		return dirfiles;
	}
	
	@Override
	public List<Class<?>> getClassByAnnotation(String packageName, Class<? extends Annotation> annotation, boolean recursive) {
		return this.getClassByAnnotation(packageName, null, annotation, recursive);
	}

	@Override
	public List<Class<?>> getClassByAnnotation(String packageName, Class<?> parent, Class<? extends Annotation> annotation, boolean recursive) {
		Validate.notBlank(packageName);
		Validate.notNull(parent);
		List<Class<?>> classes = CollectionUtil.newArrayList();
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的URL
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
				// 获取包的物理路径
				String filePath = URLDecoder.decode(url.getFile(), Const.ENCODING);
				List<Class<?>> subClasses = findClassByPackage(packageName, filePath, parent, annotation, recursive);
				if(subClasses.size() > 0){
					classes.addAll(subClasses);
				}
            }
        } catch (IOException e) {
        	LOGGER.error(e.getMessage());
        }
        return classes;
	}

}
