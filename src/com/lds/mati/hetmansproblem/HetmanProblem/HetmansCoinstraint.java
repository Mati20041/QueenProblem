package com.lds.mati.hetmansproblem.HetmanProblem;

import com.lds.mati.CSP.engine.Coinstraint;

public class HetmansCoinstraint implements Coinstraint<Integer> {

	private int hetman;

	public HetmansCoinstraint(int hetman) {
		this.hetman = hetman;
	}

	@Override
	public boolean isSatisfied(Integer[] vertices) {
		int position = vertices[hetman];
		for (int i = 0; i < vertices.length; ++i) {
			Integer position2 = vertices[i];
			if(position2==null)
				break;
			if ( i!=hetman
					&& (Math.abs(hetman - i) == Math.abs(position2 - position) || position2 == position)) {
				return false;
			}
		}
		return true;

	}
}
