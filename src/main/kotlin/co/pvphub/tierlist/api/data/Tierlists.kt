package co.pvphub.tierlist.api.data

enum class Tierlists {
    OVERALL,
    VANILLA,
    SWORD,
    UHC,
    POT,
    NETH_POT,
    SMP,
    AXE,
    HOF;

    override fun toString() = name.toLowerCase()

    companion object {
        fun find(name: String) = values().firstOrNull { it.toString() == name }
    }
}