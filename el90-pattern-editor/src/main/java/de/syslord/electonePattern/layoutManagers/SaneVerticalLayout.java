package de.syslord.electonePattern.layoutManagers;

import java.awt.Component;
import java.util.List;

public class SaneVerticalLayout extends SaneDirectionalLayout {

	@Override
	protected void setComponentBounds(List<Component> components, int index, int dynamicOffset, int staticOffset,
			int dynamicSize, int staticSize) {
		Component component = components.get(index);
		component.setBounds(staticOffset, dynamicOffset, staticSize, dynamicSize);
	}

	@Override
	protected void layoutHandler(List<Component> components, int maxWidth, int maxHeight) {
		doLayout(components, maxHeight, maxWidth);
	}

}
