package org.pinae.ndb.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pinae.ndb.action.OperationAction;

/**
 * ndb节点查询
 * 
 * @author Huiyugeng
 *
 */
public class Select extends Locator {
	private List<Object> resultList = null; //查询结果
	
	private OperationAction action = null; //需要执行的行为

	/**
	 * ndb检索数据
	 * 
	 * @param ndb 需要检索的ndb
	 * @param path 检索路径和查询条件
	 * 
	 * @return ndb中查询后的值
	 */
	public Object select (Map<String, Object> ndb, String path){
		resultList = new ArrayList<Object>();
		
		locate(ndb, path);
		
		return resultList;
	}
	
	/**
	 * 在ndb中检索数据
	 * 
	 * @param ndb 需要检索的ndb
	 * @param path 检索路径和查询条件
	 * @param action 查询动作
	 * 
	 * @return ndb中查询后的值
	 */
	public Object select (Map<String, Object> ndb, String path, OperationAction action){
		this.action = action;
		
		return select(ndb, path);
	}

	@Override
	protected void doAction(Object item) {
		if (action != null) {
			action.handle(item);
		}
		
		if (item != null){
			resultList.add(item);
		}
	}



}
