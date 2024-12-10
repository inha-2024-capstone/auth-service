package com.mog.authserver.company.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.company.dto.request.DescriptionRequestDTO;
import com.mog.authserver.company.dto.request.EmailRequestDTO;
import com.mog.authserver.company.dto.request.ImageModifyRequestDTO;
import com.mog.authserver.company.dto.request.PasswordRequestDTO;
import com.mog.authserver.company.dto.request.ShortDescriptionRequestDTO;
import com.mog.authserver.company.service.CompanyModifyService;
import com.mog.authserver.company.service.CompanyValidateService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyModifyController {
    private final CompanyModifyService companyModifyService;
    private final CompanyValidateService companyValidateService;

    @PostMapping("/password")
    public ResponseEntity<BaseResponseBody<Boolean>> validatePassword(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody PasswordRequestDTO passwordRequestDTO) {
        Boolean isSamePassword = companyValidateService.isSamePassword(authenticatedUserInfo.id(),
                passwordRequestDTO.password());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(isSamePassword));
    }

    @PostMapping("/email")
    public ResponseEntity<BaseResponseBody<Boolean>> validateEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        Boolean doesEmailExist = companyValidateService.doesEmailExist(emailRequestDTO.email());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(doesEmailExist));
    }

    @PatchMapping("/password")
    public ResponseEntity<BaseResponseBody<Void>> modifyPassword(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody PasswordRequestDTO passwordRequestDTO) {
        companyModifyService.updatePassword(authenticatedUserInfo.id(), passwordRequestDTO.password());

        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PostMapping("/description")
    public ResponseEntity<BaseResponseBody<Void>> modifyDescription(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody DescriptionRequestDTO descriptionRequestDTO) {
        companyModifyService.updateDescription(authenticatedUserInfo.id(), descriptionRequestDTO.description());

        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PostMapping("/short-description")
    public ResponseEntity<BaseResponseBody<Void>> modifyShortDescription(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody ShortDescriptionRequestDTO shortDescriptionRequestDTO) {
        companyModifyService.updateShortDescription(authenticatedUserInfo.id(),
                shortDescriptionRequestDTO.shortDescription());

        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PostMapping("/image")
    public ResponseEntity<BaseResponseBody<Void>> modifyImage(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @Valid @ModelAttribute ImageModifyRequestDTO imageModifyRequestDTO) {
        companyModifyService.updateProfileImage(authenticatedUserInfo.id(), imageModifyRequestDTO);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }
}
