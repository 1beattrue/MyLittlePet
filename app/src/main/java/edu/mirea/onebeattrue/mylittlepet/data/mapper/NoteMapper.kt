package edu.mirea.onebeattrue.mylittlepet.data.mapper

import edu.mirea.onebeattrue.mylittlepet.data.local.model.NoteDbModel
import edu.mirea.onebeattrue.mylittlepet.data.network.dto.NoteDto
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Note

fun Note.mapEntityToDbModel(): NoteDbModel = NoteDbModel(
    id = id,
    petId = petId,
    text = text,
    iconResId = iconResId
)

fun NoteDbModel.mapDbModelToEntity(): Note = Note(
    id = id,
    petId = petId,
    text = text,
    iconResId = iconResId
)

fun Note.mapEntityToDto(): NoteDto = NoteDto(
    id = id,
    petId = petId,
    text = text,
    iconResId = iconResId
)

fun NoteDto.mapDtoToEntity(): Note = Note(
    id = id,
    petId = petId,
    text = text,
    iconResId = iconResId
)

fun List<NoteDbModel>.mapDbModelListToEntities(): List<Note> = map { it.mapDbModelToEntity() }

fun List<NoteDto>.mapDtoListToEntities(): List<Note> = map { it.mapDtoToEntity() }