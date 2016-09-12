package com.dzs.projectframe.interf;

import java.util.Map;

/**
 * 网络返回接口
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-1 下午7:37:47
 */
public interface OnDataReturnListener {
    void onDateReturn(String id, Map<String, Object> result);
}
