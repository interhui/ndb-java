package test.org.pinae.ndb.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.pinae.ndb.Statement;
import org.pinae.ndb.action.OperationAction;

/**
 * ndb自定义操作行为单元测试
 * 
 * @author Huiyugeng
 *
 */
public class OperationTest {
	
	private Map<String, Object> ndb = null;
	
	private Statement statment = new Statement();
	
	/**
	 * 单元测试初始化
	 */
	@Before
	public void setUp(){
		try{
			ndb = statment.read("resource/example_1.txt");
		}catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * ndb自定义行为单元测试
	 */
	@Test
	public void testOperation() {
		Object result = null;
		List resultList= null;
		
		result = statment.execute(ndb, "update:firewall", new OperationActionTest());
		result = statment.execute( (Map<String, Object>)result, "select:firewall->flag:pinae");
		resultList = (List)result;
		assertEquals(resultList.size(), 1);
	}
	
	/**
	 * ndb自定义操作行为
	 * 
	 * @author Huiyugeng
	 *
	 */
	public class OperationActionTest implements OperationAction{

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Object handle(Object item) {
			if (item instanceof Map){
				Map map = (Map)item;
				map.put("flag", "pinae");
				return map;
			}
			return item;
		}

	}
}
