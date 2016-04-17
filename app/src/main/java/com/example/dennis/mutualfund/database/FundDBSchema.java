package com.example.dennis.mutualfund.database;

/**
 * Created by macowner on 4/16/16.
 */
public class FundDBSchema {
    public static final class FundTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TICKER = "ticker";
            public static final String WEIGHT = "weight";
        }
    }
}