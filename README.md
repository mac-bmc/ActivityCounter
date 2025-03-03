# ActivityCounterApp

## Overview
ActivityCounterApp is an Android application designed to track user tap with a simple and interactive UI built using Jetpack Compose.

## Features
- Activity tracking with a foreground service
- Compose UI for smooth user experience
- BroadcastReceiver to handle specific actions
- ViewModel and LiveData for data management

## Installation
1. Clone the repository:
2. Open the project in Android Studio.
3. Build the project and run it on an Android device or emulator.

## Usage
- Click on start button to start tracking user activities and stop button to stop tracking.
- Moving the application to background while tracking generates a notification
- Tap the notification to interact with the counter.
- The activity status will be updated based on user interactions.


## Project Structure
- **domain**
    - `ActivityStatus`: Enum representing different activity statuses - Idle,Active
- **data**
    - `CounterRepository`: Handles data operations and business logic.
- **presentation**
    - `ActivityCounterScreen`: Compose UI screen.
    - `CounterActivity`: Manages UI.
    - `CounterViewModel`: Holds UI-related data.
- **service**
    - `TapCounterService`: Handles background tasks 
- **receiver**
    - `CounterReceiver`: Handles broadcast actions from foreground service

## Implementation
- Implemented the UI with jetpack compose
- Used livedata for state management
- Started a foreground service when the application moves to background for continuing user tracking and stopped the service when application becomes active again.
- Used shared preference to locally persist the state in case of an unexpected app restart.
- Used a broadcast receiver to listen to taps while the application is in background and updated the state.