import com.google.gson.annotations.SerializedName

data class MyResponse<T> (
	@SerializedName("code") val code : Int,
	@SerializedName("msg") val msg : String,
	@SerializedName("data") val title : T?
)