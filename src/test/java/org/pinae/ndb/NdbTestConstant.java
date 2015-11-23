package org.pinae.ndb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NdbTestConstant {
	public final static String EXAMPLE_FILE = "src/test/java/resource/example.ndb";
	
	public static List<Map<String, Object>> getTestList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> data1 = new HashMap<String, Object>();
		data1.put("masking", "true");

		Map<String, Object> data2 = new HashMap<String, Object>();
		data2.put("masking", "true");
		
		Map<String, Object> data3 = new HashMap<String, Object>();
		data3.put("masking", "false");
		
		list.add(data1);
		list.add(data2);
		list.add(data3);
		
		return list;
	}
}
