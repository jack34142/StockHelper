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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseFragment
import network.co.imge.stockhelper.mvp.contract.RealtimePriceContract
import network.co.imge.stockhelper.mvp.presenter.RealtimePricePresenter
import network.co.imge.stockhelper.notification.MyService
import network.co.imge.stockhelper.pojo.TwseResponse
import network.co.imge.stockhelper.ui.activity.GoalActivity
import network.co.imge.stockhelper.ui.adapter.receclerView.RealtimePriceListAdapter

class RealtimePriceFragment : BaseFragment(), RealtimePriceContract.IRealtimePriceView {
    private val TAG: String = "RealtimePriceFragment"

    private lateinit var rView_data: RecyclerView
    private lateinit var rView_goal: RecyclerView

    private var presenter: RealtimePricePresenter? = RealtimePricePresenter()
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var datas: MutableList<TwseResponse> = mutableListOf()
    private var goals: MutableList<TwseResponse> = mutableListOf()
    private lateinit var dataAdapter: RealtimePriceListAdapter
    private lateinit var goalAdapter: RealtimePriceListAdapter

    var serviceIntent: Intent? = null

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
        getRealtimePrice()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceIntent != null){
            context?.stopService(serviceIntent)
            serviceIntent = null
        }

        presenter?.detachView()
        presenter = null
    }

    override fun dispose() {
        super.dispose()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun initView(v: View){
        rView_data = v.findViewById(R.id.realtimePrice_dataList)
        rView_data.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        dataAdapter = RealtimePriceListAdapter(this.datas)
        rView_data.adapter = dataAdapter

        rView_goal = v.findViewById(R.id.realtimePrice_goalList)
        rView_goal.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goalAdapter = RealtimePriceListAdapter(this.goals)
        rView_goal.adapter = goalAdapter
    }

    fun getRealtimePrice(){
        if (serviceIntent == null)
            serviceIntent = Intent(context, MyService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(serviceIntent)
        } else {
            context?.startService(serviceIntent)
        }
        presenter?.getRealtimePrice()
    }

    // -------------------- mvp function --------------------
    override fun getRealtimePriceCallback(datas: MutableList<TwseResponse>, goals: MutableList<TwseResponse>) {
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

    override fun stockGoal(msg: String) {
        val intent = Intent(context, GoalActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }
}