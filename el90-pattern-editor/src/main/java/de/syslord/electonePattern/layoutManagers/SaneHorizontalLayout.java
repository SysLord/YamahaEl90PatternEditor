package de.syslord.electonePattern.layoutManagers;

import java.awt.Component;
import java.util.List;

public class SaneHorizontalLayout extends SaneDirectionalLayout {

	@Override
	protected void setComponentBounds(List<Component> components, int index, int dynamicOffset, int staticOffset,
			int dynamicSize, int staticSize) {
		Component component = components.get(index);
		component.setBounds(dynamicOffset, staticOffset, dynamicSize, staticSize);
	}

	@Override
	protected void layoutHandler(List<Component> components, int maxWidth, int maxHeight) {
		doLayout(components, maxWidth, maxHeight);
	}

}
