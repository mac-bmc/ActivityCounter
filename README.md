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
    git clone [https://github.com/your-repo/ActivityCounterApp.git](https://github.com/mac-bmc/ActivityCounter)
2. Open the project in Android Studio.
3. Build the project and run it on an Android device or emulator.

## Usage
- Launch the app to start tracking activities.
- Tap the notification to interact with the counter.
- The activity status will be updated based on user interactions.

## Project Structure
- **domain**
    - `ActivityStatus`: Enum representing different activity statuses.
- **data**
    - `CounterRepository`: Handles data operations and business logic.
- **presentation**
    - `ActivityCounterScreen`: Compose UI screen.
    - `CounterActivity`: Manages UI.
    - `CounterViewModel`: Holds UI-related data.
- **service**
    - `TapCounterService`: Handles background tasks.
- **receiver**
    - `CounterReceiver`: Handles broadcasted actions.

## Contributing
Contributions are welcome! Please fork the repository and submit pull requests.

