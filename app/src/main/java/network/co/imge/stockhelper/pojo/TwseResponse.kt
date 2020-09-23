package network.co.imge.stockhelper.pojo

data class TwseResponse (
	val stockId : String,
	val stockName : String,
	val best5purchasePrice : List<Double>,
	val best5purchaseQty : List<Int>,
	val best5sellPrice : List<Double>,
	val best5sellQty : List<Int>,
	val openPrice : Double,
	val highPrice : Double,
	val lowPrice : Double,
	val yesterdayPrice : Double,
	val nowPrice : Double?,
	val nowQty : Int?,
	val totalQty : Int,
	val type : String
)