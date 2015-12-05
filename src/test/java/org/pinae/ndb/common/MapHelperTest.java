package org.pinae.ndb.common;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Map对象工具集单元测试
 * 
 * @author Huiyugeng
 *
 */
public class MapHelperTest {
	
	/**
	 * Map拷贝
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testCopy() {
		
		Map<Object, Object> map = MapHelper.toMap(new Object[][]{
			new Object[]{"name", "hui"}, 
			new Object[]{"age", "30"}, 
			new Object[]{"phone", "13343351822"}
			});
		
		Map<Object, Object> property = MapHelper.toMap(new Object[]{"name" , "age", "phone"}, 
				new Object[]{"UserName", "UserAge", "UserPhone"});
		
		Map<Object, Object> dst = (Map<Object, Object>)MapHelper.copy(map, property);
		assertEquals(dst.get("UserName"), "hui");
		assertEquals(dst.get("UserAge"), "30");
		assertEquals(dst.get("UserPhone"), "13343351822");
	}
	
	/**
	 * 字符型Map转换
	 */
	@Test
	public void testToStringMap() {
		Map<Object, Object> src = new HashMap<Object, Object>();
		src.put(new Integer(1), new Boolean(true));
		src.put(new Integer(2), new Boolean(false));
		
		Map<String, String> dst = MapHelper.toStringMap(src);
		assertEquals(dst.get("1"), "true");
		assertEquals(dst.get("2"), "false");
	}
	
	/**
	 * 对象Map转换
	 */
	@Test
	public void testToObjectMap() {
		Map<String, String> src = new HashMap<String, String>();
		src.put("1", "true");
		src.put("2", "false");
		
		Map<Object, Object> dst = MapHelper.toObjectMap(src);
		assertEquals(dst.get("1"), "true");
		assertEquals(dst.get("2"), "false");
	}
	
	/**
	 * Map转换为字符串单元测试
	 */
	@Test
	public void testToString() {
		Map<Object, Object> map = MapHelper.toMap(new Object[]{"name" , "age", "phone"}, 
				new Object[]{"hui", "30", "13343351822"});
		String value = MapHelper.toString(map, "{name}|{age}|{phone}");
		assertEquals(value, "hui|30|13343351822");
	}
	
	/**
	 * Map合并单元测试
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public void testJoin() {
		List<String> phoneList1 = new ArrayList<String>();
		phoneList1.add("13343351822");
		List<String> addressList = new ArrayList<String>();
		addressList.add("Guangdong");
		Map<String, Object> src = new HashMap<String, Object>();
		src.put("name", "Hui");
		src.put("age", 30);
		src.put("phone", phoneList1);
		src.put("address", addressList);
		src.put("company", "letour");
		
		List<String> phoneList2 = new ArrayList<String>();
		phoneList2.add("13343351822");
		List<String> companyList = new ArrayList<String>();
		companyList.add("pinae");
		Map<String, Object> dst = new HashMap<String, Object>();
		dst.put("age", 31);
		dst.put("phone", phoneList2);
		dst.put("address", "Beijing");
		dst.put("company", companyList);
		
		MapHelper.join(dst, src);
		
		assertEquals(dst.get("name"), "Hui");
		assertEquals(dst.get("age"), 30);
		Object phone = dst.get("phone");
		if (phone instanceof List) {
			assertEquals(((List)phone).size(), 2);
		}
		Object address = dst.get("address");
		if (phone instanceof List) {
			assertEquals(((List)address).size(), 2);
		}
		Object company = dst.get("company");
		if (phone instanceof List) {
			assertEquals(((List)company).size(), 2);
		}
	}
	
}
