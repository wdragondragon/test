package org.example.nativetest.usb;

import lombok.Getter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Getter
public class StenoPacket {

    private static final byte[] SYNC = "SG".getBytes();
    public static final int HEADER_SIZE = 16;  // 2 + 4 + 2 + 4 + 4

    public static final int ID_ERROR = 0x6;
    public static final int ID_OPEN = 0x11;
    public static final int ID_READ = 0x13;


    private int sequenceNumber;
    private int packetId;
    private int dataLength;
    private int p1;
    private int p2;
    private int p3;
    private int p4;
    private int p5;
    private byte[] data;

    public StenoPacket(int sequenceNumber, int packetId, int dataLength,
                       int p1, int p2, int p3, int p4, int p5, byte[] data) {
        this.sequenceNumber = sequenceNumber;
        this.packetId = packetId;
        this.dataLength = dataLength;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.data = data;
    }

    public byte[] pack() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + data.length).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(SYNC);
        buffer.putInt(sequenceNumber);
        buffer.putInt(packetId);
        buffer.putInt(dataLength);
        buffer.putInt(p1);
        buffer.putInt(p2);
        buffer.putInt(p3);
        buffer.putInt(p4);
        buffer.putInt(p5);
        buffer.put(data);
        return buffer.array();
    }

    public static StenoPacket unpack(byte[] usbPacket) {
        ByteBuffer buffer = ByteBuffer.wrap(usbPacket).order(ByteOrder.LITTLE_ENDIAN);
        byte[] sync = new byte[2];
        buffer.get(sync);
        int sequenceNumber = buffer.getInt();
        int packetId = buffer.getInt();
        int dataLength = buffer.getInt();
        int p1 = buffer.getInt();
        int p2 = buffer.getInt();
        int p3 = buffer.getInt();
        int p4 = buffer.getInt();
        int p5 = buffer.getInt();
        byte[] data = new byte[dataLength];
        buffer.get(data);
        return new StenoPacket(sequenceNumber, packetId, dataLength, p1, p2, p3, p4, p5, data);
    }

    // ... Other methods remain unchanged

    public static StenoPacket make_open_request() {
        return makeOpenRequest("REALTIME.000".getBytes(), "A".getBytes());
    }

    public static StenoPacket makeOpenRequest(byte[] fileName, byte[] diskId) {
        return new StenoPacket(0, ID_OPEN, fileName.length, diskId[0], 0, 0, 0, 0, fileName);
    }

    public static StenoPacket makeReadRequest(int fileOffset, int byteCount) {
        return new StenoPacket(0, ID_READ, 0, fileOffset, byteCount, 0, 0, 0, new byte[0]);
    }

    // ... Other methods remain unchanged


    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public void setP3(int p3) {
        this.p3 = p3;
    }

    public void setP4(int p4) {
        this.p4 = p4;
    }

    public void setP5(int p5) {
        this.p5 = p5;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
