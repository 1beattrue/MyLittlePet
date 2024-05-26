package edu.mirea.onebeattrue.mylittlepet.domain.pets.entity


interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}