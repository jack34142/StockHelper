package network.co.imge.stockhelper.base

interface IPresenter<V>{
    fun attachView(mvpView: V)
    fun detachView()
}