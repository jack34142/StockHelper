package network.co.imge.stockhelper.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseFragment
import network.co.imge.stockhelper.mvp.contract.RealtimePriceContract
import network.co.imge.stockhelper.mvp.presenter.RealtimePricePresenter
import network.co.imge.stockhelper.pojo.TwseResponse
import network.co.imge.stockhelper.ui.adapter.receclerView.RealtimePriceListAdapter

class RealtimePriceFragment : BaseFragment(), RealtimePriceContract.IRealtimePriceView {
    private val TAG: String = "RealtimePriceFragment"

    private lateinit var realtimePriceList: RecyclerView

    private var presenter: RealtimePricePresenter? = RealtimePricePresenter()
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var datas: MutableList<TwseResponse> = mutableListOf()
    private lateinit var realtimePriceAdapter: RealtimePriceListAdapter

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
        initData()
    }

    override fun onDestroy() {
        presenter?.detachView()
        presenter = null
        super.onDestroy()
    }

    private fun initView(v: View){
        realtimePriceList = v.findViewById(R.id.realtimePrice_list)

        realtimePriceList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        realtimePriceAdapter = RealtimePriceListAdapter(this.datas)
        realtimePriceList.adapter = realtimePriceAdapter
    }

    private fun initData(){
        presenter?.getRealtimePrice()
    }

    // -------------------- mvp function --------------------
    override fun getRealtimePriceCallback(datas: MutableList<TwseResponse>) {
        this.datas.clear()
        this.datas.addAll(datas)
        realtimePriceAdapter.notifyDataSetChanged()

        handler?.postDelayed({
            presenter?.getRealtimePrice()
        }, 5000)
    }
}