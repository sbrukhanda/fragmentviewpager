/*
 * Copyright (C) 2016 Serhiy Brukhanda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sbrukhanda.fragmentviewpager.utils;

import android.util.Log;

/**
 * Provides utility logging functions with the possibility to partially or fully enable/disable logs.
 *
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class Logger
{
    // =============================================================================================
    // Fields
    // =============================================================================================

    /**
     * Indicates whether logs should be printed or not.
     */
    private static boolean sLogsEnabled = true;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    private Logger() {
        // Prevent class instantiation
    }

    /**
     * Enable logs printing to console.
     */
    public static void enableLogs() {
        sLogsEnabled = true;
    }

    /**
     * Disable logs printing to console.
     */
    public static void disableLogs() {
        sLogsEnabled = false;
    }

    /**
     * Logs the specified {@code DEBUG} message with the specified {@code TAG}.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The body of the message to log.
     */
    public static void d(String tag, String message) {
        d(tag, message, true);
    }

    /**
     * Logs the specified {@code DEBUG} message with the specified {@code TAG} if the specified
     * flag is {@code true}, else the message is ignored.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     * @param shouldPrintLogsLocal A flag indicating whether the message should be printed to
     * console or not.
     */
    public static void d(String tag, String message, boolean shouldPrintLogsLocal) {
        if (sLogsEnabled && shouldPrintLogsLocal) Log.d(tag, message);
    }

    /**
     * Logs the specified {@code ERROR} message with the specified {@code TAG}.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     */
    public static void e(String tag, String message) {
        e(tag, message, true);
    }

    /**
     * Logs the specified {@code ERROR} message with the specified {@code TAG} if the specified flag
     * is {@code true}, else the message is ignored.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     * @param shouldPrintLogsLocal A flag indicating whether the message should be printed to
     * console or not.
     */
    public static void e(String tag, String message, boolean shouldPrintLogsLocal) {
        if (sLogsEnabled && shouldPrintLogsLocal) Log.e(tag, message);
    }

    /**
     * Logs the specified {@code WARNING} message with the specified {@code TAG}.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     */
    public static void w(String tag, String message) {
        w(tag, message, true);
    }

    /**
     * Logs the specified {@code WARNING} message with the specified {@code TAG} if the specified
     * flag is {@code true}, else the message is ignored.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     * @param shouldPrintLogsLocal A flag indicating whether the message should be printed to
     * console or not.
     */
    public static void w(String tag, String message, boolean shouldPrintLogsLocal) {
        if (sLogsEnabled && shouldPrintLogsLocal) Log.w(tag, message);
    }

    /**
     * Logs the specified {@code INFORMATION} message with the specified {@code TAG}.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     */
    public static void i(String tag, String message) {
        i(tag, message, true);
    }

    /**
     * Logs the specified {@code INFORMATION} message with the specified {@code TAG} if the
     * specified flag is {@code true}, else the message is ignored.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     * @param shouldPrintLogsLocal A flag indicating whether the message should be printed to
     * console or not.
     */
    public static void i(String tag, String message, boolean shouldPrintLogsLocal) {
        if (sLogsEnabled && shouldPrintLogsLocal) Log.i(tag, message);
    }

    /**
     * Logs the specified {@code VERBOSE} message with the specified {@code TAG}.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     */
    public static void v(String tag, String message) {
        v(tag, message, true);
    }

    /**
     * Logs the specified {@code VERBOSE} message with the specified {@code TAG} if the specified
     * flag is {@code true}, else the message is ignored.
     *
     * @param tag The {@code TAG} of the message to log.
     * @param message The message to log.
     * @param shouldPrintLogsLocal A flag indicating whether the message should be printed to
     * console or not.
     */
    public static void v(String tag, String message, boolean shouldPrintLogsLocal) {
        if (sLogsEnabled && shouldPrintLogsLocal) Log.v(tag, message);
    }

    /**
     * Logs the specified {@code Throwable} with the specified {@code TAG}.
     *
     * @param tag The {@code TAG} of the {@code Throwable} to log.
     * @param throwable The {@code Throwable} to log.
     */
    public static void log(String tag, Throwable throwable) {
        log(tag, throwable, true);
    }

    /**
     * Logs the specified {@code Throwable} with the specified {@code TAG} if the specified flag is
     * {@code true}, else the {@code Throwable} is ignored.
     *
     * @param tag The {@code TAG} of the {@code Throwable} to log.
     * @param throwable The {@code Throwable} to log.
     * @param shouldPrintLogsLocal A flag indicating whether the {@code Exception} should be printed
     * to console or not.
     */
    public static void log(String tag, Throwable throwable, boolean shouldPrintLogsLocal) {
        log(tag, ((throwable != null) ? throwable.getMessage() : ""), throwable, shouldPrintLogsLocal);
    }

    /**
     * Logs the specified {@code Throwable} with the specified {@code TAG} and message.
     *
     * @param tag The {@code TAG} of the {@code Throwable} to log.
     * @param message The message to log.
     * @param throwable The {@code Throwable} to log.
     */
    public static void log(String tag, String message, Throwable throwable) {
        log(tag, message, throwable, true);
    }

    /**
     * Logs the specified {@code Throwable} with the specified {@code TAG} and message if the
     * specified flag is {@code true}, else the {@code Throwable} is ignored.
     *
     * @param tag The {@code TAG} of the {@code Throwable} to log.
     * @param message The message to log.
     * @param throwable The {@code Throwable} to log.
     * @param shouldPrintLogsLocal A flag indicating whether the {@code Throwable} should be printed
     * to console or not.
     */
    public static void log(String tag, String message, Throwable throwable, boolean shouldPrintLogsLocal) {
        if ((throwable != null) && sLogsEnabled && shouldPrintLogsLocal) {
            Log.e(tag, message, throwable);
        }
    }
}