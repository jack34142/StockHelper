package network.co.imge.stockhelper.base

import io.reactivex.disposables.CompositeDisposable
import network.co.imge.stockhelper.http.HttpClient

abstract class BasePresenter<V: IView>: IPresenter<V>{

    protected var mvpView: V? = null
    protected var disposables: CompositeDisposable? = CompositeDisposable()

    override fun attachView(mvpView: V){
        this.mvpView = mvpView
    }

    override fun detachView(){
        disposables?.clear()
        disposables?.dispose()
        disposables = null
        mvpView = null
    }
}