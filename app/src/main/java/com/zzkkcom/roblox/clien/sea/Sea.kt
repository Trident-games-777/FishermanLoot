package com.zzkkcom.roblox.clien.sea

import com.zzkkcom.roblox.clien.R

class Sea {
    private val seaLoot = listOf(
        R.drawable.aaa,
        R.drawable.bbb,
        R.drawable.ccc,
        R.drawable.ddd,
        R.drawable.eee
    )

    fun getShuffledLoot(): List<Int> {
        val loot = mutableListOf<Int>()
        repeat(9) {
            loot.add(seaLoot.random())
        }
        return loot
    }

    fun getWinLines(loot: List<Int>): List<List<Int>> {
        val lines = mutableListOf<List<Int>>()
        if (loot[0] == loot[1] && loot[1] == loot[2]) lines.add(listOf(0, 1, 2))
        if (loot[3] == loot[4] && loot[4] == loot[5]) lines.add(listOf(3, 4, 5))
        if (loot[6] == loot[7] && loot[7] == loot[8]) lines.add(listOf(6, 7, 8))

        if (loot[0] == loot[4] && loot[4] == loot[8]) lines.add(listOf(0, 4, 8))
        if (loot[6] == loot[4] && loot[4] == loot[2]) lines.add(listOf(6, 4, 2))
        return lines
    }
}