package br.com.dusty.dcommon.util.stdlib

inline fun <reified T> Array<T>.copyAndAdd(element: T) = toMutableSet().run {
	val updated = add(element)
	Pair(toTypedArray(), updated)
}

inline fun <reified T> Array<T>.copyAndRemove(element: T) = toMutableSet().run {
	val updated = remove(element)
	Pair(toTypedArray(), updated)
}
