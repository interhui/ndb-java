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
	public void redirect(String redirect, Object result) {
		NodeWriter writer = new NodeWriter();
		
		Map<String, Object> resultMap = null;
		if (result instanceof Map) {
			resultMap = (Map<String, Object>)result;
		} else {
			resultMap = new HashMap<String, Object>();
			resultMap.put("result", result);
		}
		try {
			writer.write(redirect, null, resultMap);
		} catch (IOException e) {
			
		}
	}
}
