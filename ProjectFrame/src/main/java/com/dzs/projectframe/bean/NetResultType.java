package com.dzs.projectframe.bean;

import com.dzs.projectframe.R;
import com.dzs.projectframe.base.ProjectContext;

public enum NetResultType {
    NET_CONNECT_SUCCESS(ProjectContext.appContext.getString(R.string.NetConnectSuccess)),
    NET_CONNECT_FAIL(ProjectContext.resources.getString(R.string.NetConnectFail)),
    NET_NOT_CONNECT(ProjectContext.resources.getString(R.string.NetNotConnectFail)),
    NET_PARSE_FAIL(ProjectContext.resources.getString(R.string.NetParseFail));

    private String message;

    NetResultType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
