package com.greenyetilab.race;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Stores all assets
 */
public class Assets {
    private static final float EXPLOSION_FRAME_DURATION = 0.1f;
    private static final float MINE_FRAME_DURATION = 0.2f;

    public final Skin skin;
    public final Array<TextureRegion> cars = new Array<TextureRegion>();
    public final TextureRegion wheel;
    public final TextureRegion dot;
    public final TextureAtlas atlas;
    public final Animation explosion;
    public final Animation mine;
    public final TextureRegion gift;

    private final HashMap<String, TextureAtlas.AtlasRegion> mRegions = new HashMap<String, TextureAtlas.AtlasRegion>();

    /**
     * This structure is used to store scaled pad values for NinePatches. After a NinePatch has
     * been scaled, we can't use the NinePatch.getPad*() methods because pads are stored as int, so
     * their value is 0.
     */
    public static class Pads {
        float left;
        float right;
        float top;
        float bottom;
    }

    Assets() {
        this.skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        this.atlas = new TextureAtlas(Gdx.files.internal("race.atlas"));
        this.wheel = findRegion("car/wheel");
        for (int idx = 1; idx <= 6; ++idx) {
            String name = String.format("car/car%d", idx);
            this.cars.add(findRegion(name));
        }
        this.explosion = new Animation(EXPLOSION_FRAME_DURATION, this.findRegions("explosion"));
        this.mine = new Animation(MINE_FRAME_DURATION, this.findRegions("mine"));
        this.mine.setPlayMode(Animation.PlayMode.LOOP);
        this.gift = findRegion("gift");

        // Fix white-pixel to avoid fading borders
        this.dot = findRegion("white-pixel");
        this.dot.setRegionX(this.dot.getRegionX() + 2);
        this.dot.setRegionY(this.dot.getRegionY() + 2);
        this.dot.setRegionWidth(this.dot.getRegionWidth() - 4);
        this.dot.setRegionHeight(this.dot.getRegionHeight() - 4);

    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        TextureAtlas.AtlasRegion region = mRegions.get(name);
        if (region != null) {
            return region;
        }
        region = this.atlas.findRegion(name);
        if (region == null) {
            throw new RuntimeException("Failed to load a texture region named '" + name + "'");
        }
        mRegions.put(name, region);
        return region;
    }

    public Array<TextureAtlas.AtlasRegion> findRegions(String name) {
        Array<TextureAtlas.AtlasRegion> lst = this.atlas.findRegions(name);
        if (lst.size == 0) {
            throw new RuntimeException("Failed to load an array of regions named '" + name + "'");
        }
        return lst;
    }

    public NinePatch createScaledPatch(String name) {
        return createScaledPatch(name, null);
    }

    public NinePatch createScaledPatch(String name, Pads pads) {
        NinePatch patch = atlas.createPatch(name);
        if (pads != null) {
            pads.left = patch.getPadLeft();
            pads.right = patch.getPadRight();
            pads.top = patch.getPadTop();
            pads.bottom = patch.getPadBottom();
        }
        patch.scale(Constants.UNIT_FOR_PIXEL, Constants.UNIT_FOR_PIXEL);
        if (pads != null) {
            pads.left *= Constants.UNIT_FOR_PIXEL;
            pads.right *= Constants.UNIT_FOR_PIXEL;
            pads.top *= Constants.UNIT_FOR_PIXEL;
            pads.bottom *= Constants.UNIT_FOR_PIXEL;
        }
        return  patch;
    }
}
