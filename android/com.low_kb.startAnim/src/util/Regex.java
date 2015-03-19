package util;

import interfacepac.RegexRule;

import java.util.ArrayList;
import java.util.List;

public class Regex {
	private static List htmlUrleList = new ArrayList();
	private static List urlRuleList = new ArrayList();
	public static void regesitHTMLRegexRule(RegexRule rule)
	{
		Regex.htmlUrleList.add(rule);
	}
	
	public static void removeHTMLRegexRule(RegexRule rule){
		Regex.htmlUrleList.remove(rule);
	}
	
	public static void regesitUrlRegexRule(RegexRule rule)
	{
		Regex.urlRuleList.add(rule);
	}
	
	public static void removeUrlRegexRule(RegexRule rule){
		Regex.urlRuleList.remove(rule);
	}
	
	public static void initRules(){
		Regex.htmlUrleList.add(new DefaultRegexHTMLRule());
		Regex.urlRuleList.add(new DefaultRegexUrlRule());
	}
	
	private static String regex_html(String html){
		for(int i=0;i<Regex.htmlUrleList.size();i++){
			RegexRule ruleItem = (RegexRule)Regex.htmlUrleList.get(i);
			html = ruleItem.regex(html);
		}
		return html;
	}
	
	private static String regex_url(String url){
		for(int i=0;i<Regex.urlRuleList.size();i++){
			RegexRule ruleItem = (RegexRule)Regex.urlRuleList.get(i);
			url = ruleItem.regex(url);
		}
		return url;
	}
	
	public static String regexHTML(String html){
		return Regex.regex_html(html);
	}
	
	public static void init(){
		Regex.initRules();
	}
	
	public static String regexUrl(String url){
		return Regex.regex_url(url);
	}
}
