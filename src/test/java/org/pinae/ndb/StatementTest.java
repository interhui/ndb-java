package org.pinae.ndb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.ndb.action.TraversalTest.TraversalActionTest;

/**
 * ndb执行测试
 * 
 * @author Huiyugeng
 * 
 */
public class StatementTest {

	private Statement statement = new Statement();

	private Map<String, Object> ndb = null;

	/**
	 * 单元测试初始化
	 */
	@Before
	public void setUp() {
		try {
			ndb = statement.read(NdbTestConstant.EXAMPLE_FILE);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * 测试ndb的exits操作
	 */
	@Test
	public void testExist() {
		Object result = null;

		// exist测试
		result = statement.execute(ndb, "exist:root->parent->child->name:jim");
		assertEquals(((Boolean) result).booleanValue(), true);

		result = statement.execute(ndb, "exist:root->parent->child->sex:male && name:m$");
		assertEquals(((Boolean) result).booleanValue(), true);
		
		result = statement.execute(ndb, "exist:root->parent->child->sex:female && name:m$");
		assertEquals(((Boolean) result).booleanValue(), false);
	}
	
	/**
	 * 测试ndb的one操作
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testOne() {
		Object result = null;

		// one测试
		result = statement.execute(ndb, "one:root->parent->child->sex:male");
		assertEquals(((Map) result).get("name"), "jim");
		assertEquals(((Map) result).get("age"), "20");
	}

	/**
	 * 测试ndb的select操作
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testSelect() {

		Object result = null;
		List resultList = null;

		// select测试
		result = statement.execute(ndb, "select:root->parent->child->name:/.*m/");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "tom");

		result = statement.execute(ndb, "select:root->parent->child->age:[15,25]");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "lily");
		
		result = statement.execute(ndb, "select:root->parent->child->sex:^fe");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("name"), "lily");
		
		result = statement.execute(ndb, "select:root->parent->child->name:m$");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "tom");
		
		result = statement.execute(ndb, "select:root->parent->child->sex:male && age:[15,25]");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		
		result = statement.execute(ndb, "select:root->parent->child");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "lily");
		assertEquals(((Map) resultList.get(2)).get("name"), "tom");

		result = statement.execute(ndb, "select:root->parent->:/child|nephew/->sex:female");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "lucy");
		assertEquals(((Map) resultList.get(1)).get("name"), "lily");
		
		result = statement.execute(NdbTestConstant.getTestList(), "select:masking:true");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);

	}

	/**
	 * 测试ndb中delete行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDelete() {

		Object result = null;
		List resultList = null;

		// delete测试
		result = statement.execute(ndb, "delete:root->parent->child->name:jim !! [sex, age]");
		result = statement.execute((Map<String, Object>) result, "select:root->parent->child->name:jim");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(0)).get("sex"), null);
		assertEquals(((Map) resultList.get(0)).get("age"), null);

		result = statement.execute(ndb, "delete:root->parent->child->name:jim !! block");
		result = statement.execute((Map<String, Object>) result, "select:root->parent->child->name:jim");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 0);
	}

	/**
	 * 测试ndb中clean行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testClean() {
		Object result = null;
		List resultList = null;

		result = statement.execute(ndb, "select:root->parent->child");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);
		
		// clean测试
		result = statement.execute(ndb, "delete:root->parent->child->name:jim !! block");
		result = statement.execute((Map<String, Object>) result, "clean");
		result = statement.execute((Map<String, Object>) result, "select:root->parent->child");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
	}

	/**
	 * 测试ndb中update行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testUpdate() {

		Object result = null;
		List resultList = null;

		// update测试
		result = statement.execute(ndb, "update:root->parent->child->name:jim !! age=21, address=China");
		result = statement.execute((Map<String, Object>) result, "select:root->parent->child->name:jim");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("age"), "21");
		assertEquals(((Map) resultList.get(0)).get("address"), "China");

	}
	
	/**
	 * 测试ndb中insert行为
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testInsert() {

		Object result = null;
		Object selectResult = null;
		//insert 测试
		result = statement.execute(ndb, "insert:root->parent->child !! name=bill, sex=male, age=31");
		selectResult = statement.execute((Map<String, Object>) result, "select:root->parent->child->name:bill");
		assertTrue(selectResult instanceof List);
		List resultList = (List) selectResult;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("sex"), "male");
		assertEquals(((Map) resultList.get(0)).get("age"), "31");
	}

	/**
	 * 测试ndb中遍历(Travel)行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testTravel() {

		Object result = null;

		// travel 遍历
		result = statement.execute((Map<String, Object>) ndb, "select:root->parent->child");
		assertTrue(result instanceof List);
		assertEquals(((List) result).size(), 3);
		
		//修改child节点名称为children
		result = statement.execute(ndb, "travel", new TraversalActionTest());
		assertTrue(result instanceof Map);

		Object selectResult = statement.execute((Map<String, Object>) result, "select:root->parent->child");
		assertTrue(selectResult instanceof List);
		assertEquals(((List) selectResult).size(), 0);
		
		selectResult = statement.execute((Map<String, Object>) result, "select:root->parent->children");
		assertTrue(selectResult instanceof List);
		assertEquals(((List) selectResult).size(), 3);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testRedirect() {
		try {
			Map<String, Object> node = null;
			Object result = null;
			List resultList = null;
			
			statement.execute(ndb, "select:root->parent->:/child|nephew/->sex:female >> select.ndb");
			node = statement.read("select.ndb");
			result = statement.execute(node, "select:result->sex:female");
			assertTrue(result instanceof List);
			resultList = (List) result;
			assertEquals(resultList.size(), 2);
			assertEquals(((Map) resultList.get(0)).get("name"), "lucy");
			assertEquals(((Map) resultList.get(1)).get("name"), "lily");
			
			statement.execute(ndb, "insert:root->parent->child !! name=bill, sex=male, age=31 >> insert.ndb");
			node = statement.read("insert.ndb");
			result = statement.execute(node, "select:root->parent->child->name:bill");
			assertTrue(result instanceof List);
			resultList = (List) result;
			assertEquals(resultList.size(), 1);
			assertEquals(((Map) resultList.get(0)).get("name"), "bill");
			assertEquals(((Map) resultList.get(0)).get("sex"), "male");
			assertEquals(((Map) resultList.get(0)).get("age"), "31");
			
			statement.execute(ndb, "update:root->parent->child->name:jim !! age=21, address=China >> update.ndb");
			node = statement.read("update.ndb");
			result = statement.execute(node, "select:root->parent->child->name:jim");
			assertTrue(result instanceof List);
			resultList = (List) result;
			assertEquals(resultList.size(), 1);
			assertEquals(((Map) resultList.get(0)).get("name"), "jim");
			assertEquals(((Map) resultList.get(0)).get("address"), "China");
			assertEquals(((Map) resultList.get(0)).get("age"), "21");
			
			// 清理残留文件
			String[] files = {"select.ndb", "insert.ndb", "update.ndb"};
			for (String filename : files) {
				new File(filename).delete();
			}
		} catch (IOException e) {
			
		}
	}

}
