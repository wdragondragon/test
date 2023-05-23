package org.example;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JDragon
 * @date 2023/5/18 9:40
 * @description
 */

@Data
public class HBaseTable {

    private String tableName;

    private Long maxFileSize;

    private Long memStoreFlushSize;

    private String durability;

    private Boolean readOnly;

    private Boolean compactionEnabled;

    private Integer replicationScope;

    private Integer regionReplication;


    // 分区策略 1固定值切分 2自定义区间 3自定义策略算法分区
    private Integer splitPolicy;
    // 分区策略 1
    private String startKey;
    // 分区策略 1
    private String endKey;

    //分区策略2
    private List<String> splitKeys;

    //分区策略3
    //HexStringSplit
    //DecimalStringSplit
    //UniformSplit
    private String splitAlgorithmClassName;

    //分区策略1,3
    private Integer numRegions;


    //分区内的自动split策略
    //ConstantSizeRegionSplitPolicy
    //IncreasingToUpperBoundRegionSplitPolicy
    //SteppingSplitPolicy
    //KeyPrefixRegionSplitPolicy
    //DelimitedKeyPrefixRegionSplitPolicy
    //DisableSplitPolicy
    private String regionSplitPolicyClassName;


    private String metaData;

    private List<HBaseColumnFamily> HBaseColumnFamily = new LinkedList<>();

    public void addColumnFamily(HBaseColumnFamily HBaseColumnFamily) {
        this.HBaseColumnFamily.add(HBaseColumnFamily);
    }
}
