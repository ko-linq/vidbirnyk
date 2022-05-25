package io.github.vidbirnyk

import java.util.*

abstract class Vidbirnyk<Node> {

    protected abstract fun getName(node: Node): String

    protected open fun hasClass(node: Node, name: String): Boolean = false

    protected abstract fun getChildrenList(node: Node): List<Node>

    protected abstract fun <T : Any> getValue(node: Node): T?

    private fun matches(node: Node, selector: Selector): Boolean {
        if (selector.name == This.name) return true
        if (selector.name != null && selector.name != getName(node)) return false
        if (selector.classNames != null && selector.classNames.any { !hasClass(node, it) }) return false
        return true
    }

    private fun select(node: Node, selector: Selector?, deepness: Deepness, matchSelf: Boolean = false): List<Node> {
        if (selector == null) return listOf(node)
        if (matchSelf && matches(node, selector)) return select(node, selector.next, selector.nextDeepness)
        if (selector.name == This.name) return select(node, selector.next, selector.nextDeepness)
        return getChildrenList(node).flatMap { child ->
            when {
                matches(child, selector) -> select(child, selector.next, selector.nextDeepness)
                deepness == Deepness.All -> select(child, selector, deepness)
                else -> emptyList()
            }
        }

    }

    operator fun Node.get(selector: Selector): Node? {
        return select(this, selector, Deepness.All, true).firstOrNull()
    }

    fun Node.all(selector: Selector): List<Node> {
        return select(this, selector, Deepness.All, true)
    }


    operator fun Node.get(selector: String): Node? = get(selector.toSelector())
    operator fun Node.get(idx: Int): Node = getChildrenList(this)[idx]
    fun Node.all(selector: String): List<Node> = all(selector.toSelector())

    fun Node.find(selector: String): Node? = get(selector.toSelector())
    fun Node.getAt(idx: Int): Node = getChildrenList(this)[idx]
    fun Node.findAll(selector: String): List<Node> = all(selector.toSelector())

    val Node.text get() = getValue<String>(this)!!
    val Collection<Node>.text get() = map { it.text }

    val Node.children get() = getChildrenList(this)
}

enum class Deepness {
    ChildrenOnly,
    All
}

data class Selector(
    val name: String? = null,
    val classNames: List<String>? = null,

    val next: Selector? = null,
    val nextDeepness: Deepness = Deepness.All,
)

fun String.toSelector(): Selector {
    require(this.isNotBlank())
    val tokenizer = StringTokenizer(this)
    val selectors = mutableListOf<Selector>()
    while (tokenizer.hasMoreTokens()) {
        val token = tokenizer.nextToken()
        if (token.isBlank()) continue
        when (token) {
            "*" -> selectors += Selector()
            "&" -> selectors += This
            ">" -> {
                selectors.removeLastOrNull()
                    ?.let { last -> selectors += last.copy(nextDeepness = Deepness.ChildrenOnly) }
            }
            else -> {
                val idClassAndAttributes = token.split("[")
                val idAndClass = idClassAndAttributes[0]
                val subtokens = idAndClass.split(".", ":")
                val selector = Selector(
                    subtokens.getOrNull(0)?.ifBlank { null },
                    subtokens.drop(1).map {
                        if (":$it" in idAndClass) ":$it" else it
                    }.ifEmpty { null }
                )
                selectors += selector
            }
        }
    }
    return selectors.reduceRight { parent, child -> parent.copy(next = child) }
}

val This = Selector("&")

fun Selector.then(selector: Selector) = copy(next = selector)
fun Selector.then(selector: String) = copy(next = selector.toSelector())

fun Selector.child(selector: Selector) = copy(next = selector, nextDeepness = Deepness.ChildrenOnly)
fun Selector.child(selector: String) = copy(next = selector.toSelector(), nextDeepness = Deepness.ChildrenOnly)
