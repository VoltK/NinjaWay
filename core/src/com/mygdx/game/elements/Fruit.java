package com.mygdx.game.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.Constants;

import java.util.Random;

public class Fruit {

    enum FruitState{
        NORMAL,
        COLLIDE,
    }

    Vector2 position;
    Vector2 velocity;
    TextureRegion fruitTexture;
    FruitState state;
    String name;
    float cutTime;

    float width;
    float height;


    public Fruit(Vector2 position){
        Random random = new Random();
        velocity = new Vector2(0,0);
        state = FruitState.NORMAL;
        this.position = position;
        fruitTexture = Assets.instance.fruitAssets.fruits.get(random.nextInt(Assets.instance.fruitAssets.size));
        name = fruitTexture.toString();

        if(name.contains("fruit")){
            width = Constants.FRUIT_WIDTH;
            height = Constants.FRUIT_HEIGHT;
        }
        else{
            width = Constants.BOMB_WIDTH;
            height = Constants.BOMB_HEIGHT;
        }

    }

    public void update(float delta){
        velocity.y += Constants.GRAVITY * delta;
        position.y += velocity.y * delta;
        position.x += velocity.x;
    }

    public void setCollide(){
        cutTime = 0;
        state = FruitState.COLLIDE;
    }

    public void render(SpriteBatch batch){

        batch.draw(fruitTexture, position.x, position.y, width, height);

    }

}
