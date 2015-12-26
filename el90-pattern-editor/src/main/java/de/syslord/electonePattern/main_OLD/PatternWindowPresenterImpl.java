package de.syslord.electonePattern.main_OLD;

import de.syslord.electonePattern.Audio.Player;
import de.syslord.electonePattern.Audio.Player.PlayerPositionListener;
import electone.dataobjects.Pattern;

//@Component
public class PatternWindowPresenterImpl implements PatternWindowPresenter {

	private final PatternWindow view;

	private Pattern model;

	private Player player;

	PatternWindowPresenterImpl(final PatternWindow view) {
		this.view = view;
		player = new Player(new PlayerPositionListener() {

			@Override
			public void updatePosition(int position) {
				view.setHighlight(position);
			}
		});
	}

	public Pattern getModel() {
		return model;
	}

	@Override
	public void setModel(Pattern model) {
		this.model = model;
		player.setModel(model);
		view.setModel(model);
	}

	@Override
	public void onStartPattern() {
		player.start(120);
	}

	@Override
	public void onStopPattern() {
		player.stop();
	}

}
