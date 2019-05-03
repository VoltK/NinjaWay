
package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    AssetManager assetManager;

    public NinjaAssets ninjaAssets;
    public FruitAssets fruitAssets;
    public MusicAssets musicAssets;
    public ArcadeUIAssets arcadeAssets;
    public Buttons buttons;

    private Assets() {
    }

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);

        assetManager.finishLoading();
        // set asset manager error handler
        assetManager.setErrorListener(this);

        TextureAtlas atlasObjects = assetManager.get(Constants.TEXTURE_ATLAS);
        ninjaAssets = new NinjaAssets(atlasObjects);

        fruitAssets = new FruitAssets(atlasObjects);

        arcadeAssets = new ArcadeUIAssets();

        musicAssets = new MusicAssets();

        buttons = new Buttons(arcadeAssets.skin);

    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    public class NinjaAssets {

        public final TextureRegion standingRight;
        public final TextureRegion standingLeft;
        public final TextureRegion heart;
        public final Animation runRightAnimation;
        public final Animation runLeftAnimation;
        public final Animation attackRightAnimation;
        public final Animation attackLeftAnimation;

        public NinjaAssets(TextureAtlas atlas) {
            // idle
            standingRight = atlas.findRegion(Constants.STAND_RIGHT);
            standingLeft = atlas.findRegion(Constants.STAND_LEFT);
            // heart image
            heart = atlas.findRegion(Constants.HEART);
            // run animations
            runRightAnimation = makeAnimation(atlas, Constants.RUN_MODE, Constants.DIRECTION_RIGHT);
            runLeftAnimation = makeAnimation(atlas, Constants.RUN_MODE, Constants.DIRECTION_LEFT);
            // attack animations
            attackRightAnimation = makeAnimation(atlas, Constants.ATTACK_MODE, Constants.DIRECTION_RIGHT);
            attackLeftAnimation = makeAnimation(atlas, Constants.ATTACK_MODE, Constants.DIRECTION_LEFT);
        }

        private Animation makeAnimation(TextureAtlas atlas, String mode, String direction){
            Array<TextureRegion> regions = new Array<TextureRegion>();

            for(int i = Constants.RUN_MIN; i <= Constants.RUN_MAX; i++){
                regions.add(atlas.findRegion(String.format("%s-%s-%d", mode, direction, i)));
            }

            return new Animation(Constants.RUN_TIME, regions, PlayMode.LOOP);
        }

    }

    public class FruitAssets{

        public final Array<TextureRegion> fruits;
        public final Animation boom;
        public final int size;
        public  TextureRegion tnt;
        public  TextureRegion nuke;


        public FruitAssets(TextureAtlas atlas){
            Array<TextureRegion> regions = new Array<TextureRegion>();
            for(int i = 0; i < 10; i++){
                regions.add(atlas.findRegion("boom-0"));
            }
            boom = new Animation(Constants.RUN_TIME, regions, PlayMode.LOOP);

            fruits = makeFruits(atlas);
            size = fruits.size;
        }

        private Array<TextureRegion> makeFruits(TextureAtlas atlas){
            Array<TextureRegion> regions = new Array<TextureRegion>();
            for(int i = Constants.MIN_FRUIT; i <= Constants.MAX_FRUIT; i++){
                regions.add(atlas.findRegion(String.format("fruit-%d", i)));
            }
            // mix fruits with two types of bombs

            tnt = atlas.findRegion(Constants.TNT);
            nuke = atlas.findRegion(Constants.NUKE);

            regions.add(tnt);
            regions.add(nuke);

            return regions;
        }

    }

    public class MusicAssets{

        public Array<Music> sounds;

        public MusicAssets(){
            sounds = new Array<Music>();
            for(int i = 0; i < Constants.MAX_SOUNDS; i++){
                Music sound = Gdx.audio.newMusic(Gdx.files.internal(String.format("music/sound-%d.mp3", i)));
                sounds.add(sound);
            }
        }

    }

    public class ArcadeUIAssets{

        public final Skin skin;

        public ArcadeUIAssets(){
            skin = new Skin(Gdx.files.internal(Constants.ARCADE_UI));
        }
    }

    public class Buttons{

        public Button.ButtonStyle redButtonStyle;
        public Button.ButtonStyle greenButtonStyle;

        public Buttons(Skin skin){
            redButtonStyle = createButtonStyle(skin, "red");
            greenButtonStyle = createButtonStyle(skin, "green");
        }

        private Button.ButtonStyle createButtonStyle(Skin skin, String color){
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.getDrawable("button-" + color);
            buttonStyle.down = skin.getDrawable("button-pressed-" + color);
            return buttonStyle;
        }
    }

}