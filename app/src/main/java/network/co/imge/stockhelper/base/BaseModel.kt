package network.co.imge.stockhelper.base

import io.reactivex.disposables.CompositeDisposable

abstract class BaseModel: IModel {
    override fun detachView(){}
}