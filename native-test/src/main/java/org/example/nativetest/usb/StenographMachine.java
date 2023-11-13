package org.example.nativetest.usb;

import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT;

import java.nio.ByteBuffer;

/**
 * @author JDragon
 * @date 2023/11/13 13:12
 * @description
 */

public class StenographMachine {
    static SetupApiExample.SetupApi setupApi = SetupApiExample.SetupApi.INSTANCE;

    static FileApiExample.Kernel32 kernel32 = FileApiExample.Kernel32.INSTANCE;

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
        WinNT.HANDLE device_info = setupApi.SetupDiGetClassDevsW(new Guid.GUID(class_guid), null, null,
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
                new DWORD(0), dev_interface_data)) {
            if (kernel32.GetLastError() != ERROR_NO_MORE_ITEMS) {
                System.err.println("SetupDiEnumDeviceInterfaces: " + kernel32.GetLastError());
            }
            return INVALID_HANDLE_VALUE;
        }

        SetupApiExample.SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA_A dev_detail_data_ptr = new SetupApiExample.SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA_A();
        dev_detail_data_ptr.cbSize = dev_detail_data_ptr.size();
        DWORD deviceInterfaceDetailDataSize = new DWORD(dev_detail_data_ptr.size());
        if (!setupApi.SetupDiGetDeviceInterfaceDetail(
                device_info,
                dev_interface_data,
                dev_detail_data_ptr,
                deviceInterfaceDetailDataSize,
                null,
                null)) {

            System.err.println("SetupDiGetDeviceInterfaceDetail: " + kernel32.GetLastError());
            return INVALID_HANDLE_VALUE;
        }
        // Access device interface detail information as needed
        String devicePath = new String(dev_detail_data_ptr.DevicePath);

        System.out.println("okay, creating file, device path: " + devicePath);

        DWORD dwDesiredAccess = new DWORD(WinNT.GENERIC_READ | WinNT.GENERIC_WRITE);
        DWORD dwShareMode = new DWORD(WinNT.FILE_SHARE_READ | WinNT.FILE_SHARE_WRITE);
        WinBase.SECURITY_ATTRIBUTES lpSecurityAttributes = null;
        DWORD dwCreationDisposition = new DWORD(WinNT.CREATE_ALWAYS);
        DWORD dwFlagsAndAttributes = new DWORD(WinNT.FILE_ATTRIBUTE_NORMAL);
        WinNT.HANDLE hTemplateFile = null;
        // Create file handle
        WinNT.HANDLE handle = kernel32.CreateFileA(devicePath, dwDesiredAccess, dwShareMode, lpSecurityAttributes,
                dwCreationDisposition, dwFlagsAndAttributes, hTemplateFile);

        if (handle == INVALID_HANDLE_VALUE) {
            System.err.println("CreateFile: " + kernel32.GetLastError());
        }
        return handle;
    }
}
