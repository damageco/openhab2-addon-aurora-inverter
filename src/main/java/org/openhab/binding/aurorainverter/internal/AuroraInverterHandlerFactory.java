/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.aurorainverter.internal;

import static org.openhab.binding.aurorainverter.AuroraInverterBindingConstants.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.aurorainverter.handler.AuroraInverterBridgeHandler;
import org.openhab.binding.aurorainverter.handler.AuroraInverterInverterHandler;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link AuroraInverterHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Gerald Heilmann - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.aurorainverter", service = ThingHandlerFactory.class)
public class AuroraInverterHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .of(THING_TYPE_INVERTER, THING_TYPE_BRIDGE).collect(Collectors.toSet());

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_BRIDGE.equals(thingTypeUID)) {
            return new AuroraInverterBridgeHandler((Bridge) thing);
        }

        if (THING_TYPE_INVERTER.equals(thingTypeUID)) {
            return new AuroraInverterInverterHandler(thing);
        }

        return null;
    }
}
