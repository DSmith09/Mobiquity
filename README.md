Mobiquity
=========

Code Challenge

Read Me File:

 David Smith
 Mobiquity Code Challenge 4/11/14

 Code Challenge:

 	Synopsis:
 	For the code challenge I was given, I decided to use Android Studio in order to build and test my application. The task required me to utilize Dropbox in order to accomplish some of the tasks, which most were possible using Dropbox's Sync API for Android Studio. Dropbox's Core API and Chooser SDK are not readily available on Android Studio but they are available on Eclipse. I was able to accomplish a majority of the tasks assigned using the Sync API which I have detailed below.


 	Tasks Accomplished:
 		+ Dropbox Authentication for users
 		+ Ability to take photos using device camera application
 		+ Access to upload file to Dropbox directory
 		+ Information of file uploaded presented to user


 	Tasks Not Completed:
 		- Ability to view photos on Dropbox server

 	Additional Notes:
 		1. Files that are to be stored on Dropbox must come from device's sd card, the file cannot be read at the moment from internal phone storage and will show up on Dropbox as being 0 bytes in size.

 		2. I did not add a logout feature because the Sync API does not supply one. The option to unlink a user from the application removes the files associated with the application thus rendering the utility of the feature undesirable.

 		3. I attempted to add the Geotracking feature to the image for extra credit but since I could not access the photo using the Dropbox API, it would not apply.
