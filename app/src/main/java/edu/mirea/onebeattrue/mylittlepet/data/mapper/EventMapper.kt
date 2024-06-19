package edu.mirea.onebeattrue.mylittlepet.data.mapper

import edu.mirea.onebeattrue.mylittlepet.data.local.model.EventDbModel
import edu.mirea.onebeattrue.mylittlepet.data.network.dto.EventDto
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Event

fun Event.mapEntityToDbModel(): EventDbModel = EventDbModel(
    id = id,
    petId = petId,
    time = time,
    label = label,
    repeatable = repeatable
)

fun EventDbModel.mapDbModelToEntity(): Event = Event(
    id = id,
    petId = petId,
    time = time,
    label = label,
    repeatable = repeatable
)

fun Event.mapEntityToDto(): EventDto = EventDto(
    id = id,
    petId = petId,
    time = time,
    label = label,
    repeatable = repeatable
)

fun EventDto.mapDtoToEntity(): Event = Event(
    id = id,
    petId = petId,
    time = time,
    label = label,
    repeatable = repeatable
)

fun List<EventDbModel>.mapDbModelListToEntities(): List<Event> = map { it.mapDbModelToEntity() }

fun List<EventDto>.mapDtoListToEntities(): List<Event> = map { it.mapDtoToEntity() }