package co.smartreceipts.android.model.impl.columns.receipts;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import co.smartreceipts.android.currency.PriceCurrency;
import co.smartreceipts.android.model.Price;
import co.smartreceipts.android.model.Receipt;
import co.smartreceipts.android.model.factory.PriceBuilderFactory;
import co.smartreceipts.android.model.impl.ImmutableNetPriceImpl;
import co.smartreceipts.android.model.impl.columns.AbstractColumnImpl;
import co.smartreceipts.android.sync.model.SyncState;

/**
 * Provides a column that returns the category code for a particular receipt
 */
public final class ReceiptPriceColumn extends AbstractColumnImpl<Receipt> {

    public ReceiptPriceColumn(int id, @NonNull String name, @NonNull SyncState syncState) {
        super(id, name, syncState);
    }

    public ReceiptPriceColumn(int id, @NonNull String name, @NonNull SyncState syncState, long customOrderId) {
        super(id, name, syncState, customOrderId);
    }

    @Override
    public String getValue(@NonNull Receipt receipt) {
        return receipt.getPrice().getDecimalFormattedPrice();
    }

    @Override
    @NonNull
    public String getFooter(@NonNull List<Receipt> receipts) {
        if (!receipts.isEmpty()) {
            final PriceCurrency tripCurrency = receipts.get(0).getTrip().getTripCurrency();
            final List<Price> prices = new ArrayList<>();
            for (final Receipt receipt : receipts) {
                prices.add(receipt.getPrice());
            }

            final Price netPrice = new PriceBuilderFactory().setPrices(prices, tripCurrency).build();
            if (netPrice instanceof ImmutableNetPriceImpl) {
                return ((ImmutableNetPriceImpl) netPrice).getCurrencyCodeFormattedNotExchangedPrice();
            } else {
                return netPrice.getDecimalFormattedPrice();
            }

        } else {
            return "";
        }
    }
}
