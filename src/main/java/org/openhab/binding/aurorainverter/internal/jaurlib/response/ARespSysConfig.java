/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.aurorainverter.internal.jaurlib.response;

/**
 * @author Stefano Brega (09/12/14) - Initial contribution
 * @author Gerald Heilmann (08/06/18) - adaptations for using with OpenHAB
 */
public class ARespSysConfig extends AuroraResponse {
    public void setConfigCode(int code) {
        setParam1((char) code);
    }

    public int getConfigCode() {
        return getParam1();
    }

    @Override
    public String getValue() {
        return String.valueOf(getConfigCode());
    }
}
