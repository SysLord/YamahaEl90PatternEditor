package de.syslord.electonePattern.main;

import electone.dataobjects.Pattern;

public interface PatternWindowPresenter {

	void setModel(Pattern pattern);

	void onStartPattern();

	void onStopPattern();

}
