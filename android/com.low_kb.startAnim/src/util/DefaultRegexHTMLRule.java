package util;

import interfacepac.RegexRule;

public class DefaultRegexHTMLRule implements RegexRule{

	public String regex(String html) {
		// TODO Auto-generated method stub
		html = html.replaceAll("width", "");
    	html = html.replaceAll("WIDTH", "");
		return html;
	}
}
