package test.org.pinae.ndb.action;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.ndb.Statement;
import org.pinae.ndb.action.TraversalAction;

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
			ndb = statment.read("resource/example_1.txt");
		}catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * ndb遍历测试
	 */
	@Test
	public void testTraversal() {
		
		Object result = null;
		
		//Travel: 遍历
		result = statment.execute( (Map<String, Object>)ndb, "select:firewall->objectgroup");
		assertTrue(result instanceof List);
		assertTrue(((List)result).size() == 0);
		
		result = statment.execute(ndb, "travel", new TraversalActionTest());
		assertTrue(result instanceof Map);
		
		result = statment.execute((Map<String, Object>)result, "select:firewall->objectgroup");
		assertTrue(result instanceof List);
		assertTrue(((List)result).size() > 0);
	}
	
	/**
	 * ndb遍历行为测试
	 * 
	 * @author Huiyugeng
	 *
	 */
	public static class TraversalActionTest implements TraversalAction {

		@Override
		public String handleKey(String key) {
			String newKey = "";

			if (key.indexOf("-") > -1) {
				String keys[] = key.split("-");
				for (String _key : keys){
					newKey += _key;
				}
			} else {
				newKey = key;
			}
			return newKey;
		}

		@Override
		public Object handleValue(Object value) {
			return value;
		}


	}
	
}
