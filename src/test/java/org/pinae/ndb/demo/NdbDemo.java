package org.pinae.ndb.demo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.pinae.ndb.Statement;

public class NdbDemo {

	public static void main(String arg[]) {
		Statement statment = new Statement();
		try {
			Map<String, Object> ndb = statment.read("d:/example.ndb");
			Object result = statment.execute(ndb, "select:root->parent->child->name:/.*m/");
			if (result instanceof List) {
				List resultList = (List)result;
				System.out.println(((Map) resultList.get(0)).get("name"));
				System.out.println(((Map) resultList.get(1)).get("name"));
			}
		} catch (IOException e) {

		}
	}
}