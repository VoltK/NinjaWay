package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.StartGameScreen;

public class NinjaWayGame extends Game {

	@Override
	public void create()
	{
		setScreen(new StartGameScreen(this));
	}

}
