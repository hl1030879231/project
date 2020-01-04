package test;

public class NativeEquation {//ÅäÆ½º¯Êý
	static 
	{ 
//		System.loadLibrary("handWritingRecog");
		//release
		System.load("E:\\java_workspace\\project\\project\\lib\\chemEquation.dll"); 
		//debug
//		System.load("E:\\Java\\eclipseee\\webspace\\project\\build\\classes\\jscript_chemEquation\\jscript_chemEquation\\chemEquation\\chemEquation\\x64\\Release\\chemEquation.dll");
	} 
	public static native String chemBalance(String a);
	public static native void test();
}
