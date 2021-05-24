package com.example.demo;

import org.springframework.util.AntPathMatcher;

public class PathMatcherUtil {

    private final static AntPathMatcher matcher = new AntPathMatcher();

	

    public static boolean match(String pattern, String url) {
        return matcher.match(pattern, url);
    }

    public static boolean ma(String pattern,String classUrl){
        // 中间分隔符
        final AntPathMatcher ma = new AntPathMatcher(".");
        // 包含有通配符（*或？）,且不匹配
        if (ma.isPattern(classUrl)) {
            return ma.match(pattern, classUrl);
        }
        // 不包含通配符（*或？）,必须与 basePackages 开头
        return classUrl.startsWith(pattern);
    }
	
	
	private final static Pattern PATTERN = Pattern.compile("\\{(.*?)}");
	/**
     * 将url上的{id} 变成 *
     */
    private String placeholder(String url) {
        var matcher = PATTERN.matcher(url);
        while (matcher.find()) {
            String key = matcher.group();
            url = url.replace(key, "*");
        }
        return url;
    }

    public static void main(String[] args) {
        String pattern = "/abc/**/a.jsp";
        System.out.println("pattern:" + pattern);
        System.out.println("/abc/aa/bb/a.jsp:" + PathMatcherUtil.match(pattern, "/abc/aa/bb/a.jsp"));
        System.out.println("/aBc/aa/bb/a.jsp:" + matcher.match(pattern, "/aBc/aa/bb/a.jsp"));
        System.out.println("/abc/a.jsp:" + matcher.match(pattern, "/abc/a.jsp"));
    }
}
