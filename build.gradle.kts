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
import com.android.build.gradle.BaseExtension
import com.geekorum.build.SourceLicenseCheckerPlugin
import com.geekorum.build.configureJavaVersion
import com.geekorum.build.createComponentsPlatforms
import com.geekorum.build.setupGoogleContent
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("com.google.android.gms.oss-licenses-plugin") version "0.9.5.1" apply false
    id("com.google.gms.google-services") version "3.2.0" apply false
    id("io.fabric") version "1.29.0" apply false
    kotlin("android") version "1.3.50" apply false
    kotlin("kapt") version "1.3.50" apply false
    id("kotlinx-serialization") version "1.3.50" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.1.0-alpha06" apply false
}


// some extra properties
extra["compileSdkVersion"] = "android-29"

allprojects {
    repositories {
        google().setupGoogleContent()
        jcenter()
        // for kotlinx
        maven { url = URI("https://kotlin.bintray.com/kotlinx") }
        // for geekdroid
        flatDir {
            dirs(rootProject.files("libs"))
        }
    }
    dependencies {
        createComponentsPlatforms()
    }

    apply<SourceLicenseCheckerPlugin>()

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xuse-experimental=kotlin.Experimental", "-XXLanguage:+InlineClasses")
        }
    }

    afterEvaluate {
        extensions.findByType<KaptExtension>()?.arguments {
            arg("dagger.formatGeneratedSource", "enabled")
        }
        extensions.findByType<BaseExtension>()?.apply {
            configureJavaVersion()
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(buildDir)
}
