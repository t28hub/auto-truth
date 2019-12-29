/*
 * Copyright 2019 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.auto.truth.test;

import io.t28.auto.truth.AutoSubject;

@AutoSubject
public class PrimitiveValueObject {
    private final boolean booleanValue;
    private final byte byteValue;
    private final short shortValue;
    private final int intValue;
    private final long longValue;
    private final char charValue;
    private final float floatValue;
    private final double dooubleValue;

    public PrimitiveValueObject(boolean booleanValue, byte byteValue, short shortValue, int intValue, long longValue, char charValue, float floatValue, double dooubleValue) {
        this.booleanValue = booleanValue;
        this.byteValue = byteValue;
        this.shortValue = shortValue;
        this.intValue = intValue;
        this.longValue = longValue;
        this.charValue = charValue;
        this.floatValue = floatValue;
        this.dooubleValue = dooubleValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public double getDooubleValue() {
        return dooubleValue;
    }
}
