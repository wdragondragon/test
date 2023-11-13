package org.example.nativetest.usb;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinBase.OVERLAPPED;
import com.sun.jna.platform.win32.WinBase.SECURITY_ATTRIBUTES;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;

import java.nio.ByteBuffer;

public class FileApiExample {

    public interface Kernel32 extends com.sun.jna.Library {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);

        HANDLE CreateFileA(String lpFileName, DWORD dwDesiredAccess, DWORD dwShareMode,
                           SECURITY_ATTRIBUTES lpSecurityAttributes, DWORD dwCreationDisposition, DWORD dwFlagsAndAttributes,
                           HANDLE hTemplateFile);

        boolean ReadFile(HANDLE hFile, ByteBuffer lpBuffer, DWORD nNumberOfBytesToRead,
                         DWORD lpNumberOfBytesRead, OVERLAPPED lpOverlapped);


        boolean WriteFile(HANDLE hFile, byte[] lpBuffer, DWORD nNumberOfBytesToWrite,
                          DWORD lpNumberOfBytesWritten, OVERLAPPED lpOverlapped);

        boolean CloseHandle(HANDLE hObject);

        int GetLastError();
    }

    public static void main(String[] args) {
        Kernel32 kernel32 = Kernel32.INSTANCE;

        String lpFileName = "C:\\path\\to\\your\\file.txt"; // Replace with your file path
        DWORD dwDesiredAccess = new DWORD(WinNT.GENERIC_READ);
        DWORD dwShareMode = new DWORD(WinNT.FILE_SHARE_READ);
        SECURITY_ATTRIBUTES lpSecurityAttributes = null;
        DWORD dwCreationDisposition = new DWORD(WinNT.OPEN_EXISTING);
        DWORD dwFlagsAndAttributes = new DWORD(WinNT.FILE_ATTRIBUTE_NORMAL);
        HANDLE hTemplateFile = null;

        // Create file
        HANDLE hFile = kernel32.CreateFileA(lpFileName, dwDesiredAccess, dwShareMode, lpSecurityAttributes,
                dwCreationDisposition, dwFlagsAndAttributes, hTemplateFile);

        if (hFile != WinBase.INVALID_HANDLE_VALUE) {
            try {
                // Perform read/write operations or other file operations here

            } finally {
                // Close file handle
                kernel32.CloseHandle(hFile);
            }
        }
    }
}
