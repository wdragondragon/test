package org.example.nativetest.usb;

import java.io.IOError;
import java.io.IOException;

/**
 * @author JDragon
 * @date 2023/11/13 16:40
 * @description
 */
public class Stenograph {

    private final StenographMachine _machine;


    public Stenograph() {
        this._machine = new StenographMachine();
    }

    public void _on_stroke() {

    }

    public void start_capture() {

    }

    public void _connect_machine() {
        this._machine.connect();
    }

    public boolean _reconnect() {
        return true;
    }

    public StenoPacket _send_receive(StenoPacket request) {
        System.out.println("Requesting from Stenograph: " + request);
        StenoPacket response = this._machine.send_receive(request);
        System.out.println("Response from Stenograph: " + response);

        if (response == null) {
            throw new IOError(new IOException());
        }
        if (response.getPacketId() == StenoPacket.ID_ERROR) {
            int error_number = response.getP1();
            System.out.println("response error: " + error_number);
        }
        if (response.getPacketId() != request.getPacketId() ||
                response.getSequenceNumber() != request.getSequenceNumber()) {
            throw new RuntimeException("ProtocolViolationException");
        }
        return response;
    }

    public void run() {
        ReadState readState = new ReadState();
        while (true) {
            if (!readState.isRealtime_file_open()) {
                this._send_receive(StenoPacket.make_open_request());
                readState.setRealtime_file_open(true);
            }
            StenoPacket response = this._send_receive(
                    StenoPacket.makeReadRequest(1, readState.getOffset())
            );

            System.out.println("response length: " + response.getDataLength());

            if (response.getDataLength() != -1) {
                readState.setOffset(readState.getOffset() + response.getDataLength());
            } else if (!readState.isRealtime()) {
                System.out.println("state realtime");
                readState.setRealtime(true);
            }
            if (response.getDataLength() != -1 && readState.isRealtime()) {
                System.out.println("go");
            }
        }
    }


    public void stop_capture() {

    }
}
