package org.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    public void create() throws IOException {
        Admin admin = getAmin();
        // 创建表描述符对象
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf("mytable"));
        // 添加列族
        ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("cf")).build();
        tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptor);
        // 创建表
        admin.createTable(tableDescriptorBuilder.build());
    }

    public boolean existsTable(String tableName) throws IOException {
        return getAmin().tableExists(TableName.valueOf(tableName));
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

    public static void main(String[] args) throws Exception {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "192.168.1.161,192.168.1.162,192.168.1.163");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        HBase2xTest hBase2xTest = new HBase2xTest(config);
        hBase2xTest.connect();
        // 获取数据
        ResultScanner mytable = hBase2xTest.get("mytable");
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
        hBase2xTest.close();
    }
}