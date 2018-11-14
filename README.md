# TrendyArt Android Application

## Capstone Stage 2, Part of the Udacity Android Nanodegree

## Configurations
In order to run this app you will need to create an account at the Artsy API website: https://developers.artsy.net/.

Once you have created an account you will get a Client ID & Client Secret ID, which you need to get a token.

To obtain a Token, type the following in a terminal window:
curl -v -X POST "https://api.artsy.net/api/tokens/xapp_token?client_id=<yourclientid>&client_secret=<yourclientsecret>"

You will receive a token that will expire in 6 days.
 
Place your TOKEN inside `build.gradle` 
```gradle
def apiToken = project.hasProperty('token') ? apiToken : (System.getenv('TOKEN') ?: "\"YOUR_TOKEN\"")
```

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

-Google Play Services:
-https://developers.google.com/android/guides/setup