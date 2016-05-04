package org.pinae.ndb.operate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * ndb节点清理
 * 
 * @author Huiyugeng
 *
 */
public class Cleaner {

	/**
	 * 清理ndb中的节点 如果节点下面没有任何数据，则该节点会被清理
	 * 
	 * @param ndb 需要清理的ndb节点
	 * 
	 * @return 清理后的ndb节点
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, Object> clean(Map<String, Object> ndb) {
		Map<String, Object> ndbResult = new HashMap<String, Object>();

		Set<String> keySet = ndb.keySet();

		for (String key : keySet) {
			Object value = ndb.get(key);

			if (value instanceof String) {
				if (StringUtils.isNotBlank((String) value)) {
					ndbResult.put(key, value);
				}
			} else if (value instanceof List) {
				List<Object> list = (List<Object>) value;

				List<Object> tmpList = new ArrayList<Object>();

				for (Object item : list) {
					if (item instanceof String) {
						if (StringUtils.isNotBlank((String) item)) {
							tmpList.add(item);
						}
					} else if (item instanceof Map) {
						Map<String, Object> mapItem = (Map<String, Object>) item;
						if (mapItem.size() > 0) {
							tmpList.add(clean(mapItem));
						}
					}
				}
				if (tmpList.size() > 0) {
					ndbResult.put(key, tmpList);
				}
			} else if (value instanceof Map) {
				Map<String, Object> mapItem = (Map<String, Object>) value;
				if (mapItem.size() == 0) {
					ndbResult.remove(key);
				} else {
					ndbResult.put(key, clean(mapItem));
				}
			}
		}
		return ndbResult;
	}
}
