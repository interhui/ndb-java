package org.pinae.ndb.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.pinae.ndb.NdbTestConstant;
import org.pinae.ndb.common.NodeReader;

/**
 * ndb读取单元测试
 * 
 * @author Huiyugeng
 *
 */
public class NodeReaderTest {
	/**
	 * 测试ndb数据读取
	 */
	@Test
	public void testLoad(){
		NodeReader loader = new NodeReader();
		
		Map<String, Object> ndb = null;
		
		try {
			ndb = loader.read(NdbTestConstant.EXAMPLE_FILE);
			assertEquals(ndb.size(), 1);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
}
