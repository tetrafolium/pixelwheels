/*
 * Copyright 2019 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.agateau.ui.animscript;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;

public class RepeatInstruction implements Instruction {
    private final Array<Instruction> mInstructions;
    private final int mCount;

    RepeatInstruction(Array<Instruction> instructions, int count) {
        mInstructions = instructions;
        mCount = count;
    }

    @Override
    public Action run() {
        Action action;
        if (mInstructions.size > 1) {
            SequenceAction seq = Actions.sequence();
            for (Instruction instruction: mInstructions) {
                seq.addAction(instruction.run());
            }
            action = seq;
        } else {
            action = mInstructions.get(0).run();
        }
        return Actions.repeat(mCount == 0 ? RepeatAction.FOREVER : mCount, action);
    }
}
