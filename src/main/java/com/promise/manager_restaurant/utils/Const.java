package com.promise.manager_restaurant.utils;

public class Const {

    public final static class SEND_MAIL_SUBJECT {
        public final static String OTP_VERIFY = "OTP_VERIFY";
    }

    public final static class TEMPLATE_FILE_NAME {
        public final static String OTP_VERIFY = "OTP_VERIFY";
    }

    public  static enum ORDER_STATUS {
        MOI_LEN_DON("1"),
        DANG_CHUAN_BI("2"),
        DANG_SHIP("3"),
        HOAN_THANH("4"),
        TRA_HANG("5"),
        ;

        private String id;
        private ORDER_STATUS(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
