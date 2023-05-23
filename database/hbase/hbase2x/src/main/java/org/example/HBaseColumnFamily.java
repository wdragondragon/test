package org.example;

import lombok.Data;

/**
 * @author JDragon
 * @date 2023/5/18 9:33
 * @description
 * NAME：列族的名称。它是必须的参数，并且必须是字节数组。
 * VERSIONS：列族中保留的最大版本数。默认为1。如果将其设置为无限制，则可以使用Integer.MAX_VALUE。
 * IN_MEMORY：如果设置为true，则将列族保存在内存中。默认为false。
 * BLOCKCACHE：如果设置为true，则将列族的块缓存到内存中。默认为true。
 * BLOCKSIZE：列族的块大小（以字节为单位）。默认为64KB。
 * COMPRESSION：列族的压缩类型。支持的压缩类型包括NONE（无压缩）、SNAPPY、GZ（Gzip）、LZ4和LZO。
 * DATA_BLOCK_ENCODING：列族的数据块编码类型。支持的编码类型包括NONE（无编码）、PREFIX、DIFF、FAST_DIFF、ROW_INDEX_V1和ROW_INDEX_V2。
 * TTL：列族中存储的数据的生存时间（以秒为单位）。默认为永久保留。
 * MIN_VERSIONS：列族中保留的最小版本数。默认为0。
 * KEEP_DELETED_CELLS：如果设置为true，则在删除单元格时保留单元格的删除标记。默认为false。
 * BLOOMFILTER：列族的布隆过滤器类型。支持的布隆过滤器类型包括NONE、ROW、ROWCOL和ROWPREFIX_FIXED_LENGTH。
 * REPLICATION_SCOPE：列族的复制范围。支持的复制范围包括LOCAL和GLOBAL。
 * DFS_REPLICATION：列族的HDFS复制因子。默认为1。
 * METADATA：列族的元数据信息。可以存储任意字节数组
 */

@Data
public class HBaseColumnFamily {
    private String name;

    // 最高版本 max version
    private Integer versions;

    private Boolean inMemory;

    private Boolean blockCacheEnabled;

    private Integer blockSize;

    //压缩类型 LZO NONE GZ SNAPPY LZ4 BZIP2 ZSTD
    private String compressionType;

    //NONE：不进行编码，直接存储原始数据块。
    //DIFF：采用差分编码方式，将相邻数据块之间的重复部分进行编码，从而减少存储空间。
    //FAST_DIFF：采用更快速的差分编码方式，相比于DIFF编码，FAST_DIFF编码可以更快地进行编码和解码操作，但是存储空间的节省效果相对较低。
    //PREFIX：采用前缀压缩编码方式，将相邻数据块之间的共同前缀进行编码，从而减少存储空间
    private String dataBlockEncoding;

    private Integer timeToLive;

    // 最低版本
    private Integer minVersions;

    private String keepDeletedCells;

    //布隆过滤器
    //ROW：表示在rowkey上应用BloomFilter，可以快速过滤掉不存在的行。
    //ROWCOL：表示在rowkey和列族上应用BloomFilter，可以快速过滤掉不存在的行和列族。
    //NONE：表示不应用BloomFilter。
    //ROWPREFIX_FIXED_LENGTH： 前缀扫描 暂不添加
    private String bloomFilterType;

//    private Integer bloomPrefixLength;

    // 复制策略 0不复制 1同步复制
    private Integer replicationScope;

    private Short dfsReplication;

}
