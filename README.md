# TODO App with Authentication and Local Storage

- This Android application provides a user-friendly platform for managing tasks while incorporating authentication and local storage mechanisms for enhanced security and data persistence.
- The app have a `French` and an `English` version.

## Features

* **User Authentication:**
  * Users can register new accounts and securely log in to access their personalized task lists.

* **Task Management:**

  * Users can create, edit, and delete tasks effectively.
  * Tasks are stored locally using the device's storage, ensuring data availability even without an internet connection.

* **Task Notifications:**

  * Users receive email notifications upon task updates (addition, deletion, or modification).

* **Session Management:**

  * Login sessions are saved for future sessions, eliminating the need for repeated logins.

## Technologies Used

* Java
* Android Studio

## Installation

1. Clone the project repository using Git.
2. Open the project in Android Studio.
3. Connect your Android device or emulator.
4. Run the app in Android Studio.

## Usage

Launch the app.

## User Authentication

1. If you have an existing account, enter your credentials and click "Login".
2. If you don't have an account, click "Sign Up" to create a new one.

## Task Management

1. Once logged in, you can view your current task list.
2. To add a new task, click the "+" button and enter the task details.
3. To edit a task, Swipe the task to the right and make the necessary changes.
4. To delete a task, swipe left on the task and click "Yes".

## Task Notifications

* Upon adding, deleting, or modifying a task, you will receive an email notification.

## Setup Email Notifications

To enable email notifications for task-related actions, you'll need to set up the `EmailCredentials` class. Follow these steps:

1. Create a new Java class named `EmailCredentials` in the following directory: `app/src/main/java/com/example/todoapp/Utils`.

2. Inside the `EmailCredentials` class, define two final strings, `EMAIL` and `PASSWORD`, with the email address and the app password of the sender account.

   ```java
   public class EmailCredentials {
       public static final String EMAIL = "your_email@gmail.com";
       public static final String PASSWORD = "your_app_password";
   }
   ```

   * Replace "**<your_email@gmail.com>**" with the email address you want to use for sending notifications and "**your_app_password**</ins>" with the app-specific password generated for your email account.

## Session Management

1. Once logged in, you will remain logged in until you explicitly log out.
2. To log out, click the menu icon and select "Log Out".

## Purpose of the Project

* This project was developed as part of a university assignment to demonstrate proficiency in Android app development. It serves as a practical implementation of concepts learned during the course.
