package de.syslord.electonePattern.main_OLD;

import java.awt.Color;
import java.awt.LayoutManager;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.common.collect.Lists;

import de.syslord.electonePattern.layoutManagers_OLD.SaneFixedColumnHorizontalLayout;
import de.syslord.electonePattern.layoutManagers_OLD.SaneFullLayout;
import de.syslord.electonePattern.layoutManagers_OLD.SaneHorizontalLayout;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.TrackPattern;

public class TrackRow extends JPanel {

	private static final int DRAGGER_WIDTH = 20;

	private static final int INSTRUMENT_LABEL_WIDTH = 160;

	private static final Color HIGHLIGHT_COLOR = Color.GREEN;

	private static final Color NORMAL_COLOR = Color.ORANGE;

	private int currentColumnHighlight = -1;

	private static final long serialVersionUID = 1L;

	private JLabel label;

	private JLabel dragger;

	private LayoutManager layout;

	private JPanel panel;

	private LayoutManager patternLayout;

	private JPanel patternPanel;

	private List<VolumeSelect> volumeSelectors = Lists.newLinkedList();

	private TrackPattern model;

	public TrackRow() {
		super();
		initUI();
	}

	private void initUI() {
		label = new JLabel();
		label.setBackground(Color.LIGHT_GRAY);
		label.setOpaque(true);

		dragger = new JLabel("|||");
		dragger.setBackground(Color.CYAN);
		dragger.setOpaque(true);

		layout = new SaneFixedColumnHorizontalLayout(INSTRUMENT_LABEL_WIDTH, DRAGGER_WIDTH);
		panel = new JPanel(layout);
		panel.setBackground(Color.YELLOW);

		patternLayout = new SaneHorizontalLayout();
		patternPanel = new JPanel(patternLayout);

		setLayout(new SaneFullLayout());
		add(panel);

		panel.add(label);
		panel.add(patternPanel);
		panel.add(dragger);
	}

	public void setModel(TrackPattern model) {
		this.model = model;

		int countsPerBar = model.getQuantization().getCountsPerBar();
		int counts = PatternConstants.MAX_BARS * PatternConstants.MAX_BAR_COUNTS * countsPerBar;

		label.setText(model.getInstrumentRepresentation());

		for (int count = 0; count < counts; count++) {
			VolumeSelect volumeSelect = new VolumeSelect();
			int usedCount = (PatternConstants.MAX_PATTERN_LENGTH / counts) * count;
			volumeSelect.setModel(model.getVolume(usedCount));
			volumeSelectors.add(volumeSelect);
			patternPanel.add(volumeSelect);
		}
	}

	public void setHighlight(int column) {
		int countsPerBar = model.getQuantization().getCountsPerBar();
		int step = PatternConstants.MAX_SUBCOUNT / countsPerBar;
		int componentIndex = column / step;

		if (currentColumnHighlight >= 0 && componentIndex != currentColumnHighlight) {
			volumeSelectors.get(currentColumnHighlight).setHighlight(step, -1);
		}
		currentColumnHighlight = componentIndex;
		volumeSelectors.get(componentIndex).setHighlight(step, column % step);
	}

	public void setHighlightBackground(int column) {
		int countsPerBar = model.getQuantization().getCountsPerBar();
		int step = PatternConstants.MAX_SUBCOUNT / countsPerBar;
		if (column % step == 0) {
			int componentIndex = column / step;
			if (componentIndex < volumeSelectors.size()) {
				if (currentColumnHighlight >= 0) {
					setColumHighlightBackground(currentColumnHighlight, false);
				}
				currentColumnHighlight = componentIndex;
				setColumHighlightBackground(componentIndex, true);
			} else {
				System.out.println("fail" + String.valueOf(componentIndex));
			}
		}

	}

	private void setColumHighlightBackground(int column, boolean highlight) {
		Color color = NORMAL_COLOR;
		if (highlight) {
			color = HIGHLIGHT_COLOR;
		}
		volumeSelectors.get(column).setBackground(color);
	}
}
