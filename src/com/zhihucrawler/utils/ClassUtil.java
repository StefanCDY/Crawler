package com.zhihucrawler.utils;

/**
 * @author Stefan
 *
 */
public class ClassUtil {
	/**
	 * @param c
	 * @return String 返回class文件所在的目录
	 */
	public static String getClassPath(Class<?> c) {
		return c.getResource("").getPath().replaceAll("%20", " ").replaceFirst("/", "");
	}

	/**
	 * @param c
	 * @return String 返回class文件所在项目的根目录
	 */
	public static String getClassRootPath(Class<?> c) {
		return c.getResource("/").getPath().replaceAll("%20", " ").replaceFirst("/", "");
	}
	
	/**
	 * @param c
	 * @param hasName 是否包括class名
	 * @return String 返回class文件所在的目录
	 */
	public static String getClassPath(Class<?> c, boolean hasName) {
		String name = c.getSimpleName() + ".class";
		String path = c.getResource(name).getPath().replaceAll("%20", " ").replaceFirst("/", "");
		if (hasName) {
			return path;
		} else {			
			return path.substring(0, path.length() - name.length());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(getClassPath(ClassUtil.class));
		System.out.println(getClassRootPath(ClassUtil.class));
		System.out.println(getClassPath(ClassUtil.class, false));
		System.out.println(getClassPath(ClassUtil.class, true));
	}

}
