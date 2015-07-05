package org.pinae.ndb.operate;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pinae.ndb.action.OperationAction;

/**
 * ndb节点更新
 * 
 * @author Huiyugeng
 *
 */
public class Update extends Locator {
	private Map<String, String> updateMap = null; //需要更新的键值映射
	
	private OperationAction action = null; //需要执行的行为
	
	/**
	 * 更新ndb中的值
	 * 
	 * @param ndb 需要更新的ndb
	 * @param path 更新路径
	 * @param updateValue 需要更新的值，值采用 key1=value1,key2=value2的模式
	 * 
	 * @return 更新后的ndb
	 */
	public Object update (Map<String, Object> ndb, String path, String updateValue){
		updateMap = convertValueMap(updateValue);
		
		locate(ndb, path);
		
		return ndb;
	}
	
	/**
	 * 更新ndb中的值
	 * 
	 * @param ndb 需要更新的ndb
	 * @param path 更新路径
	 * @param action 更细动作
	 * 
	 * @return 更新后的ndb
	 */
	public Object update (Map<String, Object> ndb, String path, OperationAction action){
		this.action = action;
		
		locate(ndb, path);
		
		return ndb;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doAction(Object item) {
		if (action != null){
			action.handle(item);
		} else {
			if (item != null && item instanceof Map){
				Map map = (Map)item;
				
				Set<Map.Entry<String, String>> entrySet = (Set<Entry<String, String>>)updateMap.entrySet();
				for(Entry<String, String> entry : entrySet){
					map.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}


}
