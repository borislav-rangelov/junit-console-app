package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;

public interface HallService extends BaseService<Hall> {

    /**
     * If found, returns the hall with the corresponding number.
     *
     * @param number hall number
     * @return The hall. null if not found
     */
    Hall getByNumber(int number);
}
