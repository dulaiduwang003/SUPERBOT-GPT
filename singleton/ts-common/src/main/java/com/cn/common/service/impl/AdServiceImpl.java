package com.cn.common.service.impl;

import com.cn.common.configuration.OperationConfiguration;
import com.cn.common.service.AdService;
import com.cn.common.utils.EnergyUtils;
import com.cn.common.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {


    private final OperationConfiguration operationConfiguration;

    private final EnergyUtils energyUtils;


    @Override
    public void rewardedAds() {
        energyUtils.increase(UserUtils.getCurrentLoginId(), operationConfiguration.getEnergy().getAd());
    }
}
