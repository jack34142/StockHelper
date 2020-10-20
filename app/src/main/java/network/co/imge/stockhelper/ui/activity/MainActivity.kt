package network.co.imge.stockhelper.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseActivity
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.presenter.MainPresenter
import network.co.imge.stockhelper.notification.MyService
import network.co.imge.stockhelper.pojo.NoticeStock
import network.co.imge.stockhelper.pojo.TaiexBean
import network.co.imge.stockhelper.pojo.TwseResponse
import network.co.imge.stockhelper.ui.adapter.receclerView.RealtimePriceListAdapter
import network.co.imge.stockhelper.ui.dialog.AddNoticeStockDialog
import java.util.*
import kotlin.system.exitProcess

class MainActivity : BaseActivity(), MainContract.IMainView {
    private val TAG: String = "MainActivity"
    
    var backTime: Long = 0
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

    private var presenter: MainPresenter? = MainPresenter()
    private var handler: Handler? = Handler(Looper.getMainLooper())
    private var datas: MutableList<TwseResponse> = mutableListOf()
    private var goals: MutableList<TwseResponse> = mutableListOf()
    private lateinit var dataAdapter: RealtimePriceListAdapter
    private lateinit var goalAdapter: RealtimePriceListAdapter
    private lateinit var noticeStockMap: MutableMap<String, NoticeStock>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter?.attachView(this)

        initView()
        initAdapter()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyService.stopService(this)
        handler?.removeCallbacksAndMessages(null)

        presenter?.detachView()
        presenter = null
    }

    private fun initView(){
        rView_data = findViewById(R.id.main_dataList)
        rView_goal = findViewById(R.id.main_goalList)

        text_nowPrice = findViewById(R.id.main_nowPrice)
        text_taiexTitle = findViewById(R.id.main_taiexTitle)
        text_totalAmount = findViewById(R.id.main_totalAmount)
        text_totalQty = findViewById(R.id.main_totalQty)
        text_yesterdayPrice = findViewById(R.id.main_yesterdayPrice)
        text_openPrice = findViewById(R.id.main_openPrice)
        text_lowPrice = findViewById(R.id.main_low)
        text_highPrice = findViewById(R.id.main_high)
    }

    private fun initAdapter(){
        rView_data.layoutManager = object: LinearLayoutManager(this, RecyclerView.VERTICAL, false){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rView_goal.layoutManager = object: LinearLayoutManager(this, RecyclerView.VERTICAL, false){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        val onEdit = {twseResponse: TwseResponse ->
            val noticeStock = noticeStockMap[twseResponse.stockId]
            AddNoticeStockDialog(this, noticeStock){
                presenter?.updateNoticeStock(it)
            }.show()
        }

        dataAdapter = RealtimePriceListAdapter(datas,
            onEdit = onEdit,
            onDelete = {
                if (presenter!!.deleteNoticeStock(it.stockId)){
                    datas.remove(it)
                    dataAdapter.notifyDataSetChanged()
                }
            }
        )
        goalAdapter = RealtimePriceListAdapter(goals,
            onEdit = onEdit,
            onDelete = {
                if (presenter!!.deleteNoticeStock(it.stockId)){
                    goals.remove(it)
                    goalAdapter.notifyDataSetChanged()
                }
            }
        )

        rView_data.adapter = dataAdapter
        rView_goal.adapter = goalAdapter
    }

    private fun initData(){
        presenter?.getNoticeStocks()
        getRealtimePrice()
    }

    // -------------------- view function --------------------
    fun getRealtimePrice(){
        MyService.startService(this)
        presenter?.getRealtimePrice()
        presenter?.getTaiex()
    }

    fun showAdd(){
        AddNoticeStockDialog(this, null){
            if (noticeStockMap.containsKey(it.stockId)){
                val msg = getString(R.string.exist, it.stockId)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }else{
                presenter?.addNoticeStock(it)
            }
        }.show()
    }

    // -------------------- override --------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()){
            R.id.main_add -> showAdd()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        val now = Date().time

        if (now - backTime < 2000) {
            exitProcess(0)
        }else{
            Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
            backTime = now
        }
    }

    override fun getNoticeStockCallback(stocks: MutableMap<String, NoticeStock>) {
        noticeStockMap = stocks
    }

    override fun getNoticeStock(): MutableMap<String, NoticeStock> {
        return noticeStockMap
    }

    // -------------------- mvp function --------------------
    override fun getRealtimePriceCallback(datas: MutableList<TwseResponse>, goals: MutableList<TwseResponse>) {
        MyService.updateTime(this)

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
        val intent = Intent(this, GoalActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }

}