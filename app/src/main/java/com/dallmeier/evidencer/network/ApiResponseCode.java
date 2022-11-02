package com.dallmeier.evidencer.network;

/**
 *
 *
 */
public interface ApiResponseCode {
    int CANNOT_CONNECT_TO_SERVER = 0;
    int SUCCESS = 200;
    int SUCCESS_CREATE = 201;
    int SUCCESS_ACCEPT = 202;
    int BAD_REQUEST = 400;
    int METHOD_NOT_ALLOWED = 405;
    int EMAIL_EXISTS = 410;
    int USERNAME_EXISTS = 411;
    int USERNAME_OR_PASSWORD_WRONG = 412;
    int REQUEST_EXISTS = 413;
    int REQUEST_NOT_EXISTS = 414;
    int CODE_NOT_MATCH = 415;
    int INVALID_TOKEN = 416;
    int PASSWORD_NOT_MATCH = 417;
    int SYSTEM_ERROR = 500;
}
