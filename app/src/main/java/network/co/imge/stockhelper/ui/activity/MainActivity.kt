package network.co.imge.stockhelper.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseActivity
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.presenter.MainPresenter
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.ui.adapter.NoticeStockListAdapter
import network.co.imge.stockhelper.ui.dialog.AddNoticeStockDialog

class MainActivity : BaseActivity(), MainContract.IMainView {
    private val TAG: String = "MainActivity"

    private var presenter: MainPresenter? = MainPresenter()

    private lateinit var btn_test: Button
    private lateinit var noticeStockList: RecyclerView
    private lateinit var noticeStockListAdapter: NoticeStockListAdapter
    private lateinit var noticeStocks: MutableList<NoticeStock>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter?.attachView(this)

        initView()
        initListener()
        initData()
    }

    override fun onDestroy() {
        presenter?.detachView()
        presenter = null
        super.onDestroy()
    }

    // -------------------- view function --------------------

    private fun initView() {
        btn_test = findViewById(R.id.main_test)
        noticeStockList = findViewById(R.id.main_list)
    }

    private fun initListener(){
        btn_test.setOnClickListener{
            presenter?.getRealtimePrice(noticeStocks)
//            AddNoticeStockDialog(this, null){
//                presenter!!.addNoticeStock.invoke(it)
//                noticeStocks.add(it)
//                noticeStockListAdapter.notifyDataSetChanged()
//            }.show()
        }
    }

    private fun initData(){
        presenter?.getNoticeStocks()
    }

    // -------------------- mvp function --------------------
    override fun getNoticeStocksCallback(stocks: MutableList<NoticeStock>) {
        noticeStocks = stocks

        noticeStockList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        noticeStockList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        noticeStockListAdapter = NoticeStockListAdapter(noticeStocks,
            onEdit = { position, stock ->
                AddNoticeStockDialog(this, stock){
                    presenter!!.updateNoticeStock.invoke(it)
                    noticeStocks[position] = it
                    noticeStockListAdapter.notifyDataSetChanged()
                }.show()
            },
            onDelete = {
                presenter!!.deleteNoticeStock.invoke(stocks[it].id!!)
                stocks.removeAt(it)
                noticeStockListAdapter.notifyDataSetChanged()
            })
        noticeStockList.adapter = noticeStockListAdapter
    }
}