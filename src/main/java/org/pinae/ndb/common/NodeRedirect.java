package org.pinae.ndb.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ndb输出重定向
 * 
 * ndb执行结果重定向输出: 文件/网络
 * 
 * @author Huiyugeng
 *
 *
 */
public class NodeRedirect {
	
	@SuppressWarnings("unchecked")
	public void redirect(String target, Object ndb) {
		NodeWriter writer = new NodeWriter();
		
		Map<String, Object> resultMap = null;
		if (ndb instanceof Map) {
			resultMap = (Map<String, Object>)ndb;
		} else {
			resultMap = new HashMap<String, Object>();
			resultMap.put("result", ndb);
		}
		
		if (target.equalsIgnoreCase("print")) {
			System.out.println(writer.print(null, resultMap));
		} else {
			try {
				writer.write(target, null, resultMap);
			} catch (IOException e) {
				
			}
		}
	}
}
