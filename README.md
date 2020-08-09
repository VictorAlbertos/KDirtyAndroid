# DirtyAndroid

A dirty approach for truly client Android applications using Kotlin. 

![DirtyAndroid](dirty.jpg)

It's time to admit an awful fact: **almost every Android application is a pure client application**. There is no such a thing as a *domain layer*. In the unlikely event of some *bussines logic* were required, it would be wiped out as a whole new system (there you go a new library!).

> The story of my life does not exist. Such a thing does not exist. There is no purpose. Neither a line or a path.  Vast passages suggest that someone was there. That’s not true, no one was there. 

*Marguerite Duras. The lover.*

That's a real Android application. *Vast passages* filled with nothing. The same no-brain with the same telling: 

*fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw*

Those actions refer only to two layers. Data and presentation. Where is the domain layer? How knows? Probably in the server.

The architecture which conducts this project has been designed with a clear purpose: keeping things as simple as possible wihout losing the right abstraction to provide just the precise amount of indirection to make a testeable and maintainable application. There is no ceremony, no *Uncle Bob*, no clean code; just Android itself, naked, truly exposed with its dirtiness. This architecture is just a window to this reality.

## What are you going to find in this repository:

* An uncertain amount of [bugs](httpss://en.wikipedia.org/wiki/Software_bug).
* [RxJava](https://github.com/ReactiveX/RxJava) as THE pipeline to *fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw, fetch, cache, draw*.
* [Dagger 2](https://github.com/google/dagger) to resolve dependencies for both data and presentation layer as well as those dependencies which depends on an specific Android build variant.
* [Retrofit](https://github.com/square/retrofit) for making HTTP calls.
* [ReactiveCache](https://github.com/VictorAlbertos/ReactiveCache) to caching the data retrieved from HTTP calls.
* A centralized pipeline for handling error using `Observable.Transformer` and `Presenter`.
* Repositories, presenters, wireframes and views. 
  * *Repositories* get/post data to/from cloud (Server) and local (ReactiveCache) datasources. 
  * *Presenters* use repositories to retrieve/send data and tell the views how they must behave. 
  * *Wireframes* hide the internal details of screen navigation and persist the data required to be passed in the process of navigation between screens. 
  * *Views* are the Fragments and Activities. They are as dummy as possible, and they just implement the contract with the Presenter. They don't even know that a presenter is hold as a reference. This is handled at a level base class. 
* [LifecycleObserver](https://developer.android.com/topic/libraries/architecture/lifecycle.html) to allow presenters to perform actions in response to a change in the lifecycle.
* A mechanism to restore screen's data bewteen config changes combining ReactiveCache, RxLifecycle, `Activity#onRetainCustomNonConfigurationInstance` and `Fragment#setRetainInstance(true)`.
* Plenty of unit testing using [Mockito](https://github.com/mockito/mockito).
* A little of UI testing using [Espresso](https://developer.android.com/training/testing/ui-testing/espresso-testing.html).
* Static code analysis such as Checkstyle, Lint, and PMD.
* A gradle task to clean the example and leave only the foundation structure.
* A disgusting UI.

## A dirty architecture
Why is it dirty? Because it's not clean.

There aren't `interface`s to comunicate between boundaries, no data models to limit their responsability to parse json documents, no use cases and so on. To sump up, there is no makeup. What there is instead is coupling, a healthy one to provide enought identity to the project to know that this is an Android project from top to bottom. The abstractions exist because of necesity, to provide a way to unit testing components.

Therefore, there only 2 layers: data and presentation. The domain layer was killed before it was born. And every one of them is divided into two packages, foundation and sections:

### Foundation package
Here is where all the *foundation classes* are placed. And by *foundation classes* I mean the classes which are unlikely to be modified due to app specs. They are the identity of the project, the code base, those which stands up as the backbone of your architecture; those which remind you that this is an Android application and nothing else. This package is the only one who survives the [freshStart gradle task](#freshStart), because only few things need to be tunned to start another Android application.

### Sections package
Here is where all the funcionality to resolve the application specs are placed. Here is where you are going to write the classes to provide the specific funcionality for your application (repositories, wireframes, presenters and views). This project contains a simple example using the Github API to fecth users, and it tries to be as ilustrative as possible to get you confortable with this architecture.

### Data layer
This module is called data because it just manages data. Its main purpose is retrieving data from specific repositories. These repositories are classes which manage data from/to server endpoints or from/to the local cache depending on the current call from the presentation layer. This layer is in charge too of updating the local cache when a new Fcm notifications is received.

### Presentation layer
This is where all the UI logic is placed. The presenter orchestrates the view (Fragment or Activitiy) telling what to do through the view interface, which acts as a bridge between them. This indirection layer is not just makeup, it's completely justified by the fact that only using an abstraction layer here we are going to be able to unit test the presenter without being coupled with the Android platform.

The presenter is the only reference that survives config changes. This is the place to store any kind of data which isn't stored in the cache system, but it still needs to survive to config changes. The presenter syncronizes the `Observable` subscriptions using RxLifeCycle.

## Testing
Both presentation and data layer have a suite of unit tests. For the data layer only JUnit and Mockito has been used to isolate dependencies. For the presentation layer -in addition to the previous one- Espresso has been used to provide a set of UI testing.

## Run quality tools.
To run with a single gradle task all the code quality tools, you need to execute the following command in the terminal:

```
 ./gradlew check
```

## <a name="freshStart"></a> FreshStart gradle task.
To get rid of the example and be able to start your own Android CLIENT application using this architecture, run the following command:

```
 ./gradlew freshStart
```

It will destroy all the classes/resources linked to the example and will leave the project in an ideal state to craft your own app.

## Author

**Víctor Albertos**

* <https://twitter.com/_victorAlbertos>
* <https://www.linkedin.com/in/victoralbertos>
* <https://github.com/VictorAlbertos>

## Another author's libraries using RxJava:
* [Mockery](https://github.com/VictorAlbertos/Mockery): Android and Java library for mocking and testing networking layers with built-in support for Retrofit.
* [ReactiveCache](https://github.com/VictorAlbertos/ReactiveCache): A reactive cache for Android and Java which honors the Observable chain.
* [RxActivityResult](https://github.com/VictorAlbertos/RxActivityResult): A reactive-tiny-badass-vindictive library to break with the OnActivityResult implementation as it breaks the observables chain.
* [RxFcm](https://github.com/VictorAlbertos/RxFcm): RxJava extension for Android Firebase Cloud Messaging (aka fcm).
* [RxSocialConnect](https://github.com/VictorAlbertos/RxSocialConnect-Android): OAuth RxJava extension for Android.
