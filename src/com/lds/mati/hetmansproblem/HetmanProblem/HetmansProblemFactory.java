package com.lds.mati.hetmansproblem.HetmanProblem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.lds.mati.CSP.engine.Coinstraint;
import com.lds.mati.CSP.engine.DomainCoinstrantFactory;

public class HetmansProblemFactory implements DomainCoinstrantFactory<Integer>{
	private static HetmansProblemFactory singleton;
	private HetmansProblemFactory(){
	}
	
	public static HetmansProblemFactory getFactory(){
		if(singleton==null)
			singleton = new HetmansProblemFactory();
		return singleton;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer>[] getDomain(Object arg) {
		int size = (Integer) arg;
		List<Integer>[] domains = (List<Integer>[]) Array.newInstance(List.class, size);
		for(int i = 0 ; i < size ; ++i){
			List<Integer> tempDom = new ArrayList<>(size);
			for(int j = 0 ; j < size ; ++j){
				tempDom.add(j);
			}
			domains[i] = tempDom;
		}
		return domains;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Coinstraint<Integer>[] getCoinstraints(Object arg) {
		int size = (Integer) arg;
		Coinstraint<Integer>[] coinstraints = (Coinstraint<Integer>[]) Array.newInstance(Coinstraint.class, size);
		for(int i = 0 ; i < size ; ++i){
			coinstraints[i] = new HetmansCoinstraint(i);
		}
		return coinstraints;
	}
}
