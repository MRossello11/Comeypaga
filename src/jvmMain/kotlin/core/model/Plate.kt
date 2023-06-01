package core.model

data class Plate(
    val _id: String,
    val plateName: String,
    val description: String?,
    val price: String,
    val type: String
) {
    override fun toString(): String {
        return "Plate{id:$_id,name:$plateName}"
    }
}
