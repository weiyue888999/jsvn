package com.github.jsvn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weiguangyue
 */
public class Utils {
	
	private static final Logger log = LoggerFactory.getLogger(Utils.class);
	
	public static final FastDateFormat CHINA_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	
	public static final FastDateFormat CHINA_SIMPLE_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
	
	//避免Linux操作系统出现异常情况！！！
	private static final String DEFATUL_TIME_ZONE_STR = "Asia/Shanghai";
	
	private static final TimeZone DEFATUL_TIME_ZONE = TimeZone.getTimeZone(DEFATUL_TIME_ZONE_STR);

	private static Calendar newCalendar() {
		return Calendar.getInstance(DEFATUL_TIME_ZONE);
	}
	/**
	 * @description	： 获得一天的开始时间
	 * @param time
	 * @return
	 */
	public static Date getOneDayStartTime(Date time) {
		if(time == null) {
			throw new IllegalArgumentException("time is null");
		}
		Calendar lastDay = newCalendar();
		lastDay.setTime(time);
		lastDay.set(Calendar.HOUR_OF_DAY, 0);
		lastDay.set(Calendar.MINUTE, 0);
		lastDay.set(Calendar.SECOND, 0);
		lastDay.set(Calendar.MILLISECOND, 0);
		return lastDay.getTime();
	}
	
	/**
	 * @description	： 获得一天的技术时间
	 * @param time
	 * @return
	 */
	public static Date getOneDayEndTime(Date time) {
		if(time == null) {
			throw new IllegalArgumentException("time is null");
		}
		Calendar lastDay = newCalendar();
		lastDay.setTime(time);
		lastDay.set(Calendar.HOUR_OF_DAY, 23);
		lastDay.set(Calendar.MINUTE, 59);
		lastDay.set(Calendar.SECOND, 59);
		lastDay.set(Calendar.MILLISECOND, 999);
		return lastDay.getTime();
	}
	
	/**
	 * @description	： 获得几天前的开始时间
	 * @param count
	 * @return
	 */
	public static Date getBeforeDayStartTime(int count) {
		if(count < 0) {
			throw new IllegalArgumentException("count >= 0");
		}
		Calendar lastDay = newCalendar();
		lastDay.add(Calendar.DAY_OF_YEAR, -1*count);
		lastDay.set(Calendar.HOUR_OF_DAY, 0);
		lastDay.set(Calendar.MINUTE, 0);
		lastDay.set(Calendar.SECOND, 0);
		lastDay.set(Calendar.MILLISECOND, 0);
		return lastDay.getTime();
	}

	/**
	 * @description	： 获得几天前的结束时间
	 * @param count
	 * @return
	 */
	public static Date getBeforeDayEndTime(int count) {
		if(count < 0) {
			throw new IllegalArgumentException("count >= 0");
		}
		Calendar lastDay = newCalendar();
		lastDay.add(Calendar.DAY_OF_YEAR, -1*count);
		lastDay.set(Calendar.HOUR_OF_DAY, 23);
		lastDay.set(Calendar.MINUTE, 59);
		lastDay.set(Calendar.SECOND, 59);
		lastDay.set(Calendar.MILLISECOND, 999);
		return lastDay.getTime();
	}
	
	/**
	 * @description ： 压缩一个文件使，其成为一个zip文件
	 * @param 源文件
	 * @param 目标目录
	 * @param 要压缩成的压缩文件的名字
	 * @throws IOException
	 */
	public static void zipFileToFile(File srcFile, File destDir, String destZipFileName) throws IOException {
		File destZipFile = new File(destDir, destZipFileName);
		if (destZipFile.exists()) {
			destZipFile.delete();
		}
		OutputStream outputStream = new FileOutputStream(destZipFile);

		ZipEntry zipEntry = new ZipEntry(FilenameUtils.getName(srcFile.getName()));

		ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
		zipOutputStream.putNextEntry(zipEntry);
		zipOutputStream.closeEntry();
		zipOutputStream.close();
	}

	/**
	 * @description	： 压缩文件到一个目录
	 * @param srcDir 源目录
	 * @param destDir 目标目录
	 * @param destJarFileName 目标结果文件名称
	 * @throws IOException
	 */
	public static void jarDirToFile(File srcDir, File destDir, String destJarFileName) throws IOException{
		if(!destDir.exists()) {
			destDir.mkdirs();
		}
		File destJarFile = new File(destDir, destJarFileName);
		
		if (destJarFile.exists()) {
			destJarFile.delete();
		}
		destJarFile.createNewFile();
		
		OutputStream outputStream = new FileOutputStream(destJarFile);
		JarOutputStream jarOutputStream = new JarOutputStream(outputStream);
		doCompressJar(jarOutputStream,srcDir,"");
		jarOutputStream.flush();
		jarOutputStream.close();
		
	}
	private static void doCompressJar(JarOutputStream jarOutputStream, File dir,String base)throws IOException {
		
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();

			for (File fi : listFiles) {
				if (fi.isDirectory()) {
					//这个目录的实体
					String jarEntryName = base + fi.getName()+"/";
					JarEntry jarEntry = new JarEntry(jarEntryName);
					jarOutputStream.putNextEntry(jarEntry);
					jarOutputStream.closeEntry();
					//这个目录下面的所有文件
					doCompressJar(jarOutputStream, fi, base + fi.getName() + "/");
				} else {
					doJar(jarOutputStream, fi, base);
				}
			}
		} else {
			doJar(jarOutputStream, dir, base);
		}
	}

	private static void doJar(JarOutputStream jarOutputStream, File file, String base)throws IOException {
		String name = FilenameUtils.getName(file.getName());
		JarEntry jarEntry = new JarEntry(base + name);
		jarOutputStream.putNextEntry(jarEntry);
		InputStream in = new FileInputStream(file);
		org.apache.commons.io.IOUtils.copy(in, jarOutputStream);
		IOUtils.closeQuietly(in);
		jarOutputStream.closeEntry();
		jarEntry.clone();
	}
}
