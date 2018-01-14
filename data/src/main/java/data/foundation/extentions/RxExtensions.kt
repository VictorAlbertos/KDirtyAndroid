package data.foundation.extentions

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

fun Disposable.addTo(composite: CompositeDisposable) = composite.add(this)

fun <T, R> Observable<T>.withLatestFromAsPair(others: ObservableSource<out R>): Observable<Pair<T, R>>
        = withLatestFrom(others, BiFunction<T, R, Pair<T, R>> { one, other -> one to other })

fun <T> Single<T>.safeFilter(predicate: (T) -> Boolean): Single<T> =
        toObservable().filter(predicate)
                .lastOrError()
                .onErrorResumeNext { Single.never() }
