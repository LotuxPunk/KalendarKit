![Maven Central Version](https://img.shields.io/maven-central/v/be.vandeas/kalendar-kit)
[![Publish](https://github.com/LotuxPunk/KalendarKit/actions/workflows/publish.yml/badge.svg)](https://github.com/LotuxPunk/KalendarKit/actions/workflows/publish.yml)

# KalendarKit

## What is it?

KalendarKit is a Compose Multiplatform library designed to simplify the presentation of events for users to add. Using native APIs, KalendarKit allows developers to present a modal EventKit interface on iOS and open 
the default calendar application on Android, making it easier to allow user to add events.

## How to use it?

### Add KalendarKit to your project

To use KalendarKit in your project, you need to add the following dependencies to your `build.gradle.kts` file:

```kotlin
commonMain.dependencies {
    implementation("be.vandeas:kalendar-kit:1.0.0")
}
```

or using version catalog:

```toml
[versions]
kalendar-kit = "1.0.0"

[libraries]
kalendar-kit = { module = "be.vandeas:kalendar-kit", version.ref = "kalendar-kit" }
```

### iOS

#### Permission

To use KalendarKit in your iOS application, you need to ask permission to access the calendar. You can do this by adding the following `NSCalendarsUsageDescription` entry to your `Info.plist` file.

#### Setup

To use the `CalendarEventManager` class, you need to setup the `UIViewController` that will be used to present the modal interface. You can do this by calling the `setup` method on the `CalendarEventManager` class.

You can see an example in `composeApp` sample of [this repository](./composeApp).

### Android

#### Permission

To use KalendarKit in your Android 11+ application, you need to declare the queries for the calendar provider in your `AndroidManifest.xml` file. This is required to allow your app to query the calendar app.

```xml
    <queries>
        <intent>
            <action android:name="android.intent.action.INSERT" />
            <data android:mimeType="vnd.android.cursor.dir/event" />
        </intent>
    </queries>
```

#### Setup

To use the `CalendarEventManager` class, you need to setup the `Context` that will be used to present the modal interface. You can do this by calling the `setup` method on the `CalendarEventManager` class.

You can see an example in `composeApp` sample of [this repository](./composeApp).
