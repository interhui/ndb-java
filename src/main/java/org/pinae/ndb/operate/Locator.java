package org.pinae.ndb.operate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * ndb定位器
 * 
 * @author Huiyugeng
 *
 */
public abstract class Locator {
	
	/**
	 * 定位对象
	 * 
	 * 查询语句表示方法为：A->B->C:Value
	 * 如果首次开始，A为当前项，B->C:Value为子查询，如果当前项=子查询，则证明已经进入最后一个查询
	 * 
	 * @param ndb 需要查询的ndb对象
	 * @param query 查询语句
	 * @param isCreate 当查询项不存在时，是否进行创建
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void locate(Object ndb, String query, boolean isCreate){
		
		if(query == null || query.equals("")){
			return;
		}
		
		String queryKey = query; //当前项
		String subQuery = query; //子查询

		if(ndb instanceof List){
			List ndbNodeList = (List)ndb;
			
			for(Object ndbItem : ndbNodeList){
				locate(ndbItem, subQuery, isCreate);
			}
			
		} else if(ndb instanceof Map){

			if(query.indexOf("->") > 0){
				queryKey = StringUtils.substringBefore(query, "->").trim();
				subQuery = StringUtils.substringAfter(query, "->").trim();
			}
			
			Map<String, Object> ndbMap = (Map<String, Object>)ndb;

			if (isCreate && ! ndbMap.containsKey(queryKey)) {
				ndbMap.put(queryKey, new HashMap<String, Object>());
			}
			if(! subQuery.equals(queryKey) || queryKey.startsWith(":")){
				if(queryKey.startsWith(":")){ //根据路径进行查询
					String exp  = StringUtils.substring(queryKey, 1);
						
					Set<String> keySet = ndbMap.keySet();
					for (String key : keySet) {
						if (checkValue(key, exp)) {
							if (subQuery.startsWith(":")){
								locate(ndbMap, key, isCreate);
							} else {
								locate(ndbMap.get(key), subQuery, isCreate);
							}
							
						}
					}
				} else {
					locate(ndbMap.get(queryKey), subQuery, isCreate);
				}
			}else{
				if(subQuery.indexOf(":") > 0){ //根据值进行查询
					String matchItems[] = subQuery.split("&&");
					
					boolean matchResult = true;
					for (String matchItem : matchItems) {
						String items[] = matchItem.trim().split(":");
						if(items.length == 2){
							String key = items[0];
							String exp = items[1];
							
							Object value = ndbMap.get(key);
							
							if (! checkValue(value, exp)) {
								matchResult = false;
							}
						}
					}
					
					if (matchResult) {
						doAction(ndbMap);
					}
				} else {
					Object result = ndbMap.get(queryKey);
					//创建模式
					if (isCreate) {
						if (result instanceof List) {
							List list = (List)result;
							
							Map item = new HashMap();
							doAction(item);
							list.add(item);
						} else if (result instanceof Map) {
							Map item = (Map)result;
							if (item.size() == 0) {
								doAction(item);
							} else {
								Map newItem = new HashMap();
								doAction(newItem);
								
								List list = new ArrayList();
								list.add(item);
								list.add(newItem);
								
								ndbMap.put(queryKey, list);
							}
							
						}
					} else {
						if (result instanceof List) {
							List list = (List)result;
							for (Object item : list){
								doAction(item);
							}
						}else{
							doAction(result);
						}
					}
				}
			}
		}
		
	}
	
	/**
	 * 定位对象
	 * 
	 * @param query 查询语句
	 * @param ndb 需要查询的ndb对象
	 * 
	 */
	protected void locate(Object ndb, String query){
		locate(ndb, query, false);
	}
	
	private boolean checkValue (Object value, String exp) {
			
		if (exp.startsWith("/") && exp.endsWith("/")) { //正则表达式判断
			String regex = exp.substring(1, exp.length()-1);
			if(value!=null && value instanceof String && ((String)value).matches(regex)){ // 正则表达式匹配
				return true;
			}
		} else if (exp.startsWith("[") && exp.endsWith("]") && exp.indexOf(",") > 0) { //值域判断
			String region[] = exp.substring(1, exp.length()-1).split(",");
			if(value !=null && value instanceof String && region.length == 2){
				try{
					int min = Integer.parseInt(region[0].trim()); //值域中最小值
					int max = Integer.parseInt(region[1].trim()); //值域中最大值
					int intValue = Integer.parseInt((String)value);
					if(intValue >= min && intValue <= max){ //值域匹配
						return true;
					}
				}catch(NumberFormatException e){
					return false;
				}
			}
		} else if (exp.startsWith("^")) {
			String startswith = exp.substring(1);
			if(value !=null && value.toString().startsWith(startswith)) {
				return true;
			}
		} else if (exp.endsWith("$")) {
			String endswith = exp.substring(1);
			if(value !=null && value.toString().endsWith(endswith)) {
				return true;
			}
		} else {
			if(value != null && value.equals(exp)){
				return true;
			}
		}
		return false; 
	}
	
	protected abstract void doAction(Object item);
	
	protected Map<String, String> convertValueMap(String value){
		Map<String, String> updateValueMap = new HashMap<String, String>();
		
		if (StringUtils.isNotEmpty(value)) {
			String values[] = value.split(",");
			for (String tempValue : values){
				String valuePair[] = tempValue.split("=");
				if (valuePair.length == 2){
					updateValueMap.put(valuePair[0].trim(), valuePair[1].trim());
				}
			}
		}
		
		return updateValueMap;
	}
	
}
