package org.pinae.ndb.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * ndb存储
 * 
 * @author Huiyugeng
 *
 *
 */
public class NodeWriter {
	
	/**
	 * 保存文本内容
	 * 
	 * @param filename 文件名
	 * @param content 文本内容
	 * 
	 * @throws IOException IO异常
	 */
	public void write(String filename, String content) throws IOException {
		PrintWriter writer = new PrintWriter(filename);

		writer.write(content);

		writer.flush();
		writer.close();
	}

	/**
	 * 保存为ndb格式文件
	 * 
	 * @param filename 文件名
	 * @param rootNode 根节点名称
	 * @param ndb Map格式的ndb数据
	 * @param indentFlag 缩进符,默认为\t
	 * 
	 * @throws IOException IO异常
	 */
	public void write(String filename, String rootNode, Map<String, Object> ndb, String indentFlag) throws IOException {
		write(filename, print(0, rootNode, ndb, "\t"));
	}
	
	/**
	 * 保存为ndb格式文件
	 * 
	 * @param filename 文件名
	 * @param rootNode 根节点名称
	 * @param ndb Map格式的ndb数据
	 * 
	 * @throws IOException IO异常
	 */
	public void write(String filename, String rootNode, Map<String, Object> ndb) throws IOException {
		write(filename, print(0, rootNode, ndb, "\t"));
	}
	
	/**
	 * 将Map输出ndb格式
	 * 
	 * @param rootNode 根节点名称
	 * @param ndb 需要输出ndb格式的Map
	 * 
	 * @return ndb内容
	 */
	public String print(String rootNode, Map<String, Object> ndb) {
		return print(0, rootNode, ndb, "\t");
	}
	
	/**
	 * 将Map输出ndb格式
	 * 
	 * @param rootNode 根节点名称
	 * @param ndb 需要输出ndb格式的Map
	 * @param indentFlag 缩进符,默认为\t
	 * 
	 * @return ndb内容
	 */
	public String print(String rootNode, Map<String, Object> ndb, String indentFlag) {
		return print(0, rootNode, ndb, indentFlag);
	}

	/*
	 * 将Map输出ndb格式
	 * 
	 * @param indent 缩进量
	 * @param rootNode 根节点名称
	 * @param ndb 需要输出ndb格式的Map
	 * 
	 * @return ndb内容
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String print(int indent, String rootNode, Map<String, Object> ndb, String indentFlag) {
		List<String> ndbInfo = new ArrayList<String>();

		int nextIndent = indent + 1;
		
		if (StringUtils.isNotEmpty(rootNode)) {
			ndbInfo.add(StringUtils.repeat(indentFlag, indent) + rootNode + "{\n");
		} else {
			nextIndent = indent;
		}

		Set<String> nodeKeySet = ndb.keySet();
		for (String nodeKey : nodeKeySet) {
			Object nodeValue = ndb.get(nodeKey);
			if (nodeValue instanceof List) {
				List list = (List) nodeValue;
				for (Object item : list) {
					if (item instanceof Map) {
						ndbInfo.add(print(nextIndent, nodeKey, (Map<String, Object>) item, indentFlag));
					} else {
						ndbInfo.add(StringUtils.repeat(indentFlag, nextIndent) + String.format("%s:%s\n", nodeKey, item.toString()));
					}
				}
			} else if (nodeValue instanceof Map) {
				ndbInfo.add(print(nextIndent, nodeKey, (Map<String, Object>) nodeValue, indentFlag));
			} else {
				ndbInfo.add(StringUtils.repeat(indentFlag, nextIndent) + String.format("%s:%s\n", nodeKey, nodeValue.toString()));
			}
		}

		if (StringUtils.isNotEmpty(rootNode)) {
			ndbInfo.add(StringUtils.repeat(indentFlag, indent) + "}\n");
		}

		return StringUtils.join(ndbInfo, "");
	}
	
}
