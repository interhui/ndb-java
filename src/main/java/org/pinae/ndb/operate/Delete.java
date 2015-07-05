package org.pinae.ndb.operate;

import java.util.Map;

import org.pinae.ndb.action.OperationAction;

/**
 * ndb节点删除
 * 
 * @author Huiyugeng
 *
 */
public class Delete extends Locator {
	
	private boolean clear = false; //是否全部清除
	
	private String columns[] = null; //需要清除的字段名称
	
	private OperationAction action = null; //需要执行的行为
	
	/**
	 * 删除数据
	 * 
	 * @param path 需要删除的路径
	 * @param ndb 需要处理的ndb数据
	 * @param column 需要删除的列，如果忽略列则删除整个节点
	 * 
	 * @return 处理后的ndb数据
	 */
	public Object delete (Map<String, Object> ndb, String path, String column){
		if (column != null){
			if (column.startsWith("[") && column.endsWith("]")){
				column = column.trim().substring(1, column.length() -1 );
				this.columns = column.split(",");
			}else if(column.equalsIgnoreCase("block")){
				clear = true;
			}
		}

		locate(ndb, path);
		
		return ndb;
	}
	
	/**
	 * 删除数据
	 * 
	 * @param path 需要删除的路径
	 * @param ndb 需要处理的ndb数据
	 * @param action 自定义删除行为
	 * 
	 * @return 处理后的ndb数据
	 */
	public Object delete (Map<String, Object> ndb, String path, OperationAction action){
		this.action = action;

		locate(ndb, path);
		
		return ndb;
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	protected void doAction(Object item) {
		if (action != null){
			action.handle(item);
		} else {
			if (item != null && item instanceof Map){
				Map map = (Map)item;
				if (clear){
					map.clear();
				}else{
					if (columns != null && columns.length > 0){
						for (String column : columns){
							map.remove(column.trim());
						}
					}
				}
			}
		}
	}


}
