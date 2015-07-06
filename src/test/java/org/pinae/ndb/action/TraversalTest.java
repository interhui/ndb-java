package org.pinae.ndb.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.ndb.NdbTestConstant;
import org.pinae.ndb.Statement;

/**
 * ndb自定义遍历行为单元测试
 * 
 * @author Huiyugeng
 *
 */
public class TraversalTest {
	
	private Map<String, Object> ndb = null;
	
	private Statement statment = new Statement();
	
	/**
	 * 单元测试初始化
	 */
	@Before
	public void setUp() {
		try{
			ndb = statment.read(NdbTestConstant.EXAMPLE_FILE);
		}catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * ndb遍历测试
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testTraversal() {
		
		Object result = null;
		
		// Travel: 遍历
		result = statment.execute((Map<String, Object>) ndb, "select:root->parent->child");
		assertTrue(result instanceof List);
		assertEquals(((List) result).size(), 3);
		
		//修改child节点名称为children
		result = statment.execute(ndb, "travel", new TraversalActionTest());
		assertTrue(result instanceof Map);

		Object selectResult = statment.execute((Map<String, Object>) result, "select:root->parent->child");
		assertTrue(selectResult instanceof List);
		assertEquals(((List) selectResult).size(), 0);
		
		selectResult = statment.execute((Map<String, Object>) result, "select:root->parent->children");
		assertTrue(selectResult instanceof List);
		assertEquals(((List) selectResult).size(), 3);
	}
	
	/**
	 * ndb遍历行为测试
	 * 
	 * @author Huiyugeng
	 *
	 */
	public static class TraversalActionTest implements TraversalAction {

		public String handleKey(String key) {
			String newKey = "";

			if (key.equals("child")) {
				newKey = "children";
			} else {
				newKey = key;
			}
			return newKey;
		}

		public Object handleValue(Object value) {
			return value;
		}


	}
	
}
