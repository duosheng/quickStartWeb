package com.base.springmvc;

import org.apache.commons.lang3.StringUtils;

/**
 * 红杉系统角色管理
 * 
 * @author base
 *
 */
public enum RoleCodeEnum {

	// oms超级管理员代码
	ADMIN("1", "ADMIN","admin"),

	// oms找版人
	CLOTHHUNTER("30", "CLOTHHUNTER","clothHunter"),

	// oms找版审核员
	CLOTHHUNTER_AUDITOR("20", "CLOTHHUNTER_AUDITOR","clothHunterAuditor"),

	// oms商家维护组
	COMPANY_MANAGER("40", "COMPANY_MANAGER","companyManager"),

	// oms商家维护组组长
	COMPANY_MANAGER_LEADER("45", "COMPANY_MANAGER_LEADER","companyManagerLeader"),

	// oms买家维护组
	BUYER_MANAGER("46", "BUYER_MANAGER","buyerManager"),

	// oms买家维护组组长
	BUYER_MANAGER_LEADER("47", "BUYER_MANAGER_LEADER","buyerManagerLeader"),

	// oms买家维护组组长
	BUYER_MANAGER_ADMIN("48", "BUYER_MANAGER_ADMIN","buyerManagerAdmin"),

	// oms客服组长
	CUSTOMER_SERVICE_LEADER("50", "CUSTOMER_SERVICE_LEADER","customerSearviceLeader"),

	// oms客服组员
	CUSTOMER_SERVICE("55", "CUSTOMER_SERVICE","customerService"),

	// oms运营组员
	OPERATOR("60", "OPERATOR","opertor"),

	// oms运营组长
	OPERATOR_LEADER("65", "OPERATOR_LEADER","operatorLeader"),

	// oms财务处
	FINANCE("70", "FINANCE","finance"),

	// oms订单审核组员
	CHECK_MANAGER("61", "CHECK_MANAGER","checkManager"),

	// 普通用户
	SIMPLE("10999", "SIMPLE", "simple"),

	// 所有用户
	ALL("10000", "ALL", "all"),

	// 超级管理员代码
	REDWOOD_ADMIN("1000", "REDWOOD_ADMIN", "woodAdmin"),

	// 仓储文员
	WOOD_REPOSITY("1025", "WOOD_REPOSITY", "woodReposity"),
	// 仓储组长
	WOOD_REPOSITY_LEADER("1021", "WOOD_REPOSITY_LEADER", "woodReposityLeader"),

	// 销售员
	WOOD_SALES("1046", "WOOD_SALES", "woodSales"),
	// 销售组长
	WOOD_SALES_LEADER("1047", "WOOD_SALES_LEADER", "woodSalesLeader"),
	// 销售总监
	// WOOD_SALES_ADMIN("1048", "WOOD_SALES_ADMIN", "woodSalesAdmin"),

	// 提货组长
	WOOD_TAKER_LEADER("1050", "WOOD_TAKER_LEADER", "woodTakerLeader"),
	// 提货员
	WOOD_TAKER("1051", "WOOD_TAKER", "woodTaker"),

	// 验货组长
	WOOD_CHECKER_LEADER("1052", "WOOD_CHECKER_LEADER", "woodCheckerLeader"),
	// 验货员
	WOOD_CHECKER("1053", "WOOD_CHECKER", "woodChecker"),

	// 物流员
	WOOD_LOGISTICS("1055", "WOOD_LOGISTICS", "woodLogistics"),
	// 物流组长
	WOOD_LOGISTICS_LEADER("1056", "WOOD_LOGISTICS_LEADER", "woodLogisticsLeader"),

	// 剪版组长
	WOOD_CUT_CLOTH_LEADER("1060", "WOOD_CUT_CLOTH_LEADER", "woodCutClothLeader"),
	// 剪版员
	WOOD_CUT_CLOTH("1065", "WOOD_CUT_CLOTH", "woodCutCloth"),

	// 跟单组长
	WOOD_FOLLOW_LEADER("1070", "WOOD_FOLLOW_LEADER", "woodFollowLeader"),

	// 找版员
	WOOD_CLOTHHUNTER("1031", "WOOD_CLOTHHUNTER", "woodClothHunter"),

	// 找版组长
	WOOD_CLOTHHUNTER_LEADER("1030", "WOOD_CLOTHHUNTER_LEADER", "woodClothHunterLeader"),

	// 找版大组长
	WOOD_CLOTHHUNTER_ADMIN("1035", "WOOD_CLOTHHUNTER_ADMIN", "woodClothHunterAdmin"),

	// 财务
	WOOD_FINANCE("1080", "WOOD_FINANCE", "woodFinance"),

	// 买货组长
	WOOD_PURCHASER_LEADER("1090", "WOOD_PURCHASER_LEADER", "woodPurchaserLeader"),

	// 买货员
	WOOD_PURCHASER("1095", "WOOD_PURCHASER", "woodPurchaser"),



	// 跟单组长
	WOOD_FOLLOW_TEAMLEADER("1071", "WOOD_FOLLOW_TEAMLEADER", "woodFollowTeamLeader"),

	// 跟单主管
	WOOD_FOLLOW_ADMIN("1072", "WOOD_FOLLOW_ADMIN", "woodFollowAdmin");



	private String roleId;

	private String roleCode;

	private String clientCode;

	private RoleCodeEnum(String roleId, String roleCode, String clientCode) {
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.clientCode = clientCode;
	}

	public static RoleCodeEnum fromRoleId(String roleId) {
		for (RoleCodeEnum roleCodeType : RoleCodeEnum.values()) {
			if (roleCodeType.getRoleId().equals(roleId)) {
				return roleCodeType;
			}
		}
		return null;
	}

	public static RoleCodeEnum fromCode(String roleCode) {
		for (RoleCodeEnum roleCodeType : RoleCodeEnum.values()) {
			if (roleCodeType.getRoleCode().equals(roleCode)) {
				return roleCodeType;
			}
		}
		return null;
	}

	public static RoleCodeEnum fromClientCode(String clientCode) {
		if (StringUtils.isBlank(clientCode)) {
			return null;
		}
		for (RoleCodeEnum roleCodeType : RoleCodeEnum.values()) {
			if (roleCodeType.getClientCode().toLowerCase().equals(clientCode.toLowerCase())) {
				return roleCodeType;
			}
		}
		return null;
	}

	public String getRoleId() {
		return roleId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public String getClientCode() {
		return clientCode;
	}

}
