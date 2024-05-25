package org.example.nativetest;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;

/**
 * @Author: JDragon
 * @Data:2024/1/5 20:02
 * @Description:
 */
public class WyTest {

    public static void main(String[] args) {
        Wyhkm wyhkm = Native.load("wyhkm", Wyhkm.class);

        Integer s = wyhkm.HKMGetVersion();
        System.out.println(s);

        Integer devId = wyhkm.HKMSearchDevice(0x2612, 0x1701, 0);
        System.out.println(devId);

        WinDef.DWORD ipDev = wyhkm.HKMOpen(devId, 0);
        if (ipDev == null) {
            System.out.println("连接无涯键鼠失败");
            return;
        }
        System.out.println("连接无涯键鼠成功");

        Boolean moveStatus = wyhkm.HKMMoveRP(ipDev, 100, 100);

        if (moveStatus) {
            System.out.println("移动成功");
        } else {
            System.out.println("移动失败");
        }

        if (wyhkm.HKMClose(ipDev)) {
            System.out.println("关闭无涯键鼠");
        }
    }

    public interface Wyhkm extends Library {

        Integer HKMGetVersion();

        Integer HKMSearchDevice(Integer vid, Integer pid, Integer mode);

        WinDef.DWORD HKMOpen(Integer devId, Integer mode);

        Boolean HKMMoveRP(WinDef.DWORD ipDev, Integer x, Integer y);

        Boolean HKMClose(WinDef.DWORD ipDev);
    }

}


