package network.co.imge.stockhelper.base

import io.reactivex.disposables.CompositeDisposable

abstract class BaseModel: IModel {
    protected var disposables: CompositeDisposable? = CompositeDisposable()

    override fun detachView() {
        disposables?.clear()
        disposables?.dispose()
        disposables = null
    }
}