package network.co.imge.stockhelper.base

interface IView{
    fun showLoading()
    fun hideLoading()
    fun showMsg(msg: String, code: Int = 1)
}