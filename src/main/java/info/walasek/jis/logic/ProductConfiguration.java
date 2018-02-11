package info.walasek.jis.logic;

/**
 * Represents the data to be used to generate the delivery sequences for a product.
 */
public class ProductConfiguration {

    private final int productType, baseQuantity, linearGrowth, fluctuation, deviation;

    public ProductConfiguration(int productType, int baseQuantity, int linearGrowth, int fluctuation, int deviation) {
        this.productType = productType;
        this.baseQuantity = baseQuantity;
        this.linearGrowth = linearGrowth;
        this.fluctuation = fluctuation;
        this.deviation = deviation;
    }

    /**
     * Generates the demand for the given product considering the expected linear growth.
     * @param day the day for which the demand should be generated
     * @param maxDays the total number of days
     * @return
     */
    public int generateDemandForDay(int day, int maxDays) {
        return baseQuantity + day * (linearGrowth * baseQuantity) / (100 * maxDays);
    }

    public int getProductType() {

        return productType;
    }

    public int getBaseQuantity() {
        return baseQuantity;
    }

    public int getLinearGrowth() {
        return linearGrowth;
    }

    public int getFluctuation() {
        return fluctuation;
    }

    public int getDeviation() {
        return deviation;
    }
}
