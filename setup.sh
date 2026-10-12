#!/bin/bash
set -e

echo "🔧 Starting Khelo BD FF setup..."
cd ~/khelo-bd-ff

# ফোল্ডার
mkdir -p .github/workflows
mkdir -p app/src/main/java/com/mcash/wala
mkdir -p app/src/main/res/layout
mkdir -p app/src/main/res/values
mkdir -p app/src/main/res/drawable
mkdir -p app/src/main/res/mipmap-hdpi
mkdir -p app/src/main/res/mipmap-mdpi
mkdir -p app/src/main/res/mipmap-xhdpi
mkdir -p app/src/main/res/mipmap-xxhdpi
mkdir -p app/src/main/res/mipmap-xxxhdpi

echo "📁 Folders created"

# .github/workflows/build.yml
cat > .github/workflows/build.yml << 'ENDOFFILE'
name: Build APK

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3

      - name: Build Debug APK
        run: gradle assembleDebug --stacktrace --no-daemon

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: KheloBDFF-apk
          path: app/build/outputs/apk/debug/*.apk
ENDOFFILE

# settings.gradle
cat > settings.gradle << 'ENDOFFILE'
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Khelo BD FF"
include ':app'
ENDOFFILE

# build.gradle root
cat > build.gradle << 'ENDOFFILE'
plugins {
    id 'com.android.application' version '8.5.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.24' apply false
    id 'com.google.gms.google-services' version '4.4.2' apply false
}
ENDOFFILE

# gradle.properties
cat > gradle.properties << 'ENDOFFILE'
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
android.nonTransitiveRClass=true
kotlin.code.style=official
ENDOFFILE

# app/build.gradle
cat > app/build.gradle << 'ENDOFFILE'
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
}

android {
    namespace 'com.mcash.wala'
    compileSdk 34

    defaultConfig {
        applicationId "com.mcash.wala"
        minSdk 21
        targetSdk 34
        versionCode 2
        versionName "2.0"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = '17' }

    buildFeatures { viewBinding true }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'com.google.android.material:material:1.12.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    implementation platform('com.google.firebase:firebase-bom:33.1.0')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-database-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
}
ENDOFFILE

# proguard
cat > app/proguard-rules.pro << 'ENDOFFILE'
ENDOFFILE

# google-services.json
cat > app/google-services.json << 'ENDOFFILE'
{
  "project_info": {
    "project_number": "503510878884",
    "firebase_url": "https://mcash-7b610-default-rtdb.firebaseio.com",
    "project_id": "mcash-7b610",
    "storage_bucket": "mcash-7b610.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:503510878884:android:f664063c5c481a9d9e3431",
        "android_client_info": {
          "package_name": "admin.mcash.wala"
        }
      },
      "oauth_client": [],
      "api_key": [
        { "current_key": "AIzaSyATqcfsm7ELWhdZxdrhyvoIu6SJ9zRo3ys" }
      ],
      "services": {
        "appinvite_service": { "other_platform_oauth_client": [] }
      }
    },
    {
      "client_info": {
        "mobilesdk_app_id": "1:503510878884:android:f2face674d65b3929e3431",
        "android_client_info": {
          "package_name": "com.mcash.wala"
        }
      },
      "oauth_client": [],
      "api_key": [
        { "current_key": "AIzaSyATqcfsm7ELWhdZxdrhyvoIu6SJ9zRo3ys" }
      ],
      "services": {
        "appinvite_service": { "other_platform_oauth_client": [] }
      }
    }
  ],
  "configuration_version": "1"
}
ENDOFFILE

echo "✅ Base config files created"
echo "➡️  এখন Kotlin ও Layout ফাইলগুলো বানানো হবে..."
