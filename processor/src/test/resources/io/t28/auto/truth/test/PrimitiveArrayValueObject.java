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

import java.util.Arrays;

@AutoSubject
public class PrimitiveArrayValueObject {
    private final boolean[] booleans;
    private final byte[] bytes;
    private final short[] shorts;
    private final int[] ints;
    private final long[] longs;
    private final char[] chars;
    private final float[] floats;
    private final double[] doubles;

    public PrimitiveArrayValueObject(boolean[] booleans, byte[] bytes, short[] shorts, int[] ints, long[] longs, char[] chars, float[] floats, double[] doubles) {
        this.booleans = Arrays.copyOf(booleans, booleans.length);
        this.bytes = Arrays.copyOf(bytes, bytes.length);
        this.shorts = Arrays.copyOf(shorts, shorts.length);
        this.ints = Arrays.copyOf(ints, ints.length);
        this.longs = Arrays.copyOf(longs, longs.length);
        this.chars = Arrays.copyOf(chars, chars.length);
        this.floats = Arrays.copyOf(floats, floats.length);
        this.doubles = Arrays.copyOf(doubles, doubles.length);
    }

    public boolean[] getBooleans() {
        return Arrays.copyOf(booleans, booleans.length);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public short[] getShorts() {
        return Arrays.copyOf(shorts, shorts.length);
    }

    public int[] getInts() {
        return Arrays.copyOf(ints, ints.length);
    }

    public long[] getLongs() {
        return Arrays.copyOf(longs, longs.length);
    }

    public char[] getChars() {
        return Arrays.copyOf(chars, chars.length);
    }

    public float[] getFloats() {
        return Arrays.copyOf(floats, floats.length);
    }

    public double[] getDoubles() {
        return Arrays.copyOf(doubles, doubles.length);
    }
}
