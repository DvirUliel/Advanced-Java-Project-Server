// src/algoClient/model/analysis/DataMode.java
package algoClient.enums;

/**
 * Specifies the mode of data provided for subarray analysis.
 */
public enum DataMode {
    /** User provided an array of daily changes. */
    DAILY_CHANGES,
    /** User provided an array of closing prices. */
    CLOSING_PRICES
}