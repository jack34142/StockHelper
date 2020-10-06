package network.co.imge.stockhelper.mvp.presenter

import androidx.fragment.app.Fragment
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BasePresenter
import network.co.imge.stockhelper.data.MyData
import network.co.imge.stockhelper.mvp.contract.RealtimePriceContract
import network.co.imge.stockhelper.mvp.model.RealtimePriceModel
import network.co.imge.stockhelper.pojo.Aim
import network.co.imge.stockhelper.pojo.TwseResponse

class RealtimePricePresenter: BasePresenter<RealtimePriceContract.IRealtimePriceView>(), RealtimePriceContract.IRealtimePricePresenter {
    private val TAG: String = "RealtimePricePresenter"

    private var mvpModel: RealtimePriceModel? = null
    val latestPrice: MutableMap<String, Double> = mutableMapOf()
    val goalMsg: MutableMap<String, String> = mutableMapOf()

    override fun attachView(mvpView: RealtimePriceContract.IRealtimePriceView) {
        super.attachView(mvpView)
        val context = (mvpView as Fragment).context!!
        mvpModel = RealtimePriceModel(context)
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }

    override fun getRealtimePrice() {
        mvpModel!!.getRealtimePrice(){
            val datas = mutableListOf<TwseResponse>()
            val goals = mutableListOf<TwseResponse>()

            it.forEach{
                val stockId = it.stockId

                var nowPrice = it.nowPrice
                if (nowPrice == null) {
                    nowPrice = latestPrice[stockId]
                    it.nowPrice = nowPrice
                }else{
                    latestPrice[stockId] = nowPrice
                }

                MyData.noticeStocks.filter {
                    it.stockId == stockId
                }[0].run {
                    it.aim = Aim(priceFrom!!, priceTo!!)
                    if (nowPrice != null && priceFrom!! <= nowPrice && nowPrice <= priceTo!!){
                        goals.add(it)
                        goal(stockId, priceFrom!!, priceTo!!)
                    }else {
                        datas.add(it)
                    }
                }
            }

            mvpView?.getRealtimePriceCallback(datas, goals)
        }.let {
            disposables?.add(it)
        }
    }

    private fun goal(stockId: String, from: Double, to: Double){
        val context = (mvpView as Fragment).context!!

        val msg = context.getString(R.string.goal, stockId, from, to)
        if (goalMsg.containsKey(stockId) && goalMsg[stockId] == msg){
            return
        }

        goalMsg[stockId] = msg
        mvpView?.stockGoal(msg)
    }
}