package info.walasek.jis.logic;

/**
 * Represents the data to be used to generate the delivery sequences for a product.
 */
public class ProductConfiguration {

    private int productType, baseQuantity, linearGrowth, fluctuation, deviation;

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

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getBaseQuantity() {
        return baseQuantity;
    }

    public void setBaseQuantity(int baseQuantity) {
        this.baseQuantity = baseQuantity;
    }

    public int getLinearGrowth() {
        return linearGrowth;
    }

    public void setLinearGrowth(int linearGrowth) {
        this.linearGrowth = linearGrowth;
    }

    public int getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(int fluctuation) {
        this.fluctuation = fluctuation;
    }

    public int getDeviation() {
        return deviation;
    }

    public void setDeviation(int deviation) {
        this.deviation = deviation;
    }
}
