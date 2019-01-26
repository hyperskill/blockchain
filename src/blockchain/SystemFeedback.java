package blockchain;

import java.util.Optional;
import java.util.function.BiFunction;

public interface SystemFeedback<T> extends BiFunction<Integer, String, Optional<T>> {

}
