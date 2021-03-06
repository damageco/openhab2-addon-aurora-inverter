/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.aurorainverter.internal.jaurlib.response;

import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqActualTime;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqCumulatedEnergy;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqDspData;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqFwVersion;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqLastAlarms;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqMFGdate;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqProductNumber;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqSerialNumber;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqState;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqSystemConfig;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqTimeCounter;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AReqVersionId;

/**
 * @author Stefano Brega (06/12/14) - Initial contribution
 * @author Gerald Heilmann (08/06/18) - adaptations for using with OpenHAB
 */
public interface AuroraResponseBuilder {
    public AuroraResponse createResponse(AReqActualTime request);

    public AuroraResponse createResponse(AReqVersionId request);

    public AuroraResponse createResponse(AReqMFGdate request);

    public AuroraResponse createResponse(AReqTimeCounter request);

    public AuroraResponse createResponse(AReqState request);

    public AuroraResponse createResponse(AReqFwVersion request);

    public AuroraResponse createResponse(AReqSerialNumber request);

    public AuroraResponse createResponse(AReqSystemConfig request);

    public AuroraResponse createResponse(AReqProductNumber request);

    public AuroraResponse createResponse(AReqCumulatedEnergy request);

    public AuroraResponse createResponse(AReqDspData request);

    public AuroraResponse createResponse(AReqLastAlarms request);
}
