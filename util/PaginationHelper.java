package com.qin.pagination.helper.demo.utils;

import com.qin.pagination.helper.demo.model.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * 分页辅助类
 * @author qinjp
 * @date 2010-5-6
 */
public class PaginationHelper<E> {


    /**
     * 取分页
     *
     * @param jt           jdbcTemplate
     * @param sqlCountRows 查询总数的SQL
     * @param sqlFetchRows 查询数据的sql
     * @param args         查询参数
     * @param pageNo       页数
     * @param pageSize     每页大小
     * @param rowMapper
     * @return
     */
    public Page<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows, final String sqlFetchRows,
                             final Object[] args, final int pageNo, final int pageSize, final RowMapper<E> rowMapper) {
        return fetchPage(jt, sqlCountRows, sqlFetchRows, args, pageNo, pageSize, null, rowMapper);
    }

    public Page<E> fetchPage(final JdbcTemplate jt, final String sqlCountRows, final String sqlFetchRows,
                             final Object[] args, final int pageNo, final int pageSize, final Long lastMaxId,
                             final RowMapper<E> rowMapper) {
        if (pageNo <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("pageNo and pageSize must be greater than zero");
        }

        // 查询当前记录总数
        Integer rowCountInt = jt.queryForObject(sqlCountRows, Integer.class, args);
        if (rowCountInt == null) {
            throw new IllegalArgumentException("fetchPageLimit error");
        }

        final int rowCount = rowCountInt.intValue();

        // 计算页数
        int pageCount = rowCount / pageSize;
        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }

        // 创建Page对象
        final Page<E> page = new Page<E>();
        page.setPageNumber(pageNo);
        page.setPagesAvailable(pageCount);
        page.setTotalCount(rowCount);

        if (pageNo > pageCount) {
            return null;
        }

        final int startRow = (pageNo - 1) * pageSize;

        String selectSQL = "";

        if (lastMaxId != null) {
            selectSQL = sqlFetchRows + " and id > " + lastMaxId + " order by id asc" + " limit " + 0 + "," + pageSize;
        } else {
            selectSQL = sqlFetchRows + " limit " + startRow + "," + pageSize;
        }

        List<E> result = jt.query(selectSQL, args, rowMapper);

        for (E item : result) {
            page.getPageItems().add(item);
        }
        return page;
    }
}
