package info.walasek.jis.logic;

import jxl.Sheet;
import jxl.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
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

        List<ProductConfiguration> products = new ArrayList<>();
        products.add(new ProductConfiguration(0, 44, 0, 0, 0));
        products.add(new ProductConfiguration(1, 35, 0, 0, 0));
        products.add(new ProductConfiguration(2, 21, 0, 0, 0));
        products.add(new ProductConfiguration(3, 0, 0, 0, 0));

        new TableGenerator().generateDataTables(products, 0);

        File file = new File("JISModelData_var4.xls");
        assertTrue(file.exists());

        Workbook workbook = Workbook.getWorkbook(file);
        Sheet sheet = workbook.getSheet("Delivery Sequence Table");

        // Read entry objects from table row:
        List<SequenceTableEntry> entries = IntStream.range(1, sheet.getRows()).boxed()
                .map(rowId -> SequenceTableEntry.fromTableRow(sheet.getRow(rowId)))
                .collect(Collectors.toList());

        // For every call-off and every product assert that the generated total quantity matches the input value:
        entries.stream()
                .collect(Collectors.groupingBy(SequenceTableEntry::getCalloffId))
                .forEach((calloffId, entriesForCalloff) -> entriesForCalloff.stream()
                        .collect(Collectors.groupingBy(SequenceTableEntry::getProductType,
                                Collectors.summingInt(SequenceTableEntry::getProductUnits)))
                        .forEach((productType, unitsInCalloff) ->
                            assertThat("Product quantity mismatch with input value",
                                    unitsInCalloff, is(products.get(productType).getBaseQuantity()))));

        workbook.close();
        file.delete();
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