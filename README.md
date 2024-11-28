# JetpackComposeOtpViewer

JetpackComposeOtpViewer is a modern, flexible, and customizable OTP (One-Time Password) input UI component built with Jetpack Compose for Android. This library simplifies the process of building secure and user-friendly OTP input screens, providing features like auto-focus, automatic digit transition, and customizable styles—all while following best practices for Android UI development.

![Platform](https://img.shields.io/badge/Platform-Android-lightseagreen) &nbsp;
![API Level](https://img.shields.io/badge/API-21%2B-steelblue) &nbsp;
![Language](https://img.shields.io/badge/Language-Kotlin-orange)

## Screenshots

<img src="https://github.com/Aryan2k/PokeDiary/blob/main/PokeDiarySplash.png" height = "720" width = "320"/>        <img src="https://github.com/Aryan2k/PokeDiary/blob/main/PokeDiaryLoader.png" height = "720" width = "320"/>  
<img src="https://github.com/Aryan2k/PokeDiary/blob/main/PokeDiaryList.png" height = "720" width = "320"/>        <img src="https://github.com/Aryan2k/PokeDiary/blob/main/PokeDiaryDetails.png" height = "720" width = "320"/>

## Key Features

- Jetpack Compose-based: Built using Jetpack Compose, leveraging the latest Android UI toolkit for a declarative UI design.
- Auto-Complete & Auto-Focus: Automatically moves focus to the next input field as the user types, with optional auto-submit when the OTP is complete.
- Clipboard OTP Pasting: Users can easily paste OTP codes directly from the clipboard, enhancing the user experience for those who receive OTPs via SMS or email.
- Customizable: Easily customize the appearance (font size, color, shape) and behavior of the OTP input fields to match your app’s design.
- Smooth User Experience: Provides a smooth, intuitive experience for entering OTPs.
- Validation Ready: Built with validation in mind, allowing you to integrate this component seamlessly into your OTP authentication flow.

## Libraries used

- Jetpack Compose:
  - A modern toolkit for building native Android UIs with a declarative approach.
  - Simplifies and accelerates UI development.
- Kotlin Standard Library
  - Essential Kotlin features and functions, including coroutines, collection operations, and more.
- Material Components for Jetpack Compose
  - Material design components and theming for Jetpack Compose, ensuring a consistent UI design across Android apps.
  
## Installation

Step 1. Add the JitPack repository to your build file
- Add it in your root build.gradle at the end of repositories:

  ```
  dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

```
dependencies {
	        implementation 'com.github.Aryan2k:JetpackComposeOtpViewer:1.0.6'
	}
```

**Make sure to check the latest version on the releases page.**

## Usage

- Here’s a simple example of how to use the OtpViewer component in your Jetpack Compose layout:
  
```
import com.aryanm468.composeotpviewer

@Composable
fun OtpInputScreen() {

    var enteredOtp by remember { mutableStateOf("") }

    OtpInputField(
        modifier = Modifier.padding(32.dp),
        enteredOtp = enteredOtp,
        otpLength = 6,  // Customize the OTP length 
        onOtpChanged = {
            enteredOtp = it
        }
    )

}

