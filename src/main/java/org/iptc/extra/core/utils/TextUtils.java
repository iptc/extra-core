package org.iptc.extra.core.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

public class TextUtils {
	
	public static String clean(String txt) {
		
		txt = txt.replaceAll("<!--.*?-->", " ").replaceAll("<[^>]+>", " ");
		
		txt = StringEscapeUtils.unescapeHtml4(txt);
		txt = StringUtils.normalizeSpace(txt);
		txt = StringUtils.trim(txt);
		
		return txt;
	}

}
