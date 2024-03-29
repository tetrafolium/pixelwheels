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
package com.agateau.pixelwheels.debug;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;

/**
 * An helper class to register global debug shape drawers
 */
public class DebugShapeMap {
    public interface Shape {
        void draw(ShapeRenderer renderer);
    }
    private static final HashMap<Object, Shape> sMap = new HashMap<>();

    public static HashMap<Object, Shape> getMap() {
        return sMap;
    }

    public static void put(Object key, Shape shape) {
        sMap.put(key, shape);
    }

    public static void remove(Object key) {
        sMap.remove(key);
    }
}
