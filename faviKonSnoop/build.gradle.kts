/*
 * Geekttrss is a RSS feed reader application on the Android Platform.
 *
 * Copyright (C) 2017-2019 by Frederic-Charles Barthelery.
 *
 * This file is part of Geekttrss.
 *
 * Geekttrss is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Geekttrss is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Geekttrss.  If not, see <http://www.gnu.org/licenses/>.
 */
plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(enforcedPlatform(kotlin("bom")))

    api("com.squareup.okhttp3:okhttp:4.1.0")
    api("com.squareup.okio:okio:2.4.1")
    implementation("javax.inject:javax.inject:1")
    implementation("org.jsoup:jsoup:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")

    testImplementation("com.google.truth:truth:1.0")
    testImplementation(kotlin("test-junit"))
    testImplementation("io.mockk:mockk:1.9")
}
