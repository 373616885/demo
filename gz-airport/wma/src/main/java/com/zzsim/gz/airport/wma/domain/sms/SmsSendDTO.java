package com.zzsim.gz.airport.wma.domain.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * <p>短信发送数据传输对象</p>
 * @author zengdegui
 * @date 2018年9月1日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDTO {

	@NotBlank(message = "mobile.not.blank")
	private String mobile;
	
	@Builder.Default
	private Boolean flagCheckMobile = false;

}
