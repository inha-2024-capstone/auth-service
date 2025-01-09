package com.mog.authserver.company.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.company.dto.request.DescriptionRequestDTO;
import com.mog.authserver.company.dto.request.ImageModifyRequestDTO;
import com.mog.authserver.company.dto.request.ShortDescriptionRequestDTO;
import com.mog.authserver.company.service.CompanyModifyService;
import com.mog.authserver.security.userdetails.AuthenticatedUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Slf4j
public class CompanyModifyController {
    private final CompanyModifyService companyModifyService;

    @PatchMapping("/description")
    public ResponseEntity<BaseResponseBody<Void>> modifyDescription(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody DescriptionRequestDTO descriptionRequestDTO) {
        companyModifyService.updateDescription(authenticatedUserInfo.id(), descriptionRequestDTO.description());

        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PatchMapping("/short-description")
    public ResponseEntity<BaseResponseBody<Void>> modifyShortDescription(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @RequestBody ShortDescriptionRequestDTO shortDescriptionRequestDTO) {
        companyModifyService.updateShortDescription(authenticatedUserInfo.id(),
                shortDescriptionRequestDTO.shortDescription());

        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @PatchMapping("/image")
    public ResponseEntity<BaseResponseBody<Void>> modifyImage(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @Valid @ModelAttribute ImageModifyRequestDTO imageModifyRequestDTO) {
        companyModifyService.updateProfileImage(authenticatedUserInfo.id(), imageModifyRequestDTO);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }

    @DeleteMapping("/image")
    public ResponseEntity<BaseResponseBody<Void>> deleteImage(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo) {
        companyModifyService.deleteAndUpdateDefaultImage(authenticatedUserInfo.id());
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus()).body(SuccessStatus.OK.getBaseResponseBody());
    }
}
