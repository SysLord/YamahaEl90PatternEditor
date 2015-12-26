package de.syslord.electonePattern.layoutManagers_OLD;

import java.awt.Component;
import java.util.List;

public class SaneFixedColumnVerticalLayout extends SaneFixedColumnDirectionalLayout {

	public SaneFixedColumnVerticalLayout(int firstFixedWidth, int lastFixedWidth) {
		super(firstFixedWidth, lastFixedWidth);
	}

	@Override
	protected void layoutHandler(List<Component> components, int maxWidth, int maxHeight) {
		doLayout(components, maxHeight, maxWidth);
	}

	@Override
	protected void setComponentBounds(List<Component> components, int index, int dynamicOffset, int staticOffset,
			int dynamicSize, int staticSize) {
		components.get(index).setBounds(staticOffset, dynamicOffset, staticSize, dynamicSize);
	}
}
