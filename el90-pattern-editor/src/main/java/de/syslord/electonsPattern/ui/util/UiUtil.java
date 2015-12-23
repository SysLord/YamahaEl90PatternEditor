package de.syslord.electonsPattern.ui.util;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class UiUtil {

	public static RowSpec[] repeatRows(int rowss, String specString) {
		RowSpec[] rowSpecs = new RowSpec[rowss];
		RowSpec spec = RowSpec.decode(specString);
		for (int row = 0; row < rowss; row++) {
			rowSpecs[row] = spec;
		}
		return rowSpecs;
	}

	public static ColumnSpec[] getColumnSpec(String colSpec) {
		return new ColumnSpec[] { ColumnSpec.decode(colSpec) };
	}
}
