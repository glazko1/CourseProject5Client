package util.validator;

public class ProductInformationValidator {

    private static final ProductInformationValidator INSTANCE = new ProductInformationValidator();

    public static ProductInformationValidator getInstance() {
        return INSTANCE;
    }

    private ProductInformationValidator() {}

    private static final String PRODUCT_NAME_FORMAT = ".{2,50}";
    private static final String PRICE_FORMAT = "[1-9][0-9]{0,3}([.][0-9]{1,2})?";
    private static final String FILE_PATH_FORMAT = ".+\\.(jpg|png|jpeg|bmp|gif)";

    public boolean validate(String productName, String price, String filePath) {
        return productName.matches(PRODUCT_NAME_FORMAT) &&
                price.matches(PRICE_FORMAT) &&
                filePath.matches(FILE_PATH_FORMAT);
    }
}