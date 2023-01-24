package com.bink.payments.model.wallet

/**
 * Configuration for the Bink API.
 *
 * @param productionLoyaltyPlanId: This corresponds to the loyalty plan ID for Prod.
 * @param devLoyaltyPlanId: This corresponds to the loyalty plan ID for Staging.
 * @param credentialType: This will change whether the card will be POST or PUT.
 */
data class Configuration(
    val productionLoyaltyPlanId: Int,
    val devLoyaltyPlanId: Int,
    val credentialType: CredentialType,
)

/**
 * CredentialType.
 *
 * @property ADD: POST
 * @property AUTH: PUT
 */
enum class CredentialType {
    ADD,
    AUTH
}