package org.pinae.ndb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pinae.ndb.action.Action;
import org.pinae.ndb.action.OperationAction;
import org.pinae.ndb.action.TraversalAction;
import org.pinae.ndb.common.NodeReader;
import org.pinae.ndb.common.NodeWriter;
import org.pinae.ndb.operate.Cleaner;
import org.pinae.ndb.operate.Delete;
import org.pinae.ndb.operate.Insert;
import org.pinae.ndb.operate.Select;
import org.pinae.ndb.operate.Traversal;
import org.pinae.ndb.operate.Update;

/**
 * ndb语句执行器
 * 
 * @author Huiyugeng
 *
 *
 */
public class Statement {

	/**
	 * 执行ndb语句
	 * 
	 * @param query 需要执行的ndb语句
	 * @param ndb ndb信息
	 * 
	 * @return 执行结果
	 */
	public Object execute(Object ndb, String query) {
		return execute(ndb, query, null);
	}

	/**
	 * 执行ndb语句
	 * 
	 * @param query 需要执行的ndb语句
	 * @param ndb ndb信息
	 * @param action 自定义行为,如果使用自定义行为，则仅进行定位不执行值变更
	 * 
	 * @return 执行结果
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(Object ndb, String query, Action action) {
		
		if (ndb == null || ((ndb instanceof List) == false && (ndb instanceof Map) == false)) {
			return null;
		}
		
		String command = query;
		if (query.indexOf(":") > -1 ) {
			command = StringUtils.substringBefore(query, ":").trim();
			query = StringUtils.substringAfter(query, ":").trim();
		}
		
		String queryItems[] = query.split("!!");

		if (queryItems != null) {
			String path = null, value = null;

			if (queryItems.length > 0) {
				path = queryItems[0].trim();
			}
			if (queryItems.length > 1) {
				value = queryItems[1].trim();
			}
			
			List resultList = new ArrayList();
			
			if (ndb instanceof List) {
				List ndbList = (List)ndb;
				for (Object ndbItem : ndbList) {
					if (ndbItem instanceof Map) {
						if (action != null) {
							resultList.add(execute((Map<String, Object>)ndbItem, command, path, null, action));
						} else {
							resultList.add(execute((Map<String, Object>)ndbItem, command, path, value, null));
						}
					}
				}
			} else if (ndb instanceof Map) {
				if (action != null) {
					return execute((Map<String, Object>)ndb, command, path, null, action);
				} else {
					return execute((Map<String, Object>)ndb, command, path, value, null);
				}
			}
			
			if (resultList.size() > 0) {
				return resultList;
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	private Object execute(Map<String, Object> ndb, String command, String path, String value, Action action) {
		Object result = null;

		if (command != null) {
			if (command.equalsIgnoreCase("exist")) {
				result = new Select().select(ndb, path);
				
				if (result != null && result instanceof List){
					if (((List)result).size() > 0){
						result = new Boolean(true);
					} else {
						result = new Boolean(false);
					}
				}
			} else if (command.equalsIgnoreCase("select")) {
				if (action != null) {
					result = new Select().select(ndb, path, (OperationAction)action);
				} else {
					result = new Select().select(ndb, path);
				}
			} else if (command.equalsIgnoreCase("one")) {
				if (action != null) {
					result = new Select().select(ndb, path, (OperationAction)action);
				} else {
					result = new Select().select(ndb, path);
				}
				if (result != null && result instanceof List){
					if (((List)result).size() > 0){
						result = ((List)result).get(0);
					} else {
						result = null;
					}
				}
			} else if (command.equalsIgnoreCase("delete")) {
				if (action != null) {
					result = new Delete().delete(ndb, path, (OperationAction)action);
				} else {
					result = new Delete().delete(ndb, path, value);
				}
			} else if (command.equalsIgnoreCase("update")) {
				if (action != null) {
					result = new Update().update(ndb, path, (OperationAction)action);
				} else {
					result = new Update().update(ndb, path, value);
				}
				
			} else if (command.equalsIgnoreCase("insert")) {
				if (action != null) {
					result = new Insert().insert(ndb, path, (OperationAction)action);
				} else {
					result = new Insert().insert(ndb, path, value);
				}
			} else if (command.equalsIgnoreCase("clean")) {
				result = new Cleaner().clean(ndb);
			} else if (command.equalsIgnoreCase("travel")) {
				if (action != null) {
					result = new Traversal().traversal(ndb, (TraversalAction)action);
				}
			}
		}
		return result;
	}
	
	/**
	 *  将Map输出ndb格式
	 * 
	 * @param rootNode 根节点名称
	 * @param ndb 需要输出ndb格式的Map
	 * 
	 * @return ndb内容
	 */
	public String print(String rootNode, Map<String, Object> ndb){
		return new NodeWriter().print(rootNode, ndb);
	}
	
	/**
	 * 保存为ndb格式文件
	 * 
	 * @param filename 文件名
	 * @param rootNode 根节点名称
	 * @param ndb Map格式
	 * 
	 * @throws IOException
	 */
	public void write(String filename, String rootNode, Map<String, Object> ndb) throws IOException{
		new NodeWriter().write(filename, rootNode, ndb);
	}
	
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
		return new NodeReader().read(filename);
	}

}
