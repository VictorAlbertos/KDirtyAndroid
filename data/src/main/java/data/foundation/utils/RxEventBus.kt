package data.foundation.utils

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class RxEventBus<T : Any>(maxKeys: Int = 100) {
    private val buffer = RxEventBusBuffer<T>(maxKeys)
    private val subject = BehaviorSubject.createDefault<RxEventBusMessage<T>>(RxEventBusMessage("", null))

    fun listen(getPrevious: Boolean = false): Observable<T> = subject.hide()
            .skip(if (getPrevious) 0 else 1)
            .filter { it.value != null }
            .map { it.value }

    fun getAll(): List<T> = buffer.all().mapNotNull { it.value }

    fun emit(value: T) {
        val message = RxEventBusMessage("", value)
        buffer.add(message)
        subject.onNext(message)
    }

    fun filter(key: String = "") = object : RxEventBusFiltered<T> {
        override fun emit(value: T) {
            val message = RxEventBusMessage(key, value)
            buffer.add(message)
            subject.onNext(message)
        }

        override fun listen(getPrevious: Boolean): Observable<T> = subject.hide()
                .skip(if (getPrevious) 0 else 1)
                .filter { buffer.get(key) != null }
                .map { buffer.get(key) }
                .filter { it.key == key && it.value != null }
                .map { it.value }
    }

    interface RxEventBusFiltered<T> {
        fun emit(value: T)
        fun listen(getPrevious: Boolean = false): Observable<T>
    }

    private data class RxEventBusMessage<out T>(val key: String, val value: T?)

    private class RxEventBusBuffer<T>(private val bufferSize: Int) {
        private var buffer = mutableListOf<RxEventBusMessage<T>>()

        fun add(message: RxEventBusMessage<T>) {
            buffer.removeAll { it.key == message.key }
            buffer.add(0, message)
            if (buffer.size > bufferSize) {
                buffer = buffer.subList(0, bufferSize)
            }
        }

        fun get(id: String): RxEventBusMessage<T>? = buffer.find { it.key == id }

        fun all() = buffer
    }
}


