package io.github.vidbirnyk

import io.github.vidbirnyk.MapAdapter.all
import io.github.vidbirnyk.MapAdapter.get

object MapAdapter : Vidbirnyk<Map.Entry<String, *>>() {
    override fun getName(node: Map.Entry<String, *>): String {
        return node.key
    }

    override fun getChildrenList(node: Map.Entry<String, *>): List<Map.Entry<String, *>> {
        return when (val value = node.value) {
            is Map<*, *> -> value.mapKeys { it.key as String }.entries.toList()
            else -> emptyList()
        }
    }

    override fun <T : Any> getValue(node: Map.Entry<String, *>): T? {
        return node.value as? T
    }

    override fun hasClass(node: Map.Entry<String, *>, name: String): Boolean {
        return when (name) {
            "empty" -> node.value == null || (node.value as? Collection<*>)?.isEmpty() == true || (node.value as? Map<*, *>)?.isEmpty() == true
            else -> false
        }
    }
}

fun Map<String, *>.find(selector: String) = entries.firstNotNullOfOrNull { entry -> entry[selector] }
fun Map<String, *>.findAll(selector: String) = entries.flatMap { entry -> entry.all(selector) }