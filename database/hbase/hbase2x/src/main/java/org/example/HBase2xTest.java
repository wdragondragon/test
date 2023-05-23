package org.example;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.exceptions.HBaseException;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.regionserver.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.RegionSplitter;

import java.io.IOException;
import java.util.*;


@Slf4j
public class HBase2xTest {


    private Configuration config = null;

    private Connection connection = null;


    private Admin admin = null;

    private final Map<String, Table> tableCache = new HashMap<>();

    public HBase2xTest(Configuration configuration) {
        this.config = configuration;
    }

    public void connect() throws IOException {
        connection = ConnectionFactory.createConnection(config);
    }

    public Admin getAmin() throws IOException {
        if (admin != null) {
            return admin;
        }
        return admin = connection.getAdmin();
    }

    public Table getTable(String tableName) throws IOException {
        try {
            return tableCache.computeIfAbsent(tableName, e -> {
                try {
                    return connection.getTable(TableName.valueOf(tableName));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
    }

    public void closeTable(String tableName) throws IOException {
        Table table = tableCache.get(tableName);
        if (table != null) {
            try {
                table.close();
            } finally {
                tableCache.remove(tableName);
            }
        }
    }

    public void create(HBaseTable hBaseTable) throws IOException, HBaseException {
        Admin admin = getAmin();
        // 创建表描述符对象
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf(hBaseTable.getTableName()));
//        tableDescriptorBuilder.setMaxFileSize(hBaseTable.getMaxFileSize());
//        tableDescriptorBuilder.setMemStoreFlushSize(hBaseTable.getMemStoreFlushSize());
//        tableDescriptorBuilder.setDurability(Durability.valueOf(hBaseTable.getDurability()));
//        tableDescriptorBuilder.setReadOnly(hBaseTable.getReadOnly());
//        tableDescriptorBuilder.setCompactionEnabled(hBaseTable.getCompactionEnabled());
//        tableDescriptorBuilder.setReplicationScope(hBaseTable.getReplicationScope());
//        tableDescriptorBuilder.setRegionReplication(hBaseTable.getRegionReplication());
//        tableDescriptorBuilder.setRegionSplitPolicyClassName(hBaseTable.getRegionSplitPolicyClassName());
        List<HBaseColumnFamily> HBaseColumnFamily = hBaseTable.getHBaseColumnFamily();
        for (HBaseColumnFamily family : HBaseColumnFamily) {
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family.getName()));

            if (family.getVersions() != null) {
                columnFamilyDescriptorBuilder.setMaxVersions(family.getVersions());
            }
            if (family.getBlockSize() != null) {
                columnFamilyDescriptorBuilder.setBlocksize(family.getBlockSize());
            }
            if (family.getCompressionType() != null) {
                columnFamilyDescriptorBuilder.setCompressionType(Compression.Algorithm.valueOf(family.getCompressionType()));
            }
            if (family.getDataBlockEncoding() != null) {
                columnFamilyDescriptorBuilder.setDataBlockEncoding(DataBlockEncoding.valueOf(family.getDataBlockEncoding()));
            }
            if (family.getTimeToLive() != null) {
                columnFamilyDescriptorBuilder.setTimeToLive(family.getTimeToLive());
            }
            if (family.getMinVersions() != null) {
                columnFamilyDescriptorBuilder.setMinVersions(family.getMinVersions());
            }
            if (family.getBloomFilterType() != null) {
                BloomType bloomType = BloomType.valueOf(family.getBloomFilterType());
                columnFamilyDescriptorBuilder.setBloomFilterType(bloomType);
//                if (bloomType == BloomType.ROWPREFIX_FIXED_LENGTH && family.getBloomPrefixLength() != null) {
//                    columnFamilyDescriptorBuilder.setValue(BloomFilterUtil.PREFIX_LENGTH_KEY, String.valueOf(family.getBloomPrefixLength()));
//                }
            }

            if (family.getReplicationScope() != null) {
                columnFamilyDescriptorBuilder.setScope(family.getReplicationScope());
            }
//            columnFamilyDescriptorBuilder.setInMemory(family.getInMemory());
//            columnFamilyDescriptorBuilder.setBlockCacheEnabled(family.getBlockCacheEnabled());
//            columnFamilyDescriptorBuilder.setKeepDeletedCells(KeepDeletedCells.valueOf(family.getKeepDeletedCells()));
//            columnFamilyDescriptorBuilder.setDFSReplication(family.getDfsReplication());
            ColumnFamilyDescriptor columnFamilyDescriptor = columnFamilyDescriptorBuilder.build();
            tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptor);
        }

        //分区内的自动split策略
        String regionSplitPolicyClassName = hBaseTable.getRegionSplitPolicyClassName();
        if (regionSplitPolicyClassName != null && !regionSplitPolicyClassName.isEmpty()) {
            switch (regionSplitPolicyClassName) {
                case "ConstantSizeRegionSplitPolicy":
                    tableDescriptorBuilder.setRegionSplitPolicyClassName(ConstantSizeRegionSplitPolicy.class.getName());
                    break;
                case "IncreasingToUpperBoundRegionSplitPolicy":
                    tableDescriptorBuilder.setRegionSplitPolicyClassName(IncreasingToUpperBoundRegionSplitPolicy.class.getName());
                    break;
                case "SteppingSplitPolicy":
                    tableDescriptorBuilder.setRegionSplitPolicyClassName(SteppingSplitPolicy.class.getName());
                    break;
                case "KeyPrefixRegionSplitPolicy":
                    tableDescriptorBuilder.setRegionSplitPolicyClassName(KeyPrefixRegionSplitPolicy.class.getName());
                    break;
                case "DelimitedKeyPrefixRegionSplitPolicy":
                    tableDescriptorBuilder.setRegionSplitPolicyClassName(DelimitedKeyPrefixRegionSplitPolicy.class.getName());
                    break;
                case "DisabledRegionSplitPolicy":
                    tableDescriptorBuilder.setRegionSplitPolicyClassName(DisabledRegionSplitPolicy.class.getName());
                    break;
                default:
                    break;
            }
        }
        TableDescriptor tableDescriptor = tableDescriptorBuilder.build();
        Integer splitPolicy = hBaseTable.getSplitPolicy();
        // 预分区策略
        if (splitPolicy == null) {
            throw new HBaseException("split policy is null");
        } else if (splitPolicy == 1) {
            String startKey = hBaseTable.getStartKey();
            String endKey = hBaseTable.getEndKey();
            admin.createTable(tableDescriptor, Bytes.toBytes(startKey), Bytes.toBytes(endKey), hBaseTable.getNumRegions());
        } else if (splitPolicy == 2) {
            List<String> splitKeysStr = hBaseTable.getSplitKeys();
            byte[][] splitKeys = new byte[splitKeysStr.size()][];
            for (int i = 0; i < splitKeysStr.size(); i++) {
                splitKeys[i] = Bytes.toBytes(splitKeysStr.get(i));
            }
            admin.createTable(tableDescriptor, splitKeys);
        } else if (splitPolicy == 3) {
            String splitAlgorithmClassName = hBaseTable.getSplitAlgorithmClassName();
            RegionSplitter.SplitAlgorithm splitAlgorithm;

            if (RegionSplitter.HexStringSplit.class.getSimpleName().equalsIgnoreCase(splitAlgorithmClassName)) {
                splitAlgorithm = new RegionSplitter.HexStringSplit();
            } else if (RegionSplitter.DecimalStringSplit.class.getSimpleName().equalsIgnoreCase(splitAlgorithmClassName)) {
                splitAlgorithm = new RegionSplitter.DecimalStringSplit();
            } else if (RegionSplitter.UniformSplit.class.getSimpleName().equalsIgnoreCase(splitAlgorithmClassName)) {
                splitAlgorithm = new RegionSplitter.UniformSplit();
            } else {
                throw new HBaseException("splitAlgorithmClassName is not support");
            }
            byte[][] split = splitAlgorithm.split(hBaseTable.getNumRegions());
            admin.createTable(tableDescriptor, split);
        } else {
            throw new HBaseException("split policy  is not support");
        }
    }

    public boolean existsTable(String tableName) throws IOException {
        return getAmin().tableExists(TableName.valueOf(tableName));
    }

    public void deleteColumnFamily(String tableName, String columnFamily) throws IOException {
        getAmin().deleteColumnFamily(TableName.valueOf(tableName), Bytes.toBytes(columnFamily));
    }

    public void put(String tableName, String rowKey, String family, String qualifier, String value) throws IOException {
        Table table = getTable(tableName);
        // 添加数据
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
        table.put(put);
    }

    public void close() throws IOException {
        if (admin != null) {
            admin.close();
        }
        for (Table table : tableCache.values()) {
            table.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public ResultScanner get(String tableName) throws IOException {
        Table table = getTable(tableName);
        Scan scan = new Scan();
        scan.readVersions(1);
        return table.getScanner(scan);
    }

    public HBaseTable getTableInfo(String tableName) throws IOException {
        HBaseTable hBaseTable = new HBaseTable();
        Table table = getTable(tableName);
        TableDescriptor descriptor = table.getDescriptor();
        hBaseTable.setTableName(String.valueOf(descriptor.getTableName()));
        hBaseTable.setMaxFileSize(descriptor.getMaxFileSize());
        hBaseTable.setMemStoreFlushSize(descriptor.getMemStoreFlushSize());
        hBaseTable.setDurability(descriptor.getDurability().name());
        hBaseTable.setReadOnly(descriptor.isReadOnly());
        hBaseTable.setCompactionEnabled(descriptor.isCompactionEnabled());
        hBaseTable.setReplicationScope(descriptor.getRegionReplication());
        hBaseTable.setRegionReplication(descriptor.getRegionReplication());

        RegionLocator regionLocator = connection.getRegionLocator(TableName.valueOf(tableName));
        List<HRegionLocation> allRegionLocations = regionLocator.getAllRegionLocations();
        for (HRegionLocation allRegionLocation : allRegionLocations) {
            RegionInfo region = allRegionLocation.getRegion();
            byte[] startKey = region.getStartKey();
            byte[] endKey = region.getEndKey();
            String regionName = region.getRegionNameAsString();
            log.info("regionName:{} startkey:{},endkey:{}", regionName, Bytes.toString(startKey), Bytes.toString(endKey));
        }

        ColumnFamilyDescriptor[] columnFamilies = descriptor.getColumnFamilies();
        for (ColumnFamilyDescriptor columnFamilyDescriptor : columnFamilies) {
            HBaseColumnFamily HBaseColumnFamily = new HBaseColumnFamily();
            HBaseColumnFamily.setName(columnFamilyDescriptor.getNameAsString());
            HBaseColumnFamily.setVersions(columnFamilyDescriptor.getMaxVersions());
            HBaseColumnFamily.setInMemory(columnFamilyDescriptor.isInMemory());
            HBaseColumnFamily.setBlockCacheEnabled(columnFamilyDescriptor.isBlockCacheEnabled());
            HBaseColumnFamily.setBlockSize(columnFamilyDescriptor.getBlocksize());
            HBaseColumnFamily.setCompressionType(columnFamilyDescriptor.getCompressionType().name());
            HBaseColumnFamily.setDataBlockEncoding(columnFamilyDescriptor.getDataBlockEncoding().name());
            HBaseColumnFamily.setTimeToLive(columnFamilyDescriptor.getTimeToLive());
            HBaseColumnFamily.setMinVersions(columnFamilyDescriptor.getMinVersions());
            HBaseColumnFamily.setKeepDeletedCells(columnFamilyDescriptor.getKeepDeletedCells().name());
            HBaseColumnFamily.setBloomFilterType(columnFamilyDescriptor.getBloomFilterType().name());
//            byte[] bloomPrefixLengthByte = columnFamilyDescriptor.getValue(BloomFilterUtil.PREFIX_LENGTH_KEY.getBytes());
//            if (bloomPrefixLengthByte != null) {
//                HBaseColumnFamily.setBloomPrefixLength(Integer.valueOf(Bytes.toString(bloomPrefixLengthByte)));
//            }
            HBaseColumnFamily.setReplicationScope(columnFamilyDescriptor.getScope());
            HBaseColumnFamily.setDfsReplication(columnFamilyDescriptor.getDFSReplication());
            hBaseTable.addColumnFamily(HBaseColumnFamily);
        }
        return hBaseTable;
    }

    public static void main(String[] args) throws Exception {
        String userKeytabFile = "D:\\dev\\IdeaProjects\\test\\database\\hbase\\hbase2x\\src\\main\\resources\\zhjl.keytab";
        String krb5File = "D:\\dev\\IdeaProjects\\test\\database\\hbase\\hbase2x\\src\\main\\resources\\krb5.conf";
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "centos1,centos2,centos3");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        GetKerberosObject getKerberosObject = new GetKerberosObject("zhjl@HADOOP.COM", userKeytabFile, krb5File, config, false);
        HBase2xTest hBase2xTest = new HBase2xTest(config);
        getKerberosObject.doAs(() -> {
            hBase2xTest.connect();

            createTest(hBase2xTest);
//            scanTest(hBase2xTest, "mytable");
            hBase2xTest.close();
        });
    }


    public static void createTest(HBase2xTest hBase2xTest) throws HBaseException, IOException {
        HBaseTable hBaseTable = new HBaseTable();
        hBaseTable.setTableName("default_namespace:test_create_20230518_1609");
        hBaseTable.setRegionSplitPolicyClassName("org.apache.hadoop.hbase.regionserver.ConstantSizeRegionSplitPolicy");
        hBaseTable.setSplitPolicy(3);
        hBaseTable.setSplitAlgorithmClassName("DecimalStringSplit");
        hBaseTable.setNumRegions(3);

        HBaseColumnFamily HBaseColumnFamily = new HBaseColumnFamily();
        HBaseColumnFamily.setName("cf1");
        HBaseColumnFamily.setVersions(3);
        HBaseColumnFamily.setBlockSize(1024);
        HBaseColumnFamily.setCompressionType("GZ");
        HBaseColumnFamily.setDataBlockEncoding("FAST_DIFF");
        HBaseColumnFamily.setTimeToLive(86400);
        HBaseColumnFamily.setMinVersions(1);
        HBaseColumnFamily.setBloomFilterType(BloomType.ROWCOL.name());
//        HBaseColumnFamily.setBloomPrefixLength(10);
        HBaseColumnFamily.setReplicationScope(1);

        hBaseTable.addColumnFamily(HBaseColumnFamily);

        hBase2xTest.create(hBaseTable);

        HBaseTable tableInfo = hBase2xTest.getTableInfo("default_namespace:test_create_20230518_1609");
        log.info("tableInfo:{}", JSONObject.toJSONString(tableInfo));
    }

    public static void scanTest(HBase2xTest hBase2xTest, String tableName) throws IOException {
        HBaseTable hBaseTable = hBase2xTest.getTableInfo(tableName);
        String s = JSONObject.toJSONString(hBaseTable);
        // 获取数据
        ResultScanner mytable = hBase2xTest.get(tableName);
        for (Result result : mytable) {
            CellScanner cellScanner = result.cellScanner();
            byte[] row = result.getRow();
            String rowKey = Bytes.toString(row);
            System.out.println(rowKey);
            while (cellScanner.advance()) {
                Cell cell = cellScanner.current();
                String family = Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength());
                String qualifier = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                System.out.println("col:" + family + ":" + qualifier + ",value:" + value);
            }
        }
    }
}