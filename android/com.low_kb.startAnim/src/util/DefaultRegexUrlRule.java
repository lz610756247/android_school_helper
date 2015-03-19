package util;

import interfacepac.RegexRule;

public class DefaultRegexUrlRule implements RegexRule{

	public String regex(String url) {
		// TODO Auto-generated method stub
		if(!url.startsWith("http://202.203.194.10")){
    		url = "http://202.203.194.10/jwnews/(S(4idonwzrngksisquflvuyw45))/"+url;
    	}
		return url;
	}

}
