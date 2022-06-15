package com.zzkkcom.roblox.clien.app.koin

import com.zzkkcom.roblox.clien.sea.Sea
import org.koin.dsl.module

val seaModule = module {
    factory {
        Sea()
    }
}