package info.walasek.jis.logic;

import jxl.Sheet;
import jxl.Workbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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

        // Read entry objects from table row:
        List<SequenceTableEntry> entries = IntStream.range(1, sheet.getRows()).boxed()
                .map(rowId -> SequenceTableEntry.fromTableRow(sheet.getRow(rowId)))
                .collect(Collectors.toList());

        // For every call-off and every product assert that the generated total quantity matches the input value:
        entries.stream()
                .collect(Collectors.groupingBy(SequenceTableEntry::getCalloffId))
                .forEach((calloffId, entriesForCalloff) -> {
                    entriesForCalloff.stream()
                            .collect(Collectors.groupingBy(SequenceTableEntry::getProductType,
                                    Collectors.summingInt(SequenceTableEntry::getProductUnits)))
                            .forEach((productType, unitsInCalloff) ->
                                assertThat("Product quantity mismatch with input value",
                                        unitsInCalloff, is(baseCalloffQuantities[productType])));
                });

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