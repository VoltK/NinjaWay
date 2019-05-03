package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NinjaWayGame;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.ScreenDefaults;

public class GameOverScreen implements Screen {

    Viewport viewport;
    SpriteBatch batch;
    Stage stage;
    Game game;
    Sprite sprite;
    int topScore;
    MainScreen mainScreen;
    Skin skin;

    public GameOverScreen(Game game, MainScreen mainScreen, int currentScore){
        this.game = game;
        this.mainScreen = mainScreen;
        this.batch = mainScreen.batch;
        this.sprite = mainScreen.sprite;
        this.viewport = mainScreen.viewport;
        this.topScore = mainScreen.topScore;

        stage = new Stage(this.viewport, this.batch );

        stage.addActor(createTable(currentScore));

        Gdx.input.setInputProcessor(stage);
    }

    private Table createTable(int currentScore){
        skin = Assets.instance.arcadeAssets.skin;

        Label status = new Label("GAME OVER", skin, "title");
        Label restartLabel = new Label("RESTART", skin, "screen");
        Label exitLabel = new Label("EXIT", skin, "screen");

        Label highestLabel = new Label("TOP SCORE: " + topScore, skin,  "default");
        Label currentLabel = new Label("CURRENT SCORE: " + currentScore, skin, "default");

        Button exitButton = new Button(Assets.instance.buttons.redButtonStyle);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                exitGame();
            }
        });

        Button restartButton = new Button(Assets.instance.buttons.greenButtonStyle);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                restartGame();
            }
        });

        Table table = new Table();
        table.top();
        table.defaults().width(60);
        table.setFillParent(true);

        table.row().height(100).padTop(40);
        table.add(status).padRight(150);

        table.row().height(50).padTop(20);
        table.add(restartButton).padRight(70);
        table.add(exitButton).padRight(40);

        table.row().height(50);
        table.add(restartLabel).padRight(80);
        table.add(exitLabel).padRight(20);

        table.row();
        table.add(highestLabel).padRight(90);

        table.row().padTop(10);
        table.add(currentLabel).padRight(90);

        return table;
    }

    private void restartGame(){
        game.setScreen(new MainScreen((NinjaWayGame) game, sprite, topScore));
        mainScreen.sound.dispose();
    }

    public void exitGame(){
        Gdx.app.exit();
        dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenDefaults.defaultRender(batch, sprite, stage);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
