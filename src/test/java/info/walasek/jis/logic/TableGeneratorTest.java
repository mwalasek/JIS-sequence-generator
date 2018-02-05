package info.walasek.jis.logic;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TableGeneratorTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void generateDataTables() throws Exception {

        int[] baseCalloffQuantities = new int[] {34, 33, 33};

        new TableGenerator().generateDataTables(
                3,
                baseCalloffQuantities,     // call-off size of 100
                new int[3],
                new int[3],
                new int[3],
                0);

        File file = new File("JISModelData_var3.xls");
        assertTrue(file.exists());

        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet("Delivery Sequence Table");

        List<SequenceTableEntry> entries = new ArrayList<>();
        for (int i = 1; i < sheet.getRows(); i++) {
            Cell[] row = sheet.getRow(i);
            entries.add(new SequenceTableEntry(
                    Integer.valueOf(row[0].getContents()),
                    Integer.valueOf(row[1].getContents()),
                    Integer.valueOf(row[2].getContents())
            ));
        }

        // assert that the sum of product quantities equals 100 in every call-off:
        entries.stream()
                .collect(Collectors.groupingBy(SequenceTableEntry::getCalloffId,
                        Collectors.summingInt(SequenceTableEntry::getProductUnits)))
                .forEach((calloffId, calloffSize) -> assertTrue(calloffSize == 100));

        workbook.close();
        file.delete();
    }

    class SequenceTableEntry {
        final int calloffId, productType, productUnits;

        public SequenceTableEntry(int calloffId, int productType, int productUnits) {
            this.calloffId = calloffId;
            this.productType = productType;
            this.productUnits = productUnits;
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

    @Test
    public void linearDemandCurve() throws Exception {

    }

    @Test
    public void contains() throws Exception {

    }

    @Test
    public void isZeroArray() throws Exception {

    }

    @Test
    public void arrayAppend() throws Exception {

    }

    @Test
    public void arrayCopyWithDeviation() throws Exception {

    }

    @Test
    public void getRandomWithExclusion() throws Exception {

    }

}