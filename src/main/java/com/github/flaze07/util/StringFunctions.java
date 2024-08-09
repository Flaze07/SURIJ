package com.github.flaze07.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StringFunctions 
{
	public static Map<String, String> splitQuery(String queries) {
		if (queries == null || "".equals(queries)) {
			return Collections.emptyMap();
		}

		var result = new HashMap<String, String>();

		String[] queryList = queries.split("&");
		for(int i = 0; i < queryList.length; ++i) {
			String[] keyValuePair = queryList[0].split("=");
			result.put(keyValuePair[0], keyValuePair[1]);
		}

		return result;
	}
}