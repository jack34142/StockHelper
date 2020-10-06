package network.co.imge.stockhelper.pojo

data class NoticeStock (
	var id: Long? = null,
	var stockId : String? = null,
	var type : String? = null,
	var priceFrom : Double? = null,
	var priceTo : Double? = null
)