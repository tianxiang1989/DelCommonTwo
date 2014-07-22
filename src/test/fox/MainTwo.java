package test.fox;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * 去除文件注释 [路径的根目录是桌面]
 * 2014年7月22日
 */
public class MainTwo {
	/**记录处理文件的个数*/
	int count = 0;

	/**处理目录*/
	public void changeDir(File src, File des) throws Exception {
		if (!src.exists()) {
			System.out.println("输入路径错误,结束运行");
			return;
		}
		if (!des.exists()) {
			des.mkdir(); // 创建目录
		}
		File[] files = src.listFiles(); // 获取目录中的文件

		for (int i = 0; i < files.length; i++) {
			// 下一级目录/文件
			File nf = new File(des, files[i].getName());
			if (files[i].isDirectory()) {
				// 是目录，递归
				changeDir(files[i], nf);
			} else { // 文件
				changeFile(files[i], nf);
			}
		}
		System.out.println("done!");
		System.out.print("处理个数：" + count + "个,");
	}

	/**处理文件*/
	public void changeFile(File src, File des) throws Exception {
		// 只处理java文件
		if (!src.toString().endsWith("java")) {
			return;
		}
		StringBuffer content = new StringBuffer();
		String temp;
		int start;
		int end;
		int from = 0;

		BufferedReader br = new BufferedReader(new FileReader(src));
		BufferedWriter bw = new BufferedWriter(new FileWriter(des));

		while ((temp = br.readLine()) != null) {
			content.append(temp);
			content.append("\n ");
		}

		// 去除/**/注释
		while ((start = content.indexOf("/*", from)) != -1) {
			end = content.indexOf("*/", start) + 2;
			content.delete(start, end);
			from = start;
		}

		// 去除//注释
		from = 0;
		int e;
		// 指定from以提高速度
		while ((start = content.indexOf("// ", from)) != -1) {
			e = content.indexOf("\n ", start);
			end = (e == -1) ? content.length() : (e + 1);
			content.delete(start, end);
			from = start;
		}
		bw.write(content.toString());
		br.close();
		bw.close();
		count++;
		System.out.println("转换完成:" + src);
	}

	public static void main(String[] args) {
		Properties prop = System.getProperties();
		// 获取桌面位置
		String sysDir = prop.getProperty("user.home") + "\\Desktop\\";
		System.out.println("请输入需要转换的文件夹名称[回车确定]：");
		System.out.print(sysDir);
		// 获取控制台输入
		Scanner sca = new Scanner(System.in);
		String dirConvert = sca.next();
		String dirPath = sysDir + dirConvert;
		File src = new File(dirPath); //源文件
		File des = new File(dirPath + "_无注释"); //目标文件
		long startTime = System.currentTimeMillis();
		try {
			new MainTwo().changeDir(src, des);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		// 记录结束时间
		long endTime = System.currentTimeMillis();
		System.out.println("用时：" + (endTime - startTime) / 1000 + "秒");
	}
}