/*
 *  Copyright 2010 Kevin Gaudin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package b.laixuantam.myaarlibrary.widgets.ACRA.sender;

import android.content.Context;

import b.laixuantam.myaarlibrary.widgets.ACRA.collector.CrashReportData;

/**
 * A simple interface for defining various crash report senders. You can reuse
 * {@link HttpSender} to send reports to your custom server-side report
 * collection script even if you expect (or prefer) specific names for each
 * report field as {@link HttpSender#send(Context, CrashReportData)}
 * can take a {@code Map<ReportField, String>} as an input to convert each field name to
 * your preferred POST parameter name.
 * 
 * @author Kevin Gaudin
 * 
 */
public interface ReportSender {
    /**
     * Send crash report data. You don't have to take care of managing Threads,
     * just implement what is necessary to handle the data. ACRA will use a
     * specific Thread (not the UI Thread) to run your sender.
     *
     *
     *
     * @param context       Android Context in which to send the crash report.
     * @param errorContent
     *            Stores key/value pairs for each report field. A report field
     *            is identified by a {@link org.acra.ReportField} enum value.
     * @throws ReportSenderException
     *             If anything goes fatally wrong during the handling of crash
     *             data, you can (should) throw a {@link ReportSenderException}
     *             with a custom message.
     */
    public void send(Context context, CrashReportData errorContent) throws ReportSenderException;
}
