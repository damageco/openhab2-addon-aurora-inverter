package org.openhab.binding.aurorainverter.internal.jaurlib;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.openhab.binding.aurorainverter.internal.jaurlib.modbus.MB_PDU;
import org.openhab.binding.aurorainverter.internal.jaurlib.modbus.MB_address;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AuroraCumEnergyEnum;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AuroraDspRequestEnum;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AuroraRequest;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AuroraRequestFactory;
import org.openhab.binding.aurorainverter.internal.jaurlib.request.AuroraRequestPacket;
import org.openhab.binding.aurorainverter.internal.jaurlib.response.AuroraResponse;
import org.openhab.binding.aurorainverter.internal.jaurlib.response.AuroraResponseFactory;
import org.openhab.binding.aurorainverter.internal.jaurlib.response.AuroraResponsePacket;
import org.openhab.binding.aurorainverter.internal.jaurlib.response.ResponseErrorEnum;
import org.openhab.binding.aurorainverter.internal.jaurlib.utils.FormatStringUtils;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Created by sbrega on 17/11/2014.
 */
public class AuroraDriver {
    Logger log = Logger.getLogger(getClass().getSimpleName());
    protected int serialPortTimeout = 2000;
    protected final AuroraRequestFactory auroraRequestFactory;
    protected final AuroraResponseFactory auroraResponseFactory;
    protected SerialPort serialPort;
    protected AuroraResponse msgReceived = null;
    private long receivingPause = 50;
    private long communicationPause = 50;

    public AuroraDriver(SerialPort serialPort, AuroraRequestFactory reqFactory, AuroraResponseFactory respFactory) {
        this.serialPort = serialPort;

        this.auroraResponseFactory = respFactory;
        this.auroraRequestFactory = reqFactory;
    }

    protected void sendRequest(int address, MB_PDU auroraRequest) throws Exception {
        if (serialPort == null) {
            throw new UnsupportedCommOperationException("SerialPort is null!");
        }
        AuroraRequestPacket auroraRequestPacket = new AuroraRequestPacket(new MB_address(address), auroraRequest);
        Thread.sleep(communicationPause);
        // serialPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
        serialPort.getOutputStream().flush();
        serialPort.getOutputStream().write(auroraRequestPacket.toByteArray());
    }

    private AuroraResponse readResponse(AuroraRequest auroraRequest) throws Exception {

        AuroraResponse result = auroraRequest.create(auroraResponseFactory);

        if (result == null) {
            throw new Exception("No Response available for Request: " + auroraRequest);
        }
        try {
            // serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
            serialPort.getOutputStream().flush();
            Thread.sleep(receivingPause);
            // byte[] buffer = serialPort.readBytes(8, serialPortTimeout);
            byte[] buffer = new byte[8];
            serialPort.getInputStream().read(buffer);
            log.fine("Read buffer (Hex): " + FormatStringUtils.byteArrayToHex(buffer));
            AuroraResponsePacket pkt = new AuroraResponsePacket(result);
            pkt.read(new ByteArrayInputStream(buffer));
            store((AuroraResponse) pkt.getPdu());
            result = (AuroraResponse) pkt.getPdu();
        } catch (IOException ex) {
            result.setErrorCode(ResponseErrorEnum.CRC);
            // } catch (SerialPortTimeoutException e) {
            // result.setErrorCode(ResponseErrorEnum.TIMEOUT);
        } catch (Exception ue) {
            result.setErrorCode(ResponseErrorEnum.UNKNOWN);
        }

        return result;
    }

    private void store(AuroraResponse message) {
        msgReceived = message;
    }

    public void stop() {
        // try {
        // serialPort.closePort();// Close serial port
        // } catch (SerialPortException ex) {
        // System.out.println(ex);
        // }
        if (serialPort != null) {
            serialPort.close();
        }
    }

    public synchronized AuroraResponse acquireVersionId(int address) throws Exception {

        log.info("Sending acquireVersionId to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_VersionId();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireDspValue(int invAddress, AuroraDspRequestEnum requestedValue)
            throws Exception {
        log.info("Sending acquireDspValue (" + requestedValue + ") to: " + invAddress);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_DspData(requestedValue);
        sendRequest(invAddress, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireCumulatedEnergy(int address, AuroraCumEnergyEnum requestedValue)
            throws Exception {
        log.info("Sending Cumulated Energy Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_CumulatedEnergy(requestedValue);
        sendRequest(address, auroraRequest);
        AuroraResponse response = readResponse(auroraRequest);
        log.info("Received response: " + response);
        return response;

    }

    public synchronized AuroraResponse acquireState(int address) throws Exception {
        log.info("Sending State Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_State();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireFirmwareVersion(int address) throws Exception {
        log.info("Sending Firmware Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_FwVersion();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireMFGdate(int address) throws Exception {
        log.info("Sending Manufacturing Date Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_MFGdate();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireSystemConfig(int address) throws Exception {
        log.info("Sending System Configuration Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_SystemConfig();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireSerialNumber(int address) throws Exception {
        log.info("Sending Serial Number Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_SerialNumber();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireProductNumber(int address) throws Exception {
        log.info("Sending Product Number Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_ProductNumber();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireTimeCounter(int address) throws Exception {
        log.info("Sending Product Number Request to: " + address);
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_TimeCounter();
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireActualTime(int address) throws Exception {

        return acquireData(address);

    }

    public synchronized AuroraResponse acquireData(int address) throws Exception {
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_ActualTime();
        log.info("Sending Request " + auroraRequest + "to address: " + address);
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    public synchronized AuroraResponse acquireLastAlarms(int address) throws Exception {
        AuroraRequest auroraRequest = auroraRequestFactory.createAReq_AlarmsList();
        log.info("Sending Request " + auroraRequest + " to address: " + address);
        sendRequest(address, auroraRequest);
        AuroraResponse responseMsg = readResponse(auroraRequest);
        log.info("Received response: " + responseMsg);
        return responseMsg;

    }

    // public void initSerialPort() {
    // serialPort.openPort();// Open serial port
    // serialPort.setParams(19200, 8, 1, 0);// Set params.
    //
    // }
    //
    // public void setSerialPort(SerialPort aSerialPort) {
    // this.serialPort = aSerialPort;
    // }

    public void setSerialPort(String serialPortName, int serialPortBaudRate)
            throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
        if (portIdentifier.isCurrentlyOwned()) {
            log.info("Error: Port is currently in use");
        } else {
            log.info(portIdentifier.getCurrentOwner());
            CommPort commPort = portIdentifier.open(this.getClass().getName(), serialPortTimeout);
            if (commPort instanceof RXTXPort) {
                serialPort = (RXTXPort) commPort;
                serialPort.setSerialPortParams(serialPortBaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                // serialPort = new RXTXPort(serialPortName);
                // serialPort.enableReceiveTimeout(serialPortTimeout);
                // serialPort.setSerialPortParams(serialPortBaudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                // SerialPort.PARITY_NONE);

                // InputStream in = serialPort.getInputStream();
                // OutputStream out = serialPort.getOutputStream();
                //
                // (new Thread(new SerialReader(in))).start();
                // (new Thread(new SerialWriter(out))).start();

            } else {
                this.log.severe(String.format("{} not handled.", (commPort.getClass().getName())));
            }

        }

        // serialPort = new SerialPort(serialPortName);
        // serialPort.openPort();// Open serial port
        // serialPort.setParams(serialPortBaudRate, 8, 1, 0);// Set params.

    }

}
