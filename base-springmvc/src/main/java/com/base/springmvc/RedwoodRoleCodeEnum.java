package com.base.springmvc;

import org.apache.commons.lang3.StringUtils;

/**
 * 红杉系统角色管理
 * 
 * @author base
 *
 */
public enum RedwoodRoleCodeEnum {
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

	// 普通用户
	SIMPLE("10999", "SIMPLE", "simple"),

	// 所有用户
	ALL("10000", "ALL", "all");

	private String roleId;

	private String roleCode;

	private String clientCode;

	private RedwoodRoleCodeEnum(String roleId, String roleCode, String clientCode) {
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.clientCode = clientCode;
	}

	public static RedwoodRoleCodeEnum fromRoleId(String roleId) {
		for (RedwoodRoleCodeEnum roleCodeType : RedwoodRoleCodeEnum.values()) {
			if (roleCodeType.getRoleId().equals(roleId)) {
				return roleCodeType;
			}
		}
		return null;
	}

	public static RedwoodRoleCodeEnum fromCode(String roleCode) {
		for (RedwoodRoleCodeEnum roleCodeType : RedwoodRoleCodeEnum.values()) {
			if (roleCodeType.getRoleCode().equals(roleCode)) {
				return roleCodeType;
			}
		}
		return null;
	}

	public static RedwoodRoleCodeEnum fromClientCode(String clientCode) {
		if (StringUtils.isBlank(clientCode)) {
			return null;
		}
		for (RedwoodRoleCodeEnum roleCodeType : RedwoodRoleCodeEnum.values()) {
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
