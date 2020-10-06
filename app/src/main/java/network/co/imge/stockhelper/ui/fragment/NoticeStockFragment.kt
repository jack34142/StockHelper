package network.co.imge.stockhelper.ui.fragment

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseFragment
import network.co.imge.stockhelper.data.MyData
import network.co.imge.stockhelper.mvp.contract.NoticeStockContract
import network.co.imge.stockhelper.mvp.presenter.NoticeStockPresenter
import network.co.imge.stockhelper.notification.MyService
import network.co.imge.stockhelper.ui.adapter.receclerView.NoticeStockListAdapter
import network.co.imge.stockhelper.ui.dialog.AddNoticeStockDialog


/**
 * A simple [Fragment] subclass.
 * Use the [NoticeStockFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoticeStockFragment : BaseFragment(), NoticeStockContract.INoticeStockView {
    private val TAG: String = "NoticeStockFragment"

    private var presenter: NoticeStockPresenter? = NoticeStockPresenter()

    private lateinit var btn_add: Button
    private lateinit var noticeStockList: RecyclerView
    private lateinit var noticeStockListAdapter: NoticeStockListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_notice_stock, container, false)
        initView(v)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.attachView(this)
        initListener()
        initData()
    }

    override fun onDestroy() {
        presenter?.detachView()
        presenter = null
        super.onDestroy()
    }

    // -------------------- view function --------------------

    private fun initView(v: View) {
        btn_add = v.findViewById(R.id.noticeStock_add)
        noticeStockList = v.findViewById(R.id.noticeStock_list)
    }

    private fun initListener(){
        btn_add.setOnClickListener{
            AddNoticeStockDialog(context!!, null){
                val count = MyData.noticeStocks.filter {noticeStock ->
                    noticeStock.stockId == it.stockId
                }.size
                if (count > 0){
                    Toast.makeText(context, it.stockId + "已經存在", Toast.LENGTH_SHORT).show()
                    return@AddNoticeStockDialog
                }

                presenter!!.addNoticeStock.invoke(it)
                MyData.noticeStocks.add(it)
                noticeStockListAdapter.notifyDataSetChanged()
            }.show()
        }
    }

    private fun initData(){
        presenter?.getNoticeStocks()
    }

    // -------------------- mvp function --------------------
    override fun getNoticeStocksCallback() {
        noticeStockList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        noticeStockList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        noticeStockListAdapter =
            NoticeStockListAdapter(
                MyData.noticeStocks,
                onEdit = { position, stock ->
                    AddNoticeStockDialog(context!!, stock) {
                        presenter!!.updateNoticeStock.invoke(it)
                        MyData.noticeStocks[position] = it
                        noticeStockListAdapter.notifyDataSetChanged()
                    }.show()
                },
                onDelete = {
                    presenter!!.deleteNoticeStock.invoke(MyData.noticeStocks[it].id!!)
                    MyData.noticeStocks.removeAt(it)
                    noticeStockListAdapter.notifyDataSetChanged()
                })
        noticeStockList.adapter = noticeStockListAdapter
    }
}