package com.siwuxie095.spring.chapter16th.example5th.data;

/**
 * @author Jiajing Li
 * @date 2021-03-18 22:47:10
 */
@SuppressWarnings("all")
public class SpittleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private long spittleId;

    public SpittleNotFoundException(long spittleId) {
        this.spittleId = spittleId;
    }

    public long getSpittleId() {
        return spittleId;
    }

}

