package cn.ssm.utils.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FileUtils {

	public static StringBuffer read2StringBuffer(String filePath) {
		File file = new File(filePath);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			int length = 0;
			byte[] b = new byte[1024];
			while ((length = bufferedInputStream.read(b)) != -1) {
				String string = new String(b, 0, length, "UTF-8");
				stringBuffer.append(string);
			}
			bufferedInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringBuffer;
	}

	public static StringBuffer read2StringBuffer(File file) {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
			int length = 0;
			byte[] b = new byte[1024];
			while ((length = bufferedInputStream.read(b)) != -1) {
				String string = new String(b, 0, length, "UTF-8");
				stringBuffer.append(string);
			}
			bufferedInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringBuffer;
	}

}
