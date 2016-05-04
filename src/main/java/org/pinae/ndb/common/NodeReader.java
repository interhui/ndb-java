package org.pinae.ndb.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * ndbd读取
 * 
 * 将ndb读取并解析为Map类型
 * 
 * @author Huiyugeng
 *
 *
 */
public class NodeReader {

	private int linenum = 0; // 配置行号
	
	/**
	 * 载入文本文件
	 * 
	 * @param filename 文件名
	 * 
	 * @return 文件内容
	 * 
	 * @throws IOException IO异常处理
	 */
	public List<String> readAsList(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		return readAsList(br);
	}
	
	/**
	 * 载入文本文件
	 * 
	 * @param file 文件名
	 * 
	 * @return 文件内容
	 * 
	 * @throws IOException IO异常处理
	 */
	public List<String> readAsList(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		return readAsList(br);
	}
	
	private List<String> readAsList(BufferedReader br) throws IOException {
		List<String> content = new ArrayList<String>();
		if (br != null) {
			String line = "";
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
			br.close();
		}
		return content;
	}

	/**
	 * 载入解析ndb文件内容
	 * 
	 * @param filename 文件名
	 * 
	 * @return 解析后的ndb信息
	 * 
	 * @throws IOException IO异常处理
	 */
	public Map<String, Object> read(String filename) throws IOException {
		List<String> content = readAsList(filename);
		Map<String, Object> ndb = parse(content);
		return ndb;
	}
	
	/**
	 * 载入解析ndb文件内容
	 * 
	 * @param file 文件对象
	 * 
	 * @return 解析后的ndb信息
	 * 
	 * @throws IOException IO异常处理
	 */
	public Map<String, Object> read(File file) throws IOException {
		List<String> content = readAsList(file);
		Map<String, Object> ndb = parse(content);
		return ndb;
	}

	/*
	 * 解析为Map
	 * 
	 * @param fileContentList 文件内容列表
	 * 
	 * @return 解析后的Map
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> parse(List<String> fileContentList) {
		Map<String, Object> ndb = new HashMap<String, Object>();

		while (linenum < fileContentList.size()) {
			try {
				String line = fileContentList.get(linenum).trim();

				linenum++;

				if (line == null || line.equals("") || StringUtils.startsWith(line, "#")) {
					continue;
				}

				if (line.endsWith("{")) {
					String nodeName = line.substring(0, line.indexOf("{")).trim();
					Map<String, Object> nodeValue = parse(fileContentList);

					Object node = ndb.get(nodeName);
					if (node == null) {
						ndb.put(nodeName, nodeValue);
					} else {
						List nodeList = null;
						if (node instanceof List) {
							nodeList = (List) node;
						} else {
							nodeList = new ArrayList();
							nodeList.add(node);
						}
						nodeList.add(nodeValue);
						ndb.put(nodeName, nodeList);
					}

				} else if (line.endsWith("}")) {
					return ndb;
				} else {
					if (line.indexOf(":") > 0) {
						String items[] = new String[2];

						items[0] = line.substring(0, line.indexOf(":")).trim();
						items[1] = line.substring(line.indexOf(":") + 1, line.length()).trim();

						Object itemValue = ndb.get(items[0]);
						if (itemValue != null) {
							List itemValueList = null;
							if (itemValue instanceof List) {
								itemValueList = (List) itemValue;
							} else {
								itemValueList = new ArrayList();
								itemValueList.add(itemValue);
							}
							itemValueList.add(items[1]);

							ndb.put(items[0], itemValueList);
						} else {
							ndb.put(items[0], items[1]);
						}
					}
				}
			} catch (Exception e) {

			}
		}
		return ndb;
	}
}
