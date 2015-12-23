package de.syslord.electonePattern.layoutManagers;

import java.awt.Component;
import java.util.List;

public class SaneFixedColumnHorizontalLayout extends SaneFixedColumnDirectionalLayout {

	public SaneFixedColumnHorizontalLayout(int firstFixedWidth, int lastFixedWidth) {
		super(firstFixedWidth, lastFixedWidth);
	}

	@Override
	protected void layoutHandler(List<Component> components, int maxWidth, int maxHeight) {
		doLayout(components, maxWidth, maxHeight);
	}

	@Override
	protected void setComponentBounds(List<Component> components, int index, int dynamicOffset, int staticOffset,
			int dynamicSize, int staticSize) {
		components.get(index).setBounds(dynamicOffset, staticOffset, dynamicSize, staticSize);
	}
}
