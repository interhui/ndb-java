package org.pinae.ndb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.ndb.Statement;
import org.pinae.ndb.action.TraversalTest.TraversalActionTest;

/**
 * ndb执行测试
 * 
 * @author Huiyugeng
 * 
 */
public class StatementTest {

	private Statement statment = new Statement();

	private Map<String, Object> ndb = null;

	/**
	 * 单元测试初始化
	 */
	@Before
	public void setUp() {
		try {
			ndb = statment.read(NdbTestConstant.EXAMPLE_FILE);
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

		// Exist：存在测试
		result = statment.execute(ndb, "exist:root->parent->child->name:jim");
		assertEquals(((Boolean) result).booleanValue(), true);

		result = statment.execute(ndb, "exist:root->parent->child->sex:male && name:m$");
		assertEquals(((Boolean) result).booleanValue(), true);
		
		result = statment.execute(ndb, "exist:root->parent->child->sex:female && name:m$");
		assertEquals(((Boolean) result).booleanValue(), false);
	}
	
	/**
	 * 测试ndb的one操作
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testOne() {
		Object result = null;

		// One：查询测试
		result = statment.execute(ndb, "one:root->parent->child->sex:male");
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

		// Select：查询测试
		result = statment.execute(ndb, "select:root->parent->child->name:/.*m/");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "tom");

		result = statment.execute(ndb, "select:root->parent->child->age:[15,25]");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "lily");
		
		result = statment.execute(ndb, "select:root->parent->child->sex:^fe");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("name"), "lily");
		
		result = statment.execute(ndb, "select:root->parent->child->name:m$");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "tom");
		
		result = statment.execute(ndb, "select:root->parent->child->sex:male && age:[15,25]");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		
		result = statment.execute(ndb, "select:root->parent->child");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(1)).get("name"), "lily");
		assertEquals(((Map) resultList.get(2)).get("name"), "tom");

		result = statment.execute(ndb, "select:root->parent->:/child|nephew/->sex:female");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);
		assertEquals(((Map) resultList.get(0)).get("name"), "lucy");
		assertEquals(((Map) resultList.get(1)).get("name"), "lily");

	}

	/**
	 * 测试ndb中delete行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testDelete() {

		Object result = null;
		List resultList = null;

		// Delete：删除测试
		result = statment.execute(ndb, "delete:root->parent->child->name:jim !! [sex, age]");
		result = statment.execute((Map<String, Object>) result, "select:root->parent->child->name:jim");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("name"), "jim");
		assertEquals(((Map) resultList.get(0)).get("sex"), null);
		assertEquals(((Map) resultList.get(0)).get("age"), null);

		result = statment.execute(ndb, "delete:root->parent->child->name:jim !! block");
		result = statment.execute((Map<String, Object>) result, "select:root->parent->child->name:jim");
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

		result = statment.execute(ndb, "select:root->parent->child");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);

		result = statment.execute(ndb, "delete:root->parent->child->name:jim !! block");
		result = statment.execute((Map<String, Object>) result, "clean");
		result = statment.execute((Map<String, Object>) result, "select:root->parent->child");
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

		// Update：更新测试
		result = statment.execute(ndb, "update:root->parent->child->name:jim !! age=21, address=China");
		result = statment.execute((Map<String, Object>) result, "select:root->parent->child->name:jim");
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

		result = statment.execute(ndb, "insert:root->parent->child !! name=bill, sex=male, age=31");
		selectResult = statment.execute((Map<String, Object>) result, "select:root->parent->child->name:bill");
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

}
