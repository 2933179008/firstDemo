package com.tbl.modules.stock.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.tbl.modules.stock.dao.MaterielPowerDAO;
import com.tbl.modules.stock.entity.MaterielPower;
import com.tbl.modules.stock.service.MaterielPowerService;
import org.springframework.stereotype.Service;

@Service("materielPowerService")
public class MaterielPowerServiceImpl extends ServiceImpl<MaterielPowerDAO, MaterielPower> implements MaterielPowerService {
}
