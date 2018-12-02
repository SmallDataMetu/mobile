# Mobile

This repository contains an Android application, developed to simulate a vehicle's role in the real life scenario. This app lets you take photos, tag them for a journey and get passengers' emotions analyzed!

The project is developed at Mercedes - Benz Hack.Istanbul 2018 event!

![Hack Istanbul](hack-istanbul.png)

# What it does?

- The user decides when to start analyzing a journey and when to stop.
- Between the start & the stop, the user continously take photos of him/herself or a group.
- The app sends the photo to Microsoft's Azure Cognitive Services [Face API](https://azure.microsoft.com/en-us/services/cognitive-services/face/) and gets the results back.
- The app attaches vehicle, travel & driver information to emotion analysis, sends back to the API.
- The API processes each emotional record and produce meaningful results.

# How to use?

- Clone the repository
- Open the project at Android Studio
- **Update `AzureApi` and `AzureSubscriptionKey` credentials** with yours at `Rater/gradle.properties`
- Build the project
- Use it to analyze!

# Team Small Data

- Ramazan Zor
- Erkan Tomruk
- Oguzhan Unlu

# License

The MIT License
