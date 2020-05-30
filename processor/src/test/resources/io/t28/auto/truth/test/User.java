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

public class User {
    private final long id;
    private final String name;
    private final int age;
    private final boolean isAdmin;
    private final Type type;

    public User(long id, String name, int age, boolean isAdmin, Type type) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isAdmin = isAdmin;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public User.Type getType() {
        return type;
    }

    public static enum Type {
        GUEST, OWNER
    }
}
