# TrendyArt 

TrendyArt is an android application that allows the user to view beautiful art pieces.  It was built for my final Capstone Stage 2 project, which is part of the Udacity Android Nanodegree

## Screenshots

<img src="Screenshot_1542415727.png" width=300 height=500>     <img src="Screenshot_1542415742.png" width=300 height=500>
<img src="Screenshot_1542415791.png" width=300 height=500>

## Set-Up
1. In order to run this app you will need to create an account at the Artsy API website: https://developers.artsy.net/.

2. Once you have created an account you will get a Client ID & Client Secret ID, which you need to get a token.

3. To obtain a Token, type the following in a terminal window: <br>
curl -v -X POST "https://api.artsy.net/api/tokens/xapp_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET"

(NOTE:You will receive a token that will expire in 6 days.)
 
4. Place your TOKEN inside `build.gradle` 
```gradle
def apiToken = project.hasProperty('token') ? apiToken : (System.getenv('TOKEN') ?: "\"YOUR_TOKEN\"")
```
5. Place your Client ID and Client Secret inside file Utils.java

6. Place your Google Analytics Tracking ID inside file global_tracker.xml;  To obtain a tracking ID follow these instructions: https://developers.google.com/analytics/devguides/collection/android/v4/

## Useful Resources
- https://developer.android.com/topic/libraries/support-library/features#v7-palette
- https://github.com/square/retrofit
- https://github.com/square/picasso
- https://developer.android.com/topic/libraries/architecture/
- https://github.com/JakeWharton/butterknife
- https://github.com/square/okhttp
- https://developer.android.com/topic/libraries/architecture/room
- https://developer.android.com/topic/libraries/architecture/paging/
- https://developers.google.com/android/reference/com/google/android/gms/ads/package-summary
- https://developer.android.com/training/material/palette-colors
- https://github.com/facebook/fresco
- https://github.com/tiagohm/MarkdownView

- Analytics: 
- https://developers.google.com/android/reference/com/google/android/gms/analytics/package-summary
- https://developers.google.com/analytics/devguides/collection/android/v3/
- https://developers.google.com/analytics/devguides/collection/android/v4/
- https://support.google.com/analytics/answer/1008080?hl=en
- https://support.google.com/analytics/answer/1042508

- Google Play Services:
- https://developers.google.com/android/guides/setup

- Transitions:
- https://mikescamell.com/shared-element-transitions-part-1/
- http://yasirameen.com/2016/05/android-activity-transition/
- https://guides.codepath.com/android/shared-element-activity-transition
- https://factory.hr/blog/activity-animations-shared-element-transitions-demistified
- https://developer.android.com/training/transitions/start-activity#java
- https://willowtreeapps.com/ideas/material-world-animating-l-shared-view-activity-animations/
- https://www.androiddesignpatterns.com/2015/01/activity-fragment-shared-element-transitions-in-depth-part3a.html
