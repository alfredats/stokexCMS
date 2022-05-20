package visa.vttp.paf.stokexCMS.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface OrderStatus {
    public static final Integer created = 10;
    public static final Integer partiallyFulfilled = 19;
    public static final Integer fulfilled = 20;
    public static final Integer cancelled = 90;
    // public static final Integer cancelledByUser = 91;
    // public static final Integer cancelledByMatchingEngine = 92;
    public static final Map<Integer, String> constToStatus = Collections.unmodifiableMap(
        new HashMap<>() {{
            put(created, "created");
            put(partiallyFulfilled, "partially fulfilled");
            put(fulfilled, "fulfilled");
            put(cancelled, "cancelled");
        }}
    );
}