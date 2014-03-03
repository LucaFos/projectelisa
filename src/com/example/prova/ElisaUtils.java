package com.example.prova;

public class ElisaUtils {
	static String convertStreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    
	    String nice = new String(ElisaUtils.hosting24deleter(s.hasNext() ? s.next() : ""));
	    return nice ;
	}
	
	static String hosting24deleter(String in)
	{
		in = in.replaceAll(new String("<!-- Hosting24 Analytics Code -->"), new String(""));
		in = in.replaceAll(new String("<script type=\"text/javascript\" src=\"http://stats.hosting24.com/count.php\"></script>"), new String(""));
		in = in.replaceAll(new String("<!-- End Of Analytics Code -->"), new String(""));
		return in.replaceAll("[\r\n]", "");
	}
}
