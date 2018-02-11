package info.walasek.jis.logic;

import java.util.List;

/**
 * Represents the characteristics of the generated demand for one or more products.
 */
public class DemandConfiguration {

    private final List<ProductConfiguration> products;

    private final int maxTypeAggregateSize;

    private final int days;

    public DemandConfiguration(List<ProductConfiguration> products, int maxTypeAggregateSize, int days) {
        this.products = products;
        this.maxTypeAggregateSize = maxTypeAggregateSize;
        this.days = days;
    }

    public List<ProductConfiguration> getProducts() {
        return products;
    }

    public int getMaxTypeAggregateSize() {
        return maxTypeAggregateSize;
    }

    public int getDays() {
        return days;
    }
}
