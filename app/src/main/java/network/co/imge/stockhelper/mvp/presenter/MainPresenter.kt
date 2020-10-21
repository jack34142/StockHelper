package network.co.imge.stockhelper.mvp.presenter

import android.content.Context
import android.widget.Toast
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BasePresenter
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.model.MainModel
import network.co.imge.stockhelper.pojo.Aim
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TwseResponse

class MainPresenter: BasePresenter<MainContract.IMainView>(), MainContract.IMainPresenter {
    private val TAG: String = "MainPresenter"

    private var mvpModel: MainModel? = null
    val latestPrice: MutableMap<String, Double> = mutableMapOf()
    val goalMsg: MutableMap<String, String> = mutableMapOf()
    var typeMap: Map<String, String>? = null

    override fun attachView(mvpView: MainContract.IMainView) {
        super.attachView(mvpView)
        mvpModel = MainModel(mvpView as Context)
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }

    override fun addNoticeStock(noticeStock: NoticeStock) {
        val context = mvpView as Context

        val noticeStockMap = mvpView!!.getNoticeStock()
        val stockId = noticeStock.stockId!!
        if (typeMap!!.containsKey(stockId)){
            noticeStock.type = typeMap!![stockId]
            noticeStockMap.put(stockId, noticeStock)
            mvpModel?.addNoticeStock(noticeStock)
            Toast.makeText(context, "新增成功 下次刷新時顯示", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "查無代號", Toast.LENGTH_SHORT).show()
        }
    }

    override fun updateNoticeStock(noticeStock: NoticeStock) {
        mvpModel?.updateNoticeStock(noticeStock)

        val context = mvpView as Context
        Toast.makeText(context, "修改成功 下次更新時刷新", Toast.LENGTH_SHORT).show()
    }

    override fun deleteNoticeStock(stockId: String): Boolean {
        val noticeStockMap = mvpView!!.getNoticeStock()
        if (noticeStockMap.containsKey(stockId)){
            val noticeStock = noticeStockMap[stockId]!!
            mvpModel?.deleteNoticeStock(noticeStock.id!!)
            noticeStockMap.remove(stockId)

            return true
        }else{
            return false
        }
    }

    override fun getNoticeStocks() {
        mvpModel!!.getNoticeStocks().let {
            mvpView?.getNoticeStockCallback(it)
        }
    }

    override fun getRealtimePrice() {
        val noticeStockMap = mvpView!!.getNoticeStock()
        val q = noticeStockMap.values.map {
            it.type + "_" + it.stockId + ".tw"
        }.toSet().joinToString(separator = "|")

        mvpModel!!.getRealtimePrice(q){
            val datas = mutableListOf<TwseResponse>()
            val goals = mutableListOf<TwseResponse>()

            if (it.code > 0){
                it.data?.forEach{
                    val stockId = it.stockId

                    var nowPrice = it.nowPrice
                    if (nowPrice == null) {
                        nowPrice = latestPrice[stockId]
                        it.nowPrice = nowPrice
                    }else{
                        latestPrice[stockId] = nowPrice
                    }

                    if (noticeStockMap.containsKey(it.stockId)){
                        val noticeStock = noticeStockMap[it.stockId]!!
                        val priceFrom = noticeStock.priceFrom!!
                        val priceTo = noticeStock.priceTo!!

                        it.aim = Aim(priceFrom, priceTo)
                        if (nowPrice != null && priceFrom <= nowPrice && nowPrice <= priceTo){
                            goals.add(it)
                            goal(stockId, priceFrom, priceTo)
                        }else {
                            datas.add(it)
                        }
                    }
                }
            }
            mvpView?.getRealtimePriceCallback(datas, goals)
        }.let {
            disposables?.add(it)
        }
    }

    override fun getTaiex() {
        mvpModel!!.getTaiex{
            mvpView?.getTaiexCallback(it.data)
        }.let {
            disposables?.add(it)
        }
    }

    override fun getStockType(onSuccess: (List<String>) -> Unit) {
        if (typeMap != null){
            onSuccess(typeMap!!.keys.toList())
            return
        }
        mvpView?.showLoading()
        mvpModel!!.getStockType{
            mvpView?.hideLoading()
            if (it.code > 0){
                typeMap = it.data!!
                onSuccess(typeMap!!.keys.toList())
            }else{
                mvpView?.showMsg(it.msg, it.code)
            }
        }.let {
            disposables?.add(it)
        }
    }

    // -------------------- private function --------------------
    private fun goal(stockId: String, from: Double, to: Double){
        val context = mvpView as Context

        val msg = context.getString(R.string.goal, stockId, from, to)
        if (goalMsg.containsKey(stockId) && goalMsg[stockId] == msg){
            return
        }

        goalMsg[stockId] = msg
        mvpView?.stockGoal(msg)
    }
}