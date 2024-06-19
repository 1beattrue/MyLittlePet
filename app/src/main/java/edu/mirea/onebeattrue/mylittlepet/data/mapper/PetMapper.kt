package edu.mirea.onebeattrue.mylittlepet.data.mapper

import edu.mirea.onebeattrue.mylittlepet.data.local.model.PetDbModel
import edu.mirea.onebeattrue.mylittlepet.data.network.dto.PetDto
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.Pet
import edu.mirea.onebeattrue.mylittlepet.domain.pets.entity.PetType

suspend fun Pet.mapEntityToDbModel(imageMapper: ImageMapper): PetDbModel = PetDbModel(
    id = id,
    type = type,
    name = name,
    imageBase64 = imageMapper.mapUriToBase64(imageUri),
    dateOfBirth = dateOfBirth,
    weight = weight
)

suspend fun PetDbModel.mapDbModelToEntity(imageMapper: ImageMapper): Pet = Pet(
    id = id,
    type = type,
    name = name,
    imageUri = imageMapper.mapBase64ToUri(imageBase64, id),
    dateOfBirth = dateOfBirth,
    weight = weight
)

suspend fun Pet.mapEntityToDto(userToken: String, imageMapper: ImageMapper): PetDto = PetDto(
    id = id,
    type = type.mapToString(),
    name = name,
    imageUrl = imageMapper.mapUriToBase64(imageUri),
    dateOfBirth = dateOfBirth,
    weight = weight,
    userToken = userToken
)

suspend fun PetDto.mapDtoToEntity(imageMapper: ImageMapper): Pet = Pet(
    id = id,
    type = type.mapToPetType(),
    name = name,
    imageUri = imageMapper.mapBase64ToUri(imageUrl, id),
    dateOfBirth = dateOfBirth,
    weight = weight
)

suspend fun List<PetDbModel>.mapDbModelListToEntities(imageMapper: ImageMapper): List<Pet> =
    map { it.mapDbModelToEntity(imageMapper) }

suspend fun List<PetDto>.mapDtoListToEntities(imageMapper: ImageMapper): List<Pet> =
    map { it.mapDtoToEntity(imageMapper) }

private fun PetType.mapToString(): String = this.name

private fun String.mapToPetType(): PetType {
    return when (this) {
        PetType.CAT.name -> PetType.CAT
        PetType.DOG.name -> PetType.DOG
        PetType.RABBIT.name -> PetType.RABBIT
        PetType.BIRD.name -> PetType.BIRD
        PetType.FISH.name -> PetType.FISH
        PetType.SNAKE.name -> PetType.SNAKE
        PetType.TIGER.name -> PetType.TIGER
        PetType.MOUSE.name -> PetType.MOUSE
        PetType.TURTLE.name -> PetType.TURTLE
        else -> {
            throw RuntimeException("Unknown pet type")
        }
    }
}