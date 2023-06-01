package core.model

data class Address(
    val street: String,
    val town: String,
) {
    override fun toString(): String {
        return "${this.town}, ${this.street}"
    }
}
