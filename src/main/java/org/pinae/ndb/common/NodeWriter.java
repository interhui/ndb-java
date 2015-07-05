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
	 * 保存为ndb格式文件
	 * 
	 * @param filename 文件名
	 * @param rootNode 根节点名称
	 * @param ndb Map格式的ndb数据
	 * 
	 * @throws IOException
	 */
	public void write(String filename, String rootNode, Map<String, Object> ndb) throws IOException{
		PrintWriter writer = new PrintWriter(filename);
		
		writer.write(print(rootNode, ndb));
		
		writer.flush();
		writer.close();
	}
	
	/**
	 *  将Map输出ndb格式
	 * 
	 * @param rootNode 根节点名称
	 * @param ndb 需要输出ndb格式的Map
	 * 
	 * @return ndb内容
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String print(String rootNode, Map<String, Object> ndb){
		List<String> ndbInfo = new ArrayList<String>();

		ndbInfo.add(rootNode + "{\n");

		Set<String> nodeKeySet = ndb.keySet();
		for (String nodeKey : nodeKeySet){
			Object nodeValue = ndb.get(nodeKey);
			if (nodeValue instanceof List){
				List list = (List)nodeValue;
				for (Object item : list){
					if (item instanceof Map){
						ndbInfo.add(print(nodeKey, (Map<String, Object>)item));
					}else{
						ndbInfo.add(String.format("%s:%s\n", nodeKey, item.toString()));
					}
				}
			}else if(nodeValue instanceof Map){
				ndbInfo.add(print(nodeKey, (Map<String, Object>)nodeValue));
			}else{
				ndbInfo.add(String.format("%s:%s\n", nodeKey, nodeValue.toString()));
			}
		}
		ndbInfo.add("}\n");
		
        return StringUtils.join(ndbInfo, "");
	}
	
}
