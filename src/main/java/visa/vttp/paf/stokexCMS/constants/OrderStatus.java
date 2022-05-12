package visa.vttp.paf.stokexCMS.constants;

public interface OrderStatus {
    public static final Integer created = 10;
    public static final Integer partiallyFulfilled = 19;
    public static final Integer fulfilled = 20;
    public static final Integer cancelled = 90;
    public static final Integer cancelledByUser = 91;
    public static final Integer cancelledByMatchingEngine = 92;
}