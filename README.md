# YouTube Android Part 3

## Introduction

Welcome to our YouTube Part 3 repository! This section is designed for the Android YouTube app that we developed during the Advanced Programming course at Bar-Ilan University (BIU).

This repository contains the Android code, including both Part 1 and Part 3:
[Android YouTube Repo](https://github.com/theComputerGu/AndroidYouTube/tree/main_part2)

You can find the Jira repository here:
[Jira Repository](https://marksheinberg01.atlassian.net/jira/software/projects/SCRUM/boards/1/backlog?epics=visible&issueParent=10000%2C10001)

## How to Run Part 3

1. First, you need to add the server from Part 2.
2. To run this, navigate to the "Server" repository:
   [Server Repo](https://github.com/TomU2611/Server.git)
   [GitHub Repo](https://github.com/TomU2611/YouTube-Web.git)

3. Navigate to the "Server" folder, create a file named `.env.local` under the `config` folder, and fill it with the following:

```sh
CONNECTION_STRING="mongodb://localhost:27017"
PORT=12345
```

4. Then run the following commands:

```sh
npm install
npm start
```


5. Open MongoDB Compass and connect to mongodb://localhost:27017.

6. Under the "test" database, import the JSON files from the dataForServer folder. Import users.json to the users collection and videos.json to the videos collection.

7. Download the zip file of the repo project mainpart2, extract the files, and open the project in Android Studio.

## Important Note
In the strings.xml file in the project, there is the HTTP link to the server. The URL 10.0.2.2 represents your computer's IP if you are running the server on the same computer. If you are not running the server on the same computer, please change the URL to the IP address of the computer that you are using for the server.

After that, you can easily run the app. The emulator will open the project and bring you to the HomePage (main activity).

## User Interface
At the bottom of the HomePage, you can see the toolbar with four buttons:

```sh
Home button (returns to the HomePage)
Dark Mode button
Add Video button
Log In button
```

## Notes
If you are signed in and want to sign out, press the picture/sign-in button again, and the user profile will open, allowing you to sign out and change the username.
When the watch page is open, if you want to make a comment, press the comment button. The video list will disappear, allowing you to leave a comment and see all the comments.
To see all the videos that the user posted, press the photo on the watch page.
Some actions take time, so be patient! For example, signing up or adding a video can take a few seconds (up to 10 seconds).

## Written by
```sh
Tom Uklein (206594053)
Itay Zahor (208127480)
Mark Sheinberg (324078708)
```
