package org.pinae.ndb.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * ndb格式解析器
 * 
 * 将ndb数据信息解析为Map类型
 * 
 * @author Huiyugeng
 *
 *
 */
public class NodeReader {
	
	private int linenum = 0; //配置行号
	
	/**
	 * 载入解析ndb文件内容
	 * 
	 * @param filename 文件名
	 * 
	 * @return 解析后的ndb信息
	 * 
	 * @throws IOException 异常处理
	 */
	public Map<String, Object> read(String filename) throws IOException{
		List<String> fileContentList = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = "";
		while ((line = br.readLine()) != null) {
			 fileContentList.add(line);
		}
		br.close();
		
		Map<String, Object> ndb = parse(fileContentList);
		
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
	private Map<String, Object> parse(List<String> fileContentList){
		Map<String, Object> ndb = new HashMap<String, Object>();
		
		while(linenum < fileContentList.size()){
			
			String line = fileContentList.get(linenum).trim();
			
			linenum++;
			
			if(line==null || line.equals("") || StringUtils.startsWith(line, "#")){
				continue;
			}

			if(line.endsWith("{")){
				String nodeName = line.substring(0, line.indexOf("{"));
				Map<String, Object> nodeValue = parse(fileContentList);
				
				Object node = ndb.get(nodeName);
				if(node==null){
					ndb.put(nodeName, nodeValue);
				}else{
					List nodeList = null;
					if(node instanceof List){
						nodeList = (List)node;
					}else{
						nodeList = new ArrayList();
						nodeList.add(node);
					}
					nodeList.add(nodeValue);
					ndb.put(nodeName, nodeList);
				}
				
				
			}else if(line.endsWith("}")){
				return ndb;
			}else {
				String items[] = new String[2];
				if(line.indexOf(":")>0){
					items[0] = line.substring(0, line.indexOf(":"));
					items[1] = line.substring(line.indexOf(":") + 1, line.length());
				}
				
				Object itemValue = ndb.get(items[0]);
				if (itemValue != null){
					List itemValueList = null;
					if (itemValue instanceof List){
						itemValueList = (List)itemValue;
					}else{
						itemValueList = new ArrayList();
						itemValueList.add(itemValue);
					}
					itemValueList.add(items[1]);
					
					ndb.put(items[0], itemValueList);
				}else{
					ndb.put(items[0], items[1]);
				}
				
			}
			
			
		}
		
		return ndb;
	}
}
