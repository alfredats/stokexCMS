package visa.vttp.paf.stokexCMS.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface OrderType {
    public static final Integer bid = 1;
    public static final Integer ask = 2;
    public static final Integer cancel = 99;

    public static final Map<Integer,String> constToType = Collections.unmodifiableMap(
        new HashMap<>() {{
            put(bid, "bid");
            put(ask, "ask");
            put(cancel, "cancel");
        }}
    );
}
