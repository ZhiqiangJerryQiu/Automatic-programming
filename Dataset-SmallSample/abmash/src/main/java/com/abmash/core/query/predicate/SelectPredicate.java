package com.abmash.core.query.predicate;

import com.abmash.core.jquery.JQueryFactory;

public class SelectPredicate extends JQueryPredicate {

	private String selector;
	
	public SelectPredicate(String selector) {
		this.selector = selector;
		buildCommands();
	}

	@Override
	public void buildCommands() {
		add(JQueryFactory.select(selector != null && !selector.equals("") ? "'" + selector + "'" : null, 100));
	}
}
