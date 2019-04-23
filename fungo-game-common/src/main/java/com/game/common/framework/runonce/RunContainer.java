package com.game.common.framework.runonce;

import java.util.ArrayList;
import java.util.List;

public class RunContainer {

	public static List<IRunOnce> list = new ArrayList<>();
	
	public static void executeOnece(){
		for(IRunOnce o:list){
			try {
				o.excuteOnce();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void add(IRunOnce one){
		list.add(one);
	}

	
	
}
