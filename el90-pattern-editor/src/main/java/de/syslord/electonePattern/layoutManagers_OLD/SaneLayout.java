package de.syslord.electonePattern.layoutManagers_OLD;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.List;

import com.google.common.collect.Lists;

public abstract class SaneLayout implements LayoutManager {

	@Override
	public void layoutContainer(Container parent) {
		List<Component> components = Lists.newArrayList(parent.getComponents());
		if (components.size() == 0) {
			return;
		}

		Insets insets = parent.getInsets();
		int maxWidth = parent.getWidth() - (insets.left + insets.right);
		int maxHeight = parent.getHeight() - (insets.top + insets.bottom);

		layoutHandler(components, maxWidth, maxHeight);
	}

	protected abstract void setComponentBounds(List<Component> components, int index, int dynamicOffset,
			int staticOffset, int dynamicSize, int staticSize);

	protected abstract void layoutHandler(List<Component> components, int maxWidth, int maxHeight);

	protected abstract void doLayout(List<Component> components, int maxSize, int staticSize);

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
