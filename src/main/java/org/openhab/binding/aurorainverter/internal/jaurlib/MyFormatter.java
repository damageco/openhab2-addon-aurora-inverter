/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.aurorainverter.internal.jaurlib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * @author Stefano Brega (07/12/14) - Initial contribution
 * @author Gerald Heilmann (08/06/18) - adaptations for using with OpenHAB
 */
public class MyFormatter extends SimpleFormatter {
    @Override
    public synchronized String format(LogRecord record) {
        Date date = new Date(record.getMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y.M.d HH:mm:ss.SSS");

        String str = simpleDateFormat.format(date);
        StringBuilder builder = new StringBuilder();
        builder.append(str).append(" ").append(record.getThreadID()).append(" ").append(record.getLevel()).append(" ")
                .append(record.getSourceClassName()).append(" ").append(record.getSourceMethodName()).append(" \"")
                .append(record.getMessage()).append("\"\n");
        return builder.toString();
    }
}
