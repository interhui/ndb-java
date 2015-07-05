package org.pinae.ndb.action;

/**
 * ndb自定义遍历行为
 * 
 * @author Huiyugeng
 *
 *
 */
public interface TraversalAction extends Action{
	/**
	 * 处理节点名称
	 * 
	 * @param key 节点名称
	 * 
	 * @return 处理后的节点名称
	 */
	public String handleKey(String key);
	
	/**
	 * 处理节点值
	 * 
	 * @param value 节点值
	 * 
	 * @return 处理后的节点值
	 */
	public Object handleValue(Object value);

}
