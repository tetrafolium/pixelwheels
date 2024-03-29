/*
 * Copyright 2017 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Pixel Wheels is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.agateau.pixelwheels.racescreen;

import com.agateau.pixelwheels.PwGame;
import com.agateau.pixelwheels.PwRefreshHelper;
import com.agateau.ui.UiBuilder;
import com.agateau.ui.menu.Menu;
import com.agateau.utils.FileUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Appears on top of RaceScreen when paused
 */
public class PauseOverlay extends Overlay {
    private final PwGame mGame;
    private final RaceScreen mRaceScreen;

    public PauseOverlay(PwGame game, RaceScreen raceScreen) {
        super(game.getAssets().dot);
        mGame = game;
        mRaceScreen = raceScreen;
        new PwRefreshHelper(mGame, this) {
            @Override
            protected void refresh() {
                setContent(createContent());
            }
        };
        setContent(createContent());
    }

    private Actor createContent() {
        UiBuilder builder = new UiBuilder(mGame.getAssets().atlas, mGame.getAssets().ui.skin);
        Actor content = builder.build(FileUtils.assets("screens/pauseoverlay.gdxui"));

        Menu menu = builder.getActor("menu");

        menu.addButton("Resume").addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mRaceScreen.resumeRace();
            }
        });
        if (mRaceScreen.getPauseButtons() == RaceScreen.PauseButtons.ALL) {
            menu.addButton("Restart").addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mRaceScreen.onRestartPressed();
                }
            });
        }
        menu.addButton("Quit to Menu").addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mRaceScreen.onQuitPressed();
            }
        });
        menu.addButton("Settings").addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mRaceScreen.onSettingsPressed();
            }
        });
        return content;
    }
}
