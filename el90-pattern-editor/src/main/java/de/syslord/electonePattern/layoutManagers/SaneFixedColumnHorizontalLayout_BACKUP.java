package de.syslord.electonePattern.layoutManagers;

import java.awt.Component;
import java.util.List;

public class SaneFixedColumnHorizontalLayout_BACKUP extends SaneHorizontalLayout {

	private int[] firstColumnWidths;

	private int[] lastColumnWidths;

	private int firstWidthsSum;

	private int lastWidthsSum;

	private int fixedWidths;

	public SaneFixedColumnHorizontalLayout_BACKUP(int[] firstColumnWidths, int[] lastColumnWidths) {
		this.firstColumnWidths = firstColumnWidths;
		this.lastColumnWidths = lastColumnWidths;

		firstWidthsSum = 0;
		for (int width : firstColumnWidths) {
			firstWidthsSum += width;
		}
		lastWidthsSum = 0;
		for (int width : lastColumnWidths) {
			lastWidthsSum += width;
		}
		fixedWidths = firstWidthsSum + lastWidthsSum;
	}

	@Override
	protected void setComponentBounds(List<Component> components, int index, int dynamicOffset, int staticOffset,
			int dynamicSize, int staticSize) {
		Component component = components.get(index);

		if (index < firstColumnWidths.length) {
			int widths = sumFirst(index);
			component.setBounds(widths, staticOffset, firstColumnWidths[index], staticSize);
			return;
		}
		int componentsNumber = components.size();
		if (index > componentsNumber - 1 - lastColumnWidths.length) {
			int widths = sumLastWidths(componentsNumber, index);
			component.setBounds(widths, staticOffset, firstColumnWidths[index], staticSize);
			return;
		}

		component.setBounds(dynamicOffset + firstWidthsSum, staticOffset, dynamicSize - fixedWidths, staticSize);
	}

	private int sumLastWidths(int componentsNumber, int index) {
		int firstIndex = componentsNumber - 1 - lastColumnWidths.length;
		int sum = 0;
		for (int i = 0; i < firstIndex + index; i++) {
			sum += lastColumnWidths[i];
		}
		return sum;
	}

	private int sumFirst(int index) {
		int sum = 0;
		for (int i = 0; i < index; i++) {
			sum += firstColumnWidths[i];
		}
		return sum;
	}

	@Override
	protected void layoutHandler(List<Component> components, int maxWidth, int maxHeight) {
		int firstIndex = firstColumnWidths.length;
		int lastIndex = components.size() - 1 - lastColumnWidths.length;
		List<Component> dynamicComponents = components.subList(firstIndex, lastIndex);

		doLayout(dynamicComponents, maxWidth - fixedWidths, maxHeight);
	}
}
