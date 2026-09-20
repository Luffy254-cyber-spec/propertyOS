package com.him.landlordtenant.app.util

/**
 * STRIPE CONFIGURATION
 * 
 * IMPORTANT: In a production environment, you should never hardcode live secret keys
 * in the client-side code. They should be stored in a secure backend and 
 * used to communicate with Stripe's servers. 
 */
object PaymentKeys {
    const val STRIPE_PUBLISHABLE_KEY = "pk_live_51Ss2oLRpydVN2fU0HrcrAqjHfstrWgqRj8u9HsxrUHMBLsDLJg65vfK0gMDxZ8IY6IUk8maOseQX7B0Yd1Wfdlr200wGO7Uhek"

    // M-PESA CONFIGURATION (Public Info)
    const val MPESA_BUSINESS_SHORTCODE = "174379"
}
