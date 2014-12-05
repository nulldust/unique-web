package org.unique.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化类
 * @author biezhi
 * @since 1.0
 */
public class SerializeUtil {

	/**
	 * 序列化
	 * @param object 要被序列化的对象
	 * @return 序列化后的字节码
	 */
	public static byte[] serialize(Serializable object) {
		byte[] bytes = null;
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
					bytes = baos.toByteArray();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return bytes;
	}

	/**
	 * 反序列化
	 * @param bytes 反序列化的字节码
	 * @return 反序列化后的对象
	 */
	public static Serializable unserialize(byte[] bytes) {
		Serializable obj = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			obj = (Serializable) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return obj;
	}

}