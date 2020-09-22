package network.co.imge.stockhelper.pojo

import com.google.gson.annotations.SerializedName

data class TwseResponse (
	@SerializedName("queryTime") val queryTime : QueryTime,
	@SerializedName("referer") val referer : String,
	@SerializedName("rtmessage") val rtmessage : String,
	@SerializedName("exKey") val exKey : String,
	@SerializedName("msgArray") val msgArray : List<MsgArray>,
	@SerializedName("userDelay") val userDelay : Int,
	@SerializedName("rtcode") val rtcode : Int,
	@SerializedName("cachedAlive") val cachedAlive : Int
)

data class MsgArray (
	@SerializedName("c") val stockId : Int,
	@SerializedName("n") val stockName : String,
	@SerializedName("b") val best5purchasePrice : String,
	@SerializedName("g") val best5purchaseQty : String,
	@SerializedName("a") val best5sellPrice : String,
	@SerializedName("f") val best5sellQty : String,
	@SerializedName("o") val openPrice : Double,
	@SerializedName("h") val highPrice : Double,
	@SerializedName("l") val lowPrice : Double,
	@SerializedName("y") val yesterdayPrice : Double,
	@SerializedName("z") val nowPrice : Double,
	@SerializedName("s") val nowQty : Int,
	@SerializedName("v") val totalQty : Int,
	@SerializedName("ex") val stockType : String,
	@SerializedName("u") val u : Double,
	@SerializedName("mt") val mt : Int,
	@SerializedName("ps") val ps : Int,
	@SerializedName("tk0") val tk0 : String,
	@SerializedName("tlong") val tlong : Long,
	@SerializedName("t") val t : String,
	@SerializedName("it") val it : Int,
	@SerializedName("ch") val ch : String,
	@SerializedName("w") val w : Double,
	@SerializedName("pz") val pz : Double,
	@SerializedName("d") val d : Int,
	@SerializedName("tv") val tv : Int,
	@SerializedName("tk1") val tk1 : String,
	@SerializedName("ts") val ts : Int,
	@SerializedName("nf") val nf : String,
	@SerializedName("p") val p : Int,
	@SerializedName("i") val i : Int,
	@SerializedName("ip") val ip : Int
)

data class QueryTime (
	@SerializedName("stockInfoItem") val stockInfoItem : Int,
	@SerializedName("sessionKey") val sessionKey : String,
	@SerializedName("sessionStr") val sessionStr : String,
	@SerializedName("sysDate") val sysDate : Int,
	@SerializedName("sessionFromTime") val sessionFromTime : Long,
	@SerializedName("stockInfo") val stockInfo : Int,
	@SerializedName("showChart") val showChart : Boolean,
	@SerializedName("sessionLatestTime") val sessionLatestTime : Long,
	@SerializedName("sysTime") val sysTime : String
)