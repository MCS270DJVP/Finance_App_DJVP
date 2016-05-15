package com.example.dennis.mutualfund.database;

public class FundDBSchema {
    public static final class FundTable {
        public static final String NAME = "funds";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String STOCKPRICE = "stockPrice";
            public static final String TICKER = "ticker";
            public static final String WEIGHT = "weight";
            public static final String HPRICES ="hprices";
            public static final String TIME = "time";
        }
    }
}
