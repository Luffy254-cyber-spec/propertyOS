package com.him.landlordtenant.app.ui.screens.tenant

import androidx.compose.ui.graphics.vector.ImageVector
import com.him.landlordtenant.app.data.model.HouseStatus

data class TenantDashboardUIState(
    val tenantName: String,
    val apartmentName: String,
    val apartmentId: String,
    val houseId: String = "",
    val houseNumber: String,
    val floorNumber: String,
    val houseType: String,
    val location: String,
    val landlordId: String,
    val landlordName: String,
    val landlordPhone: String,
    val monthlyRent: Double,
    val totalRentDue: Double = 0.0,
    val totalUtilitiesDue: Double = 0.0,
    val waterBill: Double,
    val garbageFee: Double,
    val serviceCharge: Double,
    val additionalFees: Double = 0.0,
    val previousArrears: Double = 0.0,
    val amountPaid: Double = 0.0,
    val outstandingAmount: Double,
    val totalDue: Double,
    val dueDate: String,
    val nextPaymentDate: String,
    val rentStatus: TenantRentStatus,
    val occupancyStatus: TenantOccupancyStatus,
    val unreadNotifications: Int = 0,
    val unreadMessages: Int = 0,
    val maintenanceRequests: Int = 0,
    val loyaltyPoints: Int = 0,
    val featuredApartments: List<TenantApartmentUIModel> = emptyList(),
    val moveInChecklist: List<ChecklistItem> = emptyList()
)

data class ChecklistItem(
    val id: String,
    val task: String,
    val isCompleted: Boolean,
    val category: String // e.g., "DOCUMENTS", "UTILITIES", "PHYSICAL"
)

enum class TenantRentStatus {
    PAID, DUE_SOON, OVERDUE, PARTIALLY_PAID
}

enum class TenantOccupancyStatus {
    ACTIVE, PENDING_VERIFICATION, VACATING, NOTICE_PERIOD, INACTIVE
}



enum class HouseType {
    BEDSITTER, ONE_BEDROOM, TWO_BEDROOM, THREE_BEDROOM, FOUR_BEDROOM, FIVE_BEDROOM, SHOP, OFFICE, OTHER
}

enum class HouseCondition {
    EXCELLENT, GOOD, NEEDS_PAINTING, NEEDS_REPAIR, UNDER_CONSTRUCTION, UNDER_INSPECTION
}

data class TenantHouseUIModel(
    val houseId: String = "",
    val apartmentId: String = "",
    val houseNumber: String,
    val floorNumber: Int,
    val houseType: HouseType,
    val status: HouseStatus,
    val condition: HouseCondition,
    val monthlyRent: Double,
    val deposit: Double,
    val waterBill: Double = 0.0,
    val garbageFee: Double = 0.0,
    val electricityDeposit: Double = 0.0,
    val otherCharges: Double = 0.0,
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val maximumOccupants: Int = 1,
    val availableFrom: String? = null,
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val hasElectricity: Boolean = true,
    val hasWater: Boolean = true,
    val hasParking: Boolean = false,
    val hasInternet: Boolean = false,
    val furnished: Boolean = false,
    val apartmentName: String = "",
    val apartmentAddress: String = "",
    val landlordName: String = "",
    val landlordPhone: String = "",
    val landlordVerified: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val amenities: List<String> = emptyList(),
    val tenantName: String = "",
    val moveInDate: String = "",
    val leaseStatus: String = "Active",
    val nextPaymentDate: String = "",
    val electricityBill: Double = 0.0,
    val garbageBill: Double = 0.0,
    val verified: Boolean = true
) {
    val initialPayment: Double get() = monthlyRent + deposit + waterBill + garbageFee + electricityDeposit + otherCharges
    val monthlyTotal: Double get() = monthlyRent + waterBill + garbageBill + electricityBill + otherCharges
}

data class ApartmentFloorUIModel(
    val floorNumber: Int,
    val floorName: String,
    val houses: List<TenantHouseUIModel>
)

data class TenantBillUIModel(
    val id: String = "",
    val title: String,
    val description: String = "",
    val amount: Double,
    val dueDate: String = "",
    val status: TenantBillStatus = TenantBillStatus.PENDING,
    val type: TenantBillType = TenantBillType.OTHER,
    val billingMonth: String = "",
    val createdAt: String = "",
    val paidAt: String? = null,
    val receiptNumber: String? = null,
    val generatedBy: String = "System",
    val icon: ImageVector? = null,
    val meterNumber: String? = null,
    val units: Double? = null,
    val notes: String = ""
)

enum class TenantBillStatus {
    PAID, PENDING, OVERDUE, PARTIALLY_PAID
}

enum class TenantBillType {
    RENT, WATER, ELECTRICITY, GARBAGE, SECURITY, DEPOSIT, OTHER
}

data class TenantPaymentHistoryUIModel(
    val id: String,
    val amount: Double,
    val date: String,
    val reference: String,
    val status: TenantPaymentTransactionStatus,
    val description: String = "",
    val method: String = ""
)

enum class TenantPaymentTransactionStatus {
    SUCCESSFUL, PENDING, FAILED, REFUNDED
}

enum class TenantPaymentMethod {
    MPESA, AIRTEL_MONEY, PESAPAL, MASTERCARD, VISA, BANK, CARD
}

enum class PaymentScreenStatus {
    REVIEW, PROCESSING, SUCCESS, FAILED
}

data class TenantQuickActionUIModel(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

data class TenantPaymentUIState(
    val transactionId: String,
    val apartmentId: String,
    val apartmentName: String = "",
    val houseId: String,
    val houseNumber: String,
    val landlordName: String,
    val rent: Double,
    val deposit: Double,
    val water: Double,
    val garbage: Double,
    val serviceCharge: Double,
    val otherCharges: Double = 0.0
) {
    val total: Double get() = rent + deposit + water + garbage + serviceCharge + otherCharges
}

data class TenantPaymentReceiptUIModel(
    val receiptNumber: String,
    val transactionId: String,
    val amount: Double,
    val paymentMethod: String,
    val phoneNumber: String,
    val date: String,
    val apartmentName: String,
    val houseNumber: String,
    val floorNumber: String = "",
    val tenantName: String = "",
    val tenantPhone: String = "",
    val landlordName: String = "",
    val landlordPhone: String = "",
    val paymentPhone: String = "",
    val rentAmount: Double = 0.0,
    val waterAmount: Double = 0.0,
    val garbageAmount: Double = 0.0,
    val serviceCharge: Double = 0.0,
    val otherBills: Double = 0.0,
    val arrears: Double = 0.0,
    val totalAmount: Double = 0.0,
    val paymentTime: String = "",
    val paymentStatus: String = "SUCCESSFUL"
)

enum class MaintenanceCategory {
    PLUMBING, ELECTRICAL, WATER, DOOR_OR_WINDOW, PAINTING, GARBAGE, SECURITY, OTHER
}

enum class MaintenancePriority {
    LOW, MEDIUM, HIGH, EMERGENCY
}

enum class MaintenanceStatus {
    SUBMITTED, REVIEWING, APPROVED, ASSIGNED, IN_PROGRESS, WAITING_FOR_PARTS, COMPLETED, CANCELLED
}

data class MaintenanceRequestUIModel(
    val id: String,
    val category: MaintenanceCategory,
    val priority: MaintenancePriority,
    val location: String,
    val description: String,
    val status: MaintenanceStatus,
    val createdAt: String,
    val assignedTo: String?,
    val assignedPhone: String?,
    val attachmentUris: List<String> = emptyList(),
    val estimatedCompletion: String? = null,
    val technicianNotes: String? = null
)

data class TenantAgreementUIModel(
    val agreementId: String,
    val apartmentId: String = "",
    val agreementVersion: String,
    val apartmentName: String,
    val houseNumber: String,
    val floorNumber: String,
    val landlordId: String = "",
    val landlordName: String,
    val tenantName: String,
    val createdDate: String,
    val effectiveDate: String,
    val monthlyRent: Double,
    val deposit: Double,
    val noticePeriodDays: Int,
    val agreementContent: String,
    val agreementProofUrl: String? = null,
    val isAlreadyAccepted: Boolean = false,
    val acceptedDate: String? = null,
    val acceptedVersion: String? = null
)

data class TenantApartmentUIModel(
    val id: String,
    val name: String,
    val county: String,
    val location: String,
    val description: String,
    val availableUnits: Int,
    val totalUnits: Int,
    val startingRent: Double,
    val highestRent: Double,
    val rating: Double,
    val verified: Boolean,
    val distanceKm: Double,
    val houseTypes: List<String>,
    val amenities: List<String> = emptyList(),
    val images: List<String> = emptyList(),
    val landlordName: String = "",
    val landlordPhone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val totalFloors: Int = 1,
    val totalRevenue: String = ""
)

enum class ChatRoom {
    LANDLORD, APARTMENT_GROUP, DIRECT
}

enum class MessageStatus {
    SENDING, SENT, DELIVERED, READ, FAILED
}

enum class AttachmentType {
    NONE, IMAGE, FILE
}

data class TenantMessageUIModel(
    val id: String,
    val room: ChatRoom,
    val senderId: String,
    val senderName: String,
    val text: String,
    val time: String,
    val status: MessageStatus = MessageStatus.SENT,
    val attachmentType: AttachmentType = AttachmentType.NONE,
    val attachmentUrl: String? = null
)

enum class NotificationType {
    RENT, PAYMENT, BILL, MAINTENANCE, AGREEMENT, LANDLORD_MESSAGE, APARTMENT, VACATING, EMERGENCY, SECURITY, ANNOUNCEMENT, SYSTEM
}

enum class NotificationPriority {
    NORMAL, HIGH, URGENT
}

enum class NotificationAction {
    NONE, VIEW_RENT, VIEW_PAYMENT, VIEW_BILL, VIEW_MAINTENANCE, VIEW_AGREEMENT, OPEN_CHAT, VIEW_APARTMENT, VIEW_VACATING, VIEW_EMERGENCY
}

data class TenantNotificationUIModel(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val priority: NotificationPriority = NotificationPriority.NORMAL,
    val time: String,
    val isRead: Boolean = false,
    val referenceId: String? = null,
    val action: NotificationAction = NotificationAction.NONE
)

data class UtilityUsageUIModel(
    val month: String,
    val waterUnits: Double,
    val waterCost: Double,
    val electricityUnits: Double,
    val electricityCost: Double
)

data class TenantDocumentUIModel(
    val id: String,
    val title: String,
    val type: String, // "ID", "LEASE", "RECEIPT", "OTHER"
    val url: String,
    val date: String
)

data class PropertyReviewUIModel(
    val id: String,
    val tenantName: String,
    val rating: Int,
    val comment: String,
    val date: String,
    val isVerified: Boolean = true
)

enum class TenantVacateNoticeStatus {
    DRAFT, SUBMITTED, ACKNOWLEDGED, INSPECTION_SCHEDULED, APPROVED, VACATED, CANCELLED, REJECTED
}

enum class TenantVacateReason {
    MOVING, JOB_TRANSFER, FAMILY, FINANCIAL, PROPERTY_ISSUES, LANDLORD_ISSUES, LEASE_ENDED, OTHER
}

data class TenantVacateNoticeUIModel(
    val noticeId: String,
    val apartmentName: String,
    val houseNumber: String,
    val floorNumber: String,
    val tenantName: String,
    val tenantPhone: String,
    val landlordName: String,
    val landlordPhone: String,
    val noticeDate: String,
    val intendedVacateDate: String,
    val reason: TenantVacateReason,
    val additionalMessage: String,
    val status: TenantVacateNoticeStatus,
    val outstandingBalance: Double = 0.0
)

data class TenantVacateNoticeStatusUIModel(
    val noticeId: String,
    val apartmentName: String,
    val houseNumber: String,
    val floorNumber: String,
    val noticeDate: String,
    val intendedVacateDate: String,
    val reason: String,
    val status: TenantVacateNoticeStatus,
    val landlordName: String,
    val landlordPhone: String,
    val submittedAt: String,
    val acknowledgedAt: String? = null,
    val inspectionDate: String? = null,
    val inspectionTime: String? = null,
    val approvedAt: String? = null,
    val vacatedAt: String? = null,
    val landlordComment: String? = null,
    val outstandingBalance: Double = 0.0
)

data class TenantRentSummaryUIModel(
    val houseName: String = "My House",
    val monthlyRent: Double = 0.0,
    val billingMonth: String = "",
    val dueDate: String = "",
    val daysUntilDue: Int = 0,
    val arrears: Double = 0.0,
    val deposit: Double = 0.0
)

data class LandlordDashboardUIState(
    val businessName: String,
    val email: String,
    val totalProperties: Int,
    val totalUnits: Int,
    val totalRevenue: String,
    val occupancyRate: String,
    val recentActivities: List<LandlordActivityUIModel> = emptyList(),
    val properties: List<TenantApartmentUIModel> = emptyList(),
    val healthScore: Int = 100,
    val revenueTrend: List<Float> = emptyList(),
    val upcomingRenewals: Int = 0,
    val vacateNotices: Int = 0,
    val activeMaintenanceRequests: Int = 0,
    val pendingApplications: Int = 0,
    val maintenancePredictions: List<MaintenancePredictionUIModel> = emptyList(),
    val tenantSentiment: Double = 5.0,
    val complianceStatus: List<ComplianceUIModel> = emptyList()
)

data class MaintenancePredictionUIModel(
    val title: String,
    val property: String,
    val risk: String,
    val riskColor: String, // Hex string
    val description: String
)

data class ComplianceUIModel(
    val title: String,
    val status: String,
    val isCritical: Boolean
)

data class LandlordActivityUIModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String?,
    val time: String,
    val type: String, // e.g. "PAYMENT", "MAINTENANCE", "TENANT"
    val status: String = "SUCCESS"
)

data class LandlordBillingUIState(
    val totalOutstanding: String,
    val thisMonthCollection: String,
    val arrears: String,
    val collectedAmount: String,
    val pendingAmount: String,
    val overdueAmount: String
)
