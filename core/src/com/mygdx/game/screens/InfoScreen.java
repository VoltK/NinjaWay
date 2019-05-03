package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.Constants;


public class InfoScreen {

    public final Viewport viewport;
    final BitmapFont font;

    public InfoScreen() {

        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(1);
    }

    public void render(SpriteBatch batch, int topScore, int currentPoints, int lives) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();

        showScore(batch, topScore,currentPoints);
        showHealth(batch, lives);

        batch.end();

        }

        private void showScore(SpriteBatch batch, int topScore, int currentPoints){
            String info = "TOP SCORE: " + topScore + "\nCURRENT SCORE: " + currentPoints;
            font.draw(batch, info, viewport.getWorldWidth() - Constants.INFO_MARGIN, viewport.getWorldHeight() - Constants.INFO_MARGIN,
                    0, Align.right, false);
        }

        private void showHealth(SpriteBatch batch, int lives){
            TextureRegion heart = Assets.instance.ninjaAssets.heart;
            for (int i = 1; i <= lives; i++) {
                Vector2 heartPosition = new Vector2(
                         Constants.HEART_WIDTH * i,
                         viewport.getWorldHeight() - Constants.INFO_MARGIN - Constants.HEART_HEIGHT
                );
                batch.draw(heart, heartPosition.x, heartPosition.y, Constants.HEART_WIDTH, Constants.HEART_HEIGHT);
            }
        }

}
