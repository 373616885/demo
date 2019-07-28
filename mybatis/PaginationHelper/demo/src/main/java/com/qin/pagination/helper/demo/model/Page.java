package com.qin.pagination.helper.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 *
 * @author qinjp
 * @date 2010-5-6
 */
@Setter
@Getter
public class Page<E> {


    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 页数
     */
    private int pageNumber;
    /**
     * 总页数
     */
    private int pagesAvailable;
    /**
     * 该页内容
     */
    private List<E> pageItems = new ArrayList<E>();

}
