/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.aurorainverter.internal.jaurlib.request;

import org.openhab.binding.aurorainverter.internal.jaurlib.modbus.MbAduRequest;
import org.openhab.binding.aurorainverter.internal.jaurlib.modbus.MbPdu;
import org.openhab.binding.aurorainverter.internal.jaurlib.modbus.MbAddress;
import org.openhab.binding.aurorainverter.internal.jaurlib.modbus.MbPduFactory;

/**
 * @author Stefano Brega (26/11/14) - Initial contribution
 * @author Gerald Heilmann (08/06/18) - adaptations for using with OpenHAB
 */
public class AuroraRequestPacket extends MbAduRequest {

    static final MbPduFactory FACTORY = new AuroraRequestFactory();

    public AuroraRequestPacket(MbAddress mb_address, MbPdu pdu) {
        super(FACTORY, mb_address, pdu);
    }

    public AuroraRequestPacket() {
        super(FACTORY);
    }
}
