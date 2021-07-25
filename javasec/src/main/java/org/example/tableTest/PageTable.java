package org.example.tableTest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 23:37
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageTable<E> extends AbstractPage<Table<E>> {

    private Table<E> table;

    @Override
    protected Table<E> getRecord() {
        return this.table;
    }

}
