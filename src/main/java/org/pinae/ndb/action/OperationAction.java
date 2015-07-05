package org.pinae.ndb.action;

/**
 * ndb自定义操作行为
 * 
 * @author Huiyugeng
 *
 *
 */
public interface OperationAction extends Action{
	/**
	 * 处理节点值
	 * 
	 * @param item 处理的内容
	 * 
	 * @return 处理结果
	 */
	public Object handle(Object item);
}
