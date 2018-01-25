package br.com.dusty.dcommon.util.stdlib

import java.lang.reflect.Field
import java.lang.reflect.Modifier

var field_Field_modifiers = Field::class.java.getAccessibleField("modifiers")

fun Class<*>.getAccessibleField(name: String) = getDeclaredField(name).apply { isAccessible = true }

fun Field.setFinal(obj: Any, value: Any) {
	isAccessible = true

	field_Field_modifiers.set(this, modifiers and Modifier.FINAL.inv())

	set(obj, value)
}

object Reflections {

	val OBC_PACKAGE = "org.bukkit.craftbukkit."
	val NMS_PACKAGE = "net.minecraft.server."
	val VERSION = "v1_8_R3"

	fun NMSClass(name: String) = Class.forName(NMS_PACKAGE + VERSION + "." + name)

	fun OBCClass(name: String) = Class.forName(OBC_PACKAGE + VERSION + "." + name)
}
