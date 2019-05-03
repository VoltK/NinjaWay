package com.mygdx.game.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.Constants;

import java.util.Random;

public class Fruits {

    Array<Fruit> fruits;
    Viewport viewport;

    public Fruits(Viewport viewport){
        this.viewport = viewport;
        init();
    }

    public void init(){
        fruits = new Array<Fruit>( 100);
    }

    public void update(float delta){
        Random random = new Random();
        if(random.nextFloat() < Constants.FRUIT_SPAWN_RATE * delta / 3){
            Vector2 position = new Vector2(viewport.getWorldWidth() * random.nextFloat(),
                    viewport.getWorldHeight() + Constants.FRUIT_HEIGHT);
            fruits.add(new Fruit(position));
        }

        //float direction = delta;
        for(int x = 0; x < fruits.size; x++){
            Fruit fruit = fruits.get(x);
            fruit.update(delta);
            if(fruit.position.y < 0){
                fruits.removeIndex(x);
            }
        }
    }

    private void setExplosionSize(Fruit fruit){
        if (fruit.name.equals(Constants.NUKE) || fruit.name.equals(Constants.TNT)){
            fruit.width *= 2;
            fruit.height *= 2;
        }
    }

    private void makeExplosionAnimation(Fruit fruit, SpriteBatch batch, int i){
        setExplosionSize(fruit);

        fruit.cutTime += Constants.EXPLOSION_TIME;
        fruit.fruitTexture = Assets.instance.fruitAssets.boom.getKeyFrame(fruit.cutTime);
        fruit.render(batch);
        if(Assets.instance.fruitAssets.boom.isAnimationFinished(fruit.cutTime)) {
            fruits.removeIndex(i);
        }
    }

    public void render(SpriteBatch batch){
        for(int i = 0; i < fruits.size; i++){
            Fruit fruit = fruits.get(i);

            if(fruit.state == Fruit.FruitState.COLLIDE) {
                setExplosionSize(fruit);
                makeExplosionAnimation(fruit, batch, i);

            }else fruit.render(batch);

        }
    }

    public void dispose(){
        fruits.clear();
    }
}
