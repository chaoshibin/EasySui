package com.easysui.distribute.lock.service.impl;

import com.easysui.distribute.lock.enums.LockEnum;
import com.easysui.distribute.lock.service.DistributeLockService;

/**
 * @author Chao Shibin 2019/5/16 21:35
 */
public class ZookeeperDistributeLockServiceImpl implements DistributeLockService {
    @Override
    public boolean lock(String key, String requestId, long expireSeconds) {
        return false;
    }

    @Override
    public boolean unLock(String key, String requestId) {
        return false;
    }

    @Override
    public LockEnum type() {
        return LockEnum.ZOOKEEPER;
    }
}