package network.co.imge.stockhelper.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Button
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import network.co.imge.stockhelper.R
import network.co.imge.stockhelper.base.BaseActivity
import network.co.imge.stockhelper.http.HttpClient
import network.co.imge.stockhelper.mvp.contract.MainContract
import network.co.imge.stockhelper.mvp.presenter.MainPresenter

class MainActivity : BaseActivity(), MainContract.IMainView {
    private val TAG: String = "MainActivity"
    private lateinit var btn_test: Button

    private val presenter by lazy { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)

        initView()
        initListener()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun initView() {
        btn_test = findViewById(R.id.main_test)
    }

    fun initListener(){
        btn_test.setOnClickListener{
            presenter.test()
        }
    }
}