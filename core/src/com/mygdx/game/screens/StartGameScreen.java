package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.NinjaWayGame;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.ScreenDefaults;

public class StartGameScreen implements Screen {

    Viewport viewport;
    SpriteBatch batch;
    Stage stage;
    Sprite sprite;
    Skin skin;
    Game game;
    Texture background;

    public StartGameScreen(Game game){
        Assets.instance.init(new AssetManager());

        this.game = game;
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        batch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("hill2.png"));
        sprite = new Sprite(background, background.getWidth(),background.getHeight() + 20);

        stage = new Stage(viewport, batch);

        stage.addActor(createStartTable());

        Gdx.input.setInputProcessor(stage);

    }

    private Table createStartTable(){
        skin = Assets.instance.arcadeAssets.skin;

        Label startLabel = new Label("PLAY", skin, "title");

        Label goalLabel = new Label("GOAL: CUT FRUITS", skin, "default");

        Label avoidLabel = new Label("AVOID BOMBS", skin, "default");

//        Image nuke = new Image(Assets.instance.fruitAssets.nuke);
//        Image tnt = new Image(Assets.instance.fruitAssets.tnt);

        Button startButton = new Button(Assets.instance.buttons.greenButtonStyle);

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               startGame();
            }
        });

        Table table = new Table();
        table.center().top();
        table.defaults().width(60).height(30);
        table.setFillParent(true);

        table.row().height(100).padTop(40);
        table.add(startLabel).padRight(70);

        table.row().height(50).padTop(5);
        table.add(startButton);

        table.row().padTop(30);
        table.add(goalLabel).padRight(190);

        table.row().padTop(30);
        table.add(avoidLabel).padRight(135);

//        table.row().height(50).padTop(10);
//        //
//        table.add(tnt);
//        table.row().height(50).padTop(10);
//
//        table.add(nuke);


        return table;
    }

    private void startGame(){
        game.setScreen(new MainScreen((NinjaWayGame) game, sprite));
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
        batch.dispose();
        stage.dispose();
    }
}
