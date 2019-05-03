package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.game.NinjaWayGame;
import com.mygdx.game.elements.Fruits;
import com.mygdx.game.elements.Ninja;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.Constants;


import java.util.Random;

public class MainScreen extends ScreenAdapter {

    NinjaWayGame game;

    public SpriteBatch batch;
    public Sprite sprite;
    Texture background;
    Ninja ninja;
    Fruits fruits;
    public Music sound;

    public ExtendViewport viewport;
    InfoScreen infoScreen;

    public int topScore;

    public MainScreen(NinjaWayGame game, Sprite sprite){
        topScore = 0;
        this.game = game;
        this.sprite = sprite;
        init();
    }

    public MainScreen(NinjaWayGame game, Sprite sprite, int topScore){
        this.topScore = topScore;
        this.game = game;
        this.sprite = sprite;
        //sound.pause();
        init();
    }

    private void init(){
        //Assets.instance.init(new AssetManager());

        Random random = new Random();

        batch = new SpriteBatch();

        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        infoScreen = new InfoScreen();

        ninja = new Ninja(viewport);
        fruits = new Fruits(viewport);

        sound = Assets.instance.musicAssets.sounds.get(random.nextInt(Constants.MAX_SOUNDS));
        sound.setLooping(true);
        sound.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        infoScreen.viewport.update(width, height,true);
        //gameOverScreen.viewport.update(width, height,true);
        ninja.init();
        fruits.init();

    }

    @Override
    public void dispose() {
        batch.dispose();
        Assets.instance.dispose();
    }

    @Override
    public void render(float delta) {
        viewport.apply();

        // update positions etc
        ninja.update(fruits, delta);
        fruits.update(delta);
        int currentScore = ninja.getPoints();
        topScore = Math.max(topScore, currentScore);

        // clear screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);

        // render here
        batch.begin();
        // background
        sprite.draw(batch);
        // ninja
        ninja.render(batch);
        //fruit
        fruits.render(batch);

        batch.end();

        // show information
        infoScreen.render(batch, topScore,currentScore, ninja.getHealth());

        if(isGameOver()) {
            game.setScreen(new GameOverScreen(game, this, currentScore));
        }
//        else if(Gdx.input.isKeyPressed(Input.Keys.Z)){
//            game.setScreen(new GameOverScreen(game, this, currentScore));
//        }
    }

    public boolean isGameOver(){
        return ninja.getHealth() < 1;
    }
}
