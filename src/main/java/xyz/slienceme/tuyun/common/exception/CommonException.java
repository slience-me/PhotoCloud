/**
 * @title CommonException
 * @description ihrm_parent
 * @author slience_me
 * @version 1.0.0
 * @since 2023/1/2 21:49
 */
package xyz.slienceme.tuyun.common.exception;

import lombok.Getter;
import xyz.slienceme.tuyun.common.entity.ResponseCode;

@Getter
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private ResponseCode code = ResponseCode.SERVER_ERROR;

    public CommonException() {
    }

    public CommonException(ResponseCode resultCode) {
        super(resultCode.getDesc());
        this.code = resultCode;
    }
}