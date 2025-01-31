package io.mosip.kernel.keymanagerservice.controller;

import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.core.http.RequestWrapper;
import io.mosip.kernel.core.http.ResponseFilter;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.keymanagerservice.dto.AllCertificatesDataResponseDto;
import io.mosip.kernel.keymanagerservice.dto.CSRGenerateRequestDto;
import io.mosip.kernel.keymanagerservice.dto.KeyPairGenerateRequestDto;
import io.mosip.kernel.keymanagerservice.dto.KeyPairGenerateResponseDto;
import io.mosip.kernel.keymanagerservice.dto.RevokeKeyRequestDto;
import io.mosip.kernel.keymanagerservice.dto.RevokeKeyResponseDto;
import io.mosip.kernel.keymanagerservice.dto.SymmetricKeyGenerateRequestDto;
import io.mosip.kernel.keymanagerservice.dto.SymmetricKeyGenerateResponseDto;
import io.mosip.kernel.keymanagerservice.dto.UploadCertificateRequestDto;
import io.mosip.kernel.keymanagerservice.dto.UploadCertificateResponseDto;
import io.mosip.kernel.keymanagerservice.service.KeymanagerService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This class provides controller methods for Key manager.
 * 
 * @author Dharmesh Khandelwal
 * @author Urvil Joshi
 * @since 1.0.0
 *
 */
@SuppressWarnings("java:S5122") // Need CrossOrigin access for all the APIs, added to ignore in sonarCloud Security hotspots.
@CrossOrigin
@RestController
@Tag(name = "keymanager", description = "Operation related to Keymanagement")
public class KeymanagerController {

	/**
	 * Instance for KeymanagerService
	 */
	@Autowired
	KeymanagerService keymanagerService;

	/**
	 * Generate Master Key for the provided APP ID.
	 * 
	 * @param objectType 			   response Object Type. Support types are Certificate/CSR. Path Parameter.
	 * @param keyPairGenRequestDto     {@link KeyPairGenerateRequestDto} request
	 * @return {@link KeyPairGenerateResponseDto} instance
	*/
	@Operation(summary = "Generate Master Key for the provided APP ID", description = "Generate Master Key for the provided APP ID", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','KEY_MAKER', 'INDIVIDUAL','REGISTRATION_PROCESSOR','REGISTRATION_ADMIN','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','ID_AUTHENTICATION','TEST','PRE_REGISTRATION_ADMIN','RESIDENT')")
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getPostgeneratemasterkeyobjecttype())")
	@ResponseFilter
	@PostMapping(value = "/generateMasterKey/{objectType}")
	public ResponseWrapper<KeyPairGenerateResponseDto> generateMasterKey(
			@ApiParam("Response Type CERTIFICATE/CSR") @PathVariable("objectType") String objectType,
			@RequestBody @Valid RequestWrapper<KeyPairGenerateRequestDto> keyPairGenRequestDto) {

		ResponseWrapper<KeyPairGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.generateMasterKey(objectType, keyPairGenRequestDto.getRequest()));
		return response;
	}

	/**
	 * Request to get Certificate for the Provided APP ID & REF ID.
	 * 
	 * @param applicationId Application id of the application requesting Certificate
	 * @param referenceId   Reference id of the application requesting Certificate. Blank in case of Master Key.
	 * @return {@link KeyPairGenerateResponseDto} instance
	*/
	@Operation(summary = "Request to get Certificate for the Provided APP ID & REF ID", description = "Request to get Certificate for the Provided APP ID & REF ID", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','REGISTRATION_PROCESSOR','REGISTRATION_ADMIN','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','ID_AUTHENTICATION','TEST','PRE_REGISTRATION_ADMIN','RESIDENT')")
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getGetgetcertificate())")
	@ResponseFilter
	@GetMapping(value = "/getCertificate")
	public ResponseWrapper<KeyPairGenerateResponseDto> getCertificate(
		@ApiParam("Id of application") @RequestParam("applicationId") String applicationId,
		@ApiParam("Refrence Id as metadata") @RequestParam("referenceId") Optional<String> referenceId) {

		ResponseWrapper<KeyPairGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.getCertificate(applicationId, referenceId));
		return response;
	}

	/**
	 * Request to Generate CSR for the provided APP ID & REF ID along with other certificate params.
	 * 
	 * @param csrGenRequestDto     {@link CSRGenerateRequestDto} request
	 * @return {@link KeyPairGenerateResponseDto} instance
	*/
	@Operation(summary = "Request to Generate CSR for the provided APP ID & REF ID along with other certificate params", description = "Request to Generate CSR for the provided APP ID & REF ID along with other certificate params", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','REGISTRATION_PROCESSOR','REGISTRATION_ADMIN','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','ID_AUTHENTICATION','TEST','PRE_REGISTRATION_ADMIN','RESIDENT')")
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getPostgeneratecsr())")
	@ResponseFilter
	@PostMapping(value = "/generateCSR")
	public ResponseWrapper<KeyPairGenerateResponseDto> generateCSR(
		@RequestBody @Valid RequestWrapper<CSRGenerateRequestDto> csrGenRequestDto) {

		ResponseWrapper<KeyPairGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.generateCSR(csrGenRequestDto.getRequest()));
		return response;
	}

	/**
	 * Update signed certificate for the provided APP ID & REF ID.
	 * 
	 * @param uploadCertRequestDto     {@link UploadCertificateRequestDto} request
	 * @return {@link UploadCertificateResponseDto} instance
	*/
	@Operation(summary = "Update signed certificate for the provided APP ID & REF ID", description = "Update signed certificate for the provided APP ID & REF ID", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','REGISTRATION_PROCESSOR','REGISTRATION_ADMIN','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','ID_AUTHENTICATION','TEST','PRE_REGISTRATION_ADMIN','RESIDENT')")
	@ResponseFilter
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getPostuploadcertificate())")
	@PostMapping(value = "/uploadCertificate")
	public ResponseWrapper<UploadCertificateResponseDto> uploadCertificate(
		@RequestBody @Valid RequestWrapper<UploadCertificateRequestDto> uploadCertRequestDto) {

		ResponseWrapper<UploadCertificateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.uploadCertificate(uploadCertRequestDto.getRequest()));
		return response;
	}

	/**
	 * Update signed certificate for the provided APP ID & REF ID for other domains.
	 * 
	 * @param uploadCertRequestDto     {@link UploadCertificateRequestDto} request
	 * @return {@link UploadCertificateResponseDto} instance
	*/
	@Operation(summary = "Update signed certificate for the provided APP ID & REF ID for other domains", description = "Update signed certificate for the provided APP ID & REF ID for other domains", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','REGISTRATION_PROCESSOR','REGISTRATION_ADMIN','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','ID_AUTHENTICATION','TEST','PRE_REGISTRATION_ADMIN','RESIDENT')")
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getPostuploadotherdomaincertificate())")
	@ResponseFilter
	@PostMapping(value = "/uploadOtherDomainCertificate")
	public ResponseWrapper<UploadCertificateResponseDto> uploadOtherDomainCertificate(
		@RequestBody @Valid RequestWrapper<UploadCertificateRequestDto> uploadCertRequestDto) {

		ResponseWrapper<UploadCertificateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.uploadOtherDomainCertificate(uploadCertRequestDto.getRequest()));
		return response;
	}

	/**
	 * Request to Generate Symmetric key for the provided APP ID & REF ID.
	 * 
	 * @param symGenRequestDto     {@link SymmetricKeyGenerateRequestDto} request
	 * @return {@link SymmetricKeyGenerateResponseDto} instance
	*/
	@Operation(summary = "Request to Generate Symmetric key for the provided APP ID & REF ID", description = "Request to Generate Symmetric key for the provided APP ID & REF ID", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	//@PreAuthorize("hasAnyRole('ZONAL_ADMIN','GLOBAL_ADMIN','INDIVIDUAL','REGISTRATION_PROCESSOR','REGISTRATION_ADMIN','REGISTRATION_SUPERVISOR','REGISTRATION_OFFICER','ID_AUTHENTICATION','TEST','PRE_REGISTRATION_ADMIN','RESIDENT')")
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getPostgeneratesymmetrickey())")
	@ResponseFilter
	@PostMapping(value = "/generateSymmetricKey")
	public ResponseWrapper<SymmetricKeyGenerateResponseDto> generateSymmetricKey(
		@RequestBody @Valid RequestWrapper<SymmetricKeyGenerateRequestDto> symGenRequestDto) {

		ResponseWrapper<SymmetricKeyGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.generateSymmetricKey(symGenRequestDto.getRequest()));
		return response;
	}

	/**
	 * Request to Revoke Base Key for the provided APP ID & REF ID.
	 * 
	 * @param revokeKeyRequestDto     {@link RevokeKeyRequestDto} request
	 * @return {@link RevokeKeyResponseDto} instance
	*/
	@Operation(summary = "Request to Revoke Base Key for the provided APP ID & REF ID", description = "Request to Revoke Base Key for the provided APP ID & REF ID", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getPutrevokekey())")
	@ResponseFilter
	@PutMapping(value = "/revokeKey")
	public ResponseWrapper<RevokeKeyResponseDto> revokeKey(
		@RequestBody @Valid RequestWrapper<RevokeKeyRequestDto> revokeKeyRequestDto) {

		ResponseWrapper<RevokeKeyResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.revokeKey(revokeKeyRequestDto.getRequest()));
		return response;
	}

	/**
	 * Request to get all the Certificates for the Provided APP ID & REF ID.
	 * 
	 * @param applicationId Application id of the application requesting Certificate
	 * @param referenceId   Reference id of the application requesting Certificate. Blank in case of Master Key.
	 * @return {@link KeyPairGenerateResponseDto} instance
	*/
	@Operation(summary = "Request to get all the certificates for the Provided APP ID & REF ID", description = "Request to get all the certificates for the Provided APP ID & REF ID", tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getGetgetcertificate())")
	@ResponseFilter
	@GetMapping(value = "/getAllCertificates")
	public ResponseWrapper<AllCertificatesDataResponseDto> getAllCertificates(
		@ApiParam("Id of application") @RequestParam("applicationId") String applicationId,
		@ApiParam("Refrence Id as metadata") @RequestParam("referenceId") Optional<String> referenceId) {

		ResponseWrapper<AllCertificatesDataResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.getAllCertificates(applicationId, referenceId));
		return response;
	}

	/**
	 * Request to generate component Signature ECC Key pair & Certificate for the Provided APP ID & REF ID. 
	 * Supported Curve(s) SECP256K1, SECP256R1 and ED25519.
	 * 
	  * @param objectType 			   response Object Type. Support types are Certificate/CSR. 
	 * @param keyPairGenRequestDto     {@link KeyPairGenerateRequestDto} request
	 * @return {@link KeyPairGenerateResponseDto} instance
	*/
	@Operation(summary = "Request to generate component Signature ECC Key pair & Certificate for the Provided APP ID & REF ID.", 
				description = "Request to generate component Signature ECC Key pair & Certificate for the Provided APP ID & REF ID. " +
				"Supported Curve(s) SECP256K1, SECP256R1 and ED25519", 
				tags = { "keymanager" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success or you may find errors in error array in response"),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true))) })
	@PreAuthorize("hasAnyRole(@KeyManagerAuthRoles.getGetgetcertificate())")
	@ResponseFilter
	@PostMapping(value = "/generateECSignKey/{objectType}")
	public ResponseWrapper<KeyPairGenerateResponseDto> generateECSignKey(
		@ApiParam("Response Type CERTIFICATE/CSR") @PathVariable("objectType") String objectType, 
		@RequestBody @Valid RequestWrapper<KeyPairGenerateRequestDto> ecKeyPairGenRequestDto) {

		ResponseWrapper<KeyPairGenerateResponseDto> response = new ResponseWrapper<>();
		response.setResponse(keymanagerService.generateECSignKey(objectType, ecKeyPairGenRequestDto.getRequest()));
		return response;
	}
}
