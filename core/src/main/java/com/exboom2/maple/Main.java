package com.exboom2.maple;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.exboom2.maple.screens.LoadingScreen;

public class Main extends Game {
    public AssetManager assets;
    public Preferences data;

    @Override
    public void create() {
        assets = new AssetManager();
        data = Gdx.app.getPreferences("com.exboom2.maple");

        setScreen(new LoadingScreen(this));
    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}