package com.qbao.search.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;


public class FileUtil {
	
	final private static ESLogger logger = Loggers.getLogger(FileUtil.class);

	/**
	 * *************************************************************
	 * 构建类：Transmit
	 */
	public FileUtil() {

	}

	public static void mkdir(String path) throws IOException {
		mkdir(new File(path));
	}

	public static void mkdir(File file) throws IOException {
		if (file.exists()) {
			if (file.isFile()) {
				throw new IOException(file.getAbsolutePath()
						+ " is a exist file!");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null) {
				mkdir(parent);
			}
			file.mkdir();
		}
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(String[] files, String dest) throws IOException {
		copy(files, new File(dest), true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(File[] files, File dest) throws IOException {
		for (int i = 0; i < files.length; i++) {
			copy(files[i], dest);
		}
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(String[] files, File dest) throws IOException {
		for (int i = 0; i < files.length; i++) {
			copy(files[i], dest, true);
		}
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(File[] files, String dest) throws IOException {
		copy(files, new File(dest), true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @throws IOException
	 */
	public static void copy(String[] files, File dest, boolean cover)
			throws IOException {
		for (int i = 0; i < files.length; i++) {
			copy(files[i], dest, cover);
		}
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @throws IOException
	 */
	public static void copy(File[] files, File dest, boolean cover)
			throws IOException {
		for (int i = 0; i < files.length; i++) {
			copy(files[i], dest, cover);
		}
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @throws IOException
	 */
	public static void copy(String[] files, String dest, boolean cover)
			throws IOException {
		copy(files, new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @throws IOException
	 */
	public static void copy(File[] files, String dest, boolean cover)
			throws IOException {
		copy(files, new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(String src, String dest) throws IOException {
		copy(src, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(String src, File dest) throws IOException {
		copy(src, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(File src, String dest) throws IOException {
		copy(src, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static void copy(File src, File dest) throws IOException {
		copy(src, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(String src, String dest, boolean cover)
			throws IOException {
		copy(new File(src), new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(String src, File dest, boolean cover)
			throws IOException {
		copy(new File(src), dest, cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copy(File src, String dest, boolean cover)
			throws IOException {
		copy(src, new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：copy (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public static void copy(File src, File dest, boolean cover)
			throws IOException {
		if (!src.exists()) {
			// 源文件不存在
			throw new IOException("file " + src.getAbsolutePath()
					+ " dosn't exists!");
		} else if (src.exists() && src.isFile()) {
			// 源文件是文件
			copyFile(src, dest, cover);
		} else if (dest.exists() && dest.isFile()) {
			// 源文件是文件夹，而目标文件为文件
			throw new IOException(src.getAbsolutePath()
					+ " is a directory,but " + dest.getAbsolutePath()
					+ " is a file!");
		} else {
			// 源文件和目标文件都是文件夹
			copyDir(src, dest, cover, true);
		}
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：delete (FileOperation)
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static boolean delete(String fileName) throws IOException {
		return delete(new File(fileName));
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：delete (FileOperation)
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean delete(File file) throws IOException {
		// 如果索引文件夹存在，则先删除其内才文件,再删除该文件夹
		if (file.isDirectory() && file.exists()) {
			// 该文件夹内的文件列表
			String[] files = file.list();

			// 逐个删除
			for (int i = 0; i < files.length; i++) {
				File newFile = new File(file, files[i]);
				delete(newFile);
			}

			// 删除该文件夹
			file.delete();
		}
		// 如果是文件，则直接删除
		if (file.isFile() && file.exists()) {
			file.delete();
		}
		// 如果删除成功，返回真，否则返回假
		if (file.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：delete (FileOperation)
	 * 
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static boolean delete(String[] files) throws IOException {
		for (int i = 0; i < files.length; i++) {
			if (!delete(files[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：delete (FileOperation)
	 * 
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static boolean delete(File[] files) throws IOException {
		for (int i = 0; i < files.length; i++) {
			if (!delete(files[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String[] files, String dest) throws IOException {
		return move(files, new File(dest), true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File[] files, String dest) throws IOException {
		return move(files, new File(dest), true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String[] files, File dest) throws IOException {
		return move(files, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File[] files, File dest) throws IOException {
		return move(files, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String[] files, String dest, boolean cover)
			throws IOException {
		return move(files, new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File[] files, String dest, boolean cover)
			throws IOException {
		return move(files, new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String[] files, File dest, boolean cover)
			throws IOException {
		for (int i = 0; i < files.length; i++) {
			if (new File(files[i]).getParent().equals(dest.getAbsolutePath())) {
				continue;
			} else {
				copy(files[i], dest, cover);
				if (!delete(files[i])) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param files
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File[] files, File dest, boolean cover)
			throws IOException {
		for (int i = 0; i < files.length; i++) {
			if (files[i].getParent().equals(dest.getAbsolutePath())) {
				continue;
			} else {
				copy(files[i], dest, cover);
				if (!delete(files[i])) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String src, String dest) throws IOException {
		return move(new File(src), new File(dest), true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String src, File dest) throws IOException {
		return move(new File(src), dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, String dest) throws IOException {
		return move(src, new File(dest), true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, File dest) throws IOException {
		return move(src, dest, true);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String src, String dest, boolean cover)
			throws IOException {
		return move(new File(src), new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(String src, File dest, boolean cover)
			throws IOException {
		return move(new File(src), dest, cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, String dest, boolean cover)
			throws IOException {
		return move(src, new File(dest), cover);
	}

	/**
	 * ************************************************************* 说 明：
	 * 方法名：move (FileOperation)
	 * 
	 * @param src
	 * @param dest
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public static boolean move(File src, File dest, boolean cover)
			throws IOException {
		if (src.exists() && src.isFile()) {
			if (src.getParent().equals(dest.getAbsolutePath())) {
				return true;
			}
		} else if (src.exists() && src.isDirectory()) {
			if (src.getAbsolutePath().equals(dest.getAbsolutePath())) {
				return true;
			}
		}
		copy(src, dest, cover);
		return delete(src);
	}

	/********************** 下面是私有方法 *************************************/
	private static void copyFile(File srcFile, File dest, boolean cover)
			throws IOException {
		if (!srcFile.exists()) {
			// 源文件不存在
			throw new IOException(srcFile.getAbsolutePath() + " dosn't exists!");
		} else if (srcFile.exists() && srcFile.isDirectory()) {
			// 源文件是文件夹
			throw new IOException(srcFile.getAbsolutePath() + " is not a file!");
		}

		if (dest.exists() && dest.isFile()) {
			fileToFile(srcFile, dest, cover);
		} else {
			if (!dest.exists()) {
				dest.mkdirs();
			}
			fileToFile(srcFile,
					new File(dest.getAbsolutePath() + "/" + srcFile.getName()),
					cover);
		}
	}

	private static void copyDir(File srcDir, File destDir, boolean cover,
			boolean attachDir) throws IOException {
		if (!srcDir.exists()) {
			// 源文件夹不存在
			throw new IOException(srcDir.getAbsolutePath() + " dosn't exists!");
		} else if (srcDir.exists() && srcDir.isFile()) {
			// 源是个文件
			throw new IOException(srcDir.getAbsolutePath()
					+ " is not a directory!");
		}

		if (destDir.exists() && destDir.isFile()) {
			// 目标是文件
			throw new IOException(destDir.getAbsolutePath()
					+ " is not a directory!");
		} else if (!destDir.exists()) {
			destDir.mkdirs();
		}
		if (true == attachDir) {
			destDir = new File(destDir.getAbsolutePath() + "/"
					+ srcDir.getName());
		}
		destDir.mkdir();
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			copy(files[i], destDir, cover);
		}
	}

	private static void fileToFile(File srcFile, File destFile, boolean cover)
			throws IOException {
		if (srcFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
			return;
		}
		if (!srcFile.exists()) {
			// 源文件不存在
			throw new IOException(srcFile.getAbsolutePath() + " dosn't exists!");
		} else if (srcFile.exists() && srcFile.isDirectory()) {
			// 源文件是文件夹
			throw new IOException(srcFile.getAbsolutePath() + " is not a file!");
		} else if (destFile.exists() && destFile.isDirectory()) {
			// 目标不可为文件夹
			throw new IOException(destFile.getAbsolutePath()
					+ " can't be a directory!");
		} else if (destFile.exists()
				&& destFile.getName().equals(srcFile.getName())) {
			if (true == cover) {
				destFile.delete();
			} else {
				return;
			}
		} else if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileInputStream in = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(destFile);
		// 该文件的大小长度
		int length = (int) srcFile.length();
		if (0 >= length) {
			in.close();
			out.close();
			return;
		}
		// 每次传输的大小
		final int COUNT = 4500000;
		// 传输的次数
		int times = length / COUNT;
		int count = COUNT;
		byte[] buffer = new byte[count];
		int j = 0;
		// 最后一次
		if (j >= times) {
			count = length % COUNT;
		}
		// 一段一段的传输
		while (in.read(buffer, 0, count) != -1) {
			j++;
			out.write(buffer, 0, count);
			// 最后一次
			if (j >= times) {
				count = length % COUNT;
			}
		}
		in.close();
		out.close();
	}

	public static File createRandomDir( File parent )throws IOException{
		File tmp;
		long timeStamp = System.currentTimeMillis();
		int i = 0;
		do{
			tmp = new File( parent , timeStamp + "_" + i++ );
		}while( tmp.exists() );
		tmp.mkdirs();
		if( !tmp.exists() || !tmp.isDirectory() ){
			throw new IOException( "File:" + tmp + " does not exist or is not a directory finally!" );
		}
		return tmp;
	}
	
	public static File createRandomFile( File parent )throws IOException{
		parent.mkdirs();
		if( !parent.exists() || !parent.isDirectory() ){
			throw new IOException( "Parent:" + parent + " does not exist or is not a " +
					"directory finally!" );
		}
		File tmp;
		long timeStamp = System.currentTimeMillis();
		int i = 0;
		do{
			tmp = new File( parent , timeStamp + "_" + i++ );
		}while( tmp.exists() );
		tmp.createNewFile();
		if( !tmp.exists() || !tmp.isFile() ){
			throw new IOException( "File:" + tmp + " does not exist or is not a file finally!" );
		}
		return tmp;
	}
	
	
	public static void delete(File currIndexFile, int parentLevel)
			throws IOException {
		File parent = currIndexFile;
		while(parentLevel-- > 0){
			parent = parent.getParentFile();
			if(parent == null){
				throw new IllegalArgumentException(
					"parentLevel:" + parentLevel + " exceeds the max");
			}
		}

		File[] files = parent.listFiles();
		if(files == null){
			return;
		}
		
		String currUniquePath = currIndexFile.getCanonicalPath();
		for(File file:files){
			if(!currUniquePath.startsWith(file.getCanonicalPath())){
				FileUtil.delete(file);
				logger.info("delete:{}", file);
			}
		}
		
	}

}
