package org.pinae.ndb.operate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pinae.ndb.action.TraversalAction;

/**
 * ndb 节点遍历
 * 
 * @author Huiyugeng
 *
 */
public class Traversal {
	
	/**
	 * ndb遍历
	 * 
	 * @param ndb 需要遍历的ndb
	 * @param action 遍历行为
	 * 
	 * @return 遍历结果
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> traversal(Map<String, Object> ndb, TraversalAction action){
		Map<String, Object> ndbResult = new HashMap<String, Object>();
		
		Set<String> keySet = ndb.keySet();
		
		for (String key : keySet){
			Object value = ndb.get(key);
			
			String tempKey = key;
			if (action != null){
				key = action.handleKey(key);
			}
			if (key == null){
				key = tempKey;
			}
			if (value instanceof String){
				if (action != null){
					value = action.handleValue(value);
				}
				ndbResult.put(key, value);
			}else if (value instanceof List){
				List<Object> list = (List<Object>)value;
				
				List<Object> _list = new ArrayList<Object>();
				for(Object item : list){
					if (item instanceof String){
						if (action != null){
							item = action.handleValue(item);
						}
						_list.add(item);
					}else if (item instanceof Map){
						_list.add(traversal((Map<String, Object>)item, action));
					}
				}
				ndbResult.put(key, _list);
			}else if (value instanceof Map){
				ndbResult.put(key, traversal((Map<String, Object>)value, action));
			}
		}
		return ndbResult;
	}
}
