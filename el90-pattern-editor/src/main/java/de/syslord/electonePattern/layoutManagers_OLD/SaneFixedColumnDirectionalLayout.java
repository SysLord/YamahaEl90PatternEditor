package de.syslord.electonePattern.layoutManagers_OLD;

import java.awt.Component;
import java.util.List;

public abstract class SaneFixedColumnDirectionalLayout extends SaneLayout {

	protected int firstFixedWidth;

	protected int lastFixedWidth;

	public SaneFixedColumnDirectionalLayout(int firstFixedWidth, int lastFixedWidth) {
		this.firstFixedWidth = firstFixedWidth;
		this.lastFixedWidth = lastFixedWidth;
	}

	@Override
	protected void doLayout(List<Component> components, int maxSize, int staticSize) {
		int fixedWidths = firstFixedWidth + lastFixedWidth;
		int componentCount = components.size();
		if (componentCount > 0) {
			setComponentBounds(components, 0, 0, 0, firstFixedWidth, staticSize);
		}
		if (componentCount > 1) {
			setComponentBounds(components, 1, firstFixedWidth, 0, maxSize - fixedWidths, staticSize);
		}
		if (componentCount > 2) {
			setComponentBounds(components, 2, maxSize - lastFixedWidth, 0, lastFixedWidth, staticSize);
		}
	}

}
