package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity

import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.AlarmItem

interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}