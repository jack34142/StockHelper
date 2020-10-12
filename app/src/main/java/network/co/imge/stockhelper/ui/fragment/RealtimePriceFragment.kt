package network.co.imge.stockhelper.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseFragment
import network.co.imge.stockhelper.mvp.contract.RealtimePriceContract
import network.co.imge.stockhelper.mvp.presenter.RealtimePricePresenter
import network.co.imge.stockhelper.notification.MyService
import network.co.imge.stockhelper.pojo.TaiexBean
import network.co.imge.stockhelper.pojo.TwseResponse
import network.co.imge.stockhelper.ui.activity.GoalActivity
import network.co.imge.stockhelper.ui.adapter.receclerView.RealtimePriceListAdapter
import org.w3c.dom.Text

class RealtimePriceFragment : BaseFragment(), RealtimePriceContract.IRealtimePriceView {
    private val TAG: String = "RealtimePriceFragment"

    private lateinit var rView_data: RecyclerView
    private lateinit var rView_goal: RecyclerView
    private lateinit var text_taiexTitle: TextView
    private lateinit var text_nowPrice: TextView
    private lateinit var text_totalAmount: TextView
    private lateinit var text_totalQty: TextView
    private lateinit var text_yesterdayPrice: TextView
    private lateinit var text_openPrice: TextView
    private lateinit var text_lowPrice: TextView
    private lateinit var text_highPrice: TextView

    private var presenter: RealtimePricePresenter? = RealtimePricePresenter()
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var datas: MutableList<TwseResponse> = mutableListOf()
    private var goals: MutableList<TwseResponse> = mutableListOf()
    private lateinit var dataAdapter: RealtimePriceListAdapter
    private lateinit var goalAdapter: RealtimePriceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_realtime_price, container, false)
        initView(v)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.attachView(this)
        initAdapter()
        getRealtimePrice()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyService.stopService(context!!)
        presenter?.detachView()
        presenter = null
    }

    override fun dispose() {
        super.dispose()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun initView(v: View){
        rView_data = v.findViewById(R.id.realtimePrice_dataList)
        rView_goal = v.findViewById(R.id.realtimePrice_goalList)

        text_nowPrice = v.findViewById(R.id.realtimePrice_nowPrice)
        text_taiexTitle = v.findViewById(R.id.realtimePrice_taiexTitle)
        text_totalAmount = v.findViewById(R.id.realtimePrice_totalAmount)
        text_totalQty = v.findViewById(R.id.realtimePrice_totalQty)
        text_yesterdayPrice = v.findViewById(R.id.realtimePrice_yesterdayPrice)
        text_openPrice = v.findViewById(R.id.realtimePrice_openPrice)
        text_lowPrice = v.findViewById(R.id.realtimePrice_low)
        text_highPrice = v.findViewById(R.id.realtimePrice_high)
    }

    private fun initAdapter(){
        rView_data.layoutManager = object: LinearLayoutManager(context, RecyclerView.VERTICAL, false){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        dataAdapter = RealtimePriceListAdapter(this.datas)
        rView_data.adapter = dataAdapter

        rView_goal.layoutManager = object: LinearLayoutManager(context, RecyclerView.VERTICAL, false){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        goalAdapter = RealtimePriceListAdapter(this.goals)
        rView_goal.adapter = goalAdapter
    }

    fun getRealtimePrice(){
        MyService.startService(context!!)
        presenter?.getRealtimePrice()
        presenter?.getTaiex()
    }

    // -------------------- mvp function --------------------
    override fun getRealtimePriceCallback(datas: MutableList<TwseResponse>, goals: MutableList<TwseResponse>) {
        MyService.updateTime(context!!)

        this.datas.clear()
        this.datas.addAll(datas)
        dataAdapter.notifyDataSetChanged()

        this.goals.clear()
        this.goals.addAll(goals)
        goalAdapter.notifyDataSetChanged()

        handler?.postDelayed({
            presenter?.getRealtimePrice()
        }, 5000)
    }

    override fun getTaiexCallback(taiexbean: TaiexBean?) {
        if (taiexbean != null){
            text_taiexTitle.text = taiexbean.stockName
            text_totalAmount.text = String.format("%,.2f", taiexbean.totalAmount)
            text_totalQty.text = String.format("%,d", taiexbean.totalQty)
            text_yesterdayPrice.text = taiexbean.yesterdayPrice.toString()
            text_openPrice.text = taiexbean.openPrice.toString()
            text_lowPrice.text = taiexbean.lowPrice.toString()
            text_highPrice.text = taiexbean.highPrice.toString()

            val nowPrice =  taiexbean.nowPrice
            val yesterdayPrice =  taiexbean.yesterdayPrice
            val diff =  nowPrice - yesterdayPrice
            val diffRate = (nowPrice / yesterdayPrice - 1) * 100
            text_nowPrice.text = String.format("%.2f (%+.2f / %+.2f%%)", nowPrice, diff, diffRate)
        }

        handler?.postDelayed({
            presenter?.getTaiex()
        }, 5000)
    }

    override fun stockGoal(msg: String) {
        val intent = Intent(context, GoalActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }
}