package com.assist.parser.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import com.yicai.medialab.writingmaster.extraction.core.api.InfoExtraction;

/**
 * 
 * @author Pactera-NEN
 * @date 2016年3月30日-下午4:46:52
 */
public class ClassTest {
	public Map<String, Object> extractFromRawData(String jarFilePath, Map<String, Object> rawData) {
		InfoExtraction extraction = null;
		List<String> classNameList = JarUtil.extractFullClassNames(jarFilePath);
		
		try {
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] { new URL("file:" + jarFilePath) },
					Thread.currentThread().getContextClassLoader());
			for (String className : classNameList) {
				Class<?> clazz = urlClassLoader.loadClass(className);
				if (ClassUtil.containInterface(clazz, InfoExtraction.class)) {
					extraction = (InfoExtraction) clazz.newInstance();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (extraction == null) 
			throw new IllegalArgumentException(jarFilePath + "没有包含可以实例化成InfoExtraction的类");
		
		try {
			extraction.extractFromRawData(rawData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

