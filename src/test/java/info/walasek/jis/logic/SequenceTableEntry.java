package info.walasek.jis.logic;

import jxl.Cell;

public class SequenceTableEntry {

    private final int calloffId, productType, productUnits;

    public SequenceTableEntry(int calloffId, int productType, int productUnits) {
        this.calloffId = calloffId;
        this.productType = productType;
        this.productUnits = productUnits;
    }

    public static SequenceTableEntry fromTableRow(Cell[] row) {
        return new SequenceTableEntry(
                Integer.valueOf(row[0].getContents()),
                Integer.valueOf(row[1].getContents()),
                Integer.valueOf(row[2].getContents())
        );
    }

    public int getCalloffId() {
        return calloffId;
    }


    public int getProductType() {
        return productType;
    }


    public int getProductUnits() {
        return productUnits;
    }

}
