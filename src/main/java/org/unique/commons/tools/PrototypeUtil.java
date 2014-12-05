package org.unique.commons.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PrototypeUtil {
	
	/* 深复制 */
	public static Object deepClone(Object prototype) {

		ByteArrayOutputStream byteOut = null;
		ObjectOutputStream objOut = null;
		ByteArrayInputStream byteIn = null;
		ObjectInputStream objIn = null;

		try {
			/* 写入当前对象的二进制流 */
			byteOut = new ByteArrayOutputStream();
			objOut = new ObjectOutputStream(byteOut);
			objOut.writeObject(prototype);

			/* 读出二进制流产生的新对象 */
			byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			objIn = new ObjectInputStream(byteIn);
			
			return objIn.readObject();
		} catch (IOException e) {
			throw new RuntimeException("Clone Object failed in IO.", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class not found.", e);
		} finally {
			try {
				byteIn = null;
				byteOut = null;
				if (objOut != null){
					objOut.close();
				}
				if (objIn != null){
					objIn.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
