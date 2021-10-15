# DirtyAndroid

A dirty approach for Android apps using Kotlin and Jetpack components.

![DirtyAndroid](dirty.jpg)

It's time to admit an awful fact: **almost every Android application is a pure client application**. There is no such a thing as a *domain layer*. In the unlikely event of some *bussines logic* is required, it is wiped out as a whole new system (there you go a new library!).

The current architecture has a clear purpose: keeping things as simple as possible without losing the right abstraction to provide just the  amount of indirection to keep the app testeable. There is no ceremony, no *Uncle Bob*, no clean code; just Android itself, naked, truly exposed with its dirtiness.


## What's in this repository

* An uncertain amount of [bugs](httpss://en.wikipedia.org/wiki/Software_bug).
* [Retrofit](https://github.com/square/retrofit) for making HTTP calls.
* [Coroutines](https://github.com/ReactiveX/RxJava) for asynchronous operations.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for resolving dependencies.
* The [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started) for navigating between screens and passing data safely with [SafeArgs](https://developer.android.com/guide/navigation/navigation-pass-data).
* [Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for loading and displaying pages of data from the Github Api.
* Repositories, ViewModel(s) and Fragments.
* A Compose disgusting UI.
