package diiin.ui

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBus {

    private val bus : PublishSubject<Any> = PublishSubject.create()

    fun send(any : Any) = bus.onNext(any)

    fun toObservable() : Observable<Any> = bus

}