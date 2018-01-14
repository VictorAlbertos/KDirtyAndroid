package data.foundation

data class PaginatedItems<out T>(val offset: Int, val items: List<T>, val hasNext: Boolean)
