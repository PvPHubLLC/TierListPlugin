package co.pvphub.tierlist.api

import java.util.UUID

fun String.toUUIDNoDashes(): UUID = UUID.fromString(
    this.replaceFirst(
        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)".toRegex(), "$1-$2-$3-$4-$5"
    )
)