/*
 * 
 * 
 * 
 * 
 */
package io.mosip.kernel.cryptomanager.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.mosip.kernel.cryptomanager.constant.CryptomanagerConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Crypto-Manager-Request model
 * 
 * @author Urvil Joshi
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model representing a Crypto-Manager-Service Request")
public class CryptomanagerRequestDto {
	/**
	 * Application id of decrypting module
	 */
	@ApiModelProperty(notes = "Application id of decrypting module", example = "REGISTRATION", required = true)
	@NotBlank(message = CryptomanagerConstant.INVALID_REQUEST)
	private String applicationId;
	/**
	 * Refrence Id
	 */
	@ApiModelProperty(notes = "Refrence Id", example = "REF01")
	private String referenceId;
	/**
	 * Timestamp
	 */
	@ApiModelProperty(notes = "Timestamp as metadata", example = "2018-12-10T06:12:52.994Z", required = true)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@NotNull
	private LocalDateTime timeStamp;
	/**
	 * Data in BASE64 encoding to encrypt/decrypt
	 */
	@ApiModelProperty(notes = "Data in BASE64 encoding to encrypt/decrypt", required = true)
	@NotBlank(message = CryptomanagerConstant.INVALID_REQUEST)
	private String data;
	/**
	 * Salt to be passed as IV
	 */
	@Pattern(regexp = CryptomanagerConstant.EMPTY_REGEX, message = CryptomanagerConstant.EMPTY_ATTRIBUTE)
	@ApiModelProperty(notes = " Base64 Encoded Salt to be send as IV")
	private String salt;

	/**
	 * AAD to be passed
	 */
	@Pattern(regexp = CryptomanagerConstant.EMPTY_REGEX, message = CryptomanagerConstant.EMPTY_ATTRIBUTE)
	@ApiModelProperty(notes = " Base64 Encoded AAD(Advance Authentication Data)")
	private String aad;

	/**
	* flag to prepend certificate thumbprint, default to true. 
	*/
	@ApiModelProperty(notes = "flag to prepend certificate thumbprint to encrypted data.", example = "false", required = false)
	@Deprecated
	private Boolean prependThumbprint;

	@Override
	public String toString() {
		return "CryptomanagerRequestDto [applicationId=" + applicationId + ", referenceId=" + referenceId
				+ ", timeStamp=" + timeStamp + ", salt=" + salt + "]";
	}
}
