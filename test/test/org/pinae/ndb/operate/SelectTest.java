package test.org.pinae.ndb.operate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.pinae.ndb.common.NodeReader;
import org.pinae.ndb.operate.Select;

/**
 * ndb节点查询单元测试
 * 
 * @author Huiyugeng
 *
 */
public class SelectTest {
	
	/**
	 * ndb检索数据测试
	 */
	@Test
	public void testSelect(){
		NodeReader parser = new NodeReader();
		
		Select select = new Select();
		
		Object result = null;
		try {
			
			Map<String, Object> configMap = parser.read("config.txt");

			List resultList= null;
			
			result = select.select(configMap, "firewall->interface->name:dmz");
			assertTrue(result instanceof List);
			resultList = (List)result;
			assertEquals(resultList.size(), 1);
			assertEquals(((Map)resultList.get(0)).get("ip"), "192.168.12.2");
			
			result = select.select(configMap, "firewall->interface");
			assertTrue(result instanceof List);
			resultList = (List)result;
			assertEquals(resultList.size(), 3);
			
			result = select.select(configMap, "firewall->interface->mask:255.255.255.0");
			assertTrue(result instanceof List);
			resultList = (List)result;
			assertEquals(resultList.size(), 3);
			
			result = select.select(configMap, "firewall->access-list->action:/permit|deny/");
			assertTrue(result instanceof List);
			resultList = (List)result;
			assertEquals(resultList.size(), 11);
			
			result = select.select(configMap, "firewall->interface->security:[50,100]");
			assertTrue(result instanceof List);
			resultList = (List)result;
			assertEquals(resultList.size(), 2);
			
			result = select.select(configMap, "firewall->access-group");
			assertTrue(result instanceof List);
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
}
