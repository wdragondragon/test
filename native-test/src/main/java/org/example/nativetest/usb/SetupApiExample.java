package org.example.nativetest.usb;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.SetupApi;
import com.sun.jna.platform.win32.WinDef;

import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public class SetupApiExample {
    static {
        System.setProperty("jna.library.path", "C:\\Windows\\System32");
    }

    public interface SetupApi extends com.sun.jna.Library {

        SetupApi INSTANCE = Native.load("setupapi", SetupApi.class);

//        class GUID extends Guid.GUID {
//        }

//        class HDEVINFO extends HANDLE {
//        }


//        class SP_DEVINFO_DATA extends Structure {
//            public int cbSize;
//            // Add other fields as needed
//        }
//
//        class SP_DEVICE_INTERFACE_DATA extends Structure {
//            public int cbSize;
//            // Add other fields as needed
//        }

        class SP_DEVICE_INTERFACE_DETAIL_DATA_A extends Structure {
            public int cbSize;
            public char[] DevicePath = new char[1]; // Adjust the size based on your requirements

        }

        HANDLE SetupDiGetClassDevsW(Guid.GUID ClassGuid, String Enumerator, WinDef.HWND hwndParent, int Flags);

        boolean SetupDiDestroyDeviceInfoList(HANDLE DeviceInfoSet);

        boolean SetupDiEnumDeviceInterfaces(HANDLE DeviceInfoSet, com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA DeviceInfoData, Guid.GUID InterfaceClassGuid,
                                            WinDef.DWORD MemberIndex, com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA DeviceInterfaceData);

        boolean SetupDiGetDeviceInterfaceDetail(HANDLE DeviceInfoSet,
                                                com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA DeviceInterfaceData,
                                                SP_DEVICE_INTERFACE_DETAIL_DATA_A DeviceInterfaceDetailData,
                                                WinDef.DWORD DeviceInterfaceDetailDataSize,
                                                IntByReference RequiredSize,
                                                com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA DeviceInfoData);
    }

    public static void main(String[] args) {
        SetupApi setupApi = SetupApi.INSTANCE;

        Guid.GUID classGuid = new Guid.GUID(); // Replace with your class GUID
        WinDef.HWND hwndParent = null; // Replace with the appropriate window handle
        int flags = 0; // Replace with the appropriate flags

        HANDLE deviceInfoSet = setupApi.SetupDiGetClassDevsW(classGuid, null, hwndParent, flags);

        if (deviceInfoSet != null) {
            try {
                com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA deviceInfoData = new com.sun.jna.platform.win32.SetupApi.SP_DEVINFO_DATA();
                deviceInfoData.cbSize = deviceInfoData.size();

                // Replace MemberIndex with the desired index
                WinDef.DWORD memberIndex = new WinDef.DWORD(0);

                com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA deviceInterfaceData = new com.sun.jna.platform.win32.SetupApi.SP_DEVICE_INTERFACE_DATA();
                deviceInterfaceData.cbSize = deviceInterfaceData.size();

                if (setupApi.SetupDiEnumDeviceInterfaces(deviceInfoSet, deviceInfoData, classGuid, memberIndex,
                        deviceInterfaceData)) {

                    SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA_A deviceInterfaceDetailData = new SetupApi.SP_DEVICE_INTERFACE_DETAIL_DATA_A();
                    deviceInterfaceDetailData.cbSize = deviceInterfaceDetailData.size();
                    WinDef.DWORD deviceInterfaceDetailDataSize = new WinDef.DWORD(deviceInterfaceDetailData.size());

                    IntByReference requiredSize = new IntByReference();

                    if (setupApi.SetupDiGetDeviceInterfaceDetail(deviceInfoSet, deviceInterfaceData,
                            deviceInterfaceDetailData, deviceInterfaceDetailDataSize, requiredSize,
                            deviceInfoData)) {
                        // Access device interface detail information as needed
                    }
                }
            } finally {
                setupApi.SetupDiDestroyDeviceInfoList(deviceInfoSet);
            }
        }
    }
}
