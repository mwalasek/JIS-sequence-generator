package info.walasek.jis.logic;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.util.*;


public class TableGenerator {

    public final static String OUTPUT_FILE_NAME = "JISModelData";
    public final static String FILE_ENDING = ".xls";

    //Define date format etc.
    DateFormat previewDateFormat = new DateFormat("dd.mm.yyyy");
    WritableCellFormat wPreviewDateFormat = new WritableCellFormat(previewDateFormat);
    DateFormat detailedDateFormat = new DateFormat("dd.mm.yyyy hh:mm");
    WritableCellFormat wDetailedDateFormat = new WritableCellFormat(detailedDateFormat);

    WritableSheet deliverySequenceTable;

    /**
     * TEST - pass the pre-generated assembly sequence table over to SIMIO via table data.
     */
    WritableSheet assemblySequenceTable;

    int sequenceTableIndex = 1, orderIndex = 1, assemblySequenceTableIndex = 1;

    public void generateDataTables(DemandConfiguration demandConfig) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(OUTPUT_FILE_NAME
                    + "_var" + demandConfig.getProducts().size() + FILE_ENDING));
            WritableSheet previewTable = workbook.createSheet("Calloff Preview", 0);
            WritableSheet eventTable = workbook.createSheet("Calloff Event Table", 1);

            //Label preview table columns:
            Label DayLabel = new Label(0, 0, "Day");
            previewTable.addCell(DayLabel);

            for (int i = 0; i < demandConfig.getProducts().size(); i++) {
                Label DemandTypeLabel = new Label(i + 1, 0, "Type " + i);
                previewTable.addCell(DemandTypeLabel);
            }

            //Label event table columns:
            Label IDLabel = new Label(0, 0, "CalloffID");
            eventTable.addCell(IDLabel);

            Label DTLabel = new Label(1, 0, "DeliveryTime");
            eventTable.addCell(DTLabel);

            Label OTLabel = new Label(2, 0, "ReceiptTime");
            eventTable.addCell(OTLabel);

            deliverySequenceTable = workbook.createSheet("Delivery Sequence Table", 2);
            assemblySequenceTable = workbook.createSheet("Assembly Sequence Table", 3);

            //Label sequence table columns:
            Label SequenceIDLabel = new Label(0, 0, "CalloffID");
            deliverySequenceTable.addCell(SequenceIDLabel);
            Label SequenceTypeLabel = new Label(1, 0, "Type");
            deliverySequenceTable.addCell(SequenceTypeLabel);

            Label SequenceQuantityLabel = new Label(2, 0, "Number");
            deliverySequenceTable.addCell(SequenceQuantityLabel);

            Label SequenceDTLabel = new Label(3, 0, "DeliveryTime");
            deliverySequenceTable.addCell(SequenceDTLabel);

            deliverySequenceTable.addCell(new Label(4, 0, "RowIndex"));

            assemblySequenceTable.addCell(new Label(0, 0, "CalloffID"));
            assemblySequenceTable.addCell(new Label(1, 0, "Type"));
            assemblySequenceTable.addCell(new Label(2, 0, "Number"));

            Calendar deliveryCalendar = GregorianCalendar.getInstance();
            deliveryCalendar.set(2018, Calendar.JANUARY, 1, 8, 30, 0);
            Calendar orderCalendar = GregorianCalendar.getInstance();

            int prevTableIndex = 1, dayCount = 0;

        	/*
             * Generate the preview table:
        	 */
            while (dayCount < demandConfig.getDays()) {

                DateTime previewDTCell = new DateTime(0, prevTableIndex, deliveryCalendar.getTime(), wPreviewDateFormat);
                previewTable.addCell(previewDTCell);

                int[] previewQuantitiesDaily = new int[demandConfig.getProducts().size()];
                for (int i = 0; i < previewQuantitiesDaily.length; i++) previewQuantitiesDaily[i] = 0;

        	/*
        	 * Generate calloff events:
        	 */
                while (true) {

                    //Again, consider only working days:
                    if (deliveryCalendar.getTime().getDay() <= 5 && deliveryCalendar.getTime().getDay() >= 1) {

                        /*
                         * Calculate the preview quantities
                         */
                        final int day = dayCount;
                        int[] previewQuantities = demandConfig.getProducts().stream()
                                .mapToInt(p -> p.generateDemandForDay(day, demandConfig.getDays()))
                                .toArray();

                        int[] fluctuation = demandConfig.getProducts().stream()
                                .mapToInt(ProductConfiguration::getFluctuation)
                                .toArray();
                        previewQuantities = arrayCopyWithDeviation(previewQuantities, fluctuation);

                        //Number the calloffs consecutively:
                        Number calloffId = new Number(0, orderIndex, orderIndex);
                        eventTable.addCell(calloffId);

                        DateTime deliveryDateCell = new DateTime(1, orderIndex, deliveryCalendar.getTime(), wDetailedDateFormat);
                        eventTable.addCell(deliveryDateCell);

                        orderCalendar.setTime(deliveryCalendar.getTime());
                        orderCalendar.add(Calendar.HOUR, -4);

                        DateTime orderDateCell = new DateTime(2, orderIndex, orderCalendar.getTime(), wDetailedDateFormat);
                        eventTable.addCell(orderDateCell);
        			
        			/*
        			 * Account for the deviation from the calloff preview:
        			 */
                        int[] devQuantities = arrayCopyWithDeviation(previewQuantities, demandConfig.getProducts().stream().mapToInt(ProductConfiguration::getDeviation).toArray());
        			
        			/*
        			 * Generate exact call-off sequence:
        			 */
                        int calloffSize = demandConfig.getProducts().stream().mapToInt(ProductConfiguration::getDeviation).sum();
                        generateSequenceCalledOff(workbook, deliveryCalendar, devQuantities, calloffSize, demandConfig.getMaxTypeAggregateSize());

                        previewQuantitiesDaily = arrayAdd(previewQuantitiesDaily, previewQuantities);

                        orderIndex++;
                    }
                    deliveryCalendar.add(Calendar.HOUR, 2);

                    //reached end of the day?
                    if (deliveryCalendar.getTime().getHours() > 17) {

                        deliveryCalendar.add(Calendar.DAY_OF_YEAR, 1);
                        dayCount++;
                        deliveryCalendar.set(Calendar.HOUR_OF_DAY, 8);

                        //add the summed up daily quantities to the preview table:
                        for (int i = 0; i < previewQuantitiesDaily.length; i++) {
                            Number variantQuantity = new Number(i + 1, prevTableIndex, previewQuantitiesDaily[i]);
                            previewTable.addCell(variantQuantity);
                        }

                        break;
                    }

                }

                prevTableIndex++;

            }
            workbook.write();
            workbook.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateSequenceCalledOff(WritableWorkbook workbook, Calendar deliveryCalendar, int[] remainingQuantities,
                                           int calloffSize, int maxTypeAggregateSize) {

        Random random = new Random();

        int[] tempQts = new int[remainingQuantities.length];
        for (int k = 0; k < tempQts.length; k++) {
            tempQts[k] = 0;
        }

        int maxSeqLength = 0;
        for (int k = 0; k < remainingQuantities.length; k++) {
            maxSeqLength += remainingQuantities[k];
        }
        if (calloffSize > 0 && calloffSize < maxSeqLength) maxSeqLength = calloffSize;

        int seqLength = 0;

        int previousProductType = 1000;
        int nextProductType = -11;

        for (int j = 0; seqLength < maxSeqLength; j++) {

            int productType;
            int productQuantity = 0;

            int[] remainingProductsByType = getRemainingProductsByType(remainingQuantities);
            int[] excludeProducts = {};

            for (int k = 0; k < remainingQuantities.length; k++) {
                if (remainingQuantities[k] <= 0) {
                    excludeProducts = arrayAppend(excludeProducts, k);
                }
            }

            if (maxTypeAggregateSize > 0) {

                //Generate product type
                if (j == 0) {
                    productType = random.nextInt(remainingQuantities.length);
                } else {
                    productType = getRandomWithExclusion(random, 0, remainingQuantities.length - 1,
                            arrayAppend(excludeProducts, previousProductType));
                }
                previousProductType = productType;

                //Generate product quantity
                if (excludeProducts.length >= remainingQuantities.length - 1) {
                    //if last remaining product, just exhaust all remaining quantity:
                    productQuantity = Math.min(remainingQuantities[productType], maxSeqLength - seqLength);
                } else
                    productQuantity
                            = getRandomWithExclusion(random, 1,
                            Math.min(Math.min(remainingQuantities[productType], maxTypeAggregateSize),
                                    maxSeqLength - seqLength), new int[]{});
                //Update remaining product quantities:
                remainingQuantities[productType] -= productQuantity;
                seqLength += productQuantity;

                if (remainingQuantities[productType] <= 0 && !contains(excludeProducts, productType)) {
                    excludeProducts = arrayAppend(excludeProducts, productType);
                }

            } else {
                if (nextProductType >= 0)
                    productType = nextProductType;
                else
                    productType = remainingProductsByType[random.nextInt(remainingProductsByType.length)];

                do {
                    productQuantity++;
                    seqLength++;
                    tempQts[productType]++;
                    remainingQuantities[productType]--;
                    remainingProductsByType = getRemainingProductsByType(remainingQuantities);
                    if (remainingProductsByType.length <= 0)
                        break;
                    nextProductType = remainingProductsByType[random.nextInt(remainingProductsByType.length)];

                } while (nextProductType == productType && seqLength < maxSeqLength);
            }

            //Write data to sequence table:
            try {
                Number sequenceOrderId = new Number(0, sequenceTableIndex, orderIndex);
                deliverySequenceTable.addCell(sequenceOrderId);

                Number sequenceType = new Number(1, sequenceTableIndex, productType);
                deliverySequenceTable.addCell(sequenceType);

                Number sequenceQuantity = new Number(2, sequenceTableIndex, productQuantity);
                deliverySequenceTable.addCell(sequenceQuantity);

                DateTime sequenceDTCell = new DateTime(3, sequenceTableIndex, deliveryCalendar.getTime(), wDetailedDateFormat);
                deliverySequenceTable.addCell(sequenceDTCell);

                deliverySequenceTable.addCell(new Number(4, sequenceTableIndex, sequenceTableIndex));

                if (tempQts[productType] >= 3 || isZeroArray(remainingQuantities)) {
                    assemblySequenceTable.addCell(new Number(0, assemblySequenceTableIndex, orderIndex));
                    assemblySequenceTable.addCell(new Number(1, assemblySequenceTableIndex, productType));
                    assemblySequenceTable.addCell(new Number(2, assemblySequenceTableIndex, tempQts[productType]));
                    assemblySequenceTable.addCell(new DateTime(3, assemblySequenceTableIndex, deliveryCalendar.getTime(), wDetailedDateFormat));
                    tempQts[productType] = 0;
                    assemblySequenceTableIndex++;
                }

            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            sequenceTableIndex++;
        }
    }

    public static boolean contains(final int[] array, final int elem) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == elem) {
                return true;
            }
        }
        return false;
    }

    public static boolean isZeroArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] > 0) {
                return false;
            }
        }
        return true;
    }

    public static int[] arrayAppend(int[] array, int newElement) {
        int[] newArray = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            //return the same array if element already contained:
            if (array[i] == newElement)
                return array;

            newArray[i] = array[i];
        }
        newArray[array.length] = newElement;
        return newArray;
    }

    /**
     * @param array
     * @param maxDev
     * @return
     */
    public static int[] arrayCopyWithDeviation(int[] array, int[] maxDev) {
        int[] newArray = new int[array.length];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            Double maxD = (double) maxDev[i];
            int absoluteMaxDev = (int) Math.ceil((array[i] * maxD) / 100);
            int deviation = getRandomWithExclusion(random, -absoluteMaxDev, absoluteMaxDev, new int[]{});

            newArray[i] = array[i] + deviation;
        }
        return newArray;
    }

    public static int getRandomWithExclusion(Random rnd, int start, int end, int[] exclude) {
        for (int i = 0; i < exclude.length; i++) {
        }

        if (end - start + 1 - exclude.length <= 0) {
            return start;
        }

        int random = start + rnd.nextInt(end - start + 1 - exclude.length);
        for (int ex : exclude) {
            if (random < ex) {
                break;
            }
            random++;
        }
        return random;
    }

    private static int[] getRemainingProductsByType(int[] remainingQuantities) {
        int length = 0;
        for (int productType = 0; productType < remainingQuantities.length; productType++) {
            length += remainingQuantities[productType];
        }
        int[] productsByType = new int[length];
        int index = 0;
        for (int productType = 0; productType < remainingQuantities.length; productType++) {
            if (remainingQuantities[productType] <= 0)
                continue;
            for (int j = 0; j < remainingQuantities[productType]; j++) {
                productsByType[index + j] = productType;
            }
            index += remainingQuantities[productType];
        }
        return productsByType;
    }

    private static int[] arrayAdd(int[] array1, int[] array2) {
        int[] array3 = new int[array1.length];
        for (int i = 0; i < array1.length; i++) {
            array3[i] = array1[i] + array2[i];
        }
        return array3;
    }
}
