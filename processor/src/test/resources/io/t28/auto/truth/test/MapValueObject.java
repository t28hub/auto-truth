/*
 * Copyright 2020 Tatsuya Maki
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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@AutoSubject
public class MapValueObject {
    private final Map<String, Integer> map;
    private final HashMap<Integer, String> hashMap;
    private final EnumMap<Type, Object> enumMap;
    private final CustomMap customMap;

    public MapValueObject(Map<String, Integer> map, HashMap<Integer, String> hashMap, EnumMap<Type, Object> enumMap, CustomMap customMap) {
        this.map = map;
        this.hashMap = hashMap;
        this.enumMap = enumMap;
        this.customMap = customMap;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public HashMap<Integer, String> getHashMap() {
        return hashMap;
    }

    public EnumMap<Type, Object> getEnumMap() {
        return enumMap;
    }

    public CustomMap getCustomMap() {
        return customMap;
    }

    public static enum Type {
        ADMIN,
        OWNER,
        NORMAL,
        GUEST
    }

    public static class CustomMap extends HashMap<String, String> {
    }
}
