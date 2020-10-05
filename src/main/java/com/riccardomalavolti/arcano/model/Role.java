package com.riccardomalavolti.arcano.model;

public enum Role {
	ADMINISTRATOR(Values.ADMIN_VALUE),
	JUDGE(Values.JUDGE_VALUE),
	PLAYER(Values.PLAYER_VALUE);
	
	
	Role(String value){}

	public static class Values {
		public static final String PLAYER_VALUE = "PLAYER";
		public static final String ADMIN_VALUE = "ADMINISTRATOR";
		public static final String JUDGE_VALUE = "JUDGE";
	}
    
}