package org.example.tableTest;

import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:41
 * @Description:
 */
public class TableFactory {

    public static <T> PageTable<T> buildPageTable(long total, long pages, long size,long current, TableRef<T> table) {
        PageTable<T> pageTable = new PageTable<>();
        pageTable.setTotal(total);
        pageTable.setPages(pages);
        pageTable.setSize(size);
        pageTable.setCurrent(current);
        pageTable.setTable(buildTable(table));
        return pageTable;
    }

    private static <T> Table<T> buildTable(TableRef<T> tableRef) {
        Table<T> table = new Table<>();
        List<T> list = tableRef.getList();
        if (list == null) {
            list = new ArrayList<>();
        }
        Type type = tableRef.getType();

        Field[] fields = ((Class<?>)type).getFields();
        for (Field field : fields) {
            TableHeader tableHeader = field.getAnnotation(TableHeader.class);
            if (null != tableHeader) {
                Table.Header header = new Table.Header();
                header.setKey(field.getName());
                header.setLabel(tableHeader.value());
                header.setWidth(tableHeader.width());
                header.setSortable(tableHeader.sortable());
                table.addHeader(header);
            }
        }
        table.setBodies(list);
        return table;
    }


    public static <T> PageTable<T> buildPageTable(long total, long pages, long size,long current, List<T> list) {
        PageTable<T> pageTable = new PageTable<>();
        pageTable.setTotal(total);
        pageTable.setPages(pages);
        pageTable.setSize(size);
        pageTable.setCurrent(current);
        pageTable.setTable(buildTable(list));
        return pageTable;
    }
    private static <T> Table<T> buildTable(List<T> list) {
        Table<T> table = new Table<>();
        if (list == null) {
            list = new ArrayList<>();
        }
        Type type = TableUtils.toParameterizedType(list.getClass());



        Field[] fields = ((Class<?>)type).getFields();
        for (Field field : fields) {
            TableHeader tableHeader = field.getAnnotation(TableHeader.class);
            if (null != tableHeader) {
                Table.Header header = new Table.Header();
                header.setKey(field.getName());
                header.setLabel(tableHeader.value());
                header.setWidth(tableHeader.width());
                header.setSortable(tableHeader.sortable());
                table.addHeader(header);
            }
        }
        table.setBodies(list);
        return table;
    }
}
