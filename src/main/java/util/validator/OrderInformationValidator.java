package util.validator;

public class OrderInformationValidator {

    private static final OrderInformationValidator INSTANCE = new OrderInformationValidator();

    public static OrderInformationValidator getInstance() {
        return INSTANCE;
    }

    private OrderInformationValidator() {}

    private static final String LOCALITY_FORMAT = ".{2,50}";
    private static final String STREET_FORMAT = ".{2,100}";
    private static final String HOUSE_FORMAT = "[1-9][0-9]{0,2}";
    private static final String FLAT_FORMAT = "[1-9][0-9]{0,3}";

    public boolean validate(String locality, String street, String house, String flat) {
        return locality.matches(LOCALITY_FORMAT) && street.matches(STREET_FORMAT) &&
                house.matches(HOUSE_FORMAT) && flat.matches(FLAT_FORMAT);
    }
}