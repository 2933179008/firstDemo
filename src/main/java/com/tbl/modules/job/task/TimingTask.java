package com.tbl.modules.job.task;


import com.tbl.modules.external.service.ErpInterfaceService;
import com.tbl.modules.external.service.ErpOutstorageService;
import com.tbl.modules.external.service.ErpStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author anss
 * @date 2018-11-28
 */
@Component("testTask")
public class TimingTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * erp入库接口
	 */
	@Autowired
	private ErpInterfaceService erpInterfaceService;
	/**
	 * erp库存接口
	 */
	@Autowired
	private ErpStockService erpStockService;
	/**
	 * erp出库接口
	 */
	@Autowired
	private ErpOutstorageService erpOutstorageService;

	/**
	 * 采购入库单接口调用
	 */
	public void setPOInstock() {
		erpInterfaceService.setPOInstock();
	}

	/**
	 * 受托加工入库单接口
	 */
	public void setSTJGInStock() {
		erpInterfaceService.setSTJGInStock();
	}

	/**
	 * 生产退库单及生产用料分摊接口(自采料)
	 */
	public void setSCOutstockRed() {
		erpInterfaceService.setSCOutstockRed();
	}

	/**
	 * 生产退库单及生产用料分摊接口(客料)
	 */
	public void SetSTOutstockRed() {
		erpInterfaceService.SetSTOutstockRed();
	}


	/**
	 * 货权转移接口
	 */
	public void transferView() {
		erpStockService.transferView();
	}

	/**
	 * 库存更新接口（调拨单 散货）
	 */
	public void setDiaoBo() {
		erpStockService.setDiaoBo();
	}

	/**
	 * 库存更新接口（调拨单 整货）
	 */
	public void setRfidDiaoBo() {
		erpStockService.setRfidDiaoBo();
	}

	/**
	 * 库存更新接口（其它入库） 盘盈
	 */
	public void SetOtherInstockIT() {
		erpStockService.SetOtherInstockIT();
	}

	/**
	 * 库存更新接口（其它出库） 盘亏
	 *
	 * @author pz
	 * @date 2018-11-26
	 */
	public void setOtherOutstockIT() {
		erpStockService.setOtherOutstockIT();
	}

	/**
	 * 销毁出库
	 */
	public void destoryOutstorage() {
		erpOutstorageService.destoryOutstorage();
	}

	/**
	 * 自采退货
	 */
	public void SelfCollectionAndReturn() {
		erpOutstorageService.SelfCollectionAndReturn();
	}

	/**
	 * 公司领料
	 */
	public void materialcompanyRequisition() {
		erpOutstorageService.materialcompanyRequisition();
	}

	/**
	 * 客供领料
	 */
	public void materialCustomerRequisition() {
		erpOutstorageService.materialCustomerRequisition();
	}

	/**
	 * 越库出库接口(自采)
	 */
	public void cusCrossOutstorage(){erpOutstorageService.cusCrossOutstorage();}

	/**
	 * 越库出库接口(客供)
	 */
	public void crossOutstorage(){erpOutstorageService.crossOutstorage();}


//	/**
//	 * 保存token权限
//	 * @author anss
//	 * @date 2018-11-26
//	 * @param params （多个参数之间用逗号“，”隔开）
//	 * @return
//	 */
//	public void saveTokenAuthorization(String params){
//		logger.info("我是带参数的saveTokenAuthorization方法，正在被执行，参数为：" + params);
//
//
//		Map<String, Object> tokenInfo = sinotrasInterfaceService.getTokenbyTerminal(params);
//
//		// 清空TokenAuthorization表
//		tokenAuthorizationDAO.deleteByMap(new HashMap<>());
//		// 往TokenAuthorization表插入一条数据
//		TokenAuthorization tokenAu = new TokenAuthorization();
//		JSONObject tokenObj = JSONObject.parseObject((String) tokenInfo.get("data"));
//		String strToken = (String)tokenObj.get("data");
//
//		tokenAu.setAuthorization(strToken.replace("Bearer ", ""));
//		tokenAu.setCreateTime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
//		tokenAuthorizationDAO.insert(tokenAu);
//
//		try {
//			Thread.sleep(1000L);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		System.err.println("saveTokenAuthorization定时任务测试--------------:" + (String)tokenObj.get("data"));
//
//	}


	public void test2(){



		logger.info("我是不带参数的test2方法，正在被执行");
	}

	/**
	* @Description:  定时调用erp接口
	* @Param:
	* @return:
	* @Author: zj
	* @Date: 2019/3/22
	*/
	public void erpInterfaceCall(){
		erpInterfaceService.setPOInstock();
	}
}
