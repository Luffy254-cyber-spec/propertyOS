package com.him.landlordtenant.app.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.him.landlordtenant.app.data.model.*

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val shortMessage: String?,
    val icon: NotificationIcon,
    val priority: NotificationPriority,
    val status: NotificationStatus,
    @Embedded(prefix = "action_") val action: NotificationAction?,
    val apartmentId: String?,
    val apartmentName: String?,
    val floorId: String?,
    val houseId: String?,
    val houseNumber: String?,
    val referenceId: String?,
    val referenceType: String?,
    val senderId: String?,
    val senderName: String?,
    @Embedded(prefix = "delivery_") val delivery: NotificationDelivery,
    val scheduledAt: String?,
    val expiresAt: String?,
    val createdAt: String?,
    val readAt: String?,
    val updatedAt: String?
)

fun Notification.toEntity() = NotificationEntity(
    id = id,
    userId = userId,
    type = type,
    title = title,
    message = message,
    shortMessage = shortMessage,
    icon = icon,
    priority = priority,
    status = status,
    action = action,
    apartmentId = apartmentId,
    apartmentName = apartmentName,
    floorId = floorId,
    houseId = houseId,
    houseNumber = houseNumber,
    referenceId = referenceId,
    referenceType = referenceType,
    senderId = senderId,
    senderName = senderName,
    delivery = delivery,
    scheduledAt = scheduledAt,
    expiresAt = expiresAt,
    createdAt = createdAt,
    readAt = readAt,
    updatedAt = updatedAt
)

fun NotificationEntity.toDomain() = Notification(
    id = id,
    userId = userId,
    type = type,
    title = title,
    message = message,
    shortMessage = shortMessage,
    icon = icon,
    priority = priority,
    status = status,
    action = action,
    apartmentId = apartmentId,
    apartmentName = apartmentName,
    floorId = floorId,
    houseId = houseId,
    houseNumber = houseNumber,
    referenceId = referenceId,
    referenceType = referenceType,
    senderId = senderId,
    senderName = senderName,
    delivery = delivery,
    scheduledAt = scheduledAt,
    expiresAt = expiresAt,
    createdAt = createdAt,
    readAt = readAt,
    updatedAt = updatedAt
)


