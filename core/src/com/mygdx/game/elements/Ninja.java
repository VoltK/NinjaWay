package com.mygdx.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.util.Assets;
import com.mygdx.game.util.Constants;


public class Ninja {

    enum MoveState{
        RUNNING,
        STANDING
    }

    enum MoveDirection{
        RIGHT,
        LEFT
    }

    enum AttackState{
        ATTACKING,
        STILL
    }

    Vector2 position;
    TextureRegion skin;
    Viewport viewport;

    long runStartTime;
    float attackTime;

    MoveDirection direction;
    MoveState moveState;
    AttackState attackState;

    int health;
    int points;

    public Ninja(Viewport viewport){
        moveState = MoveState.STANDING;
        direction = MoveDirection.RIGHT;
        attackState = AttackState.STILL;
        this.viewport = viewport;
        skin = Assets.instance.ninjaAssets.standingRight;

        init();
    }

    public void init(){
        position = new Vector2((viewport.getWorldWidth()-skin.getRegionWidth())/2, 0);
        health = 3;
        points = 0;
    }

    public int getHealth(){
        return health;
    }

    public int getPoints() { return points; }

    public void update(Fruits fruits, float delta){
        // attack control
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            attackState = AttackState.ATTACKING;
            attackTime = 0;
        }

        // movement controls
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            move(delta);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            move(-delta);
        }
        else{ moveState = MoveState.STANDING; }

        isTouched(fruits);
        checkSides();
    }
    // set borders for player
    public void checkSides(){
        if(position.x + skin.getRegionWidth() > viewport.getWorldWidth()){
            position.x = viewport.getWorldWidth()-skin.getRegionWidth();
        }
        if(position.x < 0){
            position.x = 0;
        }
    }

    public void move(float delta){
        // dont start timer if player is running
        if(moveState != MoveState.RUNNING){
            runStartTime = TimeUtils.nanoTime();
        }

        moveState = MoveState.RUNNING;
        // if delta positive means player moves to the right
        direction = delta >= 0 ? MoveDirection.RIGHT: MoveDirection.LEFT;
        position.x += delta * Constants.NINJA_VELOCITY;
    }

    public void isTouched(Fruits fruits) {

        for (int i = 0; i < fruits.fruits.size; i++) {

            Fruit fruit = fruits.fruits.get(i);
            // check it's normal --> hasn't collide yet
            if(fruit.state == Fruit.FruitState.NORMAL){

                if (fruit.name.equals(Constants.NUKE) || fruit.name.equals(Constants.TNT)){
                    if (checkBetween(fruit) && checkHeadCollision(fruit)){
                        fruit.setCollide();
                        bombCollision(fruit);
                    }

                }else if(attackState == AttackState.ATTACKING){

                    if (checkBetween(fruit) && checkHeadCollision(fruit)) {
                        fruit.setCollide();
                        // if it's a fruit add point
                        if(fruit.name.contains("fruit")){
                            points++;
                        }else{
                            bombCollision(fruit);
                        }
                    }
                }
            }
        }
    }

    public boolean checkHeadCollision(Fruit fruit){
        return fruit.position.y <= skin.getRegionHeight() * 0.75;
    }

    public boolean checkBetween(Fruit fruit){
        boolean between;
        float fruitCenter = fruit.fruitTexture.getRegionWidth()/2.0f;

        between = position.x <= fruit.position.x + fruitCenter && fruit.position.x <= position.x + skin.getRegionWidth();

        if(attackState == AttackState.ATTACKING && direction == MoveDirection.LEFT){
            between = position.x - skin.getRegionWidth() <= fruit.position.x && fruit.position.x <= position.x;
        }

        return between;
    }

    public void bombCollision(Fruit fruit){
        // if nuke ninja dies, if tnt -1 HP
        if(fruit.name.equals("nuke")){
            health -= 3;
        }else{
            health--;
        }
    }

    // get time between frames
    public float getTotalTime(){
        return MathUtils.nanoToSec * (TimeUtils.nanoTime() - runStartTime);
    }

    private void setSkin(){

        if(direction == MoveDirection.RIGHT && moveState == MoveState.RUNNING){
            skin = Assets.instance.ninjaAssets.runRightAnimation.getKeyFrame(getTotalTime());
        }
        else if(direction == MoveDirection.RIGHT && moveState == MoveState.STANDING){
            skin = Assets.instance.ninjaAssets.standingRight;
        }
        else if (direction == MoveDirection.LEFT && moveState == MoveState.RUNNING){
            skin = Assets.instance.ninjaAssets.runLeftAnimation.getKeyFrame(getTotalTime());
        }
        else if(direction == MoveDirection.LEFT && moveState == MoveState.STANDING){
            skin = Assets.instance.ninjaAssets.standingLeft;
        }
    }

    private void makeAttackAnimation(SpriteBatch batch){
        // keep adding constant attack speed
        attackTime += Constants.ATTACK_TIME;
        // depending on direction pick right animation

        float x;
        if(direction == MoveDirection.RIGHT) {
            // pick next frame depending on time
            skin = Assets.instance.ninjaAssets.attackRightAnimation.getKeyFrame(attackTime);
            x = position.x;
        }
        else{
            skin = Assets.instance.ninjaAssets.attackLeftAnimation.getKeyFrame(attackTime);
            x = position.x - skin.getRegionWidth() / 2.0f;
        }

        batch.draw(skin, x, position.y, skin.getRegionWidth(), Constants.NINJA_HEIGHT);

        if(Assets.instance.ninjaAssets.attackRightAnimation.isAnimationFinished(attackTime))
            attackState = AttackState.STILL;
    }

    public void render(SpriteBatch batch){
        System.out.println("Points: " + points + "\nHealth: " + health);
        // draw all animation frames at once
        if (attackState == AttackState.ATTACKING) {
            makeAttackAnimation(batch);
        }
        else {
            setSkin();
            batch.draw(skin, position.x, position.y, skin.getRegionWidth(), Constants.NINJA_HEIGHT);
        }
    }

}
