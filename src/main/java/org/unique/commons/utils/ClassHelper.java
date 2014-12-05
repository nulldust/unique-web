package org.unique.commons.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unique.Const;

/**
 * class scanner
 * @author biezhi
 * @since 1.0
 */
public class ClassHelper {

    private static final Logger logger = LoggerFactory.getLogger(ClassHelper.class);
    
    /**
     * 扫描包
     * @param pack 包名
     * @param recursive 是否包含子包
     * @return
     */
    public static Set<Class<?>> scanPackage(final String pack, final boolean recursive) {
        Set<Class<?>> classes = CollectionUtil.newHashSet();
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的URL
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), Const.ENCODING);
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findClassesInPackageByFile(packageName, filePath, recursive, classes);
				// 如果是jar包文件
				} else if ("jar".equals(protocol)) {
					// 定义一个JarFile
					getClasses(classes,  url,  packageDirName,  packageName,  recursive);
				}
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return classes;
    }
    
    /**
     * getClasses(这里用一句话描述这个方法的作用)以jar的形式来获取包下的所有Class
     * @param classes 扫描的包在jar中的位置
     * @param url 扫描的url
     * @param packageDirName 包所在路径 
     * @param packageName 包名称
     * @param recursive 是否扫描子包
     */
	private static void getClasses(final Set<Class<?>> classes, final URL url, final String packageDirName, String packageName, final boolean recursive) {
		JarFile jar = null;
		try {
			// 获取jar
			jar = ((JarURLConnection) url.openConnection()).getJarFile();
			// 从此jar包 得到一个枚举类
			Enumeration<JarEntry> entries = jar.entries();
			// 同样的进行循环迭代
			while (entries.hasMoreElements()) {
				// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				// 如果是以/开头的
				if (name.charAt(0) == '/') {
					// 获取后面的字符串
					name = name.substring(1);
				}
				// 如果前半部分和定义的包名相同
				if (name.startsWith(packageDirName)) {
					int idx = name.lastIndexOf('/');
					// 如果以"/"结尾 是一个包
					if (idx != -1) {
						// 获取包名 把"/"替换成"."
						packageName = name.substring(0, idx).replace('/', '.');
					}
					// 如果可以迭代下去 并且是一个包
					if ((idx != -1) || recursive) {
						// 如果是一个.class文件 而且不是目录
						if (name.endsWith(".class") && !entry.isDirectory()) {
							// 去掉后面的".class" 获取真正的类名
							String className = name.substring(packageName.length() + 1, name.length() - 6);
							try {
								// 添加到classes
								classes.add(Class.forName(packageName + '.' + className));
							} catch (ClassNotFoundException e) {
								logger.error("添加用户自定义视图类错误 找不到此类的.class文件");
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("在扫描用户定义视图时从jar包获取文件出错：" + e.getMessage());
		}
	}

    /**
     * 以文件的形式来获取包下的所有Class
     */
    private static void findClassesInPackageByFile(final String packageName, final String packagePath, final boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if ((!dir.exists()) || (!dir.isDirectory())) {
            logger.warn("包 " + packageName + " 不是一个文件!");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {

            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    logger.error("在扫描用户定义视图时从jar包获取文件出错，找不到.class类文件：" + e.getMessage());
                }
            }
        }
    }
    
    /**
     * 根据包名和父类型扫描
     * @param packageName 要扫描的包名称
     * @param parent 继承的类或接口类型
     * @param recursive 是否递归
     * @return
     */
    public static List<Class<?>> scanClasses(final String packageName, final Class<?> parent, final boolean recursive){
    	Set<Class<?>> classSet = scanPackage(packageName, recursive);
    	List<Class<?>> classList = CollectionUtil.newArrayList(classSet.size());
    	for(Class<?> clazz : classSet){
    		if(null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent)){
    			classList.add(clazz);
    		}
    	}
    	return classList;
    }
    
    public static List<Class<?>> scanClassesByAnnotation(final String packageName, final Class<? extends Annotation> annotation, final boolean recursive){
    	Set<Class<?>> classSet = scanPackage(packageName, recursive);
    	List<Class<?>> classList = CollectionUtil.newArrayList(classSet.size());
    	for(Class<?> clazz : classSet){
    		if(null != clazz.getAnnotation(annotation)){
    			classList.add(clazz);
    		}
    	}
    	return classList;
    }
   
}
