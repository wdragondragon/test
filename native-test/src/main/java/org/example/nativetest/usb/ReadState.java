package org.example.nativetest.usb;

import lombok.Getter;

/**
 * @author JDragon
 * @date 2023/11/13 16:36
 * @description
 */

@Getter
public class ReadState {

    private boolean realtime;

    private boolean realtime_file_open;

    private int offset;

    public void reset() {
        this.realtime = false;

        this.realtime_file_open = false;

        this.offset = 0;
    }

    public void setRealtime(boolean realtime) {
        this.realtime = realtime;
    }

    public void setRealtime_file_open(boolean realtime_file_open) {
        this.realtime_file_open = realtime_file_open;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
