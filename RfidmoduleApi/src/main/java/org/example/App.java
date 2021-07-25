package org.example;

import com.uhf.api.cls.Reader;
import org.junit.Test;


import java.util.Arrays;
import java.util.HashMap;

import static com.uhf.api.cls.Reader.Mtr_Param.*;

/**
 * Hello world!
 */
public class App {
    private static Reader jReader = new Reader();

    private static HashMap<Reader.Mtr_Param, Object> paramsMap = new HashMap<>();

    private static Reader.READER_ERR reader_err;//每次操作的返回结果

    private static int[] ants = {1};//输入参数，存放操作使用的天线，可以有多个天线
    private static int antCnt = ants.length;//ants 数组的长度
    private static short timeout = 1000;//操作的超时时间
    private static int[] tagCnt = new int[1];//输出参数，表示读到的标签个数，所有的标签信息都存放于tags
    private static Reader.TAGINFO[] tags = new Reader.TAGINFO[200];
    //注意：读写器内部最多可以存储 200 条标签数据，如果给 pTInfo 分配 200 个标签的容量，
    //则可以保证 pTInfo 在被 TagInventory 改写的过程中不会出现越界。

    public static void main(String[] args) {
        String address = "com3";
        //读写器创建
        if ((reader_err = jReader.InitReader_Notype(address, 1)) != Reader.READER_ERR.MT_OK_ERR) {
            System.out.println("error in InitReader:" + reader_err);
            return;
        }
        init();
        getParam();
        try {
            //一行一行读
            System.out.println("==========逐一读取===========");
            if ((reader_err = jReader.TagInventory_Raw(ants, antCnt, timeout, tagCnt)) == Reader.READER_ERR.MT_OK_ERR) {
                for (int i = 0; i < tagCnt[0]; i++) {
                    if (jReader.GetNextTag(tags[0]) == Reader.READER_ERR.MT_OK_ERR) {
                        resolveTagsInfo(tags[0]);
                    }
                }
            } else {
                System.out.println("TagInventory_Raw failed:" + reader_err);
            }

            //全部标签一起读
            System.out.println("==========全部读取===========");
            if ((reader_err = jReader.TagInventory(ants, antCnt, timeout, tags, tagCnt)) == Reader.READER_ERR.MT_OK_ERR) {
                for (Reader.TAGINFO tag : tags) {
                    if (tag == null) break;
                    resolveTagsInfo(tag);
                }
            } else {
                System.out.println("TagInventory failed:" + reader_err);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jReader.CloseReader();
        }
    }

    public static void init() {
        for (int i = 0; i < tags.length; i++) {
            tags[0] = jReader.new TAGINFO();
        }
    }

    public static void resolveTagsInfo(Reader.TAGINFO tag) {
        System.out.println("取出标签数：" + tag);
        readEcpId(tag);

        //读取嵌入数据
        byte[] embededData = tag.EmbededData;
        short embededDateLen = tag.EmbededDatalen;
        System.out.println("嵌入数据解析前：" + Arrays.toString(embededData));
        System.out.println("标签被读的次数：" + tag.ReadCnt);

        int ant = 1;//在哪个天线上操作
        char bank = 3;//bank0
        int addr = 0;//第二块
        int blks = 32;//读取多少块

        write(ant, bank, addr, "为啥12355d");

        read(ant, bank, addr, blks);
    }

    public static String readEcpId(Reader.TAGINFO tag) {
        //读取ecpId
        char[] epcIdChar = new char[tag.EpcId.length * 2];
        byte[] ecpId = tag.EpcId;
        short ecpLen = tag.Epclen;
        jReader.Hex2Str(ecpId, ecpLen, epcIdChar);
        String ecpIdStr = new String(epcIdChar);
        System.out.println("读出标签解析前：" + Arrays.toString(ecpId));
        System.out.println("读出标签解析后：" + ecpIdStr);
        for (byte b : tag.EpcId) {
            String tString = Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
        }
        return ecpIdStr;
    }

    /**
     * @param ant  输入参数，存放操作使用的天线，可以有多个天线
     * @param bank 第几个bank
     * @param addr bank中第几块
     * @param data 要写入的数据
     * @return
     * @Author: Jdragon
     */
    public static boolean write(int ant, char bank, int addr, String data) {
        //写数据，先将字符转换成16进制字符串，再将16进制字符串转成byte数组
        String inData = HexUtil.str2HexStr(data);
        //多两个作结束符，获取时根据7F7F截断
        int inDataLen = inData.length();
        byte[] inDataByte = new byte[inDataLen / 2 + 2];
        jReader.Str2Hex(inData, inDataLen, inDataByte);
        inDataByte[inDataByte.length - 2] = 127;
        inDataByte[inDataByte.length - 1] = 127;
        System.out.println("存储数据：" + Arrays.toString(inDataByte));
        if ((reader_err = jReader.WriteTagData(ant, bank, addr, inDataByte, inDataByte.length, null, timeout)) != Reader.READER_ERR.MT_OK_ERR) {
            System.out.println("WriteTagData failed:" + reader_err);
            return false;
        }
        return true;
    }

    /**
     * @param ant  输入参数，存放操作使用的天线，可以有多个天线
     * @param bank 第几个bank
     * @param addr bank中第几块
     * @param blks 从addr开始读多少块
     * @Author: Jdragon
     * @Description:
     **/
    public static String read(int ant, char bank, int addr, int blks) {
        //读数据，读取后先将获取的一位byte截开高低位，分成两个16进制字符放入dataOutChar
        //将dataOutChar转换成16进制字符串，然后通过7F7F结束符截断多余字符
        byte[] data = new byte[64];//最大存储量32*16bit
        if ((reader_err = jReader.GetTagData(ant, bank, addr, blks, data, null, timeout)) != Reader.READER_ERR.MT_OK_ERR) {
            System.out.println("GetTagData failed:" + reader_err);
            return null;
        } else {
            System.out.println("读取标签数据：" + Arrays.toString(data));
            char[] dataOutChar = new char[data.length * 2];
            jReader.Hex2Str(data, data.length, dataOutChar);
            System.out.println(Arrays.toString(dataOutChar));
            String dataHex = new String(dataOutChar);
            //获取到的数据会有很多多余的，根据7F7F结束符进行截断
            int endIndex = dataHex.indexOf("7F7F");
            dataHex = dataHex.substring(0, endIndex == -1 ? dataHex.length() - 1 : endIndex);
            String dataStr = HexUtil.hexStr2Str(dataHex);
            System.out.println(dataStr);
            return dataStr;
        }
    }

    public static void getParam() {
        //Gen2 协议参数Session 合法值0，1，2，3
        paramsMap.put(MTR_PARAM_POTL_GEN2_SESSION, new int[16]);
        //Gen2 协议参数Q值 合法值为-1到15，其中-1表示自动调整 Q 值，其它为静态Q值
        paramsMap.put(MTR_PARAM_POTL_GEN2_Q, new int[16]);
        //Gen2 协议基带编码方式
        /*合法值为0，1，2，
            3，0表示FM0且只有 m6e 架构的读写器才支持；1 表示
            MILLER的M值为2；
            2表示MILLER的M 值为 4；3 表示
            MILLER的M值为8
            */
        paramsMap.put(MTR_PARAM_POTL_GEN2_TAGENCODING, new int[16]);
        //支持的最大 EPC 长度，单位为 bit
        // 合法值为 96，496(M6e 架构读写器不支持此参数)
        paramsMap.put(MTR_PARAM_POTL_GEN2_MAXEPCLEN, new int[16]);

        //读写器发射功率
        //成 员 Powers 为AntPower类型的数组，其中每个元素表示一个天线的功率配置，
        //antid 表示天线编号（从 1 开 始 编 号 ）
        //readPower 为读功率
        //writePower为写功率，单位为 centi-dbm, 成 员 antcnt 表 示 Powers数组中被设置了的元素个数
        paramsMap.put(MTR_PARAM_RF_ANTPOWER, jReader.new AntPowerConf());
        //读写器最大输出功率,单位为centi-dbm
        paramsMap.put(MTR_PARAM_RF_MAXPOWER, new int[16]);
        //读写器最小输出功率,单位为centi-dbm
        paramsMap.put(MTR_PARAM_RF_MINPOWER, new int[16]);
        //标签过滤器，可在对标签进行
        //读，写，锁，盘存操作的时候指定过滤条件
        paramsMap.put(MTR_PARAM_TAG_FILTER, jReader.new TagFilter_ST());
        paramsMap.put(MTR_PARAM_TAG_EMBEDEDDATA, jReader.new EmbededData_ST());
        paramsMap.put(MTR_PARAM_TAG_INVPOTL, jReader.new Inv_Potls_ST());
        paramsMap.put(MTR_PARAM_READER_CONN_ANTS, jReader.new ConnAnts_ST());
        paramsMap.put(MTR_PARAM_READER_AVAILABLE_ANTPORTS, new int[16]);
        paramsMap.put(MTR_PARAM_READER_IS_CHK_ANT, new int[16]);
        paramsMap.put(MTR_PARAM_READER_VERSION, jReader.new ReaderVersion());//版本号
        paramsMap.put(MTR_PARAM_READER_IP, jReader.new Reader_Ip());
        paramsMap.put(MTR_PARAM_FREQUENCY_REGION, Reader.Region_Conf.values());
        paramsMap.put(MTR_PARAM_FREQUENCY_HOPTABLE, jReader.new HoptableData_ST());
        paramsMap.put(MTR_PARAM_POTL_GEN2_BLF, new int[16]);
        paramsMap.put(MTR_PARAM_POTL_GEN2_WRITEMODE, new int[16]);
        paramsMap.put(MTR_PARAM_POTL_GEN2_TARGET, new int[16]);
        paramsMap.put(MTR_PARAM_TAGDATA_UNIQUEBYANT, new int[16]);
        paramsMap.put(MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA, new int[16]);
        paramsMap.put(MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI, new int[16]);
        paramsMap.put(MTR_PARAM_RF_HOPTIME, new int[16]);
        paramsMap.put(MTR_PARAM_RF_LBT_ENABLE, new int[16]);
        paramsMap.put(MTR_PARAM_POTL_ISO180006B_BLF, new int[16]);
        paramsMap.put(MTR_PARAM_TAG_EMDSECUREREAD, jReader.new EmbededSecureRead_ST());
        paramsMap.put(MTR_PARAM_TAG_SEARCH_MODE, new int[100]);
        paramsMap.put(MTR_PARAM_POTL_ISO180006B_MODULATION_DEPTH, 0);
        paramsMap.put(MTR_PARAM_POTL_ISO180006B_DELIMITER, 0);
        //获取失败
        paramsMap.put(MTR_PARAM_POWERSAVE_MODE, 0);
        paramsMap.put(MTR_PARAM_TRANSMIT_MODE, 0);


        paramsMap.put(MTR_PARAM_RF_TEMPERATURE, new int[16]);
        paramsMap.put(MTR_PARAM_RF_SUPPORTEDREGIONS, new int[16]);
        paramsMap.put(MTR_PARAM_POTL_SUPPORTEDPROTOCOLS, new int[16]);
        paramsMap.put(MTR_PARAM_POTL_GEN2_TARI, new int[16]);
        paramsMap.put(MTR_PARAM_RF_HOPANTTIME, new int[16]);
        paramsMap.put(MTR_PARAM_TAG_MULTISELECTORS, new int[16]);
        //获取失败
        paramsMap.put(MTR_PARAM_TRANS_TIMEOUT, 0);
        paramsMap.put(MTR_PARAM_RF_ANTPORTS_VSWR, jReader.new AntPortsVSWR());
        paramsMap.put(MTR_PARAM_CUSTOM, jReader.new CustomParam_ST());
        paramsMap.put(MTR_PARAM_READER_WATCHDOG, new int[16]);
        paramsMap.put(MTR_PARAM_READER_ERRORDATA, new int[16]);
        for (Reader.Mtr_Param value : Reader.Mtr_Param.values()) {
            Object val = paramsMap.get(value);
            try {
                jReader.ParamGet(value, val);
                if (val instanceof int[])
                    System.out.println(value + ":" + Arrays.toString((int[]) val));
                else
                    System.out.println(value + ":" + val);
            } catch (Exception ig) {
                System.out.println(value + ":获取失败:" + ig.getMessage());
            }
        }
    }
}
