package com.exboom2.maple.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.ScreenUtils;
import com.exboom2.maple.Main;

public class LoadingScreen extends ScreenAdapter {
    private final Main game;
    private final AssetManager manager;
    private SpriteBatch batch;
    private BitmapFont font;
    private FileHandleResolver resolver;
    private Boolean isLoaded = false;
    private final boolean shouldClearData = true;

    public LoadingScreen(Main game) {
        // Setgame and manager to the class
        this.game = game;
        this.manager = game.assets;

        // create a SpriteBatch
        this.batch = new SpriteBatch();

        this.resolver = new InternalFileHandleResolver();
        manager.setLoader(TiledMap.class, new TmxMapLoader(resolver));

        // LOADING SCREEN
        manager.finishLoading();

        font = new BitmapFont();

        String[] lines = Gdx.files.internal("assets.txt").readString("UTF-8").split("\\r?\\n");

        for (String path : lines) {
            path = path.trim();
            if (path.isEmpty() || path.startsWith("#"))
                continue;

            int lastDot = path.lastIndexOf(".");
            if (lastDot == -1)
                continue;
            String extension = path.substring(lastDot + 1).toLowerCase();

            switch (extension) {
                case "png":
                    load(path, Texture.class);
                    break;
                case "atlas":
                    load(path, TextureAtlas.class);
                    break;
                case "tmx":
                    load(path, TiledMap.class);
                    break;
                case "fnt":
                    load(path, BitmapFont.class);
                    break;
                case "p":
                    load(path, ParticleEffect.class);
                    break;
                case "wav":
                    load(path, Sound.class);
                    break;
                case "mp3":
                    load(path, Music.class);
                    break;
            }
        }
    }

    private <T> void load(String path, Class<T> type) {
        if (!manager.isLoaded(path)) {
            manager.load(path, type);
            Gdx.app.log("LoadingScreen", "Loaded file " + path + " to " + type.getSimpleName());
        } else {
            Gdx.app.log("LoadingScreen", "Couldn't load " + path + ", already loaded");
        }
    }

    @Override
    public void render(float delta) {
        if (manager.update() && isLoaded == false) {
            isLoaded = true;

            Gdx.graphics.setResizable(true);

            // Delete save data
            if (shouldClearData) {
                game.data.clear();
                game.data.flush();
            }

            game.setScreen(new MenuScreen(game));
        }

        ScreenUtils.clear(0, 0, 0, 1);

        float progress = manager.getProgress();

        batch.begin();

        font.draw(batch, "Loading: " + (int) (progress * 100) + "%", 20, 30);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}