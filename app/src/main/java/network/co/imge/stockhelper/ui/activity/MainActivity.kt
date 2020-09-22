package network.co.imge.stockhelper.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseActivity
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.presenter.MainPresenter
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.ui.adapter.NoticeStockListAdapter
import network.co.imge.stockhelper.ui.dialog.AddDialog

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
            AddDialog(this) {
                presenter?.addNoticeStock(it)
                for ((index, stock) in noticeStocks.withIndex()){
                    if (stock.stockId == it.stockId) {
                        noticeStocks[index] = it
                    }else if (index == noticeStocks.size-1){
                        noticeStocks.add(it)
                    }
                }
                noticeStockListAdapter.notifyDataSetChanged()
            }.show()
        }
    }

    private fun initData(){
        presenter?.getNoticeStocks()
    }

    // -------------------- mvp function --------------------
    override fun getNoticeStocksCallback(stocks: MutableList<NoticeStock>) {
        noticeStocks = stocks

        noticeStockList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        noticeStockListAdapter = NoticeStockListAdapter(noticeStocks)
        noticeStockList.adapter = noticeStockListAdapter
    }
}