package org.example.nativetest.usb;

import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;

/**
 * @author JDragon
 * @date 2023/11/13 13:12
 * @description
 */

public class StenographMachine {
    static SetupApi setupApi = SetupApi.INSTANCE;

    static Kernel32 kernel32 = Kernel32.INSTANCE;

    Guid.GUID USB_WRITER_GUID = createGuid("c5682e20-8059-604a-b761-77c4de9d5dbf");

    int CREATE_ALWAYS = 2;
    int CREATE_NEW = 1;

    int DIGCF_DEVICEINTERFACE = 0x00000010;
    int DIGCF_PRESENT = 0x00000002;


    static int ERROR_INSUFFICIENT_BUFFER = 0x0000007A;
    static int ERROR_NO_MORE_ITEMS = 0x00000103;

    int FILE_ATTRIBUTE_NORMAL = 0x80;

    int FILE_SHARE_READ = 0x00000001;
    int FILE_SHARE_WRITE = 0x00000002;
    int GENERIC_READ = 0x80000000;
    int GENERIC_WRITE = 0x40000000;


    String _STRUCT_FORMAT = "<2sIH6I";

    int HEADER_SIZE = calculateStructSize(_STRUCT_FORMAT);

    public final int MAX_READ = 0x200;  // Arbitrary read limit

    public WinNT.HANDLE _usb_device;

    public ByteBuffer _read_buffer;

    private static final WinNT.HANDLE INVALID_HANDLE_VALUE = WinBase.INVALID_HANDLE_VALUE;

    public static void main(String[] args) {
        StenographMachine stenographMachine = new StenographMachine();
        boolean connect = stenographMachine.connect();
        if (connect) {
            System.out.println("连接成功！");
        } else {
            System.err.println("连接失败！");
        }
    }


    public static int calculateStructSize(String format) {
        int size = 0;
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (c == 's') {
                // Assuming size of a string is 1 byte (adjust as needed)
                size += 1;
            } else if (c == 'I' || c == 'H') {
                // Assuming size of an int or short is 4 bytes (adjust as needed)
                size += 4;
            }
        }
        return size;
    }

    public StenographMachine() {
        this._usb_device = WinBase.INVALID_HANDLE_VALUE;
        this._read_buffer = ByteBuffer.allocate(MAX_READ + HEADER_SIZE);
    }

    public boolean connect() {
        if (this._usb_device != INVALID_HANDLE_VALUE) {
            this.disconnect();
        }
        this._usb_device = this._open_device_by_class_interface_and_instance(USB_WRITER_GUID);
        return this._usb_device != INVALID_HANDLE_VALUE;
    }

    public void disconnect() {
        if (!kernel32.CloseHandle(this._usb_device)) {
            System.out.println("CloseHandle fail:" + kernel32.GetLastError());
        }
        this._usb_device = INVALID_HANDLE_VALUE;
    }

    public Guid.GUID createGuid(String uuidString) {
        // Parse the UUID string
        java.util.UUID uuid = java.util.UUID.fromString(uuidString);

        // Convert UUID to bytes
        byte[] uuidBytes = new byte[16];
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        for (int i = 0; i < 8; i++) {
            uuidBytes[i] = (byte) (mostSigBits >>> 8 * (7 - i));
            uuidBytes[i + 8] = (byte) (leastSigBits >>> 8 * (7 - i));
        }

        // Create a GUID instance
        return new Guid.GUID(uuidBytes);
    }

    public WinNT.HANDLE _open_device_by_class_interface_and_instance(Guid.GUID class_guid) {
        WinNT.HANDLE device_info = setupApi.SetupDiGetClassDevs(new Guid.GUID(class_guid), null, null,
                DIGCF_DEVICEINTERFACE | DIGCF_PRESENT);
        if (device_info == INVALID_HANDLE_VALUE) {
            System.err.println("SetupDiGetClassDevs: " + kernel32.GetLastError());
            return INVALID_HANDLE_VALUE;
        }
        WinNT.HANDLE usb_device = StenographMachine._open_device_instance(device_info, class_guid);
        if (!setupApi.SetupDiDestroyDeviceInfoList(device_info)) {
            System.out.println("SetupDiDestroyDeviceInfoList: " + kernel32.GetLastError());
        }
        return usb_device;
    }

    public static WinNT.HANDLE _open_device_instance(WinNT.HANDLE device_info, Guid.GUID guid) {
        com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA dev_interface_data = new com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA();
        dev_interface_data.cbSize = dev_interface_data.size();

        if (!setupApi.SetupDiEnumDeviceInterfaces(
                device_info, null, guid, // Replace MemberIndex with the desired index
                0, dev_interface_data)) {
            if (kernel32.GetLastError() != ERROR_NO_MORE_ITEMS) {
                System.err.println("SetupDiEnumDeviceInterfaces: " + kernel32.GetLastError());
            }
            return INVALID_HANDLE_VALUE;
        }

        SetupApiExample.SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA_A dev_detail_data_ptr = new SetupApiExample.SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA_A();
        dev_detail_data_ptr.cbSize = dev_detail_data_ptr.size();
        if (!setupApi.SetupDiGetDeviceInterfaceDetail(
                device_info,
                dev_interface_data,
                dev_detail_data_ptr.getPointer(),
                dev_detail_data_ptr.size(),
                null,
                null)) {

            System.err.println("SetupDiGetDeviceInterfaceDetail: " + kernel32.GetLastError());
            return INVALID_HANDLE_VALUE;
        }
        // Access device interface detail information as needed
        String devicePath = new String(dev_detail_data_ptr.DevicePath);

        System.out.println("okay, creating file, device path: " + devicePath);

        // Create file handle
        WinNT.HANDLE handle = kernel32.CreateFile(devicePath,
                WinNT.GENERIC_READ | WinNT.GENERIC_WRITE,
                WinNT.FILE_SHARE_READ | WinNT.FILE_SHARE_WRITE,
                null,
                WinNT.CREATE_ALWAYS,
                WinNT.FILE_ATTRIBUTE_NORMAL,
                null);

        if (handle == INVALID_HANDLE_VALUE) {
            System.err.println("CreateFile: " + kernel32.GetLastError());
        }
        return handle;
    }


    public int _usb_writer_packet(byte[] requestPacket) {
        // Perform USB write operation
        if (!kernel32.INSTANCE.WriteFile(this._usb_device,
                requestPacket,
                MAX_READ + HEADER_SIZE,
                new IntByReference(0),
                null)) {
            System.err.println("WriteFile. Error code: " + kernel32.INSTANCE.GetLastError());
            return 0;
        }
        return requestPacket.length;
    }

    public StenoPacket _usb_read_packet() {
        // Replace with your actual read buffer
        IntByReference bytesRead = new IntByReference(0);
        // 调用ReadFile函数
        boolean result = kernel32.INSTANCE.ReadFile(
                this._usb_device,
                this._read_buffer.array(),
                MAX_READ + StenoPacket.HEADER_SIZE,
                bytesRead,
                null
        );
        // Perform USB read operation
        if (!result) {
            System.err.println("ReadFile failed. Error code: " + kernel32.INSTANCE.GetLastError());
            return null;
        }

        // Return null if not enough data was read
        if (bytesRead.getValue() < StenoPacket.HEADER_SIZE) {
            System.err.println("ReadFile: short read, " + bytesRead.getValue() + " < " + StenoPacket.HEADER_SIZE);
            return null;
        }
        byte[] array = this._read_buffer.array();
        return StenoPacket.unpack(array);
    }

    public StenoPacket send_receive(StenoPacket request) {
        if (this._usb_device == INVALID_HANDLE_VALUE) {
            throw new RuntimeException("device not open");
        }
        int i = this._usb_writer_packet(request.pack());
        if (i < StenoPacket.HEADER_SIZE) {
            return null;
        }
        return this._usb_read_packet();
    }
}
