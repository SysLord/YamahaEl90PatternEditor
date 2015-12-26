package de.syslord.electonePattern.layoutManagers_OLD;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;

public abstract class SaneDirectionalLayout extends SaneLayout {

	@Override
	protected abstract void setComponentBounds(List<Component> components, int index, int dynamicOffset,
			int staticOffset, int dynamicSize, int staticSize);

	@Override
	protected abstract void layoutHandler(List<Component> components, int maxWidth, int maxHeight);

	@Override
	protected void doLayout(List<Component> components, int maxSize, int staticSize) {
		int componentCount = components.size();
		int componentBaseSize = maxSize / componentCount;
		int remainder = maxSize - (componentBaseSize * componentCount);

		double additionalPixelStep = 0;
		if (remainder > 0) {
			additionalPixelStep = componentCount / (double) remainder;
		}
		double nextPixel = additionalPixelStep;

		int offset = 0;
		for (int index = 0; index < componentCount; index++) {

			int lastIndex = componentCount - 1;
			int ceil = (int) Math.ceil(nextPixel);

			int pixel = 0;
			if (remainder > 0 && index % ceil == 0) {
				nextPixel += additionalPixelStep;
				pixel = 1;
			}
			int componentSize = componentBaseSize + pixel;

			if (index == lastIndex && offset + componentSize < maxSize) {
				componentSize += 1;
			}
			setComponentBounds(components, index, offset, 0, componentSize, staticSize);
			offset += componentSize;
		}

		if (maxSize != offset) {
			throw new RuntimeException("FAIL");
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(parent.getComponents().length, 1);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(parent.getComponents().length, 1);
	}

	@Override
	public void addLayoutComponent(String arg0, Component arg1) {
	}

	@Override
	public void removeLayoutComponent(Component component) {
	}

}
