package co.smartreceipts.android.model.impl.columns.receipts

import android.content.Context

import co.smartreceipts.android.model.Price
import co.smartreceipts.android.model.Receipt
import co.smartreceipts.android.model.factory.PriceBuilderFactory
import co.smartreceipts.core.sync.model.SyncState
import java.util.*

/**
 * Converts the [co.smartreceipts.android.model.Receipt.getTax] plus [co.smartreceipts.android.model.Receipt.getTax2] based on the current exchange rate
 */
class ReceiptExchangedTaxColumn(
    id: Int, syncState: SyncState,
    localizedContext: Context, customOrderId: Long, uuid: UUID
) : AbstractExchangedPriceColumn(
    id,
    ReceiptColumnDefinitions.ActualDefinition.TAX_EXCHANGED,
    syncState,
    localizedContext,
    customOrderId,
    uuid
) {

    override fun getPrice(receipt: Receipt): Price =
        PriceBuilderFactory(receipt.tax).setPrice(receipt.tax.price.add(receipt.tax2.price)).setCurrency(receipt.trip.tripCurrency).build()
}
