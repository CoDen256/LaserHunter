package com.mygdx.laserhunter;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import screens.MainMenuScreen;

public class LaserHunterGame extends Game {

	public SpriteBatch batch;

	@Override
	public void create () {
        batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
        super.render();
	}
	
	@Override
	public void dispose () {
        batch.dispose();
	}
}
