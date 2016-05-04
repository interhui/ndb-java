package org.pinae.ndb;

import java.io.IOException;
import java.util.Map;

import org.pinae.ndb.common.NodeReader;
import org.pinae.ndb.common.NodeWriter;

public class Ndb {
	/**
	 * 执行ndb语句
	 * 
	 * @param query 需要执行的ndb语句
	 * @param ndb ndb信息
	 * 
	 * @return 执行结果
	 */
	public static Object execute(Object ndb, String query) {
		return new Statement().execute(ndb, query);
	}
	
	/**
	 * 将Map输出ndb格式
	 * 
	 * @param rootNode 根节点名称
	 * @param ndb 需要输出ndb格式的Map
	 * 
	 * @return ndb内容
	 */
	public static String print(String rootNode, Map<String, Object> ndb) {
		return new NodeWriter().print(rootNode, ndb);
	}

	/**
	 * 保存为ndb格式文件
	 * 
	 * @param filename 文件名
	 * @param rootNode 根节点名称
	 * @param ndb Map格式
	 * 
	 * @throws IOException IO异常
	 */
	public static void write(String filename, String rootNode, Map<String, Object> ndb) throws IOException {
		new NodeWriter().write(filename, rootNode, ndb);
	}

	/**
	 * 载入解析ndb文件内容
	 * 
	 * @param filename 文件名
	 * 
	 * @return 解析后的ndb信息
	 * 
	 * @throws IOException 异常处理
	 */
	public static Map<String, Object> read(String filename) throws IOException {
		return new NodeReader().read(filename);
	}
}
