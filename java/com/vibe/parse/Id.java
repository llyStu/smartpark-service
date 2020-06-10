package com.vibe.parse;

public class Id {
	/*@Autowired
	public static Application application;
	public static IdGenerator<Integer> gen = application.getIntIdGenerator("asset");
	
	public static int getUniqueId() {
		return gen.next();
	}*/
	
	private static int id = 1;
	public static int getUniqueId() {
		return id++;
	}
}
