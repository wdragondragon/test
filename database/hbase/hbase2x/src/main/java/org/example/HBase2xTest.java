package org.example;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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
import java.util.regex.Pattern;


@Slf4j
public class HBase2xTest {


    private Configuration config = null;

    private Connection connection = null;


    private Admin admin = null;

    private final Map<String, Table> tableCache = new HashMap<>();

    public HBase2xTest(Configuration configuration) {
        this.config = configuration;
    }

    public Connection connect() throws IOException {
        return connection = ConnectionFactory.createConnection(config);
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

    public void createNameSpace(String namespaceName) throws IOException {
        // 判断命名空间是否存在
        try {
            NamespaceDescriptor namespace = admin.getNamespaceDescriptor(namespaceName);
            if (namespace != null) {
                log.info("命名空间已存在: " + namespaceName);
            }
        } catch (NamespaceNotFoundException e) {
            // 创建命名空间描述符对象
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespaceName).build();
            // 创建命名空间
            admin.createNamespace(namespaceDescriptor);
            log.info("命名空间已创建: " + namespaceName);
        }
    }

    public void truncate(String tableName) {
        try {
            Admin admin = getAmin();
            admin.disableTable(TableName.valueOf(tableName));
            admin.truncateTable(TableName.valueOf(tableName), true);
            log.info("truncate table success: " + tableName);
        } catch (IOException e) {
            log.error("truncate table error: " + tableName, e);
        }
    }

    public void create(HBaseTable hBaseTable) throws IOException, HBaseException {
        Admin admin = getAmin();
        String tableName = hBaseTable.getTableName();
        String[] tableSplit = tableName.split(":");
        if (tableSplit.length >= 2) {
            createNameSpace(tableSplit[0]);
        }
        if (existsTable(tableName)) {
            log.info("表已存在: " + tableName);
            return;
        }
        // 创建表描述符对象
        List<HBaseColumnFamily> HBaseColumnFamily = hBaseTable.getHBaseColumnFamily();
        List<ColumnFamilyDescriptor> columnFamilyDescriptor = getColumnFamiliesDescriptor(HBaseColumnFamily);

        TableDescriptorBuilder tableDescriptorBuilder = getTableDescriptorBuilder(hBaseTable);
        tableDescriptorBuilder.setColumnFamilies(columnFamilyDescriptor);
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

    public TableDescriptorBuilder getTableDescriptorBuilder(HBaseTable hBaseTable) {
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf(hBaseTable.getTableName()));
//        tableDescriptorBuilder.setMaxFileSize(hBaseTable.getMaxFileSize());
//        tableDescriptorBuilder.setMemStoreFlushSize(hBaseTable.getMemStoreFlushSize());
//        tableDescriptorBuilder.setDurability(Durability.valueOf(hBaseTable.getDurability()));
//        tableDescriptorBuilder.setReadOnly(hBaseTable.getReadOnly());
//        tableDescriptorBuilder.setCompactionEnabled(hBaseTable.getCompactionEnabled());
//        tableDescriptorBuilder.setReplicationScope(hBaseTable.getReplicationScope());
//        tableDescriptorBuilder.setRegionReplication(hBaseTable.getRegionReplication());
//        tableDescriptorBuilder.setRegionSplitPolicyClassName(hBaseTable.getRegionSplitPolicyClassName());
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
        return tableDescriptorBuilder;
    }

    public List<ColumnFamilyDescriptor> getColumnFamiliesDescriptor(List<HBaseColumnFamily> hBaseColumnFamilies) throws IOException {
        List<ColumnFamilyDescriptor> columnFamilyDescriptors = new LinkedList<>();
        for (HBaseColumnFamily family : hBaseColumnFamilies) {
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
            columnFamilyDescriptors.add(columnFamilyDescriptor);
        }
        return columnFamilyDescriptors;
    }


    //修改表时，预分区不能修改
    public void changeTable(HBaseTable hBaseTable) throws IOException {
        TableDescriptorBuilder tableDescriptorBuilder = getTableDescriptorBuilder(hBaseTable);
        getAmin().modifyTable(tableDescriptorBuilder.build());
    }

    public boolean existsTable(String tableName) throws IOException {
        return getAmin().tableExists(TableName.valueOf(tableName));
    }

    public void addColumnFamily(String tableName, List<HBaseColumnFamily> hBaseColumnFamily) throws IOException {
        List<ColumnFamilyDescriptor> columnFamilies = getColumnFamiliesDescriptor(hBaseColumnFamily);
        for (ColumnFamilyDescriptor columnFamily : columnFamilies) {
            getAmin().addColumnFamily(TableName.valueOf(tableName), columnFamily);
        }
    }

    public boolean existsColumnFamily(String tableName, String columnFamily) throws IOException {
        if (!existsTable(tableName)) {
            return false;
        }
        Table table = getTable(tableName);
        return table.getDescriptor().hasColumnFamily(Bytes.toBytes(columnFamily));
    }

    public void deleteColumnFamily(String tableName, String columnFamily) throws IOException {
        getAmin().deleteColumnFamily(TableName.valueOf(tableName), Bytes.toBytes(columnFamily));
    }

    public void deleteTable(String tableName) throws IOException {
        getAmin().disableTable(TableName.valueOf(tableName));
        getAmin().deleteTable(TableName.valueOf(tableName));
    }

    public boolean existData(String tableName) throws IOException {
        Table table = getTable(tableName);
        Scan scan = new Scan();
        scan.readVersions(1);
        ResultScanner scanner = table.getScanner(scan);
        Result result = scanner.next();
        scanner.close();
        return result != null;
    }

    public void changeColumnFamily(String tableName, List<HBaseColumnFamily> hBaseColumnFamilies) throws IOException {
        List<ColumnFamilyDescriptor> columnFamilyDescriptorList = getColumnFamiliesDescriptor(hBaseColumnFamilies);
        for (ColumnFamilyDescriptor columnFamilyDescriptor : columnFamilyDescriptorList) {
            getAmin().modifyColumnFamily(TableName.valueOf(tableName), columnFamilyDescriptor);
        }
    }

    public List<String> tableList(String tableNameRegex) {
        List<String> tableList = new ArrayList<>();
        try {
            TableName[] tableNames = getAmin().listTableNames(Pattern.compile(tableNameRegex), true);
            for (TableName tableName : tableNames) {
                tableList.add(tableName.getNameAsString());
            }
        } catch (Exception e) {
            log.error("get table list error", e);
        }
        return tableList;
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

    public String queryVersion() throws IOException {
        // 获取 HBase 集群状态信息
        ClusterMetrics clusterMetrics = getAmin().getClusterMetrics();

        // 获取 HBase 版本号
        return clusterMetrics.getHBaseVersion();
    }

    public static void main(String[] args) throws Exception {
        String userKeytabFile = System.getProperty("krb5", "/bmdata/software/hbase/user.keytab");
        String krb5File = System.getProperty("keytab", "/bmdata/software/hbase/krb5.conf");
        String hosts = System.getProperty("hosts", "cnsz92pl00660,cnsz92pl00662,cnsz92pl00661");
        String port = System.getProperty("port", "24002");
//        String hosts = System.getProperty("hosts", "node-10-194-186-214,node-10-194-186-215,node-10-194-186-216");
//        String port = System.getProperty("port", "2181");
        String principal = System.getProperty("principal", "u_cmhk_basic_dev@HADOOP.COM");
        String hdfsSite = System.getProperty("hdfsSite", "/bmdata/software/hbase/hdfs-site.xml");
        String coreSite = System.getProperty("coreSite", "/bmdata/software/hbase/core-site.xml");
        String hbaseSite = System.getProperty("hbaseSite", "/bmdata/software/hbase/hbase-site.xml");
//        String userKeytabFile = "D:\\dev\\IdeaProjects\\test\\database\\hbase\\hbase2x\\src\\main\\resources\\zhjl.keytab";
//        String krb5File = "D:\\dev\\IdeaProjects\\test\\database\\hbase\\hbase2x\\src\\main\\resources\\krb5.conf";
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hosts);
        config.set("hbase.zookeeper.property.clientPort", port);
        config.addResource(new Path(hdfsSite));
        config.addResource(new Path(coreSite));
        config.addResource(new Path(hbaseSite));


        String testTableName = System.getProperty("tableName", "test_create_20230518_1609");
        String testFamilyName = "cf1";
        GetKerberosObject getKerberosObject = new GetKerberosObject(principal, userKeytabFile, krb5File, config, true);
        HBase2xTest hBase2xTest = new HBase2xTest(config);
        getKerberosObject.doAs(hBase2xTest::connect);
        hBase2xTest.printInfo();
        List<String> strings = hBase2xTest.tableList(".*");
        log.info("列表：" + JSONObject.toJSONString(strings, SerializerFeature.PrettyFormat));
        createTest(hBase2xTest, testTableName);
        hBase2xTest.truncate(testTableName);
        addCF(hBase2xTest, testTableName, testFamilyName);
        hBase2xTest.put(testTableName, "row1", testFamilyName, "c1", "v1");
        scanTest(hBase2xTest, testTableName);
        log.info(hBase2xTest.queryVersion());
        getKerberosObject.doAs(hBase2xTest::close);
    }


    public static void createTest(HBase2xTest hBase2xTest, String tableName) throws HBaseException, IOException {
        HBaseTable hBaseTable = new HBaseTable();
        hBaseTable.setTableName(tableName);
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

        HBaseTable tableInfo = hBase2xTest.getTableInfo(tableName);
        log.info("tableInfo:{}", JSONObject.toJSONString(tableInfo));
    }

    public static void addCF(HBase2xTest hBase2xTest, String tableName, String cfName) throws HBaseException, IOException {
        if (hBase2xTest.existsColumnFamily(tableName, cfName)) {
            log.info("列族已存在");
            return;
        }
        HBaseTable hBaseTable = new HBaseTable();
        hBaseTable.setTableName(tableName);
        HBaseColumnFamily HBaseColumnFamily = new HBaseColumnFamily();
        HBaseColumnFamily.setName(cfName);
        HBaseColumnFamily.setVersions(3);
        HBaseColumnFamily.setBlockSize(1024);
        HBaseColumnFamily.setCompressionType("GZ");
        HBaseColumnFamily.setDataBlockEncoding("FAST_DIFF");
        HBaseColumnFamily.setTimeToLive(86400);
        HBaseColumnFamily.setMinVersions(1);
        HBaseColumnFamily.setBloomFilterType(BloomType.ROWCOL.name());
        hBase2xTest.addColumnFamily(hBaseTable.getTableName(), Collections.singletonList(HBaseColumnFamily));
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


    public void printInfo() throws IOException {
        Admin amin = getAmin();
        String jarFilePath = amin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        jarFilePath = java.net.URLDecoder.decode(jarFilePath, "UTF-8");
        log.info("jarFilePath:{}", jarFilePath);
        ClusterMetrics clusterMetrics = getAmin().getClusterMetrics();
        String hBaseVersion = clusterMetrics.getHBaseVersion();
        log.info("hBaseVersion:{}", hBaseVersion);
        List<ServerName> deadServerNames = clusterMetrics.getDeadServerNames();
        log.info("deadServerNames:{}", JSONObject.toJSONString(deadServerNames));
        ServerName masterName = clusterMetrics.getMasterName();
        log.info("masterName:{}", JSONObject.toJSONString(masterName));
        List<ServerName> serversName = clusterMetrics.getServersName();
        log.info("serversName:{}", JSONObject.toJSONString(serversName));
    }
}