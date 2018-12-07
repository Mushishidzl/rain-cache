package com.mushi.raincache;

import com.mushi.raincache.to.BaseTO;

/**
 * 〈〉
 *
 * @author mushi
 * @create 2018/12/7
 * @since 1.0.0
 */
public class Employee extends BaseTO {

    /**  */
    private static final long serialVersionUID = -4620016204472694934L;

    private long              id;

    private String            name;


    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}