package de.syslord.electonePattern.layoutManagers_OLD;

import java.awt.Component;
import java.util.List;

public class SaneFullLayout extends SaneLayout {

	@Override
	protected void setComponentBounds(List<Component> components, int index, int dynamicOffset, int staticOffset,
			int dynamicSize, int staticSize) {
		//
	}

	@Override
	protected void layoutHandler(List<Component> components, int maxWidth, int maxHeight) {
		if (components.size() > 0) {
			components.get(0).setBounds(0, 0, maxWidth, maxHeight);
		}
	}

	@Override
	protected void doLayout(List<Component> components, int maxSize, int staticSize) {
		//
	}

}
