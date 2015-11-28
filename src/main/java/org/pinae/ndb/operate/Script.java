package org.pinae.ndb.operate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.pinae.ndb.Statement;
import org.pinae.ndb.common.NodeReader;

/**
 * 
 * ndb脚本执行
 * 
 * @author Huiyugeng
 *
 */
public class Script {
	
	private static Logger logger = Logger.getLogger(Script.class);
	
	private Statement statement = new Statement();
	
	/**
	 * 执行ndb脚本
	 * 
	 * @param ndb 需要操作的ndb对象 (ndb对象/ndb文件路径/ndb文件)
	 * @param scriptFilename 脚本内容
	 * 
	 * @return 经过脚本处理后的ndb对象
	 */
	public Object run(Object ndb, String scriptFilename) {
		if (ndb != null) {
			try {
				NodeReader reader = new NodeReader();
				if (ndb instanceof String) {
					ndb = reader.read((String)ndb);
				} else if (ndb instanceof File) {
					ndb = reader.readAsList((File)ndb);
				}
				List<String> content = reader.readAsList(scriptFilename);
				for (String line : content) {
					ndb = statement.execute(ndb, line);
				}
			} catch (IOException e) {
				logger.error(String.format("Run Script %s FAIL", e.getMessage()));
			}
		}
		return ndb;
	}
}
