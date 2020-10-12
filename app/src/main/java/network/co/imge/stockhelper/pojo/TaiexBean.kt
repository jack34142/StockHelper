package network.co.imge.stockhelper.pojo

data class TaiexBean (
	val stockName: String,
	val nowPrice: Double,
	val openPrice : Double,
	val highPrice : Double,
	val lowPrice : Double,
	val yesterdayPrice : Double,
	val totalAmount : Double,
	val totalQty : Int
)