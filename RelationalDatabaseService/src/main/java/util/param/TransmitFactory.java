package util.param;

import com.google.common.base.Optional;

/**
 * Created by Administrator on 2014/9/24.
 */
public interface TransmitFactory {
     Optional<Object> toObject(String[] strings);
}
