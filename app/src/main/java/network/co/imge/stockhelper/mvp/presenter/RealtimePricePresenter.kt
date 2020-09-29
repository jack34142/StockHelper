package network.co.imge.stockhelper.mvp.presenter

import android.util.Log
import androidx.fragment.app.Fragment
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
    val goalMap: MutableMap<String, Double> = mutableMapOf()

    override fun attachView(mvpView: RealtimePriceContract.IRealtimePriceView) {
        super.attachView(mvpView)
        mvpModel = RealtimePriceModel((mvpView as Fragment).context!!)
    }

    override fun detachView() {
        super.detachView()
        mvpModel?.detachView()
        mvpModel = null
    }

    override fun getRealtimePrice() {
        mvpModel!!.getRealtimePrice(){
            val datas = mutableListOf<TwseResponse>()

            it.forEach{
                val stockId = it.stockId

                var nowPrice = it.nowPrice
                if (nowPrice == null) {
                    nowPrice = latestPrice[stockId]
                    it.nowPrice = nowPrice
                }else{
                    latestPrice[stockId] = nowPrice
                }

                if (nowPrice != null){
                    MyData.noticeStocks.filter {
                        it.stockId == stockId
                    }[0].run {
                        val aimPrice = price!!
                        if (aimPrice > 0) {
                            it.aim = Aim(1, aimPrice)

                            if (nowPrice <= aimPrice)
                                goal(stockId, aimPrice)
                        }else{
                            it.aim = Aim(-1, -aimPrice)

                            if (nowPrice >= -aimPrice)
                                goal(stockId, aimPrice)
                        }
                    }
                }
                datas.add(it)
            }
            datas.sortBy {
                val nowPrice = it.nowPrice
                if (nowPrice != null) {
                    (nowPrice / it.aim!!.aimPrice - 1) * it.aim!!.method
                }else{
                    100.0
                }
            }

            mvpView?.getRealtimePriceCallback(datas)
        }.let {
            disposables?.add(it)
        }
    }

    private fun goal(stockId: String, price: Double){
        if (goalMap.containsKey(stockId)){
            val goal = goalMap[stockId]
            if (goal != price){
                goalMap[stockId] = price
                //TODO: vibrate
                Log.d(TAG, stockId + "update")
            }
        }else{
            goalMap[stockId] = price
            //TODO: vibrate
            Log.d(TAG, stockId + "add")
        }
    }
}