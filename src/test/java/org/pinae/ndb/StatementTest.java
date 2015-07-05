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
 * ndb声明执行测试
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
	 * 测试ndb中数据存在
	 */
	@Test
	public void testExist() {
		Object result = null;

		// Exist：存在测试
		result = statment.execute(ndb, "exist:firewall->interface->name:dmz && ip:192.168.12.2");
		assertEquals(((Boolean) result).booleanValue(), true);

		result = statment.execute(ndb, "exist:firewall->interface->name:boss && ip:192.168.32.21");
		assertEquals(((Boolean) result).booleanValue(), false);
	}

	/**
	 * 测试ndb的select行为
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testSelect() {

		Object result = null;
		List resultList = null;

		// Select：查询测试
		result = statment.execute(ndb, "select:firewall->interface->name:dmz && ip:192.168.12.2");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("ip"), "192.168.12.2");

		result = statment.execute(ndb, "select:firewall->interface->name:/dmz|inside/ && ip:/192.168.12.2|192.168.228.122/");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);

		result = statment.execute(ndb, "select:firewall->interface");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);

		result = statment.execute(ndb, "select:firewall->interface->mask:255.255.255.0");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);

		result = statment.execute(ndb, "select:firewall->access-list->action:/permit|deny/");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 11);

		result = statment.execute(ndb, "select:firewall->:/\\S+-list/->action:/permit|deny/");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 11);

		result = statment.execute(ndb, "select:firewall->interface->security:[50,100]");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 2);

		result = statment.execute(ndb, "select:firewall->:/host|interface/");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 4);

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
		result = statment.execute(ndb, "delete:firewall->interface->name:inside !! [mask, security]");
		result = statment.execute((Map<String, Object>) result, "select:firewall->interface->name:inside");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("mask"), null);
		assertEquals(((Map) resultList.get(0)).get("security"), null);

		result = statment.execute(ndb, "delete:firewall->interface->name:outside !! block");
		result = statment.execute((Map<String, Object>) result, "select:firewall->interface->name:outside");
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

		result = statment.execute(ndb, "select:firewall->interface");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 3);

		result = statment.execute(ndb, "delete:firewall->interface->name:outside !! block");
		result = statment.execute((Map<String, Object>) result, "clean");
		result = statment.execute((Map<String, Object>) result, "select:firewall->interface");
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
		result = statment.execute(ndb, "update:firewall->interface->name:dmz !! name=dmz2,mask=255.255.255.32");
		result = statment.execute((Map<String, Object>) result, "select:firewall->interface->name:dmz2");
		assertTrue(result instanceof List);
		resultList = (List) result;
		assertEquals(resultList.size(), 1);
		assertEquals(((Map) resultList.get(0)).get("ip"), "192.168.12.2");

	}

	/**
	 * 测试ndb中遍历(Travel)行为
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testTravel() {

		Object result = null;

		// Travel: 遍历
		result = statment.execute((Map<String, Object>) ndb, "select:firewall->objectgroup");
		assertTrue(result instanceof List);
		assertTrue(((List) result).size() == 0);

		result = statment.execute(ndb, "travel", new TraversalActionTest());
		assertTrue(result instanceof Map);

		result = statment.execute((Map<String, Object>) result, "select:firewall->objectgroup");
		assertTrue(result instanceof List);
		assertTrue(((List) result).size() > 0);
	}

	/**
	 * 测试ndb中insert行为
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testInsert() {

		Object result = null;
		Object selectResult = null;

		result = statment.execute(ndb, "insert:firewall->nat !! name=192.168.9.21,ip=192.168.9.21");
		selectResult = statment.execute((Map<String, Object>) result, "select:firewall->nat");
		assertTrue(selectResult instanceof List);
		assertTrue(((List) selectResult).size() == 1);

		result = statment.execute((Map<String, Object>) result, "insert:firewall->nat !! name=192.168.9.22,ip=192.168.9.22");
		selectResult = statment.execute((Map<String, Object>) result, "select:firewall->nat");
		assertTrue(selectResult instanceof List);
		assertTrue(((List) selectResult).size() == 2);

		result = statment.execute((Map<String, Object>) result, "insert:firewall->nat !! name=192.168.9.23,ip=192.168.9.23");
		selectResult = statment.execute((Map<String, Object>) result, "select:firewall->nat");
		assertTrue(selectResult instanceof List);
		assertTrue(((List) selectResult).size() == 3);
	}

}
