package org.pinae.ndb.operate;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pinae.ndb.action.OperationAction;

/**
 * ndb节点数据插入
 * 
 * @author Huiyugeng
 *
 */
public class Insert extends Locator {
	
	private Map<String, String> insertMap = null; //需要更新的键值映射
	
	private OperationAction action = null; //需要执行的行为
	
	/**
	 * 在ndb中插入数据
	 * 
	 * @param ndb 需要插入数据的ndb
	 * @param path 数据插入路径
	 * @param insertValue 需要插入的值
	 * 
	 * @return 插入值后的ndb数据
	 */
	public Object insert (Map<String, Object> ndb, String path, String insertValue){
		insertMap = convertValueMap(insertValue);
		
		locate(ndb, path, true);
		
		return ndb;
	}
	
	/**
	 * 在ndb中插入数据
	 * 
	 * @param ndb 需要插入数据的ndb
	 * @param path 数据插入路径
	 * @param action 值插入动作
	 * 
	 * @return 插入值后的ndb数据
	 */
	public Object insert (Map<String, Object> ndb, String path, OperationAction action){
		this.action = action;
		
		locate(ndb, path, true);
		
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
				Set<Map.Entry<String, String>> entrySet = (Set<Entry<String, String>>)insertMap.entrySet();
				for(Entry<String, String> entry : entrySet){
					map.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

}
