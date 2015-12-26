package de.syslord.electonePattern.main_OLD;

import java.awt.Component;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.common.collect.Lists;

import de.syslord.electonePattern.layoutManagers_OLD.SaneFixedColumnHorizontalLayout;
import de.syslord.electonePattern.layoutManagers_OLD.SaneFixedColumnVerticalLayout;
import de.syslord.electonePattern.layoutManagers_OLD.SaneHorizontalLayout;
import de.syslord.electonePattern.layoutManagers_OLD.SaneVerticalLayout;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.TrackPattern;

public class PatternWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private PatternWindowPresenter presenter;

	private List<TrackRow> rows = Lists.newLinkedList();

	private JButton startPlayButton;

	private JButton stopPlayButton;

	public PatternWindow() {
		super();
		initUI();

		presenter = new PatternWindowPresenterImpl(this);
	}

	private void initUI() {
		setTitle("Pattern Editor");
		setSize(1200, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel patternPanel = new JPanel(new SaneVerticalLayout());

		for (int row = 0; row < PatternConstants.MAX_TRACKS; row++) {
			TrackRow trackRow = new TrackRow();
			rows.add(trackRow);
			patternPanel.add(trackRow);
		}
		JPanel horizontalPanel = new JPanel(new SaneFixedColumnHorizontalLayout(100, 50));
		horizontalPanel.add(initTopUi());
		horizontalPanel.add(patternPanel);
		horizontalPanel.add(new Label());

		JPanel verticalPanel = new JPanel(new SaneFixedColumnVerticalLayout(50, 200));
		verticalPanel.add(new Label());
		verticalPanel.add(horizontalPanel);
		verticalPanel.add(new Label());

		this.add(verticalPanel);
	}

	private Component initTopUi() {
		JPanel topPanel = new JPanel(new SaneHorizontalLayout());

		startPlayButton = new JButton("Start");
		stopPlayButton = new JButton("Stop");

		topPanel.add(startPlayButton);
		topPanel.add(stopPlayButton);

		startPlayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				presenter.onStartPattern();
			}
		});
		stopPlayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				presenter.onStopPattern();
			}
		});

		return topPanel;
	}

	// @Deprecated
	// private void initUI2() {
	// setTitle("Pattern Editor");
	// setSize(1200, 1000);
	// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// setLocationRelativeTo(null);
	//
	// ColumnSpec[] columnSpecs = UiUtil.getColumnSpec("40px:grow");
	// RowSpec[] rowSpecs = UiUtil.repeatRows(Pattern.MAX_TRACKS,
	// "fill:18px:grow");
	// FormLayout layout = new FormLayout(columnSpecs, rowSpecs);
	//
	// CellConstraints cc = new CellConstraints();
	// // LayoutManager layout = new MyLayout();
	// JPanel panel = new JPanel(layout);
	//
	// for (int row = 0; row < Pattern.MAX_TRACKS; row++) {
	// TrackRow trackRow = new TrackRow();
	// rows.add(trackRow);
	// panel.add(trackRow, cc.xy(1, row + 1));
	// }
	//
	// this.add(panel);
	// }

	public void setModel(Pattern model) {
		for (int row = 0; row < PatternConstants.MAX_TRACKS; row++) {
			TrackPattern trackPattern = model.getTrackPattern(row);
			rows.get(row).setModel(trackPattern);
		}
	}

	public PatternWindowPresenter getPresenter() {
		return presenter;
	}

	public void setHighlight(int position) {
		for (TrackRow trackRow : rows) {
			trackRow.setHighlight(position);
		}
	}

}
