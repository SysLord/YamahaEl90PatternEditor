package de.syslord.electonePattern.main_OLD;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import electone.dataobjects.Volume;

class VolumeSelect extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private static final int STEPS = 9;

	private Volume volume;

	private Color rectColor = Color.BLACK;

	private int bar;

	private int step;

	public VolumeSelect() {
		setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.black));
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void setModel(Volume volume) {
		this.volume = volume;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth();
		int height = getHeight();

		g.setColor(rectColor);
		if (width > 0 && height > 0) {
			int rectHeight = (height * volume.getVolume()) / STEPS;
			g.fillRect(0, height - rectHeight, width, rectHeight);
		}
		if (bar > 0) {
			int x = bar * width / step;
			g.drawLine(x, 0, x, height);
		}
	}

	private void setValueFromMouse(Point point) {
		double step = getHeight() / (double) STEPS;
		int value = STEPS - (int) (point.y / step);
		volume.setVolume(value);
		repaint();
	}

	private boolean leftMouseDown(MouseEvent event) {
		return (event.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		setValueFromMouse(event.getPoint());
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent event) {

	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (leftMouseDown(event)) {
			setValueFromMouse(event.getPoint());
		}
	}

	// TODO Params
	public void setHighlight(int step, int bar) {
		this.step = step;
		this.bar = bar;
		repaint();
	}

}